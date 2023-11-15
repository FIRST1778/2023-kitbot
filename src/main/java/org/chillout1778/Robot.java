package org.chillout1778;

import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {
    PowerDistribution pdh = new PowerDistribution(1, PowerDistribution.ModuleType.kRev);
//    LineBreak lineBreak = new LineBreak();
//    MotorSpin motor = new MotorSpin();
    Controls controls = new Controls();
    Gyro gyro = new Gyro();

    @Override
    public void teleopPeriodic() {
        // TODO: add code to spin the motor if the linebreak is unbroken,
        // otherwise stop it
        if (lineBreak.broken()) {
             motor.stop();
        } else {
            motor.spin();
        }
    }
}
