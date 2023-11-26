package org.chillout1778.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SwerveModule extends SubsystemBase {
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
