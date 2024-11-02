package org.firstinspires.ftc.teamcode.commands.low_level;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

public class SetSpecimentServoPositionCommand extends InstantCommand {
    public SetSpecimentServoPositionCommand(OuttakeManager manager, OuttakeManager._SpecimentServoState state) {
        super(() -> manager.update(state));
    }
}
