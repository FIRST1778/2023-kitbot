package org.chillout1778.subsystems;

import edu.wpi.first.math.controller.HolonomicDriveController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.chillout1778.commands.DriveCommand;

public class Swerve extends SubsystemBase {
    private static class Constants {
        // The swerve modules are located at (11.75,11.75) and its
        // reflections across the x- and y-axes.
        static final double moduleXY = Units.inchesToMeters(11.75);
        static final double moduleRadius = moduleXY * Math.sqrt(2.0);

        // The gear ratio for an MK4i L2 swerve module, about 6.75 : 1.
        // https://www.swervedrivespecialties.com/products/mk4i-swerve-module
        static final double driveReduction = (14.0 / 50.0) * (27.0 / 17.0) * (15.0 / 45.0);

        static final double colsonWheelRadius = Units.inchesToMeters(2.0);
        static final double trueSpeed = DCMotor.getNEO(1).freeSpeedRadPerSec * driveReduction * colsonWheelRadius;
        static final double trueAngularSpeed = maxSpeed / moduleRadius;

        static final double maxSpeed = trueSpeed * 0.5;
        static final double maxAngularSpeed = trueAngularSpeed * 0.5;
        static final double maxAngularAcceleration = maxAngularSpeed * 2.0;

        static final double maxVoltage = 12.0;
        static final double motorOutputLimit = 1.00;
    }

    private Gyro gyro;

    private SwerveModule[] modules = {
        // Swerve module configs go here
    };

    private SimpleMotorFeedforward feedforward = SimpleMoterFeedforward(
        // kS
        0.0,
        // kV
        0.0,
        // kA
        0.0
    );

    private HolonomicDriveController controller = HolonomicDriveController(
        // X kP, kI, kD
        PIDController(0.75, 0.0, 0.15),
        // Y kP, kI, kD
        PIDController(0.75, 0.0, 0.15),
        // Rotation
        ProfiledPIDController(0.2, 0.0, 0.02,
            TrapezoidProfile.Constraints(
                Constants.maxAngularSpeed,
                Constants.maxAngularAcceleration
            )
        )
    );

    private SwerveDriveKinematics kinematics = SwerveDriveKinematics(
        modules.stream().map(m -> m.position()).toArray()
    );

    private SwerveDriveOdometry odometry = SwerveDriveOdometry(
        kinematics, gyro.yaw(), modules.stream().map(
            m -> SwerveModulePosition(m.position(), m.angle())
        ).toArray()
    )

    public Swerve(Gyro g) {
        gyro = g;
        for (final SwerveModule m : modules) {
            m.resetToZero()
        }
        setDefaultCommand(new DriveCommand(this));
    }
}
