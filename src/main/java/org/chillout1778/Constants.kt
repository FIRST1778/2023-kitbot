package org.chillout1778

import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.math.controller.ProfiledPIDController
import edu.wpi.first.math.controller.SimpleMotorFeedforward
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

        val driveReduction = 1.0 / 6.12
        val angleReduction = 1.0 / (150.0 / 7.0)
        val colsonWheelRadius = Units.inchesToMeters(2.0)
        //val neoFreeSpeed = 5676.0 * Math.PI / 30.0
        //val theoreticalMaxSpeed = neoFreeSpeed * driveReduction * colsonWheelRadius
        // WE DONT USE NEOS
        //val theoreticalMaxAngularSpeed = theoreticalMaxSpeed / moduleRadius
        // The above math is also contained in swerve.py in the root
        // of the repository.

        val maxSpeed = 1.0 //theoreticalMaxSpeed / 2.0 // **** safe testing value
        val maxAngularSpeed = 1.0 // theoreticalMaxAngularSpeed / 2.0 // ****
        val maxAngularAcceleration = 1.0 // Math.PI / 2.0
        val maxVoltage: Double = 5.0

        fun driveController() = PIDController(0.8, 0.0, 0.0)
        fun turnController()  = ProfiledPIDController(0.8, 0.0, 0.0,
            TrapezoidProfile.Constraints(
                3.0, 3.0
                // TODO: correct now that we control velocity?
            )
        )
        fun driveFeedforward() = SimpleMotorFeedforward(0.0, 0.0, 0.0)
        fun turnFeedforward()  = SimpleMotorFeedforward(0.0, 0.0, 0.0)
    }
}
