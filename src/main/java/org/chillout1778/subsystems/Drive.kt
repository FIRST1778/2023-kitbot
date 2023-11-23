package org.chillout1778.subsystems

import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.chillout1778.commands.DriveCommand

object Drive : SubsystemBase() {
    private val leftParentMotor = CANSparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless)
    private val leftChildMotor = CANSparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless)
    private val rightParentMotor = CANSparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless)
    private val rightChildMotor = CANSparkMax(4, CANSparkMaxLowLevel.MotorType.kBrushless)

    init {
        defaultCommand = DriveCommand()
        leftChildMotor.follow(leftParentMotor)
        rightChildMotor.follow(rightParentMotor)
    }

    fun setRightSpeed(speed: Double) {
        rightParentMotor.set(speed)
    }

    fun setLeftSpeed(speed: Double) {
        leftParentMotor.set(speed)
    }

    fun setBothSpeeds(speed: Double){
        setRightSpeed(speed)
        setLeftSpeed(speed)
    }
}
