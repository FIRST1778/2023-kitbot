package org.chillout1778.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import org.chillout1778.Constants;
import org.chillout1778.subsystems.Drive;

public class AutonomousCommand extends CommandBase {
    private Timer timer = new Timer();
    public AutonomousCommand() {
        addRequirements(Drive.i());
    }

    @Override
    public void initialize() {
        timer.restart();
    }

    @Override
    public void execute() {
        // Go forward / back for two seconds
        if (timer.get() < Constants.Autonomous.timeDriving/2.0) {
            Drive.i().setBothSpeeds(Constants.Autonomous.driveSpeed);
        } else if (timer.get() < Constants.Autonomous.timeDriving) {
            Drive.i().setBothSpeeds(-Constants.Autonomous.driveSpeed);
        } else {
            // Technically redundant; this will run again in end().
            // Note that if another command calls AutonomousCommand.cancel(), end() will run
            // but not this function.
            Drive.i().setBothSpeeds(0.0);
        }
    }

    @Override
    public boolean isFinished() {
        return timer.get() >= Constants.Autonomous.timeDriving;
    }

    @Override
    public void end(boolean canceled) {
        Drive.i().setBothSpeeds(0.0);
    }
}
