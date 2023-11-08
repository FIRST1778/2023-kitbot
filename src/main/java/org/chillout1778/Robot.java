package org.chillout1778;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;

public class Robot extends TimedRobot {

    // Setting PDH ID
    PowerDistribution pdh;

    Robot(){
        pdh = new PowerDistribution(1, PowerDistribution.ModuleType.kRev);
    }
    @Override
    public void teleopPeriodic() {
        // Add code to spin the motor only if the linebreak isn't broken, and otherwise stop it
    }
}
