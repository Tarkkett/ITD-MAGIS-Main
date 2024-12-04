package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.ftc.GoBildaPinpointDriver;
import com.acmerobotics.roadrunner.ftc.GoBildaPinpointDriverRR;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.managers.DriveManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;
import org.firstinspires.ftc.teamcode.util.GamepadPlus;

public class MovementControlRunnable implements Runnable {

    private final DriveManager driveManager;
    private OuttakeManager outtakeManager;
    private final Telemetry telemetry;
    private GamepadPlus gamepad;
    private volatile boolean running = true;
    private PinpointDrive drive;
    private double headingAngle;
    private final double TRIGGER_MARGIN = 0.1;

    public MovementControlRunnable(Telemetry telemetry, DriveManager driveManager, GamepadPlus gamepad, PinpointDrive drive, OuttakeManager outtakeManager) {
        this.telemetry = telemetry;
        this.driveManager = driveManager;
        this.gamepad = gamepad;
        this.drive = drive;
        this.outtakeManager = outtakeManager;
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

            driveManager.drive(new Pose2d(x, y, new Rotation2d(headingAngle)), rx, multiplier);
        }
    }

    private double calculatePowerMultiplier() {
        //! If switching back to trigger, remember to use TRIGGER_MARGIN variable.

        if (outtakeManager.managerState == OuttakeManager._OuttakeState.DEPOSIT){
            return 0.5;
        }
//        if (gamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > TRIGGER_MARGIN)
        else if (gamepad.isDown(GamepadKeys.Button.LEFT_BUMPER)) {
            return 0.2;
        }
//        if (gamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > TRIGGER_MARGIN)
        else if (gamepad.isDown(GamepadKeys.Button.RIGHT_BUMPER)) {
            return 0.4;
        }
        return 1.0;
    }

    public void stop() {
        running = false;
    }
}
