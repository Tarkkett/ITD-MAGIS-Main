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

    //!Default PID parameters
    public PID_PARAMS params = new PID_PARAMS(0.012,0,0.0003, 5);
    C_PID controller = new C_PID(params);

    protected int targetPosition;
    protected int encoderPos;

    private boolean isAutoLoop = true;

    protected  _LiftMode mode;

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

    /**
     * @return isGRIP
     */
    public boolean isClosed() {
        if (hardwareManager.outtakeClawServo.getPosition() > _OuttakeClawServoState.GRIP.getPosition() - 0.05 &&
                hardwareManager.outtakeClawServo.getPosition() < _OuttakeClawServoState.GRIP.getPosition() + 0.05){
            return true;
        }
        else return false;
    }

    public void update(_LiftState targetState, int... position) {
        if (targetState != null) {
            switch (targetState) {
                case HIGH_BUCKET:
                    targetPosition = (int) _LiftState.HIGH_BUCKET.getPosition();
                    break;
                case HIGH_CHAMBER:
                    targetPosition = (int) _LiftState.HIGH_CHAMBER.getPosition();
                    break;
                case HIGH_CHAMBER_AUTO:
                    targetPosition = (int) _LiftState.HIGH_CHAMBER_AUTO.getPosition();
                    break;
                case HIGH_CHAMBER_LOWER_REVERSED:
                    targetPosition = (int) _LiftState.HIGH_CHAMBER_LOWER_REVERSED.getPosition();
                    break;
                case HIGH_CHAMBER_REVERSED:
                    targetPosition = (int) _LiftState.HIGH_CHAMBER_REVERSED.getPosition();
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
                case HANG_READY:
                    targetPosition = (int) _LiftState.HANG_READY.getPosition();
                    break;
                case CLEARED:
                    targetPosition = (int) _LiftState.CLEARED.getPosition();
                    break;
                case CLEARED_ALL:
                    targetPosition = (int) _LiftState.CLEARED_ALL.getPosition();
                    break;
                case ZERO:
                    targetPosition = (int) _LiftState.ZERO.getPosition();
                    break;
                case HANG_DOWN:
                    targetPosition = (int) _LiftState.HANG_DOWN.getPosition();
                    break;
            }
        }
        else {
            this.targetPosition = position[0];
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
            case BALANCE:
                hardwareManager.outtakeTiltServo.setPosition(_OuttakeTiltServoState.BALANCE.getPosition());
                break;
            case DEPOSIT_SPECIMEN:
                hardwareManager.outtakeTiltServo.setPosition(_OuttakeTiltServoState.DEPOSIT_SPECIMEN.getPosition());
                break;
            case DEPOSIT_SAMPLE:
                hardwareManager.outtakeTiltServo.setPosition(_OuttakeTiltServoState.DEPOSIT_SAMPLE.getPosition());
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
            case TRANSFER_PUSH:
                hardwareManager.outtakeExtendoServo.setPosition(_ExtendoServoState.TRANSFER_PUSH.getPosition());
                break;
            case ZERO:
                hardwareManager.outtakeExtendoServo.setPosition(_ExtendoServoState.ZERO.getPosition());
                break;
            case DEPOSIT_BACK:
                hardwareManager.outtakeExtendoServo.setPosition(_ExtendoServoState.DEPOSIT_BACK.getPosition());
                break;
            case DEPOSIT_FORWARDPUSH:
                hardwareManager.outtakeExtendoServo.setPosition(_ExtendoServoState.DEPOSIT_FORWARDPUSH.getPosition());
                break;
            case DEPOSIT_BACKPUSH:
                hardwareManager.outtakeExtendoServo.setPosition(_ExtendoServoState.DEPOSIT_BACKPUSH.getPosition());
                break;
            case AUTO_DEPOSIT:
                hardwareManager.outtakeExtendoServo.setPosition(_ExtendoServoState.AUTO_DEPOSIT.getPosition());
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
            case BALANCE:
                hardwareManager.outtakeClawServo.setPosition(_OuttakeClawServoState.BALANCE.getPosition());
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

    public int GetLiftTargetPos(){
        return targetPosition;
    }
    public int GetLiftCurrentPos(){
        return encoderPos;
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

    public boolean canHang() {
        return targetPosition == _LiftState.HANG_READY.position;
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
        HIGH_CHAMBER(1400),
        TRANSFER    (120),
        CLEARED(400),
        CLEARED_ALL(800),
        HOME        (50),
        HIGH_BUCKET (2420),
        HIGH_CHAMBER_LOWER(700),
        HANG_READY(2220),
        ZERO(0),
        HIGH_CHAMBER_AUTO(1360),
        HIGH_CHAMBER_REVERSED(1900),
        HIGH_CHAMBER_LOWER_REVERSED(1430),
        HANG_DOWN(1500);

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
        AUTO_DEPOSIT(0.29f),
        PICKUP(0.0f),
        DEPOSIT(0.265f),
        TRANSFER(0.05f),
        TRANSFER_PUSH(0.08f),
        ZERO(0f),
        DEPOSIT_BACK(0.15f),
        DEPOSIT_FORWARDPUSH(0.215f),
        DEPOSIT_BACKPUSH(0.25f);

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
        DEPOSIT_SPECIMEN(0.752f),
        DEPOSIT_SAMPLE(1f),
        PICKUP(0.05f),
        TRANSFER(0.65f),
        BALANCE(0.35f);

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
        GRIP    (0.27f),
        RELEASE     (0f),
        BALANCE(0.22f);

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
        HORIZONTAL_ServoDown    (1f),
        VERTICAL     (0.36f),
        HORIZONTAL_ServoUp(0f);

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

    public Action ClawAction(_OuttakeClawServoState state) {
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

    public Action TiltAction(_OuttakeTiltServoState state) {
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

    public Action YawAction(_OuttakeYawServoState state) {
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