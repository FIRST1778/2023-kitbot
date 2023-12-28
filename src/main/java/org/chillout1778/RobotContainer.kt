package org.chillout1778

import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.InstantCommand
import edu.wpi.first.wpilibj2.command.button.JoystickButton
import org.chillout1778.commands.TeleopSwerve
import org.chillout1778.subsystems.Swerve


class RobotContainer {
    /* Controllers */
    private val driver = Joystick(0)

    /* Drive Controls */
    private val translationAxis = XboxController.Axis.kLeftY.value
    private val strafeAxis = XboxController.Axis.kLeftX.value
    private val rotationAxis = XboxController.Axis.kRightX.value

    /* Driver Buttons */
    private val zeroGyro = JoystickButton(driver, XboxController.Button.kY.value)
    private val robotCentric = JoystickButton(driver, XboxController.Button.kLeftBumper.value)

    /* Subsystems */
    private val s_Swerve: Swerve = Swerve()

    /** The container for the robot. Contains subsystems, OI devices, and commands.  */
    init {
        s_Swerve.setDefaultCommand(
            TeleopSwerve(
                s_Swerve,
                { -driver.getRawAxis(translationAxis) },
                { -driver.getRawAxis(strafeAxis) },
                { -driver.getRawAxis(rotationAxis) }
            ) { robotCentric.asBoolean }
        )

        // Configure the button bindings
        configureButtonBindings()
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a [GenericHID] or one of its subclasses ([ ] or [XboxController]), and then passing it to a [ ].
     */
    private fun configureButtonBindings() {
        /* Driver Buttons */
        zeroGyro.onTrue(InstantCommand({ s_Swerve.zeroGyro() }))
    }

    val autonomousCommand: Command
        get() {
            TODO()
        }
}