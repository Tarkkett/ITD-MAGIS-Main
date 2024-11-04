package org.firstinspires.ftc.teamcode;

public interface State<S extends Enum<S>> {

    void InitializeStateTransitionActions();
    void SetSubsystemState(S state);
    S GetSubsystemState();
    void loop();
}
