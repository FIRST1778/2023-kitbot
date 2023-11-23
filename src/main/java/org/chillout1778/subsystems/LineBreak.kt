package org.chillout1778.subsystems

import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj2.command.SubsystemBase

object LineBreak : SubsystemBase() {
    // A line break is a proximity sensor that detects when objects
    // break its line of sight.  One half of the line break fires
    // an infrared signal, the other half detects it.
    private val input = DigitalInput(1)
    private var enabled = true
    fun broken(): Boolean {
        return enabled && !input.get()
    }

    fun toggle() {
        enabled = !enabled
    }
}
