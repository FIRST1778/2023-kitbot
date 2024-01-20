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
        val angleReduction = 7.0 / 150.0
        val colsonWheelRadius = Units.inchesToMeters(2.0)
        val falconFreeSpeed = 6380.0 * Math.PI / 30.0
        //val neoFreeSpeed = 5676.0 * Math.PI / 30.0
        val theoreticalMaxSpeed = falconFreeSpeed * driveReduction * colsonWheelRadius
        val theoreticalMaxAngularSpeed = theoreticalMaxSpeed / moduleRadius

        val maxSpeed = theoreticalMaxSpeed //theoreticalMaxSpeed / 2.0 // **** safe testing value
        val maxAngularSpeed = theoreticalMaxAngularSpeed * 0.7 // theoreticalMaxAngularSpeed / 2.0 // ****
        //val maxAngularAcceleration =  // Math.PI / 2.0
        val maxVoltage: Double = 12.0

        fun driveController() = PIDController(2.0, 0.0, 0.0)
        fun turnController()  = PIDController(7.0, 0.0, 0.0)
        fun driveFeedforward() = SimpleMotorFeedforward(0.0, 0.0, 0.0)
    }
}
