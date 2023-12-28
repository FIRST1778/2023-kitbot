package org.chillout1778.subsystems

import edu.wpi.first.math.controller.HolonomicDriveController
import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.math.controller.ProfiledPIDController
import edu.wpi.first.math.controller.SimpleMotorFeedforward
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.kinematics.SwerveDriveKinematics
import edu.wpi.first.math.kinematics.SwerveDriveOdometry
import edu.wpi.first.math.trajectory.TrapezoidProfile
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.chillout1778.Constants
import org.chillout1778.subsystems.SwerveModule

object Swerve: SubsystemBase() {
    private fun moduleTranslation(x: Double, y: Double) = 
        Translation2d(x,y) * Constants.Swerve.moduleXY

    val modules = arrayOf(
        SwerveModule(
            // Front left
            driveMotorId = 6,
            turnMotorId = 5,
            turnCanCoderId = 10,
            encoderOffset = Math.toRadians(66.23),
            inverted = true,
            translation = moduleTranslation(1.0, 1.0)
        ),
        SwerveModule(
            // Front right
            driveMotorId = 8,
            turnMotorId = 7,
            turnCanCoderId = 11,
            encoderOffset = Math.toRadians(252.4),
            inverted = true,
            translation = moduleTranslation(1.0, -1.0)
        ),
        SwerveModule(
            // Back right
            driveMotorId = 2,
            turnMotorId = 1,
            turnCanCoderId = 12,
            encoderOffset = Math.toRadians(109.1),
            inverted = true,
            translation = moduleTranslation(-1.0, -1.0)
        ),
        SwerveModule(
            // Back left
            driveMotorId = 4,
            turnMotorId = 3,
            turnCanCoderId = 13,
            encoderOffset = Math.toRadians(250.2),
            inverted = true,
            translation = moduleTranslation(-1.0, 1.0)
        ),
    )

    val kinematics = SwerveDriveKinematics(
        *modules.map{it.translation}.toTypedArray()
    )

    // TODO: do properly
    val odometry = SwerveDriveOdometry(
        kinematics,
        Gyro.rotation,
        modules.map{it.position}.toTypedArray()
    )

    // TODO: ************************ CRITICAL TO TUNE vvvvv
    // These values, other than the TrapezoidProfile, are taken from
    // last year's codebase.  We should try to improve them.
    private val feedforward = SimpleMotorFeedforward(0.0, 0.0, 0.0)
    private val controller = HolonomicDriveController(
        PIDController(  // X controller
            0.75,
            0.0,
            0.15
        ),
        PIDController(  // Y controller
            0.75,
            0.0,
            0.15
        ),
        ProfiledPIDController(  // rotation controller
            0.2,
            0.0,
            0.02,
            TrapezoidProfile.Constraints(
                Constants.Swerve.theoreticalMaxAngularSpeed,
                Constants.Swerve.maxAngularAcceleration
            )
        )
    )

    fun update(x: Double, y: Double, rot: Double) {
        val states = kinematics.toSwerveModuleStates(
            // TODO: use ChassisSpeeds.discretize() once we have
            // WPILib 2024
            ChassisSpeeds.fromFieldRelativeSpeeds(
                x, y, rot, Gyro.rotation
            )
        )
    }

    fun updateOdometry() {
        odometry.update(
            Gyro.rotation,
            modules.map{it.position}.toTypedArray()
        )
    }

    fun disable() {
        // TODO: We need to hard brake here
    }
}
