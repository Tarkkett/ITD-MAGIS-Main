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

    private static final int MIN_THRESHOLD =0;
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

    private boolean canPickup = false;

    public void setCanPickup(boolean canPickup) {
        this.canPickup = canPickup;
    }

    public boolean canPickup() {
        return canPickup;
    }


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
        if (hardwareManager.outtakeDepositorClawSrv.getPosition() > _OuttakeClawServoState.CLOSED.getPosition() - 0.05 &&
                hardwareManager.outtakeDepositorClawSrv.getPosition() < _OuttakeClawServoState.CLOSED.getPosition() + 0.05){
            return true;
        }
        else return false;
    }

    public void update(_LiftState targetState, int... position) {
        if (targetState != null) {
            targetPosition = (int) targetState.getPosition();
        }
        else {
            this.targetPosition = position[0];
        }
    }

    public void update(_OuttakeTiltServoState targetState) {
        hardwareManager.setOuttakeArmTiltServos(targetState.getPosition());
    }

    public void update(_PitchServoState targetState) {
        hardwareManager.outtakeDepositorPitchSrv.setPosition(targetState.getPosition());
    }

    public void update(_OuttakeClawServoState targetState) {
        hardwareManager.outtakeDepositorClawSrv.setPosition(targetState.getPosition());
    }

    public void update(_OuttakeYawServoState targetState) {
        hardwareManager.outtakeDepositorYawSrv.setPosition(targetState.getPosition());
    }

    public int GetLiftTargetPos(){
        return targetPosition;
    }
    public int GetLiftCurrentPos(){
        return encoderPos;
    }

    private static final int MAX_CHANGE_PER_CYCLE = 10;
    private static final double SMOOTHING_FACTOR = 0.4;

    public void lowerLiftPosition(int i) {
        int desiredPos = encoderPos + i;

        if (desiredPos > MIN_THRESHOLD && desiredPos < MAX_THRESHOLD) {

            int limitedChange = (int) (Math.signum(i) * Math.min(Math.abs(i), MAX_CHANGE_PER_CYCLE));
            int rateLimitedPos = targetPosition + limitedChange;

            targetPosition = (int) (SMOOTHING_FACTOR * rateLimitedPos + (1 - SMOOTHING_FACTOR) * targetPosition);
        }
    }

    public void updateMode(boolean isAuto) {
        if (isAuto){
            mode = _LiftMode.AUTO;
        } else{
            mode = _LiftMode.MANUAL;
        }
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
        HIGH_CHAMBER(750),
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

    public enum _PitchServoState implements Positionable {
        HOME(0.24f),
        PICKUP(0.225f),
        DEPOSIT_SPECIMEN(0.73f),
        DEPOSIT_SAMPLE(0.7f),
        TRANSFER(0.24f),
        ZERO(0.0f),
        HANG(HOME.getPosition());

        private final float position;

        _PitchServoState(float position) {
            this.position = position;
        }

        @Override
        public float getPosition() {
            return position;
        }
    }

    public enum _OuttakeTiltServoState implements Positionable {
        DEPOSIT_SPECIMEN(0.97f),
        DEPOSIT_SAMPLE(0.8f),
        PICKUP(0.178f),
        TRANSFER(0.0f),
        ZERO(0.0f),
        HOME(0.28f),
        DEPOSIT_CLEARED(1.0f);

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
        CLOSED(0.9f),
        OPEN(0.13f);
        private final float position;

        _OuttakeClawServoState(float position) {
            this.position = position;
        }

        @Override
        public float getPosition() {
            return position;
        }
    }

    //Relative to HOME position
    public enum _OuttakeYawServoState implements Positionable {
        HORIZONTAL_Pickup(0.13f),
        VERTICAL     (0.48f),
        HORIZONTAL_Deposit(0.83f);

        private final float position;

        _OuttakeYawServoState(float position) {
            this.position = position;
        }

        @Override
        public float getPosition() {
            return position;
        }
    }

    public Action PitchAction(_PitchServoState state) {
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

    public Action SetLift(int position){
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