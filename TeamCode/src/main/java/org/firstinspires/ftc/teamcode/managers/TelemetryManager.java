package org.firstinspires.ftc.teamcode.managers;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

//        telemetry.addData("Can Write?", logFile.canWrite());
//        telemetry.addData("Exists?", logFile.exists());
//        telemetry.addData("Name", logFile.getName());
//        telemetry.addData("Can Read?", logFile.canRead());
//        telemetry.addData("Is File?", logFile.isFile());
//        telemetry.addData("Can Execute?", logFile.canExecute());
//        telemetry.addData("Data", logFile);

        if (logFile != null) {
            try {
                logWriter = new BufferedWriter(new FileWriter(logFile.getAbsolutePath()));
                telemetry.addData("Writer", logWriter);
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

//    public void requestPermissionsIfNeeded() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(activity, new String[]{
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.READ_EXTERNAL_STORAGE
//                }, 100); // 100 is the request code
//            }
//        }
//    }

    private boolean isPathWritable(String path) {
        File file = new File(path);
        if (!file.exists() || !file.isDirectory()) {
            telemetry.addLine("Path does not exist or is not a directory: " + path);
            telemetry.update();
            return false;
        }
        return file.canWrite();
    }

    private File getLogFile() {
        File logDir = new File("/storage/36D7-8DD8/MagLogs");

//        telemetry.addData("Can Write Dir?", logDir.canWrite());
//        telemetry.addData("Exists Dir?", logDir.exists());
//        telemetry.addData("Name Dir", logDir.getName());
//        telemetry.addData("Can Read Dir?", logDir.canRead());
//        telemetry.addData("Is Directory Dir?", logDir.isDirectory());
//        telemetry.addData("Can Execute Dir?", logDir.canExecute());
//        telemetry.addData("Data Dir", logDir);

        if (!logDir.exists() && !logDir.mkdirs()) {
            telemetry.addLine("Failed to create log directory: " + logDir.getAbsolutePath());
            telemetry.update();
            return null;
        }
        return new File(logDir, "telemetry_log_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".txt");
    }

    private void listAvailableDrives() {
        if (telemetry != null) {
            telemetry.addLine("Available Drives:");

            File primaryStorage = Environment.getExternalStorageDirectory();
            if (primaryStorage != null && primaryStorage.exists()) {
                long totalSpace = primaryStorage.getTotalSpace();
                long freeSpace = primaryStorage.getFreeSpace();

                telemetry.addData("Primary Storage",
                        primaryStorage.getAbsolutePath() + " (Total: " + formatSize(totalSpace) + ", Free: " + formatSize(freeSpace) + ")");
            }

            File storageDir = new File("/storage");
            if (storageDir.exists() && storageDir.isDirectory()) {
                File[] subDirs = storageDir.listFiles();
                if (subDirs != null) {
                    for (File dir : subDirs) {
                        if (dir.isDirectory()) {
                            long totalSpace = dir.getTotalSpace();
                            long freeSpace = dir.getFreeSpace();
                            telemetry.addData("Drive",
                                    dir.getAbsolutePath() + " (Total: " + formatSize(totalSpace) + ", Free: " + formatSize(freeSpace) + ")");
                        }
                    }
                }
            }

            // Add any other common paths with their sizes
            addDirectoryWithSize("Download Dir", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
            addDirectoryWithSize("DCIM Dir", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM));
            addDirectoryWithSize("Music Dir", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC));

            telemetry.update();
        }
    }

    private void addDirectoryWithSize(String label, File dir) {
        if (dir != null && dir.exists()) {
            long totalSpace = dir.getTotalSpace();
            long freeSpace = dir.getFreeSpace();
            telemetry.addData(label, dir.getAbsolutePath() + " (Total: " + formatSize(totalSpace) + ", Free: " + formatSize(freeSpace) + ")");
        }
    }

    private String formatSize(long size) {
        if (size <= 0) return "0 B";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int unitIndex = (int) (Math.log10(size) / Math.log10(1024));
        double unitSize = size / Math.pow(1024, unitIndex);
        return String.format("%.2f %s", unitSize, units[unitIndex]);
    }
}
