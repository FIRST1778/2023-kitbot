package org.chillout1778.lib

import com.ctre.phoenix6.hardware.TalonFX


class Falcon(id: Int) { //only using for drive motor
    private val motor = TalonFX(id)
    val velocity : Double
        get() = motor.velocity.value // returns rotations/second
    var voltage : Double
        get() = motor.supplyVoltage.value
        set(n) = motor.setVoltage(n)

}