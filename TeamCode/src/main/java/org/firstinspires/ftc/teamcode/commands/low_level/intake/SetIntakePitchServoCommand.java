package org.firstinspires.ftc.teamcode.commands.low_level.intake;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.managers.IntakeManager;

public class SetIntakePitchServoCommand extends InstantCommand {
    public SetIntakePitchServoCommand(IntakeManager manager, IntakeManager._PitchServoState pitchState) {
        super(() -> manager.update(pitchState));
    }
}
