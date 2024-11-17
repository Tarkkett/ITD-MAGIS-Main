package org.firstinspires.ftc.teamcode.commands.low_level;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

public class SetOuttakeTiltServoCommand extends InstantCommand {
    public SetOuttakeTiltServoCommand(OuttakeManager manager, OuttakeManager._OuttakeTiltServoState state) {
        super(() -> manager.update(state));
    }
}
