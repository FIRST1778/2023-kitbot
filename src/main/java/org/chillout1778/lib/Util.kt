package org.chillout1778.lib

import com.ctre.phoenix6.hardware.TalonFX
import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel

class Util {
    companion object {
        fun neo(id: Int): CANSparkMax {
            val motor = CANSparkMax(id, CANSparkMaxLowLevel.MotorType.kBrushless)
            //motor.encoder.positionConversionFactor = Math.PI * 2.0
            //motor.encoder.velocityConversionFactor = Math.PI / 30.0
            return motor
        }

        fun clamp(n: Double, range: Double) = Math.min(range, Math.max(-range, n))
        fun deadband(n: Double) = if (Math.abs(n) < 0.1) 0.0 else n
        fun wrapAngle(n: Double): Double {
            var n2 = n % (2.0*Math.PI)
            if (n2 < 0.0)
                n2 += 2.0*Math.PI
            if (n2 > Math.PI)
                n2 -= 2*Math.PI
            if (n2 < -Math.PI || n2 > Math.PI) {
                println("Something is very wrong: $n2")
            }
            return n2
        }
    }
}
