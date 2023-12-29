package org.chillout1778.subsystems


import com.ctre.phoenix6.configs.TalonFXConfiguration
import com.ctre.phoenix6.configs.TalonFXConfigurator
import com.ctre.phoenix6.hardware.CANcoder
import com.ctre.phoenix6.hardware.TalonFX
import com.ctre.phoenix6.signals.InvertedValue
import com.revrobotics.CANSparkMax
import edu.wpi.first.math.controller.SimpleMotorFeedforward
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.kinematics.SwerveModulePosition
import edu.wpi.first.math.kinematics.SwerveModuleState
import org.chillout1778.Constants
import org.chillout1778.Robot
import org.chillout1778.lib.NEO
import org.chillout1778.lib.math.Conversions
import org.chillout1778.lib.util.CTREModuleState
import org.chillout1778.lib.util.SwerveModuleConstants


class SwerveModule(var moduleNumber: Int, moduleConstants: SwerveModuleConstants) {
    private val angleOffset: Rotation2d
    private var lastAngle: Rotation2d
    private val mAngleMotor: NEO
    private val mDriveMotor: TalonFX
    private val angleEncoder: CANcoder
    private var feedforward: SimpleMotorFeedforward =
        SimpleMotorFeedforward(Constants.Swerve.driveKS, Constants.Swerve.driveKV, Constants.Swerve.driveKA)

    init {
        angleOffset = moduleConstants.angleOffset

        /* Angle Encoder Config */angleEncoder = CANcoder(moduleConstants.cancoderID)
        configAngleEncoder()

        /* Angle Motor Config */mAngleMotor = NEO(moduleConstants.angleMotorID)
        configAngleMotor()

        /* Drive Motor Config */mDriveMotor = TalonFX(moduleConstants.driveMotorID)
        configDriveMotor()
        lastAngle = state.angle
    }

    fun setDesiredState(desiredState: SwerveModuleState, isOpenLoop: Boolean) {
        /* This is a custom optimize function, since default WPILib optimize assumes continuous controller which CTRE and Rev onboard is not */
        var desiredState: SwerveModuleState = desiredState
        desiredState = CTREModuleState.optimize(desiredState, state.angle)
        setAngle(desiredState)
        setSpeed(desiredState, isOpenLoop)
    }

    private fun setSpeed(desiredState: SwerveModuleState, isOpenLoop: Boolean) {
        if (isOpenLoop) {
            val percentOutput: Double = desiredState.speedMetersPerSecond / Constants.Swerve.maxSpeed
            mDriveMotor.set(percentOutput)
        } else {
//            val velocity: Double = Conversions.MPSToFalcon(
//                desiredState.speedMetersPerSecond,
//                Constants.Swerve.wheelCircumference,
//                Constants.Swerve.driveGearRatio
//            )
//            mDriveMotor.set(percentOutput)
//            mDriveMotor.setControl(VelocityVoltage(velocity)
//                .withFeedForward(feedforward.calculate(desiredState.speedMetersPerSecond))
//                .withEnableFOC(false))
        }
    }

    private fun setAngle(desiredState: SwerveModuleState) {
        val angle =
            if (Math.abs(desiredState.speedMetersPerSecond) <= Constants.Swerve.maxSpeed * 0.01)
                lastAngle
            else
                desiredState.angle

        mAngleMotor.controller.setReference(angle.degrees, CANSparkMax.ControlType.kPosition)
        lastAngle = angle
    }

    private val angle: Rotation2d
        private get() = Rotation2d.fromRadians(
            mAngleMotor.position
        )
    val canCoder: Rotation2d
        get() = Rotation2d.fromDegrees(angleEncoder.absolutePosition.value * 360.0)

    fun resetToAbsolute() {
        mAngleMotor.position = canCoder.degrees - angleOffset.degrees
    }

    private fun configAngleEncoder() {

    }

    private fun configAngleMotor() {
        mAngleMotor.inverted = Constants.Swerve.angleMotorInvert
        mAngleMotor.neutralMode = Constants.Swerve.angleNeutralMode
        resetToAbsolute()
    }

    private fun configDriveMotor() {
        mDriveMotor.configurator.apply(TalonFXConfiguration())
        mDriveMotor.configurator.apply(Robot.ctreConfigs!!.swerveDriveFXConfig)
        val config = TalonFXConfiguration()
        config.MotorOutput.Inverted =
            if(Constants.Swerve.driveMotorInvert) InvertedValue.Clockwise_Positive
            else InvertedValue.CounterClockwise_Positive
        config.MotorOutput.NeutralMode = (Constants.Swerve.driveNeutralMode)
        mDriveMotor.configurator.apply(config)
        mDriveMotor.setPosition(0.0)
    }

    val state: SwerveModuleState
        get() = SwerveModuleState(
            Conversions.falconToMPS(
                mDriveMotor.velocity.value,
                Constants.Swerve.wheelCircumference,
                Constants.Swerve.driveGearRatio
            ),
            angle
        )
    val position: SwerveModulePosition
        get() {
            return SwerveModulePosition(
                Conversions.falconToMeters(
                    mDriveMotor.position.value,
                    Constants.Swerve.wheelCircumference,
                    Constants.Swerve.driveGearRatio
                ),
                angle
            )
        }
}


