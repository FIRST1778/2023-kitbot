package org.chillout1778.subsystems
import edu.wpi.first.wpilibj2.command.SubsystemBase
import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel
import edu.wpi.first.wpilibj.XboxController
import org.chillout1778.Robot
import org.chillout1778.RobotContainer


object Shooter: SubsystemBase() {
    val leftMotor = CANSparkMax(15, CANSparkMaxLowLevel.MotorType.kBrushless)
    val rightMotor = CANSparkMax(16, CANSparkMaxLowLevel.MotorType.kBrushless)

    override fun periodic() {
        if (!Robot.isTeleop()) {
            return
        }
        val v = RobotContainer.getShooterAxis()
        leftMotor.set(v)
        rightMotor.set(v)
    }
}