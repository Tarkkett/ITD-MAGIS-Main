package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.commands.low_level.intake.AdjustYawServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeGripStateCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeExtendoServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeTiltServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeSlidePositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetLiftPositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeClawStateCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeTiltServoPosCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeYawServoCommand;
import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;


public class TransferCommand extends SequentialCommandGroup {
    public TransferCommand(IntakeManager intake, OuttakeManager outtake){
        if (!intake.selectingProcess && !outtake.selectingProcess) {
            addCommands(
                    new SetIntakeGripStateCommand(intake, IntakeManager._GripState.GRIP),
                    new SetOuttakeExtendoServoCommand(outtake, OuttakeManager._ExtendoServoState.TRANSFER),
                    new WaitCommand(250),
                    new SetIntakeTiltServoPosCommand(intake, IntakeManager._TiltServoState.TRANSFER),
                    new WaitCommand(300),
                    new ParallelCommandGroup(
                            new SetIntakeSlidePositionCommand(intake, IntakeManager._SlideState.EXTENDED),
                            new AdjustYawServoCommand(intake, IntakeManager._YawServoState.TRANSFER, 0),
                            new SetOuttakeClawStateCommand(outtake, OuttakeManager._OuttakeClawServoState.RELEASE),
                            new SetOuttakeTiltServoCommand(outtake, OuttakeManager._OuttakeTiltServoState.TRANSFER),
                            new SetOuttakeYawServoCommand(outtake, OuttakeManager._OuttakeYawServoState.VERTICAL)
                    ),
                    new WaitCommand(200),
                    new SetLiftPositionCommand(outtake, OuttakeManager._LiftState.TRANSFER),
                    new WaitUntilCommand(outtake::isTransfer),
                    new WaitCommand(200),
                    new SequentialCommandGroup(
                        new SetIntakeSlidePositionCommand(intake, IntakeManager._SlideState.TRANSFER),
                        new WaitCommand(500),
                        new SetOuttakeClawStateCommand(outtake, OuttakeManager._OuttakeClawServoState.GRIP),
                        new WaitCommand(200),
                        new SetIntakeGripStateCommand(intake, IntakeManager._GripState.RELEASE),
                        new WaitCommand(300),
                        new SetIntakeTiltServoPosCommand(intake, IntakeManager._TiltServoState.PACKED),
                        new SetLiftPositionCommand(outtake, OuttakeManager._LiftState.CLEARED),
                        new SetOuttakeYawServoCommand(outtake, OuttakeManager._OuttakeYawServoState.HORIZONTAL_ServoDown),
                        new SetOuttakeExtendoServoCommand(outtake, OuttakeManager._ExtendoServoState.DEPOSIT),
                        new SetOuttakeTiltServoCommand(outtake, OuttakeManager._OuttakeTiltServoState.PICKUP),
                        new WaitCommand(600),
                        new SetOuttakeExtendoServoCommand(outtake, OuttakeManager._ExtendoServoState.PICKUP)
                    )
            );
        }
    }
}
