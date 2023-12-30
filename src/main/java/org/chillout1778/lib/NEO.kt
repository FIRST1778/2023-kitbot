package org.chillout1778.lib

import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMax.IdleMode
import com.revrobotics.CANSparkMaxLowLevel
import com.revrobotics.SparkMaxPIDController

class NEO(id: Int) {
    val motor = CANSparkMax(id, CANSparkMaxLowLevel.MotorType.kBrushless)
    init {
//        motor.encoder.positionConversionFactor = (1.0/42) * Math.PI * 2.0 // radians
//        motor.encoder.velocityConversionFactor = (1.0/42) * Math.PI / 30.0 // radians
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
    val controller: SparkMaxPIDController
        get() = motor.pidController

    var inverted : Boolean
        get() = motor.inverted
        set(n) {motor.inverted = n}

    var neutralMode : IdleMode
        get() = motor.idleMode
        set(n) {motor.idleMode = n}

}