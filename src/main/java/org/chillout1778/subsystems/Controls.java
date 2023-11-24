package org.chillout1778.subsystems;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import org.chillout1778.Robot;
import org.chillout1778.Constants;

public class Controls {
    private static Controls instance;
    public static Controls getInstance() {
        if (instance == null) instance = new Controls();
        return instance;
    }

    private final Joystick driver = new Joystick(0);

    private static double deadzone(double n) {
        if (Math.abs(n) < 0.1) {
            return 0.0;
        } else {
            return n;
        }
    }

    public double driveFactor() {
        return -0.5 * deadzone(driver.getRawAxis(Constants.Controls.driveAxisID));
    }

    public double turnFactor() {
        return 0.5 * deadzone(driver.getRawAxis(Constants.Controls.turnAxisID));
    }
}
