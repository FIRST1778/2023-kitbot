package org.chillout1778;

import edu.wpi.first.wpilibj.RobotBase;
import org.chillout1778.subsystems.Robot;

public final class Main {
    public static void main(String[] args) {
        RobotBase.startRobot(Robot::new);
    }
}
