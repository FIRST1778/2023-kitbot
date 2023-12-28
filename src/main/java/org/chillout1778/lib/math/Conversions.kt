package org.chillout1778.lib.math

object Conversions {
    /**
     * @param positionCounts CANCoder Position Counts
     * @param gearRatio Gear Ratio between CANCoder and Mechanism
     * @return Degrees of Rotation of Mechanism
     */
    fun CANcoderToDegrees(positionCounts: Double, gearRatio: Double): Double {
        return positionCounts * (360.0 / (gearRatio * 4096.0))
    }

    /**
     * @param degrees Degrees of rotation of Mechanism
     * @param gearRatio Gear Ratio between CANCoder and Mechanism
     * @return CANCoder Position Counts
     */
    fun degreesToCANcoder(degrees: Double, gearRatio: Double): Double {
        return degrees / (360.0 / (gearRatio * 4096.0))
    }

    /**
     * @param counts Falcon Position Counts
     * @param gearRatio Gear Ratio between Falcon and Mechanism
     * @return Degrees of Rotation of Mechanism
     */
    fun falconToDegrees(positionCounts: Double, gearRatio: Double): Double {
        return positionCounts * (360.0 / (gearRatio * 2048.0))
    }

    /**
     * @param degrees Degrees of rotation of Mechanism
     * @param gearRatio Gear Ratio between Falcon and Mechanism
     * @return Falcon Position Counts
     */
    fun degreesToFalcon(degrees: Double, gearRatio: Double): Double {
        return degrees / (360.0 / (gearRatio * 2048.0))
    }

    /**
     * @param rps rotations per second
     * @param gearRatio Gear Ratio between Falcon and Mechanism (set to 1 for Falcon RPM)
     * @return RPM of Mechanism
     */
    fun falconToRPM(rps: Double, gearRatio: Double): Double {
        val motorRPM = rps * 60.0
        return motorRPM / gearRatio
    }

    /**
     * @param RPM RPM of mechanism
     * @param gearRatio Gear Ratio between Falcon and Mechanism (set to 1 for Falcon RPM)
     * @return RPM of Mechanism
     */

    /**
     * @param velocitycounts Falcon Velocity Counts
     * @param circumference Circumference of Wheel
     * @param gearRatio Gear Ratio between Falcon and Mechanism (set to 1 for Falcon MPS)
     * @return Falcon Velocity Counts
     */
    fun falconToMPS(
        velocity: Double,
        circumference: Double,
        gearRatio: Double
    ): Double {
        val wheelRPM = falconToRPM(velocity, gearRatio)
        return wheelRPM * circumference / 60
    }

    /**
     * @param position rotation of falcon
     * @param circumference Circumference of Wheel
     * @param gearRatio Gear Ratio between Falcon and Wheel
     * @return Meters
     */
    fun falconToMeters(position: Double, circumference: Double, gearRatio: Double): Double {
        return position * (circumference / gearRatio)
    }

    /**
     * @param meters Meters
     * @param circumference Circumference of Wheel
     * @param gearRatio Gear Ratio between Falcon and Wheel
     * @return Falcon Rotation
     */
    fun MetersToFalcon(meters: Double, circumference: Double, gearRatio: Double): Double {
        return meters / (circumference / gearRatio)
    }
}