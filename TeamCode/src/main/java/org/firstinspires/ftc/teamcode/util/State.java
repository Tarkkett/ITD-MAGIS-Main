package org.firstinspires.ftc.teamcode.util;

public interface State<S extends Enum<S>> {
    void SetSubsystemState(S state);
    S GetSubsystemState();
    void loop();
}
