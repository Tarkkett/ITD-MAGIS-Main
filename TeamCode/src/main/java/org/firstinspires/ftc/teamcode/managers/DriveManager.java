package org.firstinspires.ftc.teamcode.managers;

import com.arcrobotics.ftclib.geometry.Pose2d;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.MovementControlRunnable;
import org.firstinspires.ftc.teamcode.PinpointDrive;
import org.firstinspires.ftc.teamcode.util.State;
import org.firstinspires.ftc.teamcode.util.GamepadPlus;

import java.util.HashMap;
import java.util.Map;

public class DriveManager implements Manager<DriveManager._DriveState> {

    private final HardwareManager hardwareManager;
    private final Telemetry telemetry;
    private final GamepadPlus gamepadDriver;

    private _DriveState managerState = _DriveState.LOCKED;

    private Thread movementControlThread;
    private MovementControlRunnable movementControlRunnable;

    public DriveManager(HardwareManager hardwareManager, Telemetry telemetry, GamepadPlus gamepadDriver, PinpointDrive drive) {
        this.hardwareManager = hardwareManager;
        this.telemetry = telemetry;
        this.gamepadDriver = gamepadDriver;

        configureDrive(drive);
    }

    private void configureDrive(PinpointDrive drive) {

        movementControlRunnable = new MovementControlRunnable(telemetry, this, gamepadDriver, drive);
        movementControlThread = new Thread(movementControlRunnable);
        movementControlThread.start();
    }

    @Override
    public void loop() {}

    @Override
    public _DriveState GetManagerState() {
        return managerState;
    }

    public void drive(Pose2d movementVector, double rotationInput, double powerMultiplier) {

        double cosHeading = Math.cos(-movementVector.getHeading());
        double sinHeading = Math.sin(-movementVector.getHeading());

        double adjustedX = movementVector.getX() * cosHeading - movementVector.getY() * sinHeading;
        double adjustedY = movementVector.getX() * sinHeading + movementVector.getY() * cosHeading;

        adjustedX *= 1.1;

        double maxMagnitude = Math.max(Math.abs(adjustedY) + Math.abs(adjustedX) + Math.abs(rotationInput), 1);
        double frontLeftPower = (adjustedY + adjustedX + rotationInput) / maxMagnitude;
        double backLeftPower = (adjustedY - adjustedX + rotationInput) / maxMagnitude;
        double frontRightPower = (adjustedY - adjustedX - rotationInput) / maxMagnitude;
        double backRightPower = (adjustedY + adjustedX - rotationInput) / maxMagnitude;

        if (managerState == _DriveState.UNLOCKED) {
            hardwareManager.frontLeft.setPower(frontLeftPower * powerMultiplier);
            hardwareManager.backLeft.setPower(backLeftPower * powerMultiplier);
            hardwareManager.frontRight.setPower(frontRightPower * powerMultiplier);
            hardwareManager.backRight.setPower(backRightPower * powerMultiplier);
        }
        else {
            if (gamepadDriver.gamepad.left_stick_x > 0.1 || gamepadDriver.gamepad.left_stick_x < -0.1 || gamepadDriver.gamepad.left_stick_y > 0.1 || gamepadDriver.gamepad.left_stick_y < -0.1){
                gamepadDriver.rumble(100);
            }
        }
    }

    public void stopMovementControlThread() {

        if (movementControlRunnable != null) {
            movementControlRunnable.stop();
        }

        if (movementControlThread != null) {
            try {
                movementControlThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void onLocked() {
        managerState = _DriveState.LOCKED;
        hardwareManager.lockDrivetrain();
        gamepadDriver.gamepad.setLedColor(1, 0, 0, 100000);
    }

    public void onUnlocked(){
        managerState = _DriveState.UNLOCKED;
        gamepadDriver.gamepad.setLedColor(0, 1, 0, 100000);
    }

    public enum _DriveState {
        UNLOCKED,
        LOCKED
    }
}