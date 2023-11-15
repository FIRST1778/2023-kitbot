package org.chillout1778.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.chillout1778.commands.DriveCommand;

public abstract class Drive extends SubsystemBase{
    private CANSparkMax leftParentMotor = new CANSparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless);
    private CANSparkMax leftChildMotor = new CANSparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless){
    };

    private CANSparkMax rightParentMotor = new CANSparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless);
    private CANSparkMax rightChildMotor = new CANSparkMax(4, CANSparkMaxLowLevel.MotorType.kBrushless);

    private double leftMotorSpeed = 0;
    private double rightMotorSpeed = 0;

    Drive(){
        setDefaultCommand(new DriveCommand()); // Tells DriveCommand to run whenever there is no other command using the Drive subsystem
        //TODO: Have child motors follow parents via the two commands below
    }


    public void setRightSpeed(){
        //TODO: Implement
    }
    public void setLeftSpeed(){
        //TODO: Implement
    }
}
