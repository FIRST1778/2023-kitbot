package org.chillout1778

import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.InstantCommand
import edu.wpi.first.wpilibj2.command.button.JoystickButton
import org.chillout1778.commands.TeleopSwerve
import org.chillout1778.subsystems.Swerve
import edu.wpi.first.math.MathUtil

object RobotContainer {
    /* Controllers */
    private val driver = Joystick(0)
    private val op = XboxController(1)

    /* Drive Controls */
    private val translationAxis = 2
    private val strafeAxis = 3
    private val rotationAxis = 0

    /* Driver Buttons */
    private val zeroGyro = JoystickButton(driver, XboxController.Button.kY.value)
    private val robotCentric = false

    private val shooterAxis = 3
    fun getShooterAxis() =  MathUtil.applyDeadband(op.getRawAxis(3), 0.1)

    /** The container for the robot. Contains subsystems, OI devices, and commands.  */
    init {
        Swerve.defaultCommand = TeleopSwerve(
            { driver.getRawAxis(translationAxis) },
            { -driver.getRawAxis(strafeAxis) },
            { -driver.getRawAxis(rotationAxis) }
        ) { robotCentric }

        // Configure the button bindings
        configureButtonBindings()
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a [GenericHID] or one of its subclasses ([ ] or [XboxController]), and then passing it to a [ ].
     */
    private fun configureButtonBindings() {
        /* Driver Buttons */
    }

    val autonomousCommand: Command
        get() {
            TODO()
        }
}