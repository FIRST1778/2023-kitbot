package org.chillout1778.subsystems

import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.kinematics.SwerveDriveKinematics
import edu.wpi.first.math.kinematics.SwerveDriveOdometry
import edu.wpi.first.math.kinematics.SwerveModuleState
import edu.wpi.first.math.trajectory.TrapezoidProfile
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.chillout1778.Constants
import org.chillout1778.subsystems.SwerveModule

// Most important class in the codebase.
// General comments on swerve drive and our implementation of it
// are at the bottom of this file.

object Swerve: SubsystemBase() {
    private fun moduleTranslation(x: Double, y: Double) = 
        Translation2d(x,y) * Constants.Swerve.moduleXY

    val modules = arrayOf(
        SwerveModule(
            name = "front left",
            driveMotorId = 6,
            turnMotorId = 5,
            turnCanCoderId = 10,
            encoderOffset = Math.toRadians(66.23),
            driveInverted = true,
            translation = moduleTranslation(1.0, 1.0)
        ),
        SwerveModule(
            name = "front right",
            driveMotorId = 8,
            turnMotorId = 7,
            turnCanCoderId = 11,
            encoderOffset = Math.toRadians(252.4),
            driveInverted = false,
            translation = moduleTranslation(1.0, -1.0)
        ),
        SwerveModule(
            name = "back right",
            driveMotorId = 2,
            turnMotorId = 1,
            turnCanCoderId = 12,
            encoderOffset = Math.toRadians(109.1),
            driveInverted = false,
            translation = moduleTranslation(-1.0, -1.0)
        ),
        SwerveModule(
            name = "back left",
            driveMotorId = 4,
            turnMotorId = 3,
            turnCanCoderId = 13,
            encoderOffset = Math.toRadians(250.2),
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

// WPILib does swerve math for us, but you can still learn how it works:
//
// https://www.chiefdelphi.com/uploads/default/original/3X/e/f/ef10db45f7d65f6d4da874cd26db294c7ad469bb.pdf
// https://dominik.win/blog/programming-swerve-drive/
//
// (The latter is more intuitive because it uses vectors
// instead of trigonometry, and is more mathematically general.)
//
// We use both a CANCoder (absolute) and the NEO's built-in
// encoder (relative, but faster) to measure swerve azimuth.
// Last year, we implemented a hack that would reset the NEO
// to the CANCoder if the motor didn't move for 10 seconds,
// because there was an edge case where the CANCoder wouldn't be
// accurate at the start of the match.  We can remove this hack
// or simplify (only use the CANCoder) if we test properly.
//
// https://store.ctr-electronics.com/blog/cancoder-firmware-update-22012/
// https://discord.com/channels/887922855084425266/890436659450118254/1170883077195714590
// https://github.com/SwerveDriveSpecialties/Do-not-use-swerve-lib-2022-unmaintained/blob/55f3f1ad9e6bd81e56779d022a40917aacf8d3b3/src/main/java/com/swervedrivespecialties/swervelib/rev/NeoSteerControllerFactoryBuilder.java#L128C3-L128C3
// https://github.com/FIRST1778/2023-Robot-Code/blob/36a38de7a1d7970517fe18e7506582187ec46491/src/main/java/org/frc1778/lib/FalconNeoSwerveModule.kt#L127
