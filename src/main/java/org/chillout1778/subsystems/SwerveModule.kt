package org.chillout1778.subsystems


import com.ctre.phoenix6.configs.MagnetSensorConfigs
import com.ctre.phoenix6.configs.TalonFXConfiguration
import com.ctre.phoenix6.controls.DutyCycleOut
import com.ctre.phoenix6.controls.VelocityVoltage
import com.ctre.phoenix6.hardware.CANcoder
import com.ctre.phoenix6.hardware.TalonFX
import com.ctre.phoenix6.signals.AbsoluteSensorRangeValue
import com.ctre.phoenix6.signals.InvertedValue
import com.ctre.phoenix6.signals.SensorDirectionValue
import com.revrobotics.CANSparkMax
import edu.wpi.first.math.controller.SimpleMotorFeedforward
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.kinematics.SwerveModulePosition
import edu.wpi.first.math.kinematics.SwerveModuleState
import org.chillout1778.Constants
import org.chillout1778.Robot
import org.chillout1778.lib.NEO
import org.chillout1778.lib.math.Conversions
import org.chillout1778.lib.util.SwerveModuleConstants
import kotlin.math.PI


class SwerveModule(var moduleNumber: Int, moduleConstants: SwerveModuleConstants, var driveMotorInvert : Boolean) {
    private val angleOffset: Rotation2d
    private var lastAngle: Rotation2d
    private val mAngleMotor: NEO
    private val mDriveMotor: TalonFX
    private val angleEncoder: CANcoder
    private var driveFeedForward: SimpleMotorFeedforward =
        SimpleMotorFeedforward(Constants.Swerve.driveKS, Constants.Swerve.driveKV, Constants.Swerve.driveKA)

    /* drive motor control requests */
    private val driveDutyCycle = DutyCycleOut(0.0)
    private val driveVelocity = VelocityVoltage(0.0)
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

    fun setDesiredState(
        state: SwerveModuleState,
        isOpenLoop: Boolean
    ) {/* This is a custom optimize function, since default WPILib optimize assumes continuous controller which CTRE and Rev onboard is not */
        var desiredState: SwerveModuleState = state
        desiredState = SwerveModuleState.optimize(desiredState, state.angle)
//        desiredState = CTREModuleState.optimize(desiredState, state.angle)
        setAngle(desiredState)
        setSpeed(desiredState, isOpenLoop)
    }

    private fun setSpeed(desiredState: SwerveModuleState, isOpenLoop: Boolean) {
        if(isOpenLoop){
            driveDutyCycle.Output = desiredState.speedMetersPerSecond / Constants.Swerve.maxSpeed
            mDriveMotor.setControl(driveDutyCycle)
        } else {
            driveVelocity.Velocity = Conversions.MPSToRPS(desiredState.speedMetersPerSecond, Constants.Swerve.wheelCircumference)
            driveVelocity.FeedForward = driveFeedForward.calculate(desiredState.speedMetersPerSecond)
            mDriveMotor.setControl(driveVelocity)
        }
    }

    private fun angleWrap(rot: Rotation2d): Rotation2d {
        var rad = rot.radians % (2.0*Math.PI)
        if (rad < 0.0) {
            rad += 2.0*Math.PI
        }
        return Rotation2d.fromRadians(rad)
    }

    private fun setAngle(desiredState: SwerveModuleState) {
        var angle = if (Math.abs(desiredState.speedMetersPerSecond) <= Constants.Swerve.maxSpeed * 0.01) lastAngle
        else desiredState.angle
        angle = angleWrap(angle)
        println(angle)
        mAngleMotor.controller.setReference(angle.radians, CANSparkMax.ControlType.kPosition)
        lastAngle = angle
    }

    private val angle: Rotation2d
        private get() = Rotation2d.fromRadians(
            mAngleMotor.position
        )
    val canCoder: Rotation2d
        get() = Rotation2d.fromDegrees(angleEncoder.absolutePosition.valueAsDouble * 360.0)

    fun resetToAbsolute() {

        mAngleMotor.position = canCoder.radians
    }

    private fun configAngleEncoder() {
        angleEncoder.configurator.apply(
            MagnetSensorConfigs().withMagnetOffset(
                -angleOffset.degrees / 360.0 // TODO: Figure out wtf this needs as input
            ).withAbsoluteSensorRange(AbsoluteSensorRangeValue.Unsigned_0To1)
                .withSensorDirection(
                    if (Constants.Swerve.canCoderInvert) SensorDirectionValue.Clockwise_Positive
                    else SensorDirectionValue.CounterClockwise_Positive
                )
        )
    }

    private fun configAngleMotor() {
        mAngleMotor.controller.setPositionPIDWrappingEnabled(true);
        mAngleMotor.controller.setPositionPIDWrappingMinInput(0.0);
        mAngleMotor.controller.setPositionPIDWrappingMaxInput(2 * Math.PI);
        mAngleMotor.inverted = Constants.Swerve.angleMotorInvert
        mAngleMotor.neutralMode = Constants.Swerve.angleNeutralMode
        mAngleMotor.controller.p = Constants.Swerve.angleKP
        mAngleMotor.controller.i = Constants.Swerve.angleKI
        mAngleMotor.controller.d = Constants.Swerve.angleKD
        mAngleMotor.controller.ff = Constants.Swerve.angleKF
        mAngleMotor.motor.encoder.positionConversionFactor =
            (1.0 / Constants.Swerve.chosenModule.angleGearRatio) * 2.0 * PI
        mAngleMotor.motor.encoder.velocityConversionFactor =
            (1.0 / Constants.Swerve.chosenModule.angleGearRatio) * Math.PI / 30.0 // radians
        mAngleMotor.motor.burnFlash()
        resetToAbsolute()
    }

    private fun configDriveMotor() {
        mDriveMotor.configurator.apply(TalonFXConfiguration())
        mDriveMotor.configurator.apply(Robot.ctreConfigs!!.swerveDriveFXConfig)
        val config = TalonFXConfiguration()
        config.MotorOutput.Inverted = if (driveMotorInvert) InvertedValue.Clockwise_Positive
        else InvertedValue.CounterClockwise_Positive
        config.MotorOutput.NeutralMode = (Constants.Swerve.driveNeutralMode)
        mDriveMotor.configurator.apply(config)
        mDriveMotor.setPosition(0.0)
    }

    val state: SwerveModuleState
        get() = SwerveModuleState(
            Conversions.falconToMPS(
                mDriveMotor.velocity.value, Constants.Swerve.wheelCircumference, Constants.Swerve.driveGearRatio
            ), angle
        )
    val position: SwerveModulePosition
        get() {
            return SwerveModulePosition(
                Conversions.falconToMeters(
                    mDriveMotor.position.value, Constants.Swerve.wheelCircumference, Constants.Swerve.driveGearRatio
                ), angle
            )
        }
}


