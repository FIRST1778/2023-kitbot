package org.chillout1778;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class MotorSpin extends SubsystemBase {
    private CANSparkMax motor = new CANSparkMax(11, CANSparkMaxLowLevel.MotorType.kBrushless);
    private boolean spinning = false;

    void spin() {
        if (spinning)
            return;
        // TODO: add code to start motor
        spinning = true;
    }
    void stop() {
        if (!spinning)
            return;
        // TODO: add code to stop motor
        spinning = false;
    }
}
