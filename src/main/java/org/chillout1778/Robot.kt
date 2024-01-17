package org.chillout1778

import edu.wpi.first.wpilibj.RobotBase
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.CommandScheduler
import com.pathplanner.lib.path.PathPlannerPath
import com.pathplanner.lib.auto.AutoBuilder


object Robot : TimedRobot() {
    fun start(){
        RobotBase.startRobot{this}
    }
    var ctreConfigs: CTREConfigs? = null

    private var m_autonomousCommand: Command? = null

    /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     */
    override fun robotInit() {
        ctreConfigs = CTREConfigs
    }

    /**
     * This function is called every robot packet, no matter the mode. Use this for items like
     * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
     *
     *
     * This runs after the mode specific periodic functions, but before LiveWindow and
     * SmartDashboard integrated updating.
     */
    override fun robotPeriodic() {
        // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
        // commands, running already-scheduled commands, removing finished or interrupted commands,
        // and running subsystem periodic() methods.  This must be called from the robot's periodic
        // block in order for anything in the Command-based framework to work.
        CommandScheduler.getInstance().run()
    }

    override fun disabledInit() {}

    override fun disabledPeriodic() {}

    override fun autonomousInit() {
        val path = PathPlannerPath.fromChoreoTrajectory("NewPath")
        m_autonomousCommand = AutoBuilder.followPathWithEvents(path)
        println("===== Not scheduling autonomous command, check trajectories and Robot.kt")
        //m_autonomousCommand.schedule()
    }

    override fun autonomousPeriodic() {}

    override fun teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (m_autonomousCommand != null) {
            m_autonomousCommand!!.cancel()
        }
    }

    

    override fun testInit() {
        // Cancels all running commands at the start of test mode.
        CommandScheduler.getInstance().cancelAll()
    }

    override fun testPeriodic() {}

}
