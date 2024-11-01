package org.firstinspires.ftc.teamcode.commands.low_level;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.managers.IntakeManager;

public class SetBroomStateCommand extends InstantCommand {
    public SetBroomStateCommand(IntakeManager manager, IntakeManager._BroomState broomState) {
        super(() -> manager.update(broomState));
    }
}
