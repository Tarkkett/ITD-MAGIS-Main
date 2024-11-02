package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.commands.low_level.SetBucketPositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.SetLiftPositionCommand;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

public class SetOuttakeStateCommand extends SequentialCommandGroup {

    OuttakeManager manager;
    public SetOuttakeStateCommand(OuttakeManager._OuttakeState targetState, OuttakeManager outtakeManager) {
        manager = outtakeManager;
        if (targetState == OuttakeManager._OuttakeState.HOME){
            addCommands(
                    new SetLiftPositionCommand(manager, OuttakeManager._LiftState.HIGH_CHAMBER),
                    new SetBucketPositionCommand(manager, OuttakeManager._BucketServoState.LOW)
            );
        }
    }
}
