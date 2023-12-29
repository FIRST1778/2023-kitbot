package org.chillout1778.subsystems

import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.kinematics.SwerveDriveKinematics
import edu.wpi.first.math.kinematics.SwerveDriveOdometry
import edu.wpi.first.math.kinematics.SwerveModuleState
import edu.wpi.first.math.trajectory.TrapezoidProfile
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.chillout1778.commands.DriveCommand
import org.chillout1778.Constants
import org.chillout1778.subsystems.SwerveModule

// Swerve math resources:
// https://dominik.win/blog/programming-swerve-drive/
// https://www.chiefdelphi.com/uploads/default/original/3X/e/f/ef10db45f7d65f6d4da874cd26db294c7ad469bb.pdf

object Swerve: SubsystemBase() {
    init {
        setDefaultCommand(DriveCommand())
    }

    private fun moduleTranslation(x: Double, y: Double) = 
        Translation2d(x,y) * Constants.Swerve.moduleXY

    val modules = arrayOf(
        SwerveModule(
            name = "front left",
            encoderOffset = Math.toRadians(66.23), // *****
            driveMotorId = 6,
            turnMotorId = 5,
            turnCanCoderId = 10,
            driveInverted = true,
            translation = moduleTranslation(1.0, 1.0)
        ),
        SwerveModule(
            name = "front right",
            encoderOffset = Math.toRadians(252.4), // *****
            driveMotorId = 8,
            turnMotorId = 7,
            turnCanCoderId = 11,
            driveInverted = false,
            translation = moduleTranslation(1.0, -1.0)
        ),
        SwerveModule(
            name = "back right",
            encoderOffset = Math.toRadians(109.1), // *****
            driveMotorId = 2,
            turnMotorId = 1,
            turnCanCoderId = 12,
            driveInverted = false,
            translation = moduleTranslation(-1.0, -1.0)
        ),
        SwerveModule(
            name = "back left",
            encoderOffset = Math.toRadians(250.2), // *****
            driveMotorId = 4,
            turnMotorId = 3,
            turnCanCoderId = 13,
            driveInverted = true,
            translation = moduleTranslation(-1.0, 1.0)
        ),
    )

    val kinematics = SwerveDriveKinematics(
        *modules.map{it.translation}.toTypedArray()
    )

    // TODO: double check
    val odometry = SwerveDriveOdometry(
        kinematics,
        Gyro.rotation,
        modules.map{it.position}.toTypedArray()
    )

    fun drive(x: Double, y: Double, rot: Double) {
        // TODO: use ChassisSpeeds.discretize() once we have
        // WPILib 2024; this accounts for setting motor outputs
        // every 20ms instead of continuously.
        val states: Array<SwerveModuleState> = kinematics.toSwerveModuleStates(
            ChassisSpeeds.fromFieldRelativeSpeeds(
                x, y, rot, Gyro.rotation
            )
        )

        // Reduce module speeds so that none are faster than the
        // maximum.  The ratio between speeds is kept the same.
        SwerveDriveKinematics.desaturateWheelSpeeds(states,
            Constants.Swerve.maxSpeed)

        for (i in states.indices)
            modules[i].drive(states[i])

        odometry.update(
            Gyro.rotation,
            modules.map{it.position}.toTypedArray()
        )
    }
}
