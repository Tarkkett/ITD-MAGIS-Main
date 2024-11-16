package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.commands.low_level.SetGripStateCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.SetBucketPositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.SetIntakeSlidePositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.SetLiftPositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.SetTiltServoPosCommand;
import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

public class TransferCommand extends SequentialCommandGroup {

    public TransferCommand(IntakeManager intake, OuttakeManager outtake){
        if (!intake.isSelectingIntakePosition && !outtake.selectingProcess) {
            addCommands(
                    new ParallelCommandGroup(
                            new SetIntakeSlidePositionCommand(intake, IntakeManager._SlideState.EXTENDED),
                            new SetTiltServoPosCommand(intake, IntakeManager._TiltServoState.TRANSFER),
                            new SetGripStateCommand(intake, IntakeManager._GripState.RELEASE)
                    ),
                    new WaitCommand(1000),
                    new ParallelCommandGroup(
                            new SetLiftPositionCommand(outtake, OuttakeManager._LiftState.TRANSFER),
                            new SetBucketPositionCommand(outtake, OuttakeManager._BucketServoState.LOW)
                    ),
                    new WaitUntilCommand(outtake::isTransfer),
                    new WaitCommand(1000),
                    new SequentialCommandGroup(
                            new SetTiltServoPosCommand(intake, IntakeManager._TiltServoState.TRANSFER),
                            new SetIntakeSlidePositionCommand(intake, IntakeManager._SlideState.TRANSFER),
                            new WaitCommand(1000),
//                            new SetBroomStateCommand(intake, IntakeManager._GripState.TRANSFERING),
                            new WaitCommand(3000),
                            new ParallelCommandGroup(
                                    new SetGripStateCommand(intake, IntakeManager._GripState.GRIP),
                                    new SetTiltServoPosCommand(intake, IntakeManager._TiltServoState.AIMING)
                            )
                    )
            );
        }
        else {
            addCommands(
                    //new InstantCommand(() -> gamepad_driver.gamepad.rumble(1))
            );
        }
    }
}
