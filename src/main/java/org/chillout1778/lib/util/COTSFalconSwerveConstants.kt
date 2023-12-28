package org.chillout1778.lib.util

import edu.wpi.first.math.util.Units


/* Contains values and required settings for common COTS swerve modules. */
class COTSFalconSwerveConstants(
    val wheelDiameter: Double,
    val angleGearRatio: Double,
    val driveGearRatio: Double,
    val angleKP: Double,
    val angleKI: Double,
    val angleKD: Double,
    val angleKF: Double,
    val driveMotorInvert: Boolean,
    val angleMotorInvert: Boolean,
    val canCoderInvert: Boolean
) {
    val wheelCircumference: Double

    init {
        wheelCircumference = wheelDiameter * Math.PI
    }

    /* Drive Gear Ratios for all supported modules */
    object driveGearRatios {
        /* SDS MK3 */
        /** SDS MK3 - 8.16 : 1  */
        const val SDSMK3_Standard = 8.16 / 1.0

        /** SDS MK3 - 6.86 : 1  */
        const val SDSMK3_Fast = 6.86 / 1.0
        /* SDS MK4 */
        /** SDS MK4 - 8.14 : 1  */
        const val SDSMK4_L1 = 8.14 / 1.0

        /** SDS MK4 - 6.75 : 1  */
        const val SDSMK4_L2 = 6.75 / 1.0

        /** SDS MK4 - 6.12 : 1  */
        const val SDSMK4_L3 = 6.12 / 1.0

        /** SDS MK4 - 5.14 : 1  */
        const val SDSMK4_L4 = 5.14 / 1.0
        /* SDS MK4i */
        /** SDS MK4i - 8.14 : 1  */
        const val SDSMK4i_L1 = 8.14 / 1.0

        /** SDS MK4i - 6.75 : 1  */
        const val SDSMK4i_L2 = 6.75 / 1.0

        /** SDS MK4i - 6.12 : 1  */
        const val SDSMK4i_L3 = 6.12 / 1.0
    }

    companion object {
        /** Swerve Drive Specialties - MK3 Module */
        fun SDSMK3(driveGearRatio: Double): COTSFalconSwerveConstants {
            val wheelDiameter = Units.inchesToMeters(4.0)

            /** 12.8 : 1  */
            val angleGearRatio = 12.8 / 1.0
            val angleKP = 0.2
            val angleKI = 0.0
            val angleKD = 0.0
            val angleKF = 0.0
            val driveMotorInvert = false
            val angleMotorInvert = false
            val canCoderInvert = false
            return COTSFalconSwerveConstants(
                wheelDiameter,
                angleGearRatio,
                driveGearRatio,
                angleKP,
                angleKI,
                angleKD,
                angleKF,
                driveMotorInvert,
                angleMotorInvert,
                canCoderInvert
            )
        }

        /** Swerve Drive Specialties - MK4 Module */
        fun SDSMK4(driveGearRatio: Double): COTSFalconSwerveConstants {
            val wheelDiameter = Units.inchesToMeters(4.0)

            /** 12.8 : 1  */
            val angleGearRatio = 12.8 / 1.0
            val angleKP = 0.2
            val angleKI = 0.0
            val angleKD = 0.0
            val angleKF = 0.0
            val driveMotorInvert = false
            val angleMotorInvert = false
            val canCoderInvert = false
            return COTSFalconSwerveConstants(
                wheelDiameter,
                angleGearRatio,
                driveGearRatio,
                angleKP,
                angleKI,
                angleKD,
                angleKF,
                driveMotorInvert,
                angleMotorInvert,
                canCoderInvert
            )
        }

        /** Swerve Drive Specialties - MK4i Module */
        fun SDSMK4i(driveGearRatio: Double): COTSFalconSwerveConstants {
            val wheelDiameter = Units.inchesToMeters(4.0)

            /** (150 / 7) : 1  */
            val angleGearRatio = 150.0 / 7.0 / 1.0
            val angleKP = 0.3
            val angleKI = 0.0
            val angleKD = 0.0
            val angleKF = 0.0
            val driveMotorInvert = false
            val angleMotorInvert = true
            val canCoderInvert = false
            return COTSFalconSwerveConstants(
                wheelDiameter,
                angleGearRatio,
                driveGearRatio,
                angleKP,
                angleKI,
                angleKD,
                angleKF,
                driveMotorInvert,
                angleMotorInvert,
                canCoderInvert
            )
        }
    }
}



