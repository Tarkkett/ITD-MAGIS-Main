package org.firstinspires.ftc.teamcode.commands.low_level;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

public class PrepLiftForAscentCommand extends SequentialCommandGroup {
    public PrepLiftForAscentCommand(IntakeManager intakeManager, OuttakeManager outtakeManager) {
        addCommands(

                new SetTiltServoPosCommand(intakeManager, IntakeManager._TiltServoState.LOWERED),
                new WaitCommand(500),
                new SetBucketPositionCommand(outtakeManager, OuttakeManager._BucketServoState.HIGH),
                new WaitCommand(500),
                new SetIntakeSlidePositionCommand(intakeManager, IntakeManager._SlideState.RETRACTED),
                new SetTiltServoPosCommand(intakeManager, IntakeManager._TiltServoState.PACKED)
        );
    }
}
