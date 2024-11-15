package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.ftc.GoBildaPinpointDriver;
import com.acmerobotics.roadrunner.ftc.GoBildaPinpointDriverRR;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.managers.DriveManager;

public class MovementControlRunnable implements Runnable {

    private final DriveManager driveManager;
    private final Telemetry telemetry;
    private final GamepadEx gamepad;
    private volatile boolean running = true;
    private PinpointDrive drive;
    private double headingAngle;
    private final double TRIGGER_MARGIN = 0.1;

    public MovementControlRunnable(Telemetry telemetry, DriveManager driveManager, GamepadEx gamepad, PinpointDrive drive) {
        this.telemetry = telemetry;
        this.driveManager = driveManager;
        this.gamepad = gamepad;
        this.drive = drive;
    }

    @Override
    public void run() {
        while (running) {
            ElapsedTime loopTime = new ElapsedTime();
            loopTime.reset();

            double y = gamepad.getLeftY();
            double x = gamepad.getLeftX();
            double rx = gamepad.getRightX();
            double multiplier = calculatePowerMultiplier();

            drive.pinpoint.update(GoBildaPinpointDriver.readData.ONLY_UPDATE_HEADING);
            drive.pinpoint.update(GoBildaPinpointDriverRR.readData.ONLY_UPDATE_HEADING);

            headingAngle = drive.pinpoint.getPosition().getHeading(AngleUnit.RADIANS);

            telemetry.addData("Heading:", headingAngle);

            driveManager.drive(new Pose2d(x, y, new Rotation2d(headingAngle)), rx, multiplier);
        }
    }

    private double calculatePowerMultiplier() {
        if (gamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > TRIGGER_MARGIN) {
            return 0.4;
        } else if (gamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > TRIGGER_MARGIN) {
            return 0.2;
        }
        return 1.0;
    }

    public void stop() {
        running = false;
    }
}
