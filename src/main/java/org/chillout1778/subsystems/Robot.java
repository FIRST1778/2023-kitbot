package org.chillout1778.subsystems;

import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.TimedRobot;
import org.chillout1778.Constants;
import org.chillout1778.subsystems.Controls;
import org.chillout1778.subsystems.Drive;
import org.chillout1778.subsystems.Gyro;

public class Robot extends TimedRobot {
    PowerDistribution pdh = new PowerDistribution(1, PowerDistribution.ModuleType.kRev);
//    LineBreak lineBreak = new LineBreak();
//    MotorSpin motor = new MotorSpin();
    Gyro gyro = new Gyro();

    @Override
    public void teleopPeriodic() {

    }

    @Override
    public void disabledInit() {
        // TODO: add code to spin the motor if the linebreak is unbroken,
        // otherwise stop it
        /*if (lineBreak.broken()) {
             motor.stop();
        } else {
            motor.spin();
        }*/
    }
}
