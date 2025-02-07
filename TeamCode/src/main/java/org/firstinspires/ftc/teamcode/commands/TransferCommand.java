package org.firstinspires.ftc.teamcode.commands;

import com.acmerobotics.roadrunner.SleepAction;
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

            intake.managerState = IntakeManager._IntakeState.TRANSFER;
            outtake.managerState = OuttakeManager._OuttakeState.TRANSFER;

            addCommands(
                    new SetIntakeGripStateCommand(intake, IntakeManager._GripState.GRIP),
                    new AdjustYawServoCommand(intake, IntakeManager._YawServoState.TRANSFER),
                    new SetIntakeTiltServoPosCommand( intake, IntakeManager._TiltServoState.TRANSFER),
                    new SetIntakeSlidePositionCommand(intake, IntakeManager._SlideState.TRANSFER_WAIT),
                    new SetLiftPositionCommand(outtake, OuttakeManager._LiftState.ZERO),

                    new SetOuttakeTiltServoCommand(outtake, OuttakeManager._OuttakeTiltServoState.TRANSFER),
                    new SetOuttakeExtendoServoCommand(outtake, OuttakeManager._ExtendoServoState.ZERO),
                    new SetOuttakeClawStateCommand(outtake, OuttakeManager._OuttakeClawServoState.RELEASE),
                    new SetOuttakeYawServoCommand(outtake, OuttakeManager._OuttakeYawServoState.VERTICAL),

                    new WaitCommand(1100),
                    new SetIntakeSlidePositionCommand(intake, IntakeManager._SlideState.TRANSFER),
                    new WaitCommand(300),
                    new SetOuttakeClawStateCommand(outtake, OuttakeManager._OuttakeClawServoState.GRIP),
                    new WaitCommand(150),
                    new SetIntakeGripStateCommand(intake, IntakeManager._GripState.RELEASE),
                    new WaitCommand(300),
                    new SetLiftPositionCommand(outtake, OuttakeManager._LiftState.CLEARED_ALL),
                    new WaitCommand(100),
                    new SetIntakeSlidePositionCommand(intake, IntakeManager._SlideState.RETRACTED),
                    new SetOuttakeYawServoCommand(outtake, OuttakeManager._OuttakeYawServoState.HORIZONTAL_ServoUp),
                    new WaitCommand(350),
                    new SetOuttakeClawStateCommand(outtake, OuttakeManager._OuttakeClawServoState.BALANCE),
                    new SetOuttakeTiltServoCommand(outtake, OuttakeManager._OuttakeTiltServoState.BALANCE),
                    new WaitCommand(500),
                    new SetOuttakeClawStateCommand(outtake, OuttakeManager._OuttakeClawServoState.GRIP),
                    new SetLiftPositionCommand(outtake, OuttakeManager._LiftState.HIGH_BUCKET),
                    new WaitCommand(150),
                    new SetOuttakeExtendoServoCommand(outtake, OuttakeManager._ExtendoServoState.AUTO_DEPOSIT),
                    new SetOuttakeTiltServoCommand(outtake, OuttakeManager._OuttakeTiltServoState.DEPOSIT_SAMPLE)


            );
        }
    }
}
