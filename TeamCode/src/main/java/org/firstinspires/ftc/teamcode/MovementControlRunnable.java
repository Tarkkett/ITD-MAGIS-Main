package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.geometry.Vector2d;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.drivers.GoBildaPinpointDriver;
import org.firstinspires.ftc.teamcode.managers.DriveManager;
import org.firstinspires.ftc.teamcode.managers.HardwareManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;
import org.firstinspires.ftc.teamcode.util.GamepadPlus;

public class MovementControlRunnable implements Runnable {

    private DriveManager driveManager;
    private OuttakeManager outtakeManager;
    private HardwareManager hardwareManager;
    private GamepadPlus gamepad;
    private volatile boolean running = true;
    private double currentHeading;
    private double TRIGGER_MARGIN = 0.1;

    public MovementControlRunnable(DriveManager driveManager, GamepadPlus gamepad, OuttakeManager outtakeManager, HardwareManager hardwareManager) {
        this.driveManager = driveManager;
        this.gamepad = gamepad;
        this.outtakeManager = outtakeManager;
        this.hardwareManager = hardwareManager;
    }

    @Override
    public void run() {
        while (running) {

            if (gamepad.driveInput()){
                gamepad.rumble(200);
            }

            double y = gamepad.getLeftY();
            double x = gamepad.getLeftX();
            double rx = gamepad.getRightX();
            double multiplier = calculatePowerMultiplier();

            hardwareManager.pinpointDriver.update(GoBildaPinpointDriver.readData.ONLY_UPDATE_HEADING);

            currentHeading = hardwareManager.pinpointDriver.getPosition().getHeading(AngleUnit.RADIANS);

            driveManager.drive(new Vector2d(x, y), -rx, multiplier, currentHeading);
        }
    }

    private double calculatePowerMultiplier() {
        //! If switching back to trigger, remember to use TRIGGER_MARGIN variable.

        if (outtakeManager.GetLiftCurrentPos() > 2200){
            if (gamepad.isDown(GamepadKeys.Button.LEFT_BUMPER)) {
                return 0.2;
            }
            return 0.5;
        }
//        if (gamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > TRIGGER_MARGIN){
        else if (gamepad.isDown(GamepadKeys.Button.LEFT_BUMPER)) {
            return 0.3;
        }
//        if (gamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > TRIGGER_MARGIN){
        else if (gamepad.isDown(GamepadKeys.Button.RIGHT_BUMPER)) {
            return 0.435;
        }
        return 1.0;
    }

    public void stop() {
        running = false;
    }
}