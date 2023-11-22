package org.chillout1778.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.chillout1778.Constants;
import org.chillout1778.subsystems.Controls;
import org.chillout1778.subsystems.Drive;

public final class DriveCommand extends CommandBase {
    private double capDriveSpeed(double speed){
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
    private double handleDeadzone(double currPos){
        if(Math.abs(currPos)< 0.1) {
            return 0.0;
        }
        else {
            return currPos;
        }


    }
    @Override
    public void execute(){
        double speed = Controls.driverController.getRawAxis(Constants.Controls.driveAxisID);
        double turn = Controls.driverController.getRawAxis(Constants.Controls.turnAxisID);

        double rightSpeed = capDriveSpeed(speed - turn) / 2.0; // divided by two because otherwise too fast
        double leftSpeed = capDriveSpeed(speed + turn) / 2.0;

        Drive.setRightSpeed(handleDeadzone(rightSpeed));
        Drive.setLeftSpeed(handleDeadzone(leftSpeed));

    }
    @Override
    public boolean isFinished(){
        return false;
    }
    @Override
    public boolean runsWhenDisabled(){
        return false;
    }

    @Override
    public void cancel() {
        super.cancel();
        Drive.setRightSpeed(0.0);
        Drive.setLeftSpeed(0.0);
    }
}
