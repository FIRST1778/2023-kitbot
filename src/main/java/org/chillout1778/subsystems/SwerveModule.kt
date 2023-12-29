package org.chillout1778.subsystems

import com.ctre.phoenix.sensors.CANCoder
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
        val driveInverted: Boolean,
        val translation: Translation2d,
) {
    private val driveMotor: CANSparkMax = Util.defaultNeo(driveMotorId)
    private val turnMotor: CANSparkMax  = Util.defaultNeo(turnMotorId)
    private val turnCanCoder: CANCoder = Util.defaultCanCoder(turnCanCoderId)

    private val drivePID: PIDController = Constants.Swerve.driveController()
    private val turnPID: ProfiledPIDController = Constants.Swerve.turnController()
    private val driveFeedforward: SimpleMotorFeedforward = Constants.Swerve.driveFeedforward()
    private val turnFeedforward:  SimpleMotorFeedforward = Constants.Swerve.turnFeedforward()

    init {
        driveMotor.setInverted(driveInverted)
        turnMotor.setInverted(true)
        turnCanCoder.configMagnetOffset(encoderOffset)
        turnPID.enableContinuousInput(-Math.PI, Math.PI)
        resetRelative()
    }

    val state get() = SwerveModuleState(
        driveMotor.encoder.velocity, Rotation2d(turnMotor.encoder.position)
    )
    val position get() = SwerveModulePosition(
        driveMotor.encoder.position, Rotation2d(turnMotor.encoder.position)
    )

    private var turnStationaryTicks: Int = 0

    private fun resetRelative() {
        System.out.printf("Resetting %s swerve encoder from absolute\n", name)
        // Use the CANCoder's .getAbsolutePosition() API instead of
        // .getPosition().  Even though we tell the CANCoder to boot up
        // in absolute mode (see Util.kt), there were apparently some
        // firmware bugs that would rarely cause .getPosition() to report
        // incorrect values after startup.
        // https://store.ctr-electronics.com/blog/cancoder-firmware-update-22012/
        // https://discord.com/channels/887922855084425266/890436659450118254/1170883077195714590
        // https://github.com/SwerveDriveSpecialties/Do-not-use-swerve-lib-2022-unmaintained/blob/55f3f1ad9e6bd81e56779d022a40917aacf8d3b3/src/main/java/com/swervedrivespecialties/swervelib/rev/NeoSteerControllerFactoryBuilder.java#L128C3-L128C3
        turnMotor.encoder.position = turnCanCoder.absolutePosition
        turnStationaryTicks = 0
    }
    
    fun maybeResetRelative() {
        // Count how many ticks this turn motor has been stationary;
        // if we've waited long enough, we take this as a cue to reset
        // relative encoders back to absolute.
        if (turnMotor.encoder.velocity < 0.3) {
            turnStationaryTicks += 1
        }
        if (turnStationaryTicks > 250) {
            resetRelative()
        }
    }

    fun drive(unoptimizedState: SwerveModuleState) {
        // Optimize the swerve module state (i.e., drive velocity
        // and turn position) so that we never turn more than 90 degrees.
        val rotation = Rotation2d(turnMotor.encoder.position)
        val state = SwerveModuleState.optimize(unoptimizedState, rotation)

        // Scale by cosine.  If the module is far from its
        // desired angle, then we drive it correspondingly less.
        state.speedMetersPerSecond *= (state.angle - rotation).getCos()

        // TODO: My understanding from last year's code is that
        // all modules have azimuth inversion and half have
        // drive inversion.
        driveMotor.setVoltage(
            drivePID.calculate(driveMotor.encoder.velocity, state.speedMetersPerSecond)
            + driveFeedforward.calculate(state.speedMetersPerSecond)
        )
        turnMotor.setVoltage(
            turnPID.calculate(turnMotor.encoder.position, state.angle.radians)
            + turnFeedforward.calculate(turnPID.setpoint.velocity)
        )
    }
}
