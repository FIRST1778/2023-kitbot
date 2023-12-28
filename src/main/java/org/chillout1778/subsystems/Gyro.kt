package org.chillout1778.subsystems

import com.ctre.phoenix.sensors.Pigeon2
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.wpilibj2.command.SubsystemBase

object Gyro: SubsystemBase() {
    private val pigeon = Pigeon2(21)

    init {
        /* pigeon.configMountPose(Pigeon2.AxisDirection.PositiveY,
                Pigeon2.AxisDirection.PositiveZ, 500) */
    }

    val rotation get() = Rotation2d(Math.toRadians(pigeon.yaw))
}
