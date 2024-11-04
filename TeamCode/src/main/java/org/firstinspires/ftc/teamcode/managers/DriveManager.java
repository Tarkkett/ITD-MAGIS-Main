package org.firstinspires.ftc.teamcode.managers;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.MovementControlRunnable;
import org.firstinspires.ftc.teamcode.State;

import java.util.HashMap;
import java.util.Map;

public class DriveManager implements State<DriveManager.DriveState> {

    private final HardwareManager hardwareManager;
    private final Telemetry telemetry;
    private final GamepadEx gamepadDriver;

    private DriveState state = DriveState.LOCKED;
    private DriveState previousState = DriveState.LOCKED;

    private Thread movementControlThread;
    private MovementControlRunnable movementControlRunnable;

    private final Map<Transition, Runnable> stateTransitionActions = new HashMap<>();

    public DriveManager(HardwareManager hardwareManager, Telemetry telemetry, GamepadEx gamepadDriver) {

        this.hardwareManager = hardwareManager;
        this.telemetry = telemetry;
        this.gamepadDriver = gamepadDriver;

        configureDrive();
        InitializeStateTransitionActions();
    }

    private void configureDrive() {

        hardwareManager.frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        hardwareManager.backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        movementControlRunnable = new MovementControlRunnable(telemetry, this, gamepadDriver, hardwareManager);
        movementControlThread = new Thread(movementControlRunnable);
        movementControlThread.start();
    }

    @Override
    public void InitializeStateTransitionActions() {

        stateTransitionActions.put(new Transition(DriveState.LOCKED, DriveState.UNLOCKED), this::onUnlock);
        stateTransitionActions.put(new Transition(DriveState.UNLOCKED, DriveState.LOCKED), this::onLock);
    }

    @Override
    public void SetSubsystemState(DriveState newState) {
        if (state != newState) {
            previousState = state;
            state = newState;

            Transition transition = new Transition(previousState, newState);
            Runnable action = stateTransitionActions.get(transition);
            if (action != null) {
                action.run();
            }

            telemetry.addData("Drive State Changed:", newState);
            telemetry.update();
        }
    }

    private void onUnlock() {
        telemetry.addLine("Drive system unlocked and ready to move.");
        telemetry.update();


    }

    private void onLock() {
        telemetry.addLine("Drive system locked.");
        telemetry.update();
        hardwareManager.lockDrivetrain();

    }

    @Override
    public DriveState GetSubsystemState(){
        return state;
    }

    @Override
    public void loop() {}

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

        if (GetSubsystemState() != DriveState.LOCKED) {
            hardwareManager.frontLeft.setPower(frontLeftPower * powerMultiplier);
            hardwareManager.backLeft.setPower(backLeftPower * powerMultiplier);
            hardwareManager.frontRight.setPower(frontRightPower * powerMultiplier);
            hardwareManager.backRight.setPower(backRightPower * powerMultiplier);
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

    public enum DriveState {
        UNLOCKED,
        LOCKED
    }
}