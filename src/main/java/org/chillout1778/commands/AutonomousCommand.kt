package org.chillout1778.commands

import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj2.command.CommandBase
import org.chillout1778.Constants

/*
class AutonomousCommand : CommandBase() {
    private val timer = Timer()
    override fun initialize() {
        timer.restart()
    }

    override fun execute() {
        // Go forward / back for two seconds
        if (timer.get() < Constants.Autonomous.timeDriving / 2.0) {
            Drive.setBothSpeeds(Constants.Autonomous.driveSpeed)
        } else if (timer.get() < Constants.Autonomous.timeDriving) {
            Drive.setBothSpeeds(-Constants.Autonomous.driveSpeed)
        } else {
            // Technically redundant; this will run again in end().
            // Note that if another command calls AutonomousCommand.cancel(), end() will run
            // but not this function.
            Drive.setBothSpeeds(0.0)
        }
    }

    override fun isFinished(): Boolean {
        return timer.get() >= Constants.Autonomous.timeDriving
    }

    override fun end(canceled: Boolean) {
        Drive.setBothSpeeds(0.0)
    }
}
*/
