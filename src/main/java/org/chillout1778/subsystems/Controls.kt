package org.chillout1778.subsystems

import edu.wpi.first.wpilibj.Joystick

object Controls {
    val driver = Joystick(0)
    fun x() = driver.getRawAxis(2)
    fun y() = driver.getRawAxis(3)
    fun rot() = driver.getRawAxis(0)
    // Autoalign: driver.getRawButton(1)
}
