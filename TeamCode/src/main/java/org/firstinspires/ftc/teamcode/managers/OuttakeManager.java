package org.firstinspires.ftc.teamcode.managers;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.State;
import org.firstinspires.ftc.teamcode.drivers.C_PID;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import kotlin.math.UMathKt;

@SuppressWarnings("rawtypes")
@Config
public class OuttakeManager implements State<OuttakeManager._OuttakeState> {

    HardwareManager hardwareManager;
    Telemetry telemetry;

    OuttakeManager._OuttakeState state;
    OuttakeManager._OuttakeState previousState;

    C_PID controller = new C_PID(0.015,0,0.0003);
    public static int targetPosition = 0;
    public static double encoderPos;

    private final Map<Transition, Runnable> stateTransitionActions = new HashMap<>();


    public OuttakeManager(HardwareManager hardwareManager, Telemetry telemetry){

        this.hardwareManager = hardwareManager;
        this.telemetry = telemetry;
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

        hardwareManager.liftLeft.setPower(power);
        hardwareManager.liftRight.setPower(power);
    }

    private double Average(float p1, float p2) {
        return (p1 + p2) / 2;
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

    public void update(_BucketServoState targetState) {
        switch (targetState){
            case LOW:
                hardwareManager.bucketServo.setPosition(_BucketServoState.LOW.getPosition());
                break;
            case HIGH:
                hardwareManager.bucketServo.setPosition(_BucketServoState.HIGH.getPosition());
                break;
        }
    }

    public void update(_SpecimentServoState targetState) {
        switch (targetState){
            case OPEN:
                hardwareManager.bucketServo.setPosition(_SpecimentServoState.OPEN.getPosition());
                break;
            case CLOSED:
                hardwareManager.bucketServo.setPosition(_SpecimentServoState.CLOSED.getPosition());
                break;
        }
    }

    public enum _OuttakeState{
        HOME,
        DEPOSIT,
        SPECIMENT_GRAB,
        TRANSFER
    }

    public enum _LiftState{
        LOW_CHAMBER (100),
        LOW_RUNG    (200),
        HIGH_CHAMBER(600),
        HIGH_RUNG   (1000),
        TRANSFER    (2300),
        HOME        (50),
        STUCK       (0),
        HIGH_BUCKET (400),
        LOW_BUCKET  (1200);

        private final float position;

        _LiftState(float position) {
            this.position = position;
        }

        public float getPosition() {
            return position;
        }

        public Supplier<Object> positionSupplier() {
            return () -> position;
        }
    }

    public enum _SpecimentServoState{
        OPEN    (0f),
        CLOSED  (1f);

        private final float position;

        _SpecimentServoState(float position) {
            this.position = position;
        }

        public float getPosition() {
            return position;
        }
    }

    public enum _BucketServoState{
        HIGH    (1f),
        LOW     (0f);

        private final float position;

        _BucketServoState(float position) {
            this.position = position;
        }

        public float getPosition() {
            return position;
        }
    }

}
