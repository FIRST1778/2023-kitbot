package org.chillout1778

import com.ctre.phoenix6.configs.MotorOutputConfigs
import com.ctre.phoenix6.signals.NeutralModeValue
import com.revrobotics.CANSparkMax.IdleMode

import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.kinematics.SwerveDriveKinematics
import edu.wpi.first.math.util.Units
import org.chillout1778.lib.util.COTSFalconSwerveConstants
import org.chillout1778.lib.util.SwerveModuleConstants


object Constants {

    object Pigeon {
        const val canId = 21
    }

    object Autonomous {
        const val driveSpeed = 0.1
        const val timeDriving = 4.0
    }

    object Controls {
        const val driveAxisID = 1
        const val turnAxisID = 4
        val stickDeadband: Double = 0.02
    }

    object Swerve {
        const val pigeonID = 1
        const val invertGyro = false // Always ensure Gyro is CCW+ CW-


        val chosenModule =
            COTSFalconSwerveConstants.SDSMK4i(COTSFalconSwerveConstants.driveGearRatios.SDSMK4i_L3)

        /* Drivetrain Constants */
        val trackWidth = Units.inchesToMeters(23.5)

        val wheelBase = Units.inchesToMeters(23.5)

        val wheelCircumference = chosenModule.wheelCircumference

        /* Swerve Kinematics
         * No need to ever change this unless you are not doing a traditional rectangular/square 4 module swerve */
        val swerveKinematics = SwerveDriveKinematics(
            Translation2d(wheelBase / 2.0, trackWidth / 2.0),
            Translation2d(wheelBase / 2.0, -trackWidth / 2.0),
            Translation2d(-wheelBase / 2.0, trackWidth / 2.0),
            Translation2d(-wheelBase / 2.0, -trackWidth / 2.0)
        )

        /* Module Gear Ratios */
        val driveGearRatio = chosenModule.driveGearRatio
        val angleGearRatio = chosenModule.angleGearRatio

        /* Motor Inverts */
        val angleMotorInvert = chosenModule.angleMotorInvert
        val driveMotorInvert = chosenModule.driveMotorInvert

        /* Angle Encoder Invert */
        val canCoderInvert = chosenModule.canCoderInvert

        /* Swerve Current Limiting */
        const val angleContinuousCurrentLimit = 25
        const val anglePeakCurrentLimit = 40.0
        const val anglePeakCurrentDuration = 0.1
        const val angleEnableCurrentLimit = true

        const val driveContinuousCurrentLimit = 35
        const val drivePeakCurrentLimit = 60.0
        const val drivePeakCurrentDuration = 0.1
        const val driveEnableCurrentLimit = true

        /* These values are used by the drive falcon to ramp in open loop and closed loop driving.
         * We found a small open loop ramp (0.25) helps with tread wear, tipping, etc */
        const val openLoopRamp = 0.25
        const val closedLoopRamp = 0.0

        /* Angle Motor PID Values */
        val angleKP = chosenModule.angleKP
        val angleKI = chosenModule.angleKI
        val angleKD = chosenModule.angleKD
        val angleKF = chosenModule.angleKF

        /* Drive Motor PID Values */
        const val driveKP = 0.05 //TODO: This must be tuned to specific robot

        const val driveKI = 0.0
        const val driveKD = 0.0
        const val driveKF = 0.0

        /* Drive Motor Characterization Values
         * Divide SYSID values by 12 to convert from volts to percent output for CTRE */
        const val driveKS = 0.32 / 12 //TODO: This must be tuned to specific robot

        const val driveKV = 1.51 / 12
        const val driveKA = 0.27 / 12

        /* Swerve Profiling Values */
        /* Swerve Profiling Values */
        /** Meters per Second  */
        const val maxSpeed = 4.5 //TODO: This must be tuned to specific robot

        /** Radians per Second  */
        const val maxAngularVelocity = 10.0 //TODO: This must be tuned to specific robot


        /* Neutral Modes */
        val angleNeutralMode: IdleMode = IdleMode.kCoast
        val driveNeutralMode: NeutralModeValue = NeutralModeValue.Brake

        /* Module Specific Constants */ /* Front Left Module - Module 0 */
        object Mod0 {
            //TODO: This must be tuned to specific robot
            const val driveMotorID = 1
            const val angleMotorID = 2
            const val canCoderID = 1
            val angleOffset = Rotation2d.fromDegrees(0.0)
            val constants = SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset)
        }


        /* Front Right Module - Module 1 */
        object Mod1 {
            //TODO: This must be tuned to specific robot
            const val driveMotorID = 3
            const val angleMotorID = 4
            const val canCoderID = 2
            val angleOffset = Rotation2d.fromDegrees(0.0)
            val constants = SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset)
        }


        /* Back Left Module - Module 2 */
        object Mod2 {
            //TODO: This must be tuned to specific robot
            const val driveMotorID = 5
            const val angleMotorID = 6
            const val canCoderID = 3
            val angleOffset = Rotation2d.fromDegrees(0.0)
            val constants = SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset)
        }


        /* Back Right Module - Module 3 */
        object Mod3 {
            //TODO: This must be tuned to specific robot
            const val driveMotorID = 7
            const val angleMotorID = 8
            const val canCoderID = 4
            val angleOffset = Rotation2d.fromDegrees(0.0)
            val constants = SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset)
        }

    }
}
