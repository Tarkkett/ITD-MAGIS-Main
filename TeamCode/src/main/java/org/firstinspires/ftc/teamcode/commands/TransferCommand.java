package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.commands.low_level.intake.AdjustYawServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeGripStateCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeTiltServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeSlidePositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetLiftPositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeClawStateCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeTiltServoPosCommand;
import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;


public class TransferCommand extends SequentialCommandGroup {

    public TransferCommand(IntakeManager intake, OuttakeManager outtake){
        if (!intake.isSelectingIntakePosition && !outtake.selectingProcess) {
            addCommands(
                    new ParallelCommandGroup(
                            new SetIntakeSlidePositionCommand(intake, IntakeManager._SlideState.TRANSFER_WAIT),
                            new SetIntakeTiltServoPosCommand(intake, IntakeManager._TiltServoState.TRANSFER),
                            new SetIntakeGripStateCommand(intake, IntakeManager._GripState.GRIP),
                            new AdjustYawServoCommand(intake, IntakeManager._YawServoState.TRANSFER, 0)
                    ),
                    new WaitCommand(300),
                    new ParallelCommandGroup(
                            new SetOuttakeClawStateCommand(outtake, OuttakeManager._OuttakeClawServoState.RELEASE),
                            new SetLiftPositionCommand(outtake, OuttakeManager._LiftState.TRANSFER),
                            new SetOuttakeTiltServoCommand(outtake, OuttakeManager._OuttakeTiltServoState.LOW)
                    ),
                    new WaitUntilCommand(outtake::isTransfer),
                    new SequentialCommandGroup(
                            new SetIntakeSlidePositionCommand(intake, IntakeManager._SlideState.TRANSFER),
                            new WaitCommand(2000),
                            new SetOuttakeClawStateCommand(outtake, OuttakeManager._OuttakeClawServoState.GRIP),
                            new WaitCommand(250),
                            new SetIntakeSlidePositionCommand(intake, IntakeManager._SlideState.EXTENDED),
                            new SetIntakeGripStateCommand(intake, IntakeManager._GripState.RELEASE),
                            new WaitCommand(250),
                            new SetOuttakeTiltServoCommand(outtake, OuttakeManager._OuttakeTiltServoState.HIGH)
//
//                            new WaitCommand(500),
//                            new ParallelCommandGroup(
//                                    new SetGripStateCommand(intake, IntakeManager._GripState.RELEASE),
//                                    new SetTiltServoPosCommand(intake, IntakeManager._TiltServoState.AIMING)
//                            )
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
