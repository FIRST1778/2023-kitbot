package org.chillout1778.subsystems

import com.ctre.phoenix6.hardware.CANcoder
import com.ctre.phoenix6.hardware.TalonFX
import com.ctre.phoenix6.signals.InvertedValue
import com.ctre.phoenix6.configs.MotorOutputConfigs
import com.ctre.phoenix6.configs.MagnetSensorConfigs
import com.revrobotics.CANSparkMax
import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.math.controller.ProfiledPIDController
import edu.wpi.first.math.controller.SimpleMotorFeedforward
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.kinematics.SwerveModulePosition
import edu.wpi.first.math.kinematics.SwerveModuleState
import edu.wpi.first.math.trajectory.TrapezoidProfile
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.chillout1778.Constants
import org.chillout1778.lib.Util

class SwerveModule(
        val name: String,
        driveMotorId: Int, turnMotorId: Int, turnCanCoderId: Int,
        val encoderOffset: Double,
        val driveInversion: InvertedValue,
        val translation: Translation2d,
) {
    private val driveMotor = TalonFX(driveMotorId)
    private val turnMotor: CANSparkMax  = Util.neo(turnMotorId)
    private val turnCanCoder = CANcoder(turnCanCoderId)

    private val drivePID: PIDController = Constants.Swerve.driveController()
    private val turnPID: ProfiledPIDController = Constants.Swerve.turnController()
    private val driveFeedforward: SimpleMotorFeedforward = Constants.Swerve.driveFeedforward()
    private val turnFeedforward:  SimpleMotorFeedforward = Constants.Swerve.turnFeedforward()

    // TODO: Voltage compensation???

    init {
        driveMotor.configurator.apply(
            MotorOutputConfigs().withInverted(driveInversion)
        )
        turnMotor.setInverted(true)
        turnCanCoder.configurator.apply(
            MagnetSensorConfigs().withMagnetOffset(encoderOffset)
        )
        turnPID.enableContinuousInput(0.0, 2.0*Math.PI) // TODO: make sure inputs are wrapped to this range
        resetRelative()
    }

    val driveVelocity get() = driveMotor.velocity.value * 2.0*Math.PI * Constants.Swerve.driveReduction * Constants.Swerve.colsonWheelRadius
    val drivePosition get() = driveMotor.position.value * 2.0*Math.PI * Constants.Swerve.driveReduction * Constants.Swerve.colsonWheelRadius
    val turnPosition get() = turnMotor.encoder.position * 2.0*Math.PI
    val turnVelocity get() = turnMotor.encoder.velocity * 2.0*Math.PI
    val coderPosition get() = turnCanCoder.position.value * 2.0*Math.PI
    val coderVelocity get() = turnCanCoder.velocity.value * 2.0*Math.PI

    val state get() = SwerveModuleState(
        driveVelocity, Rotation2d(turnPosition)
    )
    val position get() = SwerveModulePosition(
        drivePosition, Rotation2d(turnPosition)
    )

    private var turnStationaryTicks: Int = 0

    private fun resetRelative() {
        //System.out.printf("Resetting %s swerve encoder from absolute\n", name)
        // Use the CANCoder's .getAbsolutePosition() API instead of
        // .getPosition().  Even though we tell the CANCoder to boot up
        // in absolute mode (see Util.kt), there were apparently some
        // firmware bugs that would rarely cause .getPosition() to report
        // incorrect values after startup.
        // https://store.ctr-electronics.com/blog/cancoder-firmware-update-22012/
        // https://discord.com/channels/887922855084425266/890436659450118254/1170883077195714590
        // https://github.com/SwerveDriveSpecialties/Do-not-use-swerve-lib-2022-unmaintained/blob/55f3f1ad9e6bd81e56779d022a40917aacf8d3b3/src/main/java/com/swervedrivespecialties/swervelib/rev/NeoSteerControllerFactoryBuilder.java#L128C3-L128C3
        turnMotor.encoder.position = turnCanCoder.absolutePosition.value
        turnStationaryTicks = 0
    }
    
    fun maybeResetRelative() {
        // Count how many ticks this turn motor has been stationary;
        // if we've waited long enough, we take this as a cue to reset
        // relative encoders back to absolute.
        if (turnVelocity < 0.3) {
            turnStationaryTicks += 1
        }
        if (turnStationaryTicks > 250) {
            resetRelative()
        }
    }

    private fun clamp(n: Double, r: Double) = Math.min(r, Math.max(-r, n))
    private fun clampVoltage(n: Double) = clamp(n, Constants.Swerve.maxVoltage)

    private fun wrapAngle(n: Double): Double {
        val n2 = n % (2.0*Math.PI)
        if (n2 < 0.0)
            return n2 + 2.0*Math.PI
        else
            return n2
    }

    fun drive(unoptimizedState: SwerveModuleState) {
        // Optimize the swerve module state (i.e., drive velocity
        // and turn position) so that we never turn more than 90 degrees.
        val rotation = Rotation2d(turnPosition)
        val state = SwerveModuleState.optimize(unoptimizedState, rotation)

        // Scale by cosine.  If the module is far from its
        // desired angle, then we drive it correspondingly less.
        state.speedMetersPerSecond *= (state.angle - rotation).getCos()

        val driveAmount = drivePID.calculate(driveVelocity, state.speedMetersPerSecond)
        if (name == "front left") {
            //println("$name cur $driveVelocity want ${state.speedMetersPerSecond} volts $driveAmount")
            println("$name raw ${driveMotor.velocity.value} cur $driveVelocity want ${state.speedMetersPerSecond} volts $driveAmount")
        }
            //+ driveFeedforward.calculate(state.speedMetersPerSecond)
        //val turnAmount = turnPID.calculate(wrapAngle(turnPosition), state.angle.radians)
            //+ turnFeedforward.calculate(turnPID.setpoint.velocity)
        driveMotor.setVoltage(1.0)//clampVoltage(driveAmount))
        //turnMotor.setVoltage(clampVoltage(turnAmount))
    }
}
