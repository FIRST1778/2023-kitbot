package org.chillout1778.commands

import edu.wpi.first.wpilibj2.command.Command
import org.chillout1778.Constants
import org.chillout1778.subsystems.Controls
import org.chillout1778.subsystems.Swerve
import javax.naming.ldap.Control

class DriveCommand: Command() {
    init {
        addRequirements(Swerve)
    }

    private fun deadband(n: Double) = if (Math.abs(n) < 0.1) 0.0 else n
    private fun square(n: Double) = n*n*Math.signum(n)

    override fun execute() {
        Swerve.drive(
            square(deadband(Controls.x())) * Constants.Swerve.maxSpeed,
            -square(deadband(Controls.y())) * Constants.Swerve.maxSpeed,
            -square(deadband(Controls.rot())) * Constants.Swerve.maxAngularSpeed
        )
    }
}
