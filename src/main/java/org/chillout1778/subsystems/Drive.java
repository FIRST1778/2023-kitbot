package org.chillout1778.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.chillout1778.Constants;
import org.chillout1778.commands.DriveCommand;

public class Drive extends SubsystemBase {
    private CANSparkMax leftParentMotor = new CANSparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless);
    private CANSparkMax leftChildMotor = new CANSparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless);

    private CANSparkMax rightParentMotor = new CANSparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless);
    private CANSparkMax rightChildMotor = new CANSparkMax(4, CANSparkMaxLowLevel.MotorType.kBrushless);

    public Drive(){
        setDefaultCommand(new DriveCommand(this));
        leftChildMotor.follow(leftParentMotor);
        rightChildMotor.follow(rightParentMotor);
    }

    public void setRightSpeed(double speed){
        rightParentMotor.set(speed);
    }
    public void setLeftSpeed(double speed) {
        leftParentMotor.set(speed);
    }

    public void setBothSpeeds(double speed){
        setLeftSpeed(speed);
        setRightSpeed(speed);
    }
}
