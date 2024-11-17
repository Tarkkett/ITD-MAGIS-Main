package org.firstinspires.ftc.teamcode.commands.low_level;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

public class SetOuttakeClawStateCommand extends InstantCommand {

    public SetOuttakeClawStateCommand(OuttakeManager manager, OuttakeManager._OuttakeClawServoState state){
        super(()-> manager.update(state));
    }
}
