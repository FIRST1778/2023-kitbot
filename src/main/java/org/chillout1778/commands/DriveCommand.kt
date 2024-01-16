package org.chillout1778.commands

import edu.wpi.first.wpilibj2.command.Command
import org.chillout1778.Constants
import org.chillout1778.subsystems.Controls
import org.chillout1778.subsystems.Swerve
import javax.naming.ldap.Control
import org.chillout1778.lib.Util

class DriveCommand: Command() {
    init {
        addRequirements(Swerve)
    }

    private fun square(n: Double) = n*n*Math.signum(n)

    override fun execute() {
        Swerve.drive(
             square(Util.deadband(Controls.x())) * Constants.Swerve.maxSpeed,
            -square(Util.deadband(Controls.y())) * Constants.Swerve.maxSpeed,
            -square(Util.deadband(Controls.rot())) * Constants.Swerve.maxAngularSpeed
        )
    }
}
