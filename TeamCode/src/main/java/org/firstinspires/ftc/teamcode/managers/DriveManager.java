package org.firstinspires.ftc.teamcode.managers;

import com.arcrobotics.ftclib.geometry.Pose2d;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.MovementControlRunnable;
import org.firstinspires.ftc.teamcode.util.GamepadPlus;

public class DriveManager implements Manager<DriveManager._DriveState> {

    private final HardwareManager hardwareManager;
    private final Telemetry telemetry;
    private GamepadPlus gamepadDriver;

    private _DriveState managerState = _DriveState.LOCKED;

    private Thread movementControlThread;
    private MovementControlRunnable movementControlRunnable;

    public DriveManager(HardwareManager hardwareManager, Telemetry telemetry, GamepadPlus gamepadDriver, OuttakeManager outtakeManager) {
        this.hardwareManager = hardwareManager;
        this.telemetry = telemetry;
        this.gamepadDriver = gamepadDriver;

        configureDrive(outtakeManager);
    }

    private void configureDrive(OuttakeManager outtakeManager) {
        movementControlRunnable = new MovementControlRunnable(telemetry, this, gamepadDriver, outtakeManager, hardwareManager);
        movementControlThread = new Thread(movementControlRunnable);
        movementControlThread.start();
    }

    @Override
    public void loop() { /* Continue */ }

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
            if (gamepadDriver.driveInput()) {
                gamepadDriver.rumble(100);
            }
        }
    }

    public void Lock() {
        managerState = _DriveState.LOCKED;
        hardwareManager.stopDriveMotors();
        gamepadDriver.setLedColor(1.0d, 0, 0, 100000);
    }

    public void Unlock(){
        managerState = _DriveState.UNLOCKED;
        hardwareManager.stopDriveMotors();
        gamepadDriver.setLedColor(0, 1.0d, 0, 100000);
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

    public enum _DriveState {
        UNLOCKED,
        LOCKED
    }
}