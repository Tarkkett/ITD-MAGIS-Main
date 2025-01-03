package org.firstinspires.ftc.teamcode.managers;

import android.os.Environment;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TelemetryManager {

    private static TelemetryManager instance;

    private Telemetry telemetry;
    private final Map<String, String> telemetryData = new HashMap<>();
    private BufferedWriter logWriter;
    private boolean isLogging = false;

    private TelemetryManager() {}

    public static TelemetryManager getInstance() {
        if (instance == null) {
            instance = new TelemetryManager();
        }
        return instance;
    }

    public void setTelemetry(Telemetry telemetry) {
        this.telemetry = telemetry;
        listAvailableDrives(); // Show all drives on initialization
    }

    public void addData(String key, String value) {
        telemetryData.put(key, value);
    }

    public void updateTelemetry() {
        if (telemetry != null) {
            telemetry.clear();
            for (Map.Entry<String, String> entry : telemetryData.entrySet()) {
                telemetry.addData(entry.getKey(), entry.getValue());
            }
            telemetry.update();
        }
        logData();
    }

    public void startLogging() {
        File logFile = getLogFile();
        if (logFile != null) {
            try {
                logWriter = new BufferedWriter(new FileWriter(logFile));
                isLogging = true;
                log("Logging started: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            } catch (IOException e) {
                if (telemetry != null) {
                    telemetry.addLine("Failed to start logging: " + e.getMessage());
                    telemetry.update();
                }
            }
        }
    }

    public void stopLogging() {
        if (isLogging) {
            try {
                log("Logging stopped: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                logWriter.close();
                isLogging = false;
            } catch (IOException e) {
                if (telemetry != null) {
                    telemetry.addLine("Failed to stop logging: " + e.getMessage());
                    telemetry.update();
                }
            }
        }
    }

    private void logData() {
        if (isLogging && logWriter != null) {
            try {
                StringBuilder logEntry = new StringBuilder();
                logEntry.append(new SimpleDateFormat("HH:mm:ss.SSS").format(new Date()));
                for (Map.Entry<String, String> entry : telemetryData.entrySet()) {
                    logEntry.append(", ").append(entry.getKey()).append(": ").append(entry.getValue());
                }
                logWriter.write(logEntry.toString());
                logWriter.newLine();
                logWriter.flush();
            } catch (IOException e) {
                if (telemetry != null) {
                    telemetry.addLine("Error writing log: " + e.getMessage());
                    telemetry.update();
                }
            }
        }
    }

    private void log(String message) {
        if (isLogging && logWriter != null) {
            try {
                logWriter.write(message);
                logWriter.newLine();
                logWriter.flush();
            } catch (IOException e) {
                if (telemetry != null) {
                    telemetry.addLine("Error writing log: " + e.getMessage());
                    telemetry.update();
                }
            }
        }
    }

    private File getLogFile() {
        File logDir = new File(Environment.getExternalStorageDirectory(), "FTCLogs");
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
        return new File(logDir, "telemetry_log_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".txt");
    }

    private void listAvailableDrives() {
        if (telemetry != null) {
            telemetry.addLine("Available Drives:");
            // Add primary external storage
            File primaryStorage = Environment.getExternalStorageDirectory();
            if (primaryStorage != null && primaryStorage.exists()) {
                telemetry.addData("Primary Storage", primaryStorage.getAbsolutePath());
            }

            // List all files in common storage directories
            File storageDir = new File("/storage");
            if (storageDir.exists() && storageDir.isDirectory()) {
                File[] subDirs = storageDir.listFiles();
                if (subDirs != null) {
                    for (File dir : subDirs) {
                        telemetry.addData("Drive", dir.getAbsolutePath());
                    }
                }
            }

            // Add any other common paths
            telemetry.addData("Download Dir", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
            telemetry.addData("DCIM Dir", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath());
            telemetry.addData("Music Dir", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath());

            telemetry.update();
        }
    }
}
