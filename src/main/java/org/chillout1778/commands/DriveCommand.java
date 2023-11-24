package org.chillout1778.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.chillout1778.Constants;
import org.chillout1778.subsystems.Controls;
import org.chillout1778.subsystems.Drive;

public class DriveCommand extends CommandBase {
    private Drive drive;

    public DriveCommand(Drive d) {
        drive = d;
        addRequirements(drive);
    }

    private double capDriveSpeed(double speed) {
        if (speed > 1) {
            return 1;
        } else if (speed < -1) {
            return -1;
        } else {
            return speed;
        }
    }

    @Override
    public void execute() {
        double speed = Controls.getInstance().driveFactor();
        double turn = Controls.getInstance().turnFactor();

        double rightSpeed = capDriveSpeed(speed - turn);
        double leftSpeed = capDriveSpeed(speed + turn);

        drive.setRightSpeed(rightSpeed);
        drive.setLeftSpeed(leftSpeed);

    }

    @Override
    public void cancel() {
        super.cancel();
        drive.setRightSpeed(0.0);
        drive.setLeftSpeed(0.0);
    }
}
