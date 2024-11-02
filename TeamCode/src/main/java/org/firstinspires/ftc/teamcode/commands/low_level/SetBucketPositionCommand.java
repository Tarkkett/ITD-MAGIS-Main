package org.firstinspires.ftc.teamcode.commands.low_level;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

public class SetBucketPositionCommand extends InstantCommand {
    public SetBucketPositionCommand(OuttakeManager manager, OuttakeManager._BucketServoState state) {
        super(() -> manager.update(state));
    }
}
