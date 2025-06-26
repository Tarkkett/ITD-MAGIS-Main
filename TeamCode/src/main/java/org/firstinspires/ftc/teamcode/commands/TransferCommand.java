package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.commands.low_level.intake.AdjustYawServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeClawStateCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakePitchServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeSlidePositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeYawServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetLiftPositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeTiltServoPosCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeClawStateCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakePitchServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeTiltServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeYawServoCommand;
import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

public class TransferCommand extends SequentialCommandGroup {
    public TransferCommand(IntakeManager intake, OuttakeManager outtake){

        intake.managerState = IntakeManager._IntakeState.TRANSFER;
        outtake.managerState = OuttakeManager._OuttakeState.TRANSFER;

        addCommands(
                new SetIntakeSlidePositionCommand(intake, IntakeManager._SlideState.EXTENDED),
                new InstantCommand(() -> intake.SetYawMode(IntakeManager._YawMode.AUTO)),
                new SetIntakeClawStateCommand(intake, IntakeManager._ClawState.LOOSE),
                new WaitCommand(300),
                new AdjustYawServoCommand(intake, IntakeManager._YawServoState.TRANSFER_RAISED),
                new SetIntakeTiltServoPosCommand( intake, IntakeManager._TiltServoState.TRANSFER),
                new SetIntakePitchServoCommand(intake, IntakeManager._PitchServoState.TRANSFER),
                new SetLiftPositionCommand(outtake, OuttakeManager._LiftState.TRANSFER),
                new SetOuttakeTiltServoCommand(outtake, OuttakeManager._OuttakeTiltServoState.TRANSFER),
                new SetOuttakePitchServoCommand(outtake, OuttakeManager._PitchServoState.TRANSFER),
                new SetOuttakeYawServoCommand(outtake, OuttakeManager._OuttakeYawServoState.HORIZONTAL_Pickup),
                new SetOuttakeClawStateCommand(outtake, OuttakeManager._OuttakeClawServoState.OPEN),
                new WaitCommand(1000),
                new SetIntakeSlidePositionCommand(intake, IntakeManager._SlideState.TRANSFER),
                new WaitCommand(700),
                new SetIntakeYawServoCommand(intake, IntakeManager._YawServoState.TRANSFER),
                new WaitCommand(400),
                new SetOuttakeClawStateCommand(outtake, OuttakeManager._OuttakeClawServoState.CLOSED),
                new WaitCommand(100),
                new SetIntakeClawStateCommand(intake, IntakeManager._ClawState.OPEN),
                new SetIntakeTiltServoPosCommand(intake, IntakeManager._TiltServoState.AIMING),
                new InstantCommand(() -> intake.SetYawMode(IntakeManager._YawMode.MANUAL))
        );

    }
}
