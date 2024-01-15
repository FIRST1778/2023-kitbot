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
import com.ctre.phoenix6.signals.InvertedValue
import com.ctre.phoenix6.hardware.Pigeon2
import com.pathplanner.lib.auto.AutoBuilder

// Swerve math resources:
// https://dominik.win/blog/programming-swerve-drive/
// https://www.chiefdelphi.com/uploads/default/original/3X/e/f/ef10db45f7d65f6d4da874cd26db294c7ad469bb.pdf

object Swerve: SubsystemBase() {
    init {
        AutoBuilder.configureHolonomic(
            { odometry.getPoseMeters() },
            { odometry.resetPosition() },
            { kinematics.toChassisSpeeds(modules.map{it.state}.toTypedArray()) },
        )
    }

    val gyro = Pigeon2(21)

    init {
        defaultCommand = DriveCommand()
        gyro.reset()
    }

    private fun moduleTranslation(x: Double, y: Double) = 
        Translation2d(x,y) * Constants.Swerve.moduleXY

    val modules = arrayOf(
        SwerveModule(
            name = "front left",
            encoderOffset = Math.toRadians(335.8), // *****
            driveMotorId = 1,
            turnMotorId = 2,
            turnCanCoderId = 10,
            driveInversion = InvertedValue.Clockwise_Positive,
            translation = moduleTranslation(1.0, 1.0)
        ),
        SwerveModule(
            name = "front right",
            encoderOffset = Math.toRadians(161.3), // *****
            driveMotorId = 3,
            turnMotorId = 4,
            turnCanCoderId = 11,
            driveInversion = InvertedValue.CounterClockwise_Positive,
            translation = moduleTranslation(1.0, -1.0)
        ),
        SwerveModule(
            name = "back right",
            encoderOffset = Math.toRadians(15.6), // *****
            driveMotorId = 7,
            turnMotorId = 8,
            turnCanCoderId = 14,
            driveInversion = InvertedValue.CounterClockwise_Positive,
            translation = moduleTranslation(-1.0, -1.0)
        ),
        SwerveModule(
            name = "back left",
            encoderOffset = Math.toRadians(161.3), // *****
            driveMotorId = 5,
            turnMotorId = 6,
            turnCanCoderId = 13,
            driveInversion = InvertedValue.Clockwise_Positive,
            translation = moduleTranslation(-1.0, 1.0)
        ),
    )

    val kinematics = SwerveDriveKinematics(
        *modules.map{it.translation}.toTypedArray()
    )

    val odometry = SwerveDriveOdometry(
        kinematics,
        gyro.rotation2d,
        modules.map{it.position}.toTypedArray(),
        // TODO: optional argument here specifies our starting position.
        // We'd need to calculate this with values from SmartDashboard.
    )

    fun drive(x: Double, y: Double, rot: Double, fieldRelative: Boolean = true) {
        println("rot, ${rot}")
        println("x, ${x}")
        println("y, ${y}")
        // TODO: use ChassisSpeeds.discretize() once we have
        // WPILib 2024; this accounts for setting motor outputs
        // every 20ms instead of continuously.
        val states: Array<SwerveModuleState> = kinematics.toSwerveModuleStates(
            fieldRelative ? ChassisSpeeds.fromFieldRelativeSpeeds(x, y, rot, gyro.rotation2d)
                          : ChassisSpeeds(x, y, rot)
        )

        // Reduce module speeds so that none are faster than the
        // maximum.  The ratio between speeds is kept the same.
        SwerveDriveKinematics.desaturateWheelSpeeds(states,
            Constants.Swerve.maxSpeed)

        for (i in states.indices)
            modules[i].drive(states[i])

        odometry.update(
            gyro.rotation2d,
            modules.map{it.position}.toTypedArray()
        )
    }
}
