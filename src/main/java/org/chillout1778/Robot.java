package org.chillout1778;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;

public class Robot extends TimedRobot {
    private DifferentialDrive drive;
    private Joystick leftStick;
    private Joystick rightStick;

    @Override
    public void robotInit() {
        drive = new DifferentialDrive(new PWMSparkMax(0), new PWMSparkMax(1));
        leftStick = new Joystick(0);
        rightStick = new Joystick(1);
    }

    @Override
    public void teleopPeriodic() {
        drive.tankDrive(leftStick.getY(), rightStick.getY());
    }
}
