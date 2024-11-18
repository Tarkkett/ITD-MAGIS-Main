package org.firstinspires.ftc.teamcode.commands.low_level.intake;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.managers.IntakeManager;

public class SetIntakeTiltServoPosCommand extends InstantCommand {
    public SetIntakeTiltServoPosCommand(IntakeManager manager, IntakeManager._TiltServoState tiltServoState) {
        super(() -> manager.update(tiltServoState));
    }
}
