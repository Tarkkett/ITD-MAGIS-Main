package org.firstinspires.ftc.teamcode.managers;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.State;

public class LiftManager implements State<LiftManager.StateEnum> {

    HardwareManager hardwareManager;
    Telemetry telemetry;

    public LiftManager(HardwareManager hardwareManager, Telemetry telemetry){
        this.hardwareManager = hardwareManager;
        this.telemetry = telemetry;
    }

    @Override
    public void SetSubsystemState(StateEnum state) {
        currentState = state;
    }

    @Override
    public StateEnum GetSubsystemState() {
        return currentState;
    }


    @Override
    public void loop() {
//        hardwareManager.liftLeft.setTargetPosition(currentState.ordinal());
//        hardwareManager.liftRight.setTargetPosition(currentState.ordinal());
        hardwareManager.liftLeft.setPower(0);
        hardwareManager.liftRight.setPower(0);

        telemetry.addData("LiftState: ", currentState);
        telemetry.update();
    }

    @Override
    public void InitializeStateTransitionActions() {

    }


    public enum StateEnum{
        LOW_CHAMBER(0),
        LOW_RUNG(0),
        HIGH_CHAMBER(1),
        HIGH_RUNG(0.5f),
        LOWERED(0),
        TRAVELLING(0),
        STUCK(0);

        StateEnum(float position) {
        }
    }

    StateEnum currentState = StateEnum.LOWERED;


}
