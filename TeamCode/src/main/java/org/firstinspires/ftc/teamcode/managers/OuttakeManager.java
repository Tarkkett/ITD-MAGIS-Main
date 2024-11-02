package org.firstinspires.ftc.teamcode.managers;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.State;
import org.firstinspires.ftc.teamcode.drivers.C_PID;

import java.util.HashMap;
import java.util.Map;

import kotlin.math.UMathKt;

@SuppressWarnings("rawtypes")
@Config
public class OuttakeManager implements State<OuttakeManager._OuttakeState> {

    HardwareManager hardwareManager;
    Telemetry telemetry;

    OuttakeManager._OuttakeState state;
    OuttakeManager._OuttakeState previousState;

    C_PID controller = new C_PID();
    public static int targetPosition = 0;
    public static double encoderPos;

    private final Map<Transition, Runnable> stateTransitionActions = new HashMap<>();

    public OuttakeManager(HardwareManager hardwareManager, Telemetry telemetry){

        controller.tune(0,0,0); //PID config

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
                targetPosition = _LiftState.HIGH_RUNG.ordinal();
                break;
            case LOW_RUNG:
                targetPosition = _LiftState.LOW_RUNG.ordinal();
                break;
            case HIGH_CHAMBER:
                targetPosition = _LiftState.HIGH_CHAMBER.ordinal();
                break;
            case LOW_CHAMBER:
                targetPosition = _LiftState.LOW_CHAMBER.ordinal();
                break;
            case TRANSFER:
                targetPosition = _LiftState.TRANSFER.ordinal();
                break;
            case HOME:
                targetPosition = _LiftState.HOME.ordinal();
                break;
            case STUCK:
                break;
        }
    }

    public void update(_BucketServoState targetState) {
        switch (targetState){
            case LOW:
                hardwareManager.bucketServo.setPosition(_BucketServoState.LOW.ordinal());
                break;
            case HIGH:
                hardwareManager.bucketServo.setPosition(_BucketServoState.HIGH.ordinal());
                break;
        }
    }

    public void update(_SpecimentServoState targetState) {
        switch (targetState){
            case OPEN:
                hardwareManager.bucketServo.setPosition(_SpecimentServoState.OPEN.ordinal());
                break;
            case CLOSED:
                hardwareManager.bucketServo.setPosition(_SpecimentServoState.CLOSED.ordinal());
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
        LOW_CHAMBER (0),
        LOW_RUNG    (0),
        HIGH_CHAMBER(1),
        HIGH_RUNG   (0.5f),
        TRANSFER    (100),
        HOME        (0),
        STUCK       (0);

        _LiftState(float position) {
        }
    }

    public enum _SpecimentServoState{
        OPEN    (0f),
        CLOSED  (1f);

        _SpecimentServoState(float position){}
    }

    public enum _BucketServoState{
        HIGH    (1f),
        LOW     (0f);

        _BucketServoState(float position){}
    }

}
