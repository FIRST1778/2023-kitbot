package org.chillout1778

import edu.wpi.first.wpilibj.PowerDistribution
import edu.wpi.first.wpilibj.RobotBase
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj2.command.CommandScheduler
import org.chillout1778.subsystems.Swerve
import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel
import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.math.MathUtil

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

    val leftMotor = CANSparkMax(15, CANSparkMaxLowLevel.MotorType.kBrushless)
    val rightMotor = CANSparkMax(16, CANSparkMaxLowLevel.MotorType.kBrushless)

    override fun teleopPeriodic() {
        val v = MathUtil.applyDeadband(m_robotContainer!!.op.getRawAxis(3), 0.1)
        leftMotor.set(v)
        rightMotor.set(v)
    }

    override fun testInit() {
        // Cancels all running commands at the start of test mode.
        CommandScheduler.getInstance().cancelAll()
    }

    override fun testPeriodic() {}
}
