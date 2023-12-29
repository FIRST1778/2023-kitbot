package org.chillout1778.commands

import edu.wpi.first.wpilibj2.command.CommandBase
import org.chillout1778.Constants
import org.chillout1778.subsystems.Controls
import org.chillout1778.subsystems.Swerve

class DriveCommand: CommandBase() {
    init {
        addRequirements(Swerve)
    }

    private fun capDriveSpeed(speed: Double) =
        if (speed > 1.0) {
            1.0
        } else if (speed < -1.0) {
            -1.0
        } else {
            speed
        }

    private fun deadband(n: Double) = if (Math.abs(n) < 0.1) 0.0 else n
    private fun square(n: Double) = n*n*Math.signum(n)

    override fun execute() {
        // TODO: some kind of drive inversion?  This is unnecessary if
        // the field is rotationally symmetrical (most years, not 2023).
        Swerve.drive(
            square(deadband(Controls.x())) * Constants.Swerve.maxSpeed,
            -square(deadband(Controls.y())) * Constants.Swerve.maxSpeed,
            -square(deadband(Controls.rot())) * Constants.Swerve.maxAngularSpeed
        )
    }
}
