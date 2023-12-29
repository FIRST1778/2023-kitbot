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

        // The gear ratio for an MK4i L2 swerve module, about 6.75 : 1.
        // We multiply by "(driving gear teeth) / (driven gear teeth)"
        // for each stage of the gearbox.
        // https://www.swervedrivespecialties.com/products/mk4i-swerve-module
        const val driveReduction = (14.0 / 50.0) * (27.0 / 17.0) * (15.0 / 45.0)
        val colsonWheelRadius = Units.inchesToMeters(2.0)
        val neoFreeSpeed = 5676.0 * Math.PI / 30.0
        val theoreticalMaxSpeed = neoFreeSpeed * driveReduction * colsonWheelRadius
        val theoreticalMaxAngularSpeed = theoreticalMaxSpeed / moduleRadius

        val maxSpeed = theoreticalMaxSpeed / 2.0 // **** safe testing value
        val maxAngularSpeed = theoreticalMaxAngularSpeed / 2.0 // ****
        val maxAngularAcceleration = Math.PI / 2.0
        val maxVoltage: Double = 12.0

        // TODO: ************************ CRITICAL TO TUNE vvvvv
        // If the PID values are too slow, they will limit our max speed.
        // These map error to velocity.
        //
        // If we are driving at 0.1 m/s and want to drive at 1 m/s, the
        // velocity error is 0.9.  We calculate an output using the
        // driveController: 0.09 m/s.  (This is purposely very slow).
        //
        // If our turning encoder is 1 rad from where it needs to be,
        // we plug that into turnController and get an output: 0.1 rad/s.
        //
        // Then we do outputVelocity / maxVelocity * maxVoltage to convert
        // those velocities into voltages (see SwerveModule.drive()).
        fun driveController() = PIDController(0.1, 0.0, 0.01)
        fun turnController()  = ProfiledPIDController(0.1, 0.0, 0.01,
            TrapezoidProfile.Constraints(
                maxAngularSpeed, maxAngularAcceleration
            )
        )
        fun driveFeedforward() = SimpleMotorFeedforward(0.0, 0.0, 0.0)
        fun turnFeedforward()  = SimpleMotorFeedforward(0.0, 0.0, 0.0)
    }
}
