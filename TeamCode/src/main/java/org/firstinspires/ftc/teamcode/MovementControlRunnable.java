package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.drivers.GoBildaPinpointDriver;
import org.firstinspires.ftc.teamcode.managers.DriveManager;
import org.firstinspires.ftc.teamcode.managers.HardwareManager;

public class MovementControlRunnable implements Runnable {

    private final DriveManager driveManager;
    private final HardwareManager hardwareManager;
    private final Telemetry telemetry;
    private final GamepadEx gamepad;
    private volatile boolean running = true;

    private int loopCounter = 0;
    private double headingAngle;
    private final double triggerMargin = 0.1;

    public MovementControlRunnable(Telemetry telemetry, DriveManager driveManager, GamepadEx gamepad, HardwareManager hardwareManager) {
        this.telemetry = telemetry;
        this.driveManager = driveManager;
        this.gamepad = gamepad;
        this.hardwareManager = hardwareManager;
    }

    @Override
    public void run() {
        while (running) {
            ElapsedTime loopTime = new ElapsedTime();
            loopTime.reset();

            hardwareManager.odo.update(GoBildaPinpointDriver.readData.ONLY_UPDATE_HEADING);

            double y = -gamepad.getLeftY();
            double x = -gamepad.getLeftX();
            double rx = gamepad.getRightX();
            double multiplier = calculatePowerMultiplier();

            if (loopCounter % hardwareManager.IMU_DATA_SAMPLING_RATE == 0) {
                headingAngle = hardwareManager.odo.getHeading();
            }
            loopCounter++;

            driveManager.drive(new Pose2d(x, y, new Rotation2d(headingAngle)), rx, multiplier);

            telemetry.addData("Drive Status", "Active");
            telemetry.addData("LX", x);
            telemetry.addData("Loop Time (ms)", loopTime.milliseconds());
        }
    }

    private double calculatePowerMultiplier() {
        if (gamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > triggerMargin) {
            return 1.0;
        } else if (gamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > triggerMargin) {
            return 0.4;
        }
        return 0.6;
    }

    public void stop() {
        running = false;
    }
}
