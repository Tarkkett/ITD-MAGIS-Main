package org.firstinspires.ftc.teamcode.commands.low_level.outtake;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

public class SetOuttakePitchServoCommand extends InstantCommand {
    public SetOuttakePitchServoCommand(OuttakeManager manager, OuttakeManager._PitchServoState state) {
        super(() -> manager.update(state));
    }
}
