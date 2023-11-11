package org.chillout1778;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.util.Units;

public class MotorSpin extends SubsystemBase {
    private CANSparkMax motor = new CANSparkMax(11,
            CANSparkMaxLowLevel.MotorType.kBrushless);

    private static final double kP = 1.0;
    private static final double kI = 0.0;
    private static final double kD = 0.0;

    private PIDController controller = new PIDController(kP, kI, kD);
    private double desired = 0.0;  // rad/s

    public void spin() {
        desired = 0.1;
    }

    public void stop() {
        desired = 0.0;
    }

    private double angularVelocity() {
        return Units.rotationsPerMinuteToRadiansPerSecond(
                motor.getEncoder().getVelocity()
        );
    }

    @Override
    public void periodic() {
        double voltage = controller.calculate(angularVelocity(), desired);
    }
}
