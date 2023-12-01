package org.chillout1778.lib

import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel

class NEO(id: Int) {
    private val motor = CANSparkMax(id, CANSparkMaxLowLevel.MotorType.kBrushless)
    init {
        motor.encoder.positionConversionFactor = Math.PI * 2.0
        motor.encoder.velocityConversionFactor = Math.PI / 30.0
    }
    var position: Double
        get() = motor.encoder.position
        set(n) { motor.encoder.position = n }
    val velocity: Double
        get() = motor.encoder.velocity
    var voltage: Double = 0.0
        set(n) { 
            voltage = n
            motor.setVoltage(n)
        }
}