package org.firstinspires.ftc.teamcode.commands.low_level.intake;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.managers.IntakeManager;

public class SetIntakeClawStateCommand extends InstantCommand {
    public SetIntakeClawStateCommand(IntakeManager manager, IntakeManager._ClawState clawState) {
        super(() -> manager.update(clawState));
    }
}
