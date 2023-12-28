package org.chillout1778.commands

import edu.wpi.first.wpilibj2.command.CommandBase
import org.chillout1778.Constants
import org.chillout1778.subsystems.Controls
import org.chillout1778.subsystems.Drive
import kotlin.math.abs

class DriveCommand : CommandBase() {


    private fun capDriveSpeed(speed: Double): Double {
        return if (speed > 1.0) {
            1.0
        } else if (speed < -1.0) {
            -1.0
        } else {
            speed
        }
    }

    private fun handleDeadzone(currPos: Double): Double {
        return if (abs(currPos) < 0.1) {
            0.0
        } else {
            currPos
        }
    }

    override fun execute() {
        val speed = -Controls.driverController.getRawAxis(Constants.Controls.driveAxisID) / 2
        val turn = Controls.driverController.getRawAxis(Constants.Controls.turnAxisID) / 2
        val rightSpeed = capDriveSpeed(speed - turn) // divided by two because otherwise too fast
        val leftSpeed = capDriveSpeed(speed + turn)
        Drive.setRightSpeed(handleDeadzone(rightSpeed))
        Drive.setLeftSpeed(handleDeadzone(leftSpeed))
    }

    override fun isFinished(): Boolean {
        return false
    }

    override fun runsWhenDisabled(): Boolean {
        return false
    }

    override fun cancel() {
        Drive.setRightSpeed(0.0)
        Drive.setLeftSpeed(0.0)
        super.cancel()
    }
}