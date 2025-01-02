package org.firstinspires.ftc.teamcode.commands.low_level.outtake;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

public class SetOuttakeYawServoCommand extends InstantCommand {
    public SetOuttakeYawServoCommand(OuttakeManager manager, OuttakeManager._OuttakeYawServoState outtakeYawServoState) {
        super(() -> manager.update(outtakeYawServoState));
    }
}
