package org.firstinspires.ftc.teamcode.commands.low_level.intake;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.managers.IntakeManager;

public class SetIntakeYawServoCommand extends InstantCommand {
    public SetIntakeYawServoCommand(IntakeManager manager, IntakeManager._YawServoState yawServoState) {
        super(() -> manager.update(yawServoState));
    }
}
