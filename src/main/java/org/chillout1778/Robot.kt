package org.chillout1778

import edu.wpi.first.wpilibj.PowerDistribution
import edu.wpi.first.wpilibj.RobotBase
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj2.command.CommandScheduler
import org.chillout1778.subsystems.Drive

object Robot : TimedRobot() {
    fun start(){
        RobotBase.startRobot{this}
    }

    var pdh = PowerDistribution(1, PowerDistribution.ModuleType.kRev)
    override fun robotInit() {}
    override fun robotPeriodic() {
        CommandScheduler.getInstance().run()
    }

    override fun teleopInit() {}
    override fun teleopPeriodic() {
//        Drive.update();
    }

    override fun disabledInit() {
        // TODO: add code to spin the motor if the linebreak is unbroken,
        // otherwise stop it
        /*if (lineBreak.broken()) {
             motor.stop();
        } else {
            motor.spin();
        }*/
        Drive.setRightSpeed(0.0)
        Drive.setLeftSpeed(0.0)
    }
}
