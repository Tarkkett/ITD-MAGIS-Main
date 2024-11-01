package org.firstinspires.ftc.teamcode;

public interface Command {

    void initialize();

    void execute();

    boolean isFinished();

    void end(boolean interrupted);
}
