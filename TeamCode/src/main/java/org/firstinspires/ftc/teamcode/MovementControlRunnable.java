package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.geometry.Vector2d;
import com.pedropathing.follower.Follower;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.drivers.GoBildaPinpointDriver;
import org.firstinspires.ftc.teamcode.managers.DriveManager;
import org.firstinspires.ftc.teamcode.managers.HardwareManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;
import org.firstinspires.ftc.teamcode.util.GamepadPlus;

public class MovementControlRunnable implements Runnable {

    private HardwareManager hardwareManager;
    private DriveManager driveManager;
    private OuttakeManager outtakeManager;
    private GamepadPlus gamepad;
    private volatile boolean running = true;
    private Telemetry telemetry;
    private double currentHeading;
    private double TRIGGER_MARGIN = 0.1;
    private Follower imuFollower;

    public MovementControlRunnable(Telemetry telemetry, DriveManager driveManager, GamepadPlus gamepad, OuttakeManager outtakeManager, Follower imuFollower, HardwareManager hardwareManager) {
        this.driveManager = driveManager;
        this.gamepad = gamepad;
        this.outtakeManager = outtakeManager;
        this.imuFollower = imuFollower;
        this.telemetry = telemetry;
        this.hardwareManager = hardwareManager;


    }

    @Override
    public void run() {
        long lastPrint = System.currentTimeMillis();

        try {
            while (running) {
                try {

//                    imuFollower.update();

                    double y = gamepad.getLeftY();
                    double x = gamepad.getLeftX();
                    double rx = gamepad.getRightX();
                    double multiplier = calculatePowerMultiplier();


//                    if (hardwareManager.pinpointDriver != null && hardwareManager.pinpointDriver.getPosition() != null) {
//                        hardwareManager.pinpointDriver.update(GoBildaPinpointDriver.readData.ONLY_UPDATE_HEADING);
//                        currentHeading = hardwareManager.pinpointDriver.getPosition().getHeading(AngleUnit.RADIANS);
//                    }

//                    currentHeading = imuFollower.getPose().getHeading();

                    currentHeading = hardwareManager.GetRobotHeading();
                    driveManager.drive(new Vector2d(x, y), -rx, multiplier, currentHeading);

                    // Debug heartbeat to telemetry
                    if (System.currentTimeMillis() - lastPrint > 1000) {
                        telemetry.addData("Drive Thread", "Alive at %d ms", System.currentTimeMillis() - lastPrint - 1000);
                        telemetry.addData("Heading (rad)", currentHeading);
                        telemetry.update();
                        lastPrint = System.currentTimeMillis();
                    }

                } catch (Exception e) {
                    telemetry.addData("Drive Thread Error", e.getMessage());
                    telemetry.update();
                    running = false;
                }
            }
        } finally {
            telemetry.addData("Drive Thread", "Exited safely");
            telemetry.update();
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