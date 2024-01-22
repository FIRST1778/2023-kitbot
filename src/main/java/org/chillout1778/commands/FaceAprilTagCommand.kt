package org.chillout1778.commands

import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.wpilibj2.command.Command
import org.chillout1778.subsystems.Swerve
import org.chillout1778.subsystems.Vision

class FaceAprilTagCommand : Command() {
    init {
        addRequirements(Vision)
        addRequirements(Swerve)
    }

    val pid = PIDController(0.2, 0.0, 0.0)
    var calculated : Double = 0.0

    override fun execute() {
        if(Vision.aprilTagPresent) {
            Swerve.drive(0.0, 0.0, pid.calculate(Vision.centerOffset, 0.0))
        } else {
            Swerve.drive(0.0, 0.0, 0.0)
        }
    }

    override fun isFinished(): Boolean {
        return false
    }

    override fun end(interrupted: Boolean) {
        Swerve.drive(0.0,0.0,0.0)
    }
}