package org.firstinspires.ftc.teamcode.commands.low_level;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.managers.IntakeManager;

public class AdjustYawServoCommand extends InstantCommand {
    public AdjustYawServoCommand(IntakeManager manager, IntakeManager._YawServoState state, double i) {
        super(() -> manager.update(state, i));
    }
}
