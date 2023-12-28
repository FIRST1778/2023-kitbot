package org.chillout1778.subsystems

import edu.wpi.first.wpilibj2.command.SubsystemBase
import com.ctre.phoenix.sensors.Pigeon2

object Gyro: SubsystemBase() {
    private val pigeon = Pigeon2(21)

    init {
        /* pigeon.configMountPose(Pigeon2.AxisDirection.PositiveY,
                Pigeon2.AxisDirection.PositiveZ, 500) */
    }

    val yaw get() = Math.toRadians(pigeon.yaw)
}
