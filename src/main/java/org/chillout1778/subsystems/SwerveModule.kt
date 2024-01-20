package org.chillout1778.subsystems

import com.ctre.phoenix6.configs.FeedbackConfigs
import com.ctre.phoenix6.configs.MagnetSensorConfigs
import com.ctre.phoenix6.configs.MotorOutputConfigs
import com.ctre.phoenix6.hardware.CANcoder
import com.ctre.phoenix6.hardware.TalonFX
import com.ctre.phoenix6.signals.InvertedValue
import com.ctre.phoenix6.signals.AbsoluteSensorRangeValue
import com.ctre.phoenix6.signals.SensorDirectionValue
import com.revrobotics.CANSparkMax
import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.math.controller.ProfiledPIDController
import edu.wpi.first.math.controller.SimpleMotorFeedforward
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.kinematics.SwerveModulePosition
import edu.wpi.first.math.kinematics.SwerveModuleState
import edu.wpi.first.math.trajectory.TrapezoidProfile
import edu.wpi.first.util.sendable.Sendable
import edu.wpi.first.util.sendable.SendableBuilder
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.chillout1778.Constants
import org.chillout1778.lib.Util

class SwerveModule(
        val name: String,
        driveMotorId: Int, turnMotorId: Int, turnCanCoderId: Int,
        val encoderOffset: Double,
        val driveInversion: InvertedValue,
        val translation: Translation2d,
): Sendable {
    private val driveMotor = TalonFX(driveMotorId)
    private val turnMotor: CANSparkMax = Util.neo(turnMotorId)
    private val turnCanCoder = CANcoder(turnCanCoderId)

    private val drivePID: PIDController = Constants.Swerve.driveController()
    private val turnPID: PIDController = Constants.Swerve.turnController()
    private val driveFeedforward: SimpleMotorFeedforward = Constants.Swerve.driveFeedforward()

    // TODO: Voltage compensation???

    init {
        driveMotor.configurator.apply(
            MotorOutputConfigs().withInverted(driveInversion)
        )
        driveMotor.configurator.apply(
            FeedbackConfigs().withSensorToMechanismRatio(1.0)
                             .withRotorToSensorRatio(1.0)
        )
        turnMotor.inverted = true
        turnMotor.idleMode = CANSparkMax.IdleMode.kCoast
        turnMotor.encoder.positionConversionFactor = 1.0
        turnMotor.burnFlash()
        turnCanCoder.configurator.apply(
            MagnetSensorConfigs().withMagnetOffset(-encoderOffset / (2.0*Math.PI))
                .withAbsoluteSensorRange(AbsoluteSensorRangeValue.Unsigned_0To1)
                .withSensorDirection(SensorDirectionValue.CounterClockwise_Positive)
        )
        turnPID.enableContinuousInput(-Math.PI, Math.PI) // TODO: make sure inputs are wrapped to this range
        resetRelative()
        Shuffleboard.getTab("Swerve Module " + name).add(name, this).withSize(3,6)
    }

    val driveVelocity get() = 2.0*Math.PI * driveMotor.velocity.value * Constants.Swerve.driveReduction * Constants.Swerve.colsonWheelRadius
    val drivePosition get() = 2.0*Math.PI * driveMotor.position.value * Constants.Swerve.driveReduction * Constants.Swerve.colsonWheelRadius
    val turnPosition get()  = Util.wrapAngle(2.0*Math.PI * Constants.Swerve.angleReduction * turnMotor.encoder.position)
    val coderPosition get() = Util.wrapAngle(2.0*Math.PI * turnCanCoder.absolutePosition.value)
    val turnVelocity get()  = Math.PI/30.0 * Constants.Swerve.angleReduction * turnMotor.encoder.velocity

    val state get() = SwerveModuleState(
        driveVelocity, Rotation2d(turnPosition)
    )
    val position get() = SwerveModulePosition(
        drivePosition, Rotation2d(turnPosition)
    )

    private var turnStationaryTicks: Int = 0

    private fun resetRelative() {
        // Use the CANCoder's .getAbsolutePosition() API instead of
        // .getPosition().  Even though we tell the CANCoder to boot up
        // in absolute mode (see Util.kt), there were apparently some
        // firmware bugs that would rarely cause .getPosition() to report
        // incorrect values after startup.
        // https://store.ctr-electronics.com/blog/cancoder-firmware-update-22012/
        // https://discord.com/channels/887922855084425266/890436659450118254/1170883077195714590
        // https://github.com/SwerveDriveSpecialties/Do-not-use-swerve-lib-2022-unmaintained/blob/55f3f1ad9e6bd81e56779d022a40917aacf8d3b3/src/main/java/com/swervedrivespecialties/swervelib/rev/NeoSteerControllerFactoryBuilder.java#L128C3-L128C3
        turnMotor.encoder.position = turnCanCoder.absolutePosition.value / Constants.Swerve.angleReduction
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

    private fun clampVoltage(n: Double) = Util.clamp(n, Constants.Swerve.maxVoltage)

    fun drive(unoptimizedState: SwerveModuleState) {
        // Optimize the swerve module state (i.e., drive velocity
        // and turn position) so that we never turn more than 90 degrees.
        val rotation = Rotation2d(turnPosition)
        var optimizedState = SwerveModuleState.optimize(unoptimizedState, rotation)

        // Scale by cosine.  If the module is far from its
        // desired angle, then we drive it correspondingly less.
        optimizedState.speedMetersPerSecond *= (optimizedState.angle - rotation).getCos()

        val driveAmount = drivePID.calculate(driveVelocity, optimizedState.speedMetersPerSecond)
            + driveFeedforward.calculate(optimizedState.speedMetersPerSecond)
        driveMotor.setVoltage(clampVoltage(driveAmount))
        val turnAmount = turnPID.calculate(turnPosition, Util.wrapAngle(optimizedState.angle.radians))
        turnMotor.setVoltage(clampVoltage(turnAmount))
    }

    override fun initSendable(builder: SendableBuilder?) {
        builder!!
        builder.addDoubleProperty("Turn position (deg)", {Math.toDegrees(turnPosition)}, {})
        builder.addDoubleProperty("Diff from CANCoder (deg)", {Math.toDegrees(turnPosition - coderPosition)}, {})
    }
}
