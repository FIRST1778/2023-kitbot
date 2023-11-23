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

    // Available methods:
    // robotInit()      robotPeriodic()
    // disabledInit()   disabledPeriodic()   disabledExit()
    // autonomousInit() autonomousPeriodic() autonomousExit()
    // teleopInit()     teleopPeriodic()     teleopExit()
    // testInit()       testPeriodic()       testExit()

    @Override
    public void autonomousInit() {
        new AutonomousCommand().schedule();
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }

    @Override
    public void disabledInit() {
        Drive.i().setRightSpeed(0.0);
        Drive.i().setLeftSpeed(0.0);
    }
}
