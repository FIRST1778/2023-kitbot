package org.chillout1778.subsystems

import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel
import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.math.util.Units
import edu.wpi.first.wpilibj2.command.SubsystemBase

object MotorSpin : SubsystemBase() {
    private const val kP = 1.0
    private const val kI = 0.0
    private const val kD = 0.0
    private val motor = CANSparkMax(
        11,
        CANSparkMaxLowLevel.MotorType.kBrushless
    )
    private val controller = PIDController(kP, kI, kD)
    private var desired = 0.0 // rad/s
    fun spin() {
        desired = 0.1
    }

    fun stop() {
        desired = 0.0
    }

    private fun angularVelocity(): Double {
        return Units.rotationsPerMinuteToRadiansPerSecond(
            motor.getEncoder().velocity
        )
    }

    override fun periodic() {
        val voltage = controller.calculate(angularVelocity(), desired)
        motor.setVoltage(voltage)
    }
}
