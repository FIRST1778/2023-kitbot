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
        driveMotorId: Int, turnMotorId: Int, turnCanCoderId: Int,
        val encoderOffset: Double,
        val driveInversion: Double,
        val translation: Translation2d
) {
    private val driveMotor: CANSparkMax = Util.defaultNeo(driveMotorId)
    private val turnMotor: CANSparkMax  = Util.defaultNeo(turnMotorId)
    private val turnCanCoder: CANCoder = Util.defaultCanCoder(turnCanCoderId)
        .also { it.configMagnetOffset(encoderOffset) }

    private val drivePID: PIDController = Constants.Swerve.driveController()
    private val turnPID: ProfiledPIDController = Constants.Swerve.turnController()
        .also { it.enableContinuousInput(-Math.PI, Math.PI) }
    private val driveFeedforward: SimpleMotorFeedforward = Constants.Swerve.driveFeedforward()
    private val turnFeedforward:  SimpleMotorFeedforward = Constants.Swerve.turnFeedforward()

    private fun resetRelative() {
        turnMotor.encoder.position = turnCanCoder.position
    }
    
    init { resetRelative() }

    val state get() = SwerveModuleState(
        driveMotor.encoder.velocity, Rotation2d(turnMotor.encoder.position)
    )
    val position get() = SwerveModulePosition(
        driveMotor.encoder.position, Rotation2d(turnMotor.encoder.position)
    )

    private var turnStationaryTicks: Int = 0

    fun update(unoptimizedState: SwerveModuleState) {
        // Count how many ticks this turn motor has been stationary;
        // if we've waited long enough, we take this as a cue to reset
        // relative encoders back to absolute.
        if (turnMotor.encoder.velocity < 0.3) {
            turnStationaryTicks += 1
        } else {
            turnStationaryTicks = 0
        }
        if (turnStationaryTicks > 250) {
            turnStationaryTicks = 0
            resetRelative()
        }

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
        driveMotor.setVoltage(driveInversion * (
            drivePID.calculate(driveMotor.encoder.velocity, state.speedMetersPerSecond)
            + driveFeedforward.calculate(state.speedMetersPerSecond)
        ))
        turnMotor.setVoltage(-1.0 * (
            turnPID.calculate(turnMotor.encoder.position, state.angle.radians)
            + turnFeedforward.calculate(turnPID.setpoint.velocity)
        ))
    }
}
