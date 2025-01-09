package org.firstinspires.ftc.teamcode.kotlin

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.util.ElapsedTime

/**
 * TelemetryManager: A helper class for managing telemetry updates.
 */
class TelemetryManager(private val opMode: OpMode) {
    // A map to store telemetry key-value pairs
    private val telemetryData = mutableMapOf<String, Any>()

    // Track the time for telemetry updates (optional)
    private val runtime = ElapsedTime()

    /**
     * Add or update a telemetry entry.
     * @param key The label or identifier for the telemetry data.
     * @param value The value to display for the given key.
     */
    fun addData(key: String, value: Any) {
        telemetryData[key] = value
    }

    /**
     * Remove a telemetry entry by its key.
     * @param key The label of the telemetry data to remove.
     */
    fun removeData(key: String) {
        telemetryData.remove(key)
    }

    /**
     * Clear all telemetry entries.
     */
    fun clearData() {
        telemetryData.clear()
    }

    /**
     * Display all telemetry data.
     */
    fun updateTelemetry() {
        opMode.telemetry.clear()

        // Add runtime info
        opMode.telemetry.addLine("Runtime: ${runtime.seconds()} sec")

        // Add all telemetry entries
        for ((key, value) in telemetryData) {
            opMode.telemetry.addData(key, value.toString())
        }

        opMode.telemetry.update()
    }
}
