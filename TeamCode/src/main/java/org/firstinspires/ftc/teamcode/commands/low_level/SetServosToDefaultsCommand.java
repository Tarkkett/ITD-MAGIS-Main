package org.firstinspires.ftc.teamcode.commands.low_level;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

public class SetServosToDefaultsCommand extends SequentialCommandGroup {
    public SetServosToDefaultsCommand(OuttakeManager outtakeManager, IntakeManager intakeManager) {
        addCommands(
                new SetTiltServoPosCommand(intakeManager, IntakeManager._TiltServoState.LOWERED),
                new WaitCommand(500),
                new SetBucketPositionCommand(outtakeManager, OuttakeManager._BucketServoState.LOW),
                new WaitCommand(1000),
                new SetTiltServoPosCommand(intakeManager, IntakeManager._TiltServoState.TRANSFER)
        );

    }
}
