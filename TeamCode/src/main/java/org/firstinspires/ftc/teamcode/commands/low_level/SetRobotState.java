package org.firstinspires.ftc.teamcode.commands.low_level;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.opMode.StateMachine;

public class SetRobotState extends InstantCommand {
    public SetRobotState(StateMachine manager, StateMachine._RobotState state){
        super(() -> manager.SetSubsystemState(state));
    }
}
