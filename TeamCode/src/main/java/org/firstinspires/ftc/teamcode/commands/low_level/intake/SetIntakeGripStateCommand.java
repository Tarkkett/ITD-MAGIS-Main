package org.firstinspires.ftc.teamcode.commands.low_level.intake;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.managers.IntakeManager;

public class SetIntakeGripStateCommand extends InstantCommand {
    public SetIntakeGripStateCommand(IntakeManager manager, IntakeManager._ClawState clawState) {
        super(() -> manager.update(clawState));
    }
}
