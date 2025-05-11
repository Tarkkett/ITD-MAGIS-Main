package org.firstinspires.ftc.teamcode.managers;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.geometry.Vector2d;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.MovementControlRunnable;
import org.firstinspires.ftc.teamcode.drivers.C_PID;
import org.firstinspires.ftc.teamcode.util.GamepadPlus;

@Config
public class DriveManager implements Manager<DriveManager._DriveState> {

    private final HardwareManager hardwareManager;

    private C_PID headingController = new C_PID(0.3, 0, 0);
    private final Telemetry telemetry;
    private GamepadPlus gamepadDriver;

    private _DriveState managerState = _DriveState.LOCKED;

    private Thread movementControlThread;
    private MovementControlRunnable movementControlRunnable;
    public static double P_Heading = -1.6;
    public static double I_Heading = 0.0;
    public static double D_Heading = -0.1;

    private double targetHeading = 0;
    private double currentHeading = 0;
    private boolean holdingHeading = false;

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

    private double wrapAngle(double angle) {
        while (angle > Math.PI) angle -= 2 * Math.PI;
        while (angle < -Math.PI) angle += 2 * Math.PI;
        return angle;
    }

    public void drive(Vector2d movementVector, double rotationInput, double powerMultiplier, double currentHeading) {

        double cosHeading = Math.cos(-currentHeading);
        double sinHeading = Math.sin(-currentHeading);

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
                gamepadDriver.Warn();
            }
        }

//! Heading auto correction - disabled for competition. After ESD robot starts spinning until reconnect.
//        this.currentHeading = currentHeading;
//
//        this.currentHeading = wrapAngle(currentHeading);
//
//        headingController.tune(P_Heading, I_Heading, D_Heading);
//
//        FtcDashboard dashboard = FtcDashboard.getInstance();
//        Telemetry telemetry = dashboard.getTelemetry();
//
//        double movementMagnitude = movementVector.magnitude();
//
//        telemetry.addData("CurrentRotation", currentHeading);
//
//        double cosHeading = Math.cos(-currentHeading);
//        double sinHeading = Math.sin(-currentHeading);
//
//        if (Math.abs(rotationInput) > 0.05) {
//            targetHeading = currentHeading;
//            holdingHeading = false;
//        } else if (!holdingHeading) {
//            holdingHeading = true;
//        }
//
//        targetHeading = wrapAngle(targetHeading);
//
//        telemetry.addData("IsHoldingHeading? ", holdingHeading);
//
//        double headingError = wrapAngle(targetHeading - currentHeading);
//        double headingCorrection = 0;
//
//        if (holdingHeading) {
//            if (movementMagnitude > 0.1) {
//                double adaptiveGain = Math.min(1.0, Math.abs(headingError) / Math.toRadians(10)); // Scale correction
//                headingCorrection = headingController.update(headingError, 0) * adaptiveGain;
//            }
//        }
//
//        double adjustedX = movementVector.getX() * cosHeading - movementVector.getY() * sinHeading;
//        double adjustedY = movementVector.getX() * sinHeading + movementVector.getY() * cosHeading;
//
//        adjustedX *= 1.1;
//
//        telemetry.addData("TargetRotation", targetHeading);
//
//        double correctedRotation = rotationInput + headingCorrection;
//
//        telemetry.addData("CorrectedRot", correctedRotation);
//        telemetry.update();
//
//        double maxMagnitude = Math.max(Math.abs(adjustedY) + Math.abs(adjustedX) + Math.abs(correctedRotation), 1);
//        double frontLeftPower = (adjustedY + adjustedX + correctedRotation) / maxMagnitude;
//        double backLeftPower = (adjustedY - adjustedX + correctedRotation) / maxMagnitude;
//        double frontRightPower = (adjustedY - adjustedX - correctedRotation) / maxMagnitude;
//        double backRightPower = (adjustedY + adjustedX - correctedRotation) / maxMagnitude;
//
//        if (managerState == _DriveState.UNLOCKED) {
//            hardwareManager.frontLeft.setPower(frontLeftPower * powerMultiplier);
//            hardwareManager.backLeft.setPower(backLeftPower * powerMultiplier);
//            hardwareManager.frontRight.setPower(frontRightPower * powerMultiplier);
//            hardwareManager.backRight.setPower(backRightPower * powerMultiplier);
//        }
//        else {
//            if (gamepadDriver.driveInput()) {
//                gamepadDriver.Warn();
//            }
//        }
    }


    public void Lock() {
        managerState = _DriveState.LOCKED;
        hardwareManager.stopDriveMotors();
        gamepadDriver.setLedColor(1.0d, 0, 0, 1000);
    }

    public void Unlock(){
        managerState = _DriveState.UNLOCKED;
        hardwareManager.stopDriveMotors();
        gamepadDriver.setLedColor(0, 1.0d, 0, 1000);
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

    public void resetHeading() {
        currentHeading = 0;
        targetHeading = 0;
    }

    public enum _DriveState {
        UNLOCKED,
        LOCKED
    }
}