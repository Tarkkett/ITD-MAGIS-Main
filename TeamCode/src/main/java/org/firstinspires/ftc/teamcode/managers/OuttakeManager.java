package org.firstinspires.ftc.teamcode.managers;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.PID_PARAMS;
import org.firstinspires.ftc.teamcode.drivers.C_PID;

@SuppressWarnings("rawtypes")





@Config
public class OuttakeManager implements Manager<OuttakeManager._OuttakeState> {

    public interface Positionable {
        float getPosition();
    }

    private static final int MIN_THRESHOLD =70;
    private static final int MAX_THRESHOLD =2200;
    public boolean selectingProcess = false;

    HardwareManager hardwareManager;
    IntakeManager intakeManager;
    Telemetry telemetry;

    public OuttakeManager._OuttakeState managerState;

    public PID_PARAMS params = new PID_PARAMS(0.012,0,0.00009, 5);
    C_PID controller = new C_PID(params);

    public int targetPosition;
    public int encoderPos;

    private boolean isAutoLoop = true;

    _LiftMode mode;

    public OuttakeManager(HardwareManager hardwareManager, Telemetry telemetry, IntakeManager intakeManager){

        this.hardwareManager = hardwareManager;
        this.telemetry = telemetry;
        this.intakeManager = intakeManager;

        mode = _LiftMode.AUTO;
    }

    @Override
    public void loop() {
        controller.tune(params);
        encoderPos = calculateEncoderAverage();
        double power = controller.update(targetPosition, encoderPos);

        if (mode == _LiftMode.AUTO) {
            updateLiftMotors(power);
        }

        updateTelemetry();
    }

    private int calculateEncoderAverage() {
        return (int) ((hardwareManager.liftLeft.getCurrentPosition() +
                hardwareManager.liftRight.getCurrentPosition()) / 2.0);
    }

    private void updateLiftMotors(double power) {
        hardwareManager.liftLeft.setPower(power);
        hardwareManager.liftRight.setPower(power);
    }

    private void updateTelemetry() {
        FtcDashboard dashboard = FtcDashboard.getInstance();
        Telemetry telemetry = dashboard.getTelemetry();
        telemetry.addData("CurrentPosLift", encoderPos);
        telemetry.addData("TargetPosLift", targetPosition);
        telemetry.update();
    }

    @Override
    public _OuttakeState GetManagerState() {
        if (managerState == _OuttakeState.DEPOSIT && selectingProcess && encoderPos > 150){
            return _OuttakeState.DEPOSIT;
        } else if (managerState == _OuttakeState.HOME && encoderPos < 150){
            return _OuttakeState.HOME;
        } else if (managerState == _OuttakeState.TRANSFER && encoderPos < 240){
            return _OuttakeState.TRANSFER;
        } else return _OuttakeState.IDLE;
    }

    private int Average(float p1, float p2) {
        return (int) ((p1 + p2) / 2);
    }

    public void update(_LiftState targetState) {
        switch (targetState){
            case HIGH_RUNG:
                targetPosition = (int) _LiftState.HIGH_RUNG.getPosition();
                break;
            case LOW_RUNG:
                targetPosition = (int) _LiftState.LOW_RUNG.getPosition();
                break;
            case LOW_BUCKET:
                targetPosition = (int) _LiftState.LOW_BUCKET.getPosition();
                break;
            case HIGH_BUCKET:
                targetPosition = (int) _LiftState.HIGH_BUCKET.getPosition();
                break;
            case HIGH_CHAMBER:
                targetPosition = (int) _LiftState.HIGH_CHAMBER.getPosition();
                break;
            case LOW_CHAMBER:
                targetPosition = (int) _LiftState.LOW_CHAMBER.getPosition();
                break;
            case TRANSFER:
                targetPosition = (int) _LiftState.TRANSFER.getPosition();
                break;
            case HOME:
                targetPosition = (int) _LiftState.HOME.getPosition();
                break;
            case HIGH_CHAMBER_LOWER:
                targetPosition = (int) _LiftState.HIGH_CHAMBER_LOWER.getPosition();
                break;
            case HANG:
                targetPosition = (int) _LiftState.HANG.getPosition();
                break;
            case CLEARED:
                targetPosition = (int) _LiftState.CLEARED.getPosition();
                break;
            case ZERO:
                targetPosition = (int) _LiftState.ZERO.getPosition();
                break;
            case STUCK:
                //!Mag4.2
                break;
        }
    }

    public void update(_OuttakeTiltServoState targetState) {
        switch (targetState){
            case PICKUP:
                hardwareManager.outtakeTiltServo.setPosition(_OuttakeTiltServoState.PICKUP.getPosition());
                break;
            case TRANSFER:
                hardwareManager.outtakeTiltServo.setPosition(_OuttakeTiltServoState.TRANSFER.getPosition());
                break;
            case DEPOSIT:
                hardwareManager.outtakeTiltServo.setPosition(_OuttakeTiltServoState.DEPOSIT.getPosition());
                break;
            case DEPOSIT_POST:
                hardwareManager.outtakeTiltServo.setPosition(_OuttakeTiltServoState.DEPOSIT_POST.getPosition());
                break;
        }
    }

