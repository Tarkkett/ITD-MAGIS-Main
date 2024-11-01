package org.firstinspires.ftc.teamcode.commands.low_level;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.managers.IntakeManager;

public class SetTiltServoPosCommand extends InstantCommand {
    public SetTiltServoPosCommand(IntakeManager manager, IntakeManager._TiltServoState tiltServoState) {
        super(() -> manager.update(tiltServoState));
    }
}
