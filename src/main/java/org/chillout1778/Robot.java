package org.chillout1778;

import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import org.chillout1778.Constants;
import org.chillout1778.commands.AutonomousCommand;
import org.chillout1778.commands.DriveCommand;
import org.chillout1778.subsystems.Controls;
import org.chillout1778.subsystems.Drive;
import org.chillout1778.subsystems.Gyro;

public class Robot extends TimedRobot {
    private final PowerDistribution pdh = new PowerDistribution(1, PowerDistribution.ModuleType.kRev);
    private final Drive drive = new Drive();

    @Override
    public void robotInit() {
        // If the field is asymmetric rotationally (aka, things can be
        // on your left or your right depending on the alliance), we
        // would add the inversion logic here.
    }

    @Override
    public void autonomousInit() {
        new AutonomousCommand(drive).schedule();
    }

    @Override
    public void teleopInit() {}

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
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
