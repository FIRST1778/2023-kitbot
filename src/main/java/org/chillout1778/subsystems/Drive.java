package org.chillout1778.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.chillout1778.Constants;
import org.chillout1778.commands.DriveCommand;

public class Drive extends SubsystemBase{
    private static CANSparkMax leftParentMotor = new CANSparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless);
    private static CANSparkMax leftChildMotor = new CANSparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless);

    private static CANSparkMax rightParentMotor = new CANSparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless);
    private static CANSparkMax rightChildMotor = new CANSparkMax(4, CANSparkMaxLowLevel.MotorType.kBrushless);

    public Drive(){
        this.setDefaultCommand(new DriveCommand());
        leftChildMotor.follow(leftParentMotor);
        rightChildMotor.follow(rightParentMotor);
    }

    private static double capDriveSpeed(double speed){
        if(speed > 1){
            return 1;
        }
        else if(speed < -1){
            return -1;
        }
        else {
            return speed;
        }
    }
    private static double handleDeadzone(double currPos){
        if(Math.abs(currPos)< 0.1) {
            return 0.0;
        }
        else {
            return currPos;
        }


    }

    public static void setRightSpeed(double speed){
        rightParentMotor.set(speed);
    }
    public static void setLeftSpeed(double speed) {
        leftParentMotor.set(speed);
    }

    public static void update(){
        double speed = Controls.driverController.getRawAxis(Constants.Controls.driveAxisID);
        double turn = Controls.driverController.getRawAxis(Constants.Controls.turnAxisID);

        double rightSpeed = capDriveSpeed(speed - turn); // divided by two because otherwise too fast
        double leftSpeed = capDriveSpeed(speed + turn);

        Drive.setRightSpeed(handleDeadzone(rightSpeed));
        Drive.setLeftSpeed(handleDeadzone(leftSpeed));
    }
}
