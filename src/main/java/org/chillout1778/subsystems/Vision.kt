package org.chillout1778.subsystems

import edu.wpi.first.util.sendable.Sendable
import edu.wpi.first.util.sendable.SendableBuilder
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj2.command.Subsystem
import org.chillout1778.lib.LimelightHelpers

object Vision : Subsystem, Sendable{
    val limelightName : String = "limelight"
    init{
        Shuffleboard.getTab("Limelight").add("Limelight", this).withSize(2,2)
    }
    val tx : Double
        get() = LimelightHelpers.getTX(limelightName)
    val tv : Boolean
        get() = LimelightHelpers.getTV(limelightName)
    val ta : Double
        get() = LimelightHelpers.getTA(limelightName)
    val ty : Double
        get() = LimelightHelpers.getTY(limelightName)
    val aprilTagPresent : Boolean
        get() = tv
    val centerOffset : Double
        get() = LimelightHelpers.getTX(limelightName)

    override fun initSendable(builder: SendableBuilder?) {
        builder!!
        builder.addDoubleProperty("left right", {centerOffset}, {})
        builder.addBooleanProperty("exists", {aprilTagPresent}, {})
    }
}