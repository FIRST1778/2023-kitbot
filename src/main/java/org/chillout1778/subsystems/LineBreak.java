package org.chillout1778.subsystems;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DigitalInput;

public class LineBreak extends SubsystemBase {
    // A line break is a proximity sensor that detects when objects
    // break its line of sight.  One half of the line break fires
    // an infrared signal, the other half detects it.

    private DigitalInput input = new DigitalInput(1);
    private Boolean enabled = true;

    public Boolean broken() {
        return enabled && !input.get();
    }

    public void toggle() {
        enabled = !enabled;
    }
}
