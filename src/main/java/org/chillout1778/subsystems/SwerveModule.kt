package org.chillout1778.subsystems

import org.chillout1778.lib.NEO
import com.ctre.phoenix.sensors.CANCoder
import com.ctre.phoenix.sensors.CANCoderConfiguration
import com.ctre.phoenix.sensors.SensorTimeBase
import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel
import edu.wpi.first.wpilibj2.command.SubsystemBase

class SwerveModule(driveMotorId: Int, turnMotorId: Int, turnCanCoderId: Int): SubsystemBase() {
    // Hall-Sensor Encoder Resolution: 42 counts per rev.
    private val driveMotor = NEO(driveMotorId)
    private val turnMotor = NEO(turnMotorId)

    private val turnCanCoder = CANCoder(turnCanCoderId)
    init {
        val config = CANCoderConfiguration()
        config.sensorCoefficient = Math.PI * 2.0 / 4096.0
        config.unitString = "rad"
        config.sensorTimeBase = SensorTimeBase.PerSecond
        turnCanCoder.configAllSettings(config)
    }

    fun resetRelative() {
        turnMotor.position = turnCanCoder.position / 360.0
    }

    fun getTurnPosition() = turnMotor.position
    fun getTurnVelocity() = turnMotor.velocity
    fun getDriveVelocity() = driveMotor.velocity

    private var turnStationaryTicks = 0
    override fun periodic() {
        if (getTurnVelocity() < 0.3) {
            turnStationaryTicks += 1
        } else {
            turnStationaryTicks = 0
        }
        if (turnStationaryTicks > 250) {
            turnStationaryTicks = 0
            resetRelative()
        }
    }
}