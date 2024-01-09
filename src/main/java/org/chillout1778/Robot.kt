package org.chillout1778

import edu.wpi.first.wpilibj.PowerDistribution
import edu.wpi.first.wpilibj.RobotBase
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj2.command.CommandScheduler
import org.chillout1778.subsystems.Swerve

object Robot : TimedRobot() {
    fun start() {
        RobotBase.startRobot{this}
        // Probably a good idea to initialize singleton objects
        Swerve
        System.out.println("===== 1778's new swerve code is running")
    }

    var pdh = PowerDistribution(1, PowerDistribution.ModuleType.kRev)

    override fun robotInit() {

    }
    override fun robotPeriodic() {
        CommandScheduler.getInstance().run()
        for (mod in Swerve.modules)
            mod.maybeResetRelative()
    }

    override fun teleopPeriodic() {
    }

    override fun disabledInit() {
        //Swerve.disable()
    }
}
