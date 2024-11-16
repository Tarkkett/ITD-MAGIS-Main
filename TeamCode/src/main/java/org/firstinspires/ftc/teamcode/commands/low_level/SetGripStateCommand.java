package org.firstinspires.ftc.teamcode.commands.low_level;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.managers.IntakeManager;

public class SetGripStateCommand extends InstantCommand {
    public SetGripStateCommand(IntakeManager manager, IntakeManager._GripState broomState) {
        super(() -> manager.update(broomState));
    }
}
