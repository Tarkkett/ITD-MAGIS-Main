package org.firstinspires.ftc.teamcode.commands.low_level.outtake;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

public class SetOuttakeExtendoServoCommand extends InstantCommand {
    public SetOuttakeExtendoServoCommand(OuttakeManager manager, OuttakeManager._ExtendoServoState state) {
        super(() -> manager.update(state));
    }
}
