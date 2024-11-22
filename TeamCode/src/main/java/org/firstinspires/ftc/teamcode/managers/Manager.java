package org.firstinspires.ftc.teamcode.managers;

public interface Manager<S extends Enum<S>> {
    void loop();
    S GetManagerState();
}
