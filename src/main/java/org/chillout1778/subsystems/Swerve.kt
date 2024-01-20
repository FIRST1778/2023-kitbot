package org.chillout1778.subsystems

import com.ctre.phoenix6.hardware.Pigeon2
import com.pathplanner.lib.auto.AutoBuilder
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.kinematics.*
import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.chillout1778.Constants
import org.chillout1778.lib.SwerveModule
import java.util.function.Supplier

import com.pathplanner.lib.util.HolonomicPathFollowerConfig
import com.pathplanner.lib.util.PIDConstants
import com.pathplanner.lib.util.ReplanningConfig
import edu.wpi.first.wpilibj.DriverStation

object Swerve : SubsystemBase() {
    var swerveOdometry: SwerveDriveOdometry
//    var swerveKinematics : SwerveDriveKinematics
    var mSwerveMods: Array<SwerveModule>
    var gyro: Pigeon2

    init {
        gyro = Pigeon2(Constants.Swerve.pigeonID)
        zeroGyro()
        mSwerveMods = arrayOf<SwerveModule>(
            SwerveModule(0, Constants.Swerve.Mod0.constants, true),
            SwerveModule(1, Constants.Swerve.Mod1.constants, false),
            SwerveModule(2, Constants.Swerve.Mod2.constants, true),
            SwerveModule(3, Constants.Swerve.Mod3.constants, false)
        )

        /* By pausing init for a second before setting module offsets, we avoid a bug with inverting motors.
         * See https://github.com/Team364/BaseFalconSwerve/issues/8 for more info.
         */
        Timer.delay(1.0)
        resetModulesToAbsolute()
        swerveOdometry = SwerveDriveOdometry(Constants.Swerve.swerveKinematics, yaw, modulePositions)

        //AUTO, last part of init
//        AutoBuilder.configureHolonomic(
//            { pose },
//            { resetOdometry(pose) },
//            {  }
//
//
//
//
//        )
        AutoBuilder.configureHolonomic(
            { swerveOdometry.getPoseMeters() },
            { pose: Pose2d -> swerveOdometry.resetPosition(yaw, modulePositions, pose) },
            { Constants.Swerve.swerveKinematics.toChassisSpeeds(*moduleStates) },
            { speeds: ChassisSpeeds -> driveChassisSpeeds(speeds, false) },
            HolonomicPathFollowerConfig(
                PIDConstants(5.0,0.0,0.0),
                PIDConstants(5.0,0.0,0.0),
                4.5,
                Constants.Swerve.radius,
                ReplanningConfig()
            ),
            {
                val alliance = DriverStation.getAlliance()
                alliance.isPresent() && alliance.get() == DriverStation.Alliance.Red
            },
            this
        )
    }

    fun driveChassisSpeeds(speeds: ChassisSpeeds, isOpenLoop: Boolean) {
        val swerveModuleStates: Array<SwerveModuleState> = Constants.Swerve.swerveKinematics.toSwerveModuleStates(speeds)
        SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, Constants.Swerve.maxSpeed)
        for (mod in mSwerveMods) {
            mod.setDesiredState(swerveModuleStates[mod.moduleNumber], isOpenLoop)
        }
    }

    fun drive(translation: Translation2d, rotation: Double, fieldRelative: Boolean, isOpenLoop: Boolean) {
        driveChassisSpeeds(
            if (fieldRelative) ChassisSpeeds.fromFieldRelativeSpeeds(
                translation.x,
                translation.y,
                rotation,
                yaw
            ) else ChassisSpeeds(
                translation.x,
                translation.y,
                rotation
            ),
            isOpenLoop
        )
    }

    val pose : Pose2d
        get() = swerveOdometry.poseMeters

//    val chassisSpeeds: ChassisSpeeds
//        get() = swerveKinematics.toChassisSpeeds()


    fun resetOdometry(pose: Pose2d?) {
        swerveOdometry.resetPosition(yaw, modulePositions, pose)
    }

    var moduleStates: Array<SwerveModuleState?>
        get() {
            val states = arrayOfNulls<SwerveModuleState>(4)
            for (mod in mSwerveMods) {
                states[mod.moduleNumber] = mod.state
            }
            return states
        }
        /* Used by SwerveControllerCommand in Auto */ set(desiredStates) {
            SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, Constants.Swerve.maxSpeed)
            for (mod in mSwerveMods) {
                mod.setDesiredState(desiredStates[mod.moduleNumber]!!, false)
            }
        }
    val modulePositions: Array<SwerveModulePosition?>
        get() {
            val positions = arrayOfNulls<SwerveModulePosition>(4)
            for (mod in mSwerveMods) {
                positions[mod.moduleNumber] = mod.position
            }
            return positions
        }

    fun zeroGyro() {
        gyro.setYaw(0.0)
    }

    val yaw: Rotation2d
        get() = if (Constants.Swerve.invertGyro) Rotation2d.fromDegrees(360 - gyro.yaw.value) else Rotation2d.fromDegrees(
            gyro.yaw.value
        )

    fun resetModulesToAbsolute() {
        for (mod in mSwerveMods) {
            mod.resetToAbsolute()
        }
    }

    override fun periodic() {
        swerveOdometry.update(yaw, modulePositions)
        for (mod in mSwerveMods) {
            SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Cancoder", mod.canCoder.degrees)
            SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Integrated", mod.position.angle.degrees)
            SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Velocity", mod.state.speedMetersPerSecond)
        }
    }

}