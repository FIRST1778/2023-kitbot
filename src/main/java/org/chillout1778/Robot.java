package org.chillout1778;

import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import org.chillout1778.Constants;
import org.chillout1778.commands.DriveCommand;
import org.chillout1778.subsystems.Controls;
import org.chillout1778.subsystems.Drive;
import org.chillout1778.subsystems.Gyro;

public class Robot extends TimedRobot {
    PowerDistribution pdh = new PowerDistribution(1, PowerDistribution.ModuleType.kRev);
//    LineBreak lineBreak = new LineBreak();
//    MotorSpin motor = new MotorSpin();
//    Gyro gyro = new Gyro();
    public static final Drive drive = new Drive();


    @Override
    public void robotInit(){

    }
    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }

    @Override
    public void teleopInit() {

    }

    @Override
    public void teleopPeriodic() {
//        Drive.update();
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
        drive.setRightSpeed(0.0);
        drive.setLeftSpeed(0.0);
    }

}
