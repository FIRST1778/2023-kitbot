package org.chillout1778;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;

public abstract class Constants {
    public static class Pigeon {
        private static final int canId = 21;
    }

    public static class Autonomous {
        public static final double driveSpeed = 0.1;
        public static final double timeDriving = 4.0;
    }

    public static class Controls {
        public static final int driveAxisID = 1;
        public static final int turnAxisID = 4;
    }

    public static class Swerve {
        // The swerve modules are located at (11.75,11.75) and its
        // reflections across the x- and y-axis.
        private static final double moduleXY = Units.inchesToMeters(11.75);
        private static final double moduleRadius = moduleXY * Math.sqrt(2);

        // The gear ratio for an MK4i L2 swerve module, about 6.75 : 1.
        // We multiply by "(driving gear teeth) / (driven gear teeth)"
        // for each stage of the gearbox.
        // https://www.swervedrivespecialties.com/products/mk4i-swerve-module
        private static final double driveReduction = (14.0 / 50.0) * (27.0 / 17.0) * (15.0 / 45.0);

        private static final double colsonWheelRadius = Units.inchesToMeters(2.0);
        private static final double maxSpeed = DCMotor.getNEO(1).freeSpeedRadPerSec * driveReduction * colsonWheelRadius;
        private static final double maxAngularSpeed = maxSpeed / moduleRadius;

        // TODO: azimuth encoder issues?
        //
        // We use both a CANCoder (absolute) and the NEO's built-in
        // encoder (relative, but faster) to measure swerve azimuth.
        // Last year, we implemented a hack that would reset the NEO
        // to the CANCoder if the motor didn't move for 10 seconds,
        // because there was an edge case where the CANCoder wouldn't be
        // accurate at the start of the match.  We can add this hack
        // back if we experience CANCoder issues.
        //
        // https://store.ctr-electronics.com/blog/cancoder-firmware-update-22012/
        // https://discord.com/channels/887922855084425266/890436659450118254/1170883077195714590
        // https://github.com/SwerveDriveSpecialties/Do-not-use-swerve-lib-2022-unmaintained/blob/55f3f1ad9e6bd81e56779d022a40917aacf8d3b3/src/main/java/com/swervedrivespecialties/swervelib/rev/NeoSteerControllerFactoryBuilder.java#L128C3-L128C3
        // https://github.com/FIRST1778/2023-Robot-Code/blob/36a38de7a1d7970517fe18e7506582187ec46491/src/main/java/org/frc1778/lib/FalconNeoSwerveModule.kt#L127
    }
}
