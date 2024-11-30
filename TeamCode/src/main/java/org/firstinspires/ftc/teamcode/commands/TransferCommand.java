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
        if (!intake.selectingProcess && !outtake.selectingProcess) {
            addCommands(
                    new SetIntakeGripStateCommand(intake, IntakeManager._GripState.GRIP),
                    new WaitCommand(500),
                    new SetIntakeTiltServoPosCommand(intake, IntakeManager._TiltServoState.TRANSFER),
                    new ParallelCommandGroup(
                            new SetIntakeSlidePositionCommand(intake, IntakeManager._SlideState.TRANSFER_WAIT),
                            new SetIntakeGripStateCommand(intake, IntakeManager._GripState.GRIP),
                            new AdjustYawServoCommand(intake, IntakeManager._YawServoState.TRANSFER, 0),
                            new SetOuttakeClawStateCommand(outtake, OuttakeManager._OuttakeClawServoState.RELEASE),
                            new SetOuttakeTiltServoCommand(outtake, OuttakeManager._OuttakeTiltServoState.LOW)
                    ),
                    new WaitCommand(400),
                    new SetLiftPositionCommand(outtake, OuttakeManager._LiftState.TRANSFER),

                    new WaitUntilCommand(outtake::isTransfer),
                    new WaitCommand(400),
                    new SequentialCommandGroup(
                            new SetIntakeSlidePositionCommand(intake, IntakeManager._SlideState.TRANSFER),
                            new WaitCommand(700),
                            new SetOuttakeClawStateCommand(outtake, OuttakeManager._OuttakeClawServoState.GRIP),
                            new WaitCommand(500),
                            new SetIntakeGripStateCommand(intake, IntakeManager._GripState.RELEASE),
                            new SetIntakeTiltServoPosCommand(intake, IntakeManager._TiltServoState.PACKED),
//                            new WaitCommand(250),
//                            new SetIntakeSlidePositionCommand(intake, IntakeManager._SlideState.EXTENDED),
                            new WaitCommand(650),
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
