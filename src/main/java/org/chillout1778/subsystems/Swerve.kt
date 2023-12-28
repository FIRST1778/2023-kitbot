package org.chillout1778.subsystems

import com.ctre.phoenix6.hardware.Pigeon2
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.kinematics.*
import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.chillout1778.Constants
import org.chillout1778.subsystems.SwerveModule


class Swerve : SubsystemBase() {
    var swerveOdometry: SwerveDriveOdometry
    var mSwerveMods: Array<SwerveModule>
    var gyro: Pigeon2

    init {
        gyro = Pigeon2(Constants.Swerve.pigeonID)
        zeroGyro()
        mSwerveMods = arrayOf<SwerveModule>(
            SwerveModule(0, Constants.Swerve.Mod0.constants),
            SwerveModule(1, Constants.Swerve.Mod1.constants),
            SwerveModule(2, Constants.Swerve.Mod2.constants),
            SwerveModule(3, Constants.Swerve.Mod3.constants)
        )

        /* By pausing init for a second before setting module offsets, we avoid a bug with inverting motors.
         * See https://github.com/Team364/BaseFalconSwerve/issues/8 for more info.
         */Timer.delay(1.0)
        resetModulesToAbsolute()
        swerveOdometry = SwerveDriveOdometry(Constants.Swerve.swerveKinematics, yaw, modulePositions)
    }

    fun drive(translation: Translation2d, rotation: Double, fieldRelative: Boolean, isOpenLoop: Boolean) {
        val swerveModuleStates: Array<SwerveModuleState> = Constants.Swerve.swerveKinematics.toSwerveModuleStates(
            if (fieldRelative) ChassisSpeeds.fromFieldRelativeSpeeds(
                translation.x,
                translation.y,
                rotation,
                yaw
            ) else ChassisSpeeds(
                translation.x,
                translation.y,
                rotation
            )
        )
        SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, Constants.Swerve.maxSpeed)
        for (mod in mSwerveMods) {
            mod.setDesiredState(swerveModuleStates[mod.moduleNumber], isOpenLoop)
        }
    }

    val pose: Pose2d
        get() = swerveOdometry.poseMeters

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