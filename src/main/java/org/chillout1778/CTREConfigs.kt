package org.chillout1778

import com.ctre.phoenix6.configs.CANcoderConfiguration
import com.ctre.phoenix6.configs.CurrentLimitsConfigs
import com.ctre.phoenix6.configs.TalonFXConfiguration
import com.ctre.phoenix6.signals.AbsoluteSensorRangeValue
import com.ctre.phoenix6.signals.SensorDirectionValue


class CTREConfigs {
    var swerveAngleFXConfig: TalonFXConfiguration
    var swerveDriveFXConfig: TalonFXConfiguration
    var swerveCanCoderConfig: CANcoderConfiguration

    init {
        swerveAngleFXConfig = TalonFXConfiguration()
        swerveDriveFXConfig = TalonFXConfiguration()
        swerveCanCoderConfig = CANcoderConfiguration()

        /* Swerve Angle Motor Configurations */

        swerveAngleFXConfig.Slot0.kP = Constants.Swerve.angleKP
        swerveAngleFXConfig.Slot0.kI = Constants.Swerve.angleKI
        swerveAngleFXConfig.Slot0.kD = Constants.Swerve.angleKD
        swerveAngleFXConfig.CurrentLimits.SupplyCurrentLimit = Constants.Swerve.anglePeakCurrentLimit

        /* Swerve Drive Motor Configuration */

        swerveDriveFXConfig.Slot0.kP = Constants.Swerve.driveKP
        swerveDriveFXConfig.Slot0.kI = Constants.Swerve.driveKI
        swerveDriveFXConfig.Slot0.kD = Constants.Swerve.driveKD
        swerveDriveFXConfig.CurrentLimits.SupplyCurrentLimit = Constants.Swerve.drivePeakCurrentLimit
        swerveDriveFXConfig.OpenLoopRamps.TorqueOpenLoopRampPeriod = Constants.Swerve.openLoopRamp //TODO: Verify that Torque is correct
        swerveDriveFXConfig.ClosedLoopRamps.TorqueClosedLoopRampPeriod = Constants.Swerve.closedLoopRamp //TODO: Verify that Torque is correct

        /* Swerve CANCoder Configuration */
        swerveCanCoderConfig.MagnetSensor.AbsoluteSensorRange =
            AbsoluteSensorRangeValue.Unsigned_0To1
        swerveCanCoderConfig.MagnetSensor.SensorDirection =
            if(Constants.Swerve.canCoderInvert) SensorDirectionValue.Clockwise_Positive
            else SensorDirectionValue.CounterClockwise_Positive
    }
}
