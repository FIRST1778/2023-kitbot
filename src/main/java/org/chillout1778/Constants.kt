package org.chillout1778

import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.math.controller.ProfiledPIDController
import edu.wpi.first.math.controller.SimpleMotorFeedforward
import edu.wpi.first.math.system.plant.DCMotor
import edu.wpi.first.math.trajectory.TrapezoidProfile
import edu.wpi.first.math.util.Units
import kotlin.math.sqrt

object Constants {
    object Pigeon {
        const val canId = 21
    }

    object Autonomous {
        const val driveSpeed = 0.1
        const val timeDriving = 4.0
    }

    object Controls {
        const val driveAxisID = 1
        const val turnAxisID = 4
    }

    object Swerve {
        // The swerve modules are located at (11.75,11.75) and its
        // reflections across the x- and y-axis.
        val moduleXY = Units.inchesToMeters(11.75)
        val moduleRadius = moduleXY * sqrt(2.0)

        // The gear ratio for an MK4i L2 swerve module, about 6.75 : 1.
        // We multiply by "(driving gear teeth) / (driven gear teeth)"
        // for each stage of the gearbox.
        // https://www.swervedrivespecialties.com/products/mk4i-swerve-module
        const val driveReduction = 14.0 / 50.0 * (27.0 / 17.0) * (15.0 / 45.0)
        val colsonWheelRadius = Units.inchesToMeters(2.0)
        val theoreticalMaxSpeed = DCMotor.getNEO(1).freeSpeedRadPerSec * driveReduction * colsonWheelRadius
        val theoreticalMaxAngularSpeed = theoreticalMaxSpeed / moduleRadius // TODO: azimuth encoder issues?

        val maxSpeed = theoreticalMaxSpeed  // maybe this is a bad idea?
        val maxAngularSpeed = theoreticalMaxAngularSpeed
        val maxAngularAcceleration = Math.PI / 2.0

        // TODO: ************************ CRITICAL TO TUNE vvvvv
        // These values, other than the TrapezoidProfile, are taken from
        // last year's codebase.  The trapezoid profile calculations
        // were wrong; the new values may be too fast.
        // If the PID values are too slow, it will limit our max speed.
        fun driveController() = PIDController(0.75, 0.0, 0.15)
        fun turnController()  = ProfiledPIDController(0.2, 0.0, 0.02,
            TrapezoidProfile.Constraints(
                maxAngularSpeed, maxAngularAcceleration
            )
        )
        // TODO: These need to be created, but I don't know how to do
        // motor characterizations.
        fun driveFeedforward() = SimpleMotorFeedforward(0.0, 0.0, 0.0)
        fun turnFeedforward()  = SimpleMotorFeedforward(0.0, 0.0, 0.0)

        val maxDriveVoltage = 12.0
        val maxTurnVoltage = 12.0
    }
}
