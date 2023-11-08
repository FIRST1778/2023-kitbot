package org.chillout1778;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class MotorSpin extends SubsystemBase {
    private CANSparkMax motor;
    private boolean spinning;
    MotorSpin(){
        spinning = false;
        motor = new CANSparkMax(11, CANSparkMaxLowLevel.MotorType.kBrushless);
    }
    void spin(){
        // This 'if' statement prevents you from feeding constant voltage commands in the periodic function
        if(!spinning) {
            // Add code to spin 'motor' at 2 volts here
        }
    }
    void stop(){
        spinning = false;
        // Add code to stop 'motor' here
    }
}