    public void update(_ExtendoServoState targetState) {
        switch (targetState){
            case PICKUP:
                hardwareManager.outtakeExtendoServo.setPosition(_ExtendoServoState.PICKUP.getPosition());
                break;
            case DEPOSIT:
                hardwareManager.outtakeExtendoServo.setPosition(_ExtendoServoState.DEPOSIT.getPosition());
                break;
            case TRANSFER:
                hardwareManager.outtakeExtendoServo.setPosition(_ExtendoServoState.TRANSFER.getPosition());
                break;
        }
    }

    public void update(_OuttakeClawServoState targetState) {
        switch (targetState){
            case GRIP:
                hardwareManager.outtakeClawServo.setPosition(_OuttakeClawServoState.GRIP.getPosition());
                break;
            case RELEASE:
                hardwareManager.outtakeClawServo.setPosition(_OuttakeClawServoState.RELEASE.getPosition());
                break;
        }
    }

    public void update(_OuttakeYawServoState targetState) {
        switch (targetState){
            case HORIZONTAL_ServoDown:
                hardwareManager.outtakeYawServo.setPosition(_OuttakeYawServoState.HORIZONTAL_ServoDown.getPosition());
                break;
            case VERTICAL:
                hardwareManager.outtakeYawServo.setPosition(_OuttakeYawServoState.VERTICAL.getPosition());
                break;
            case HORIZONTAL_ServoUp:
                hardwareManager.outtakeYawServo.setPosition(_OuttakeYawServoState.HORIZONTAL_ServoUp.getPosition());
                break;
        }
    }

    public void lowerLiftPosition(int i) {
        int newPos = encoderPos + i;
        if (newPos > MIN_THRESHOLD && newPos < MAX_THRESHOLD){
            targetPosition = newPos;
        }
    }

    public void updateMode(boolean isAuto) {
        if (isAuto){
            mode = _LiftMode.AUTO;
        } else{
            mode = _LiftMode.MANUAL;
        }
    }

    //!Watch
    public boolean canHome() {
        if (hardwareManager.outtakeExtendoServo.getPosition() > (double) _ExtendoServoState.PICKUP.position - 0.1 && hardwareManager.outtakeExtendoServo.getPosition() < (double) _ExtendoServoState.PICKUP.position + 0.1d){
            return false;
        }
        else return true;
    }

    public enum _OuttakeState{
        HOME,
        DEPOSIT,
        IDLE,
        PICKUP,
        CALIBRATION,
        TRANSFER
    }

    public enum _LiftMode{
        MANUAL,
        AUTO
    }

    public enum _LiftState implements Positionable{
        LOW_CHAMBER (100),
        LOW_RUNG    (200),
        HIGH_CHAMBER(1670),
        HIGH_RUNG   (1000),
        TRANSFER    (250),
        CLEARED(400),
        HOME        (50),
        STUCK       (0),
        HIGH_BUCKET (2600),
        LOW_BUCKET  (1200),
        HIGH_CHAMBER_LOWER(1100),
        HANG(1900),
        ZERO(0);

        private final float position;

        _LiftState(float position) {
            this.position = position;
        }

        @Override
        public float getPosition() {
            return position;
        }
    }

    public enum _ExtendoServoState implements Positionable {
        PICKUP(0.035f),
        DEPOSIT(0.3f),
        TRANSFER(0.15f);

        private final float position;

        _ExtendoServoState(float position) {
            this.position = position;
        }

        @Override
        public float getPosition() {
            return position;
        }
    }

    public enum _OuttakeTiltServoState implements Positionable {
        DEPOSIT(1f),
        DEPOSIT_POST(0.9f),
        PICKUP(0.1f),
        TRANSFER(1f);

        private final float position;

        _OuttakeTiltServoState(float position) {
            this.position = position;
        }

        @Override
        public float getPosition() {
            return position;
        }
    }

    public enum _OuttakeClawServoState implements Positionable {
        GRIP    (0.25f),
        RELEASE     (0f);

        private final float position;

        _OuttakeClawServoState(float position) {
            this.position = position;
        }

        @Override
        public float getPosition() {
            return position;
        }
    }

    public enum _OuttakeYawServoState implements Positionable {
        HORIZONTAL_ServoDown    (0f),
        VERTICAL     (0.5f),
        HORIZONTAL_ServoUp(1f);

        private final float position;

        _OuttakeYawServoState(float position) {
            this.position = position;
        }

        @Override
        public float getPosition() {
            return position;
        }
    }

    public Action OuttakeExtendoAction(_ExtendoServoState state) {
        return new Action(){
            private boolean initialize = false;
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!initialize){
                    update(state);
                    initialize = true;
                }
                return false;
            }
        };
    }

    public Action LoopLift(){
        return new Action(){
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket){
                if (isAutoLoop) {
                    loop();
                }
                else{ return false;}

                return true;
            }
        };
    }

    public Action DriveLift(int position){
        return new Action(){

            private boolean initialized = false;
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!initialized){
                    targetPosition = position;
                    initialized = true;
                }
                return false;

            }
        };
    }

    public Action StopLift() {
        return new Action(){
            private boolean initialize = false;
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!initialize){
                    isAutoLoop = false;
                    initialize = true;
                }
                return false;
            }
        };
    }
}