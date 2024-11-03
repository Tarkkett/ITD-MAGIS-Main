package org.firstinspires.ftc.teamcode.commands.low_level;

import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

public class TransferCommand extends SequentialCommandGroup {

    public TransferCommand(IntakeManager intake, OuttakeManager outtake){
        addCommands(
                new ParallelCommandGroup(
                        new SetIntakeSlidePositionCommand(intake, IntakeManager._SlideState.EXTENDED),
                        new SetTiltServoPosCommand(intake, IntakeManager._TiltServoState.RAISED),
                        new SetBroomStateCommand(intake, IntakeManager._BroomState.INTAKEING)
                ),
                new WaitCommand(1000),
                new ParallelCommandGroup(
                        new SetLiftPositionCommand(outtake, OuttakeManager._LiftState.TRANSFER),
                        new SetBucketPositionCommand(outtake, OuttakeManager._BucketServoState.LOW)
                ),
                new WaitUntilCommand(outtake::isTransfer),
                new WaitCommand(1000),
                new SequentialCommandGroup(
                        new SetTiltServoPosCommand(intake, IntakeManager._TiltServoState.RAISED),
                        new SetIntakeSlidePositionCommand(intake, IntakeManager._SlideState.TRANSFER),
                        new WaitCommand(1000),
                        new SetBroomStateCommand(intake, IntakeManager._BroomState.TRANSFERING),
                        new WaitCommand(1000),
                        new ParallelCommandGroup(
                                new SetBroomStateCommand(intake, IntakeManager._BroomState.STOPPED),
                                new SetIntakeSlidePositionCommand(intake, IntakeManager._SlideState.EXTENDED)
                        )
                )




        );
    }
}
