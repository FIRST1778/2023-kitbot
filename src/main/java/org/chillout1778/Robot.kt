package org.chillout1778

import edu.wpi.first.wpilibj.PowerDistribution
import edu.wpi.first.wpilibj.RobotBase
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj2.command.CommandScheduler
import org.chillout1778.subsystems.Swerve
import org.chillout1778.subsystems.Gyro

object Robot : TimedRobot() {
    fun start(){
        RobotBase.startRobot{this}
        // Probably a good idea to initialize singleton objects
        Gyro
        Swerve
    }

    var pdh = PowerDistribution(1, PowerDistribution.ModuleType.kRev)

    override fun robotPeriodic() {
        CommandScheduler.getInstance().run()
    }

    override fun teleopPeriodic() {
    }

    override fun disabledInit() {
        Swerve.disable()
    }
}
