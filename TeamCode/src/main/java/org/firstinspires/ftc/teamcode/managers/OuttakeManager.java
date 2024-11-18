package org.firstinspires.ftc.teamcode.managers;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.State;
import org.firstinspires.ftc.teamcode.drivers.C_PID;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
@Config
public class OuttakeManager implements State<OuttakeManager._OuttakeState> {

    private static final int MIN_THRESHOLD =70;
    private static final int MAX_THRESHOLD =2200;
    public boolean selectingProcess = false;

    HardwareManager hardwareManager;
    IntakeManager intakeManager;
    Telemetry telemetry;

    OuttakeManager._OuttakeState state;
    OuttakeManager._OuttakeState previousState;

    C_PID controller = new C_PID(0.015,0,0.0003);
    public int targetPosition;
    public int encoderPos;

    private final Map<Transition, Runnable> stateTransitionActions = new HashMap<>();
    private boolean isTransfer = false;

    private boolean isAutoLoop = true;

    _LiftMode mode = _LiftMode.AUTO;

    public OuttakeManager(HardwareManager hardwareManager, Telemetry telemetry, IntakeManager intakeManager){

        this.hardwareManager = hardwareManager;
        this.telemetry = telemetry;
        this.intakeManager = intakeManager;

//        targetPosition = (int) _LiftState.HOME.getPosition();

    }

    @Override
    public void SetSubsystemState(_OuttakeState newState) {
        if (state != newState) {
            previousState = state;
            state = newState;

            Transition transition = new Transition(previousState, newState);
            Runnable action = stateTransitionActions.get(transition);
            if (action != null) {
                action.run();
            }

            telemetry.addData("Intake State Changed:", newState);
            telemetry.update();
        }
    }

    @Override
    public OuttakeManager._OuttakeState GetSubsystemState() {
        return state;
    }


    @Override
    public void loop() {

        encoderPos = Average(hardwareManager.liftLeft.getCurrentPosition(), hardwareManager.liftRight.getCurrentPosition());
        double power = controller.update(targetPosition, encoderPos);

        FtcDashboard dashboard = FtcDashboard.getInstance();
        telemetry = dashboard.getTelemetry();
        telemetry.addData("CurrentPosLift", encoderPos);
        telemetry.addData("TargetPosLift", targetPosition);
        telemetry.update();

        if (mode == _LiftMode.AUTO) {
            hardwareManager.liftLeft.setPower(power);
            hardwareManager.liftRight.setPower(power);
        }

        CheckForPosition(encoderPos);
    }

    private void CheckForPosition(double encoderPos) {
        int threshold = 20;
        isTransfer = encoderPos > _LiftState.TRANSFER.getPosition() - threshold && encoderPos < _LiftState.TRANSFER.getPosition() + threshold;

    }

    private int Average(float p1, float p2) {
        return (int) ((p1 + p2) / 2);
    }

    @Override
    public void InitializeStateTransitionActions() {

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
            case STUCK:
                break;
        }
    }

    public void update(_OuttakeTiltServoState targetState) {
//        if (!intakeManager.isSelectingIntakePosition){
            switch (targetState){
                case LOW:
                    hardwareManager.outtakeTiltServo.setPosition(_OuttakeTiltServoState.LOW.getPosition());
                    break;
                case HIGH:
                    hardwareManager.outtakeTiltServo.setPosition(_OuttakeTiltServoState.HIGH.getPosition());
                    break;
            }
//        }
    }

    public void update(_SpecimentServoState targetState) {
        switch (targetState){
            case OPEN:
                hardwareManager.specimentServo.setPosition(_SpecimentServoState.OPEN.getPosition());
                break;
            case CLOSED:
                hardwareManager.specimentServo.setPosition(_SpecimentServoState.CLOSED.getPosition());
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

    public boolean isTransfer() {
        return isTransfer;
    }

    public void lowerLiftPosition(int i) {
        int newPos = encoderPos + i;
        if (newPos > MIN_THRESHOLD && newPos < MAX_THRESHOLD){
            targetPosition = newPos;
        }
    }

    public Action stop() {
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

    public void updateMode(boolean isAuto) {
        if (isAuto){
            mode = _LiftMode.AUTO;
        } else{
            mode = _LiftMode.MANUAL;
        }
    }

    public class LoopLift implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (isAutoLoop) {
                    loop();
                }
                else{ return false;}

                return true;

            }
    }
    public Action loopLift(){
        return new LoopLift();
    }

    public Action OpenCloseSpeciment(boolean isOpen) {
        return new Action(){
            private boolean initialize = false;
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!initialize){
                    if (isOpen){
                        hardwareManager.specimentServo.setPosition(0);
                    }
                    else hardwareManager.specimentServo.setPosition(0.5);
                    initialize = true;
                }
                return false;
            }
        };
    }

    @SuppressWarnings("unused")
    public enum _OuttakeState{
        HOME,
        DEPOSIT,
        SPECIMENT_GRAB,
        TRANSFER
    }

    public boolean isAtPosition(int position, int targetPosition, int margin){
        if (position < targetPosition + margin && position > targetPosition - margin){
            return false;
        }
        else return true;
    }

    public enum _LiftState{
        LOW_CHAMBER (100),
        LOW_RUNG    (200),
        HIGH_CHAMBER(1400),
        HIGH_RUNG   (1000),
        TRANSFER    (30),
        HOME        (50),
        STUCK       (0),
        HIGH_BUCKET (2300),
        LOW_BUCKET  (1200);

        private final float position;

        _LiftState(float position) {
            this.position = position;
        }

        public float getPosition() {
            return position;
        }
    }

    public enum _SpecimentServoState{
        OPEN    (0f),
        CLOSED  (0.5f);

        private final float position;

        _SpecimentServoState(float position) {
            this.position = position;
        }

        public float getPosition() {
            return position;
        }
    }

    public enum _LiftMode{
        MANUAL,
        AUTO

    }

    public enum _OuttakeTiltServoState {
        HIGH    (0f),
        LOW     (0.77f);

        private final float position;

        _OuttakeTiltServoState(float position) {
            this.position = position;
        }

        public float getPosition() {
            return position;
        }
    }

    public enum _OuttakeClawServoState {
        GRIP    (0.75f),
        RELEASE     (0.15f);

        private final float position;

        _OuttakeClawServoState(float position) {
            this.position = position;
        }

        public float getPosition() {
            return position;
        }
    }

    public Action driveLift(int position){
        return new Action(){

            private boolean initialized = false;
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!initialized){
                    targetPosition = position;
                    initialized = true;
                }
                return isAtPosition(encoderPos, targetPosition, 10);

            }
        };

    }
}
