package org.chillout1778.lib

import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel
import com.ctre.phoenix.sensors.CANCoder
import com.ctre.phoenix.sensors.CANCoderConfiguration
import com.ctre.phoenix.sensors.SensorTimeBase

class Util {
    companion object {
        fun defaultNeo(id: Int): CANSparkMax {
            val motor = CANSparkMax(id, CANSparkMaxLowLevel.MotorType.kBrushless)
            motor.encoder.positionConversionFactor = Math.PI * 2.0
            motor.encoder.velocityConversionFactor = Math.PI / 30.0
            return motor
        }

        fun defaultCanCoder(id: Int): CANCoder {
            // TODO: start in absolute mode
            val cancoder = CANCoder(id)
            cancoder.configFeedbackCoefficient(2*Math.PI/4096.0, "rad", SensorTimeBase.PerSecond)
            return cancoder
        }
    }
}
