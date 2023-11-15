package org.chillout1778;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;

public final class Controls {
    public Joystick driverController;

    Controls(){
        driverController = new Joystick(1);
    }
}
