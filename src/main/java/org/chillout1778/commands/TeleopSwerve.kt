package org.chillout1778.commands

import edu.wpi.first.math.MathUtil
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.wpilibj2.command.CommandBase
import org.chillout1778.Constants
import org.chillout1778.subsystems.Swerve
import java.util.function.BooleanSupplier
import java.util.function.DoubleSupplier


class TeleopSwerve(
    s_Swerve: Swerve,
    translationSup: DoubleSupplier,
    strafeSup: DoubleSupplier,
    rotationSup: DoubleSupplier,
    robotCentricSup: BooleanSupplier
) :
    CommandBase() {
    private val s_Swerve: Swerve
    private val translationSup: DoubleSupplier
    private val strafeSup: DoubleSupplier
    private val rotationSup: DoubleSupplier
    private val robotCentricSup: BooleanSupplier

    init {
        this.s_Swerve = s_Swerve
        addRequirements(s_Swerve)
        this.translationSup = translationSup
        this.strafeSup = strafeSup
        this.rotationSup = rotationSup
        this.robotCentricSup = robotCentricSup
    }

    override fun execute() {
        /* Get Values, Deadband*/
        val translationVal = MathUtil.applyDeadband(translationSup.asDouble, Constants.Controls.stickDeadband)
        val strafeVal = MathUtil.applyDeadband(strafeSup.asDouble, Constants.Controls.stickDeadband)
        val rotationVal = MathUtil.applyDeadband(rotationSup.asDouble, Constants.Controls.stickDeadband)

        /* Drive */s_Swerve.drive(
            Translation2d(translationVal, strafeVal).times(Constants.Swerve.maxSpeed),
            rotationVal * Constants.Swerve.maxAngularVelocity,
            !robotCentricSup.asBoolean,
            true
        )
    }
}