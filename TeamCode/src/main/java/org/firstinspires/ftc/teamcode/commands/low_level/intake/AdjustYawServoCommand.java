package org.firstinspires.ftc.teamcode.commands.low_level.intake;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.managers.IntakeManager;

public class AdjustYawServoCommand extends InstantCommand {
    public AdjustYawServoCommand(IntakeManager manager, IntakeManager._YawServoState state) {
        super(() -> manager.update(state));
    }
}
