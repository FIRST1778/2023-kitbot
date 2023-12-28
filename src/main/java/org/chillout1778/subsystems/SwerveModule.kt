package org.chillout1778.subsystems

import com.ctre.phoenix.sensors.CANCoder
import com.revrobotics.CANSparkMax
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.kinematics.SwerveModulePosition
import edu.wpi.first.math.kinematics.SwerveModuleState
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.chillout1778.lib.Util

class SwerveModule(
        driveMotorId: Int, turnMotorId: Int, turnCanCoderId: Int,
        val encoderOffset: Double,
        val inverted: Boolean,
        val translation: Translation2d
) {
    private val driveMotor: CANSparkMax = Util.defaultNeo(driveMotorId)
    private val turnMotor: CANSparkMax  = Util.defaultNeo(turnMotorId)
    private val turnCanCoder: CANCoder = Util.defaultCanCoder(turnCanCoderId)

    private fun resetRelative() {
        turnMotor.encoder.position = turnCanCoder.position
    }

    val state get() = SwerveModuleState(
        driveMotor.encoder.velocity, Rotation2d(turnMotor.encoder.position)
    )
    val position get() = SwerveModulePosition(
        driveMotor.encoder.position, Rotation2d(turnMotor.encoder.position)
    )

    private var turnStationaryTicks: Int = 0
    fun update() {
        if (turnMotor.encoder.velocity < 0.3) {
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
