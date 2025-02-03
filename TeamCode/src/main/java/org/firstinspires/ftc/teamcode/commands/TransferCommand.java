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
                    new SetIntakeSlidePositionCommand(intake, IntakeManager._SlideState.TRANSFER),
                    new SetLiftPositionCommand(outtake, OuttakeManager._LiftState.CLEARED_ALL),

                    new SetOuttakeTiltServoCommand(outtake, OuttakeManager._OuttakeTiltServoState.PICKUP),
                    new SetOuttakeExtendoServoCommand(outtake, OuttakeManager._ExtendoServoState.AUTO_DEPOSIT),
                    new SetOuttakeClawStateCommand(outtake, OuttakeManager._OuttakeClawServoState.RELEASE),
                    new SetOuttakeYawServoCommand(outtake, OuttakeManager._OuttakeYawServoState.HORIZONTAL_ServoDown),

                    new WaitCommand(2000),
                    new SetOuttakeTiltServoCommand(outtake, OuttakeManager._OuttakeTiltServoState.TRANSFER),
                    new WaitCommand(1000),
                    new SetOuttakeExtendoServoCommand(outtake, OuttakeManager._ExtendoServoState.TRANSFER),

                    new WaitCommand(1000),
                    new SetLiftPositionCommand(outtake, OuttakeManager._LiftState.TRANSFER),
                    new WaitCommand(500),
                    new SetOuttakeExtendoServoCommand(outtake, OuttakeManager._ExtendoServoState.TRANSFER_PUSH),
                    new WaitCommand(500),

                    new SetOuttakeClawStateCommand(outtake, OuttakeManager._OuttakeClawServoState.GRIP),
                    new WaitCommand(1000),
                    new SetIntakeGripStateCommand(intake, IntakeManager._GripState.RELEASE),
                    new SetLiftPositionCommand(outtake, OuttakeManager._LiftState.CLEARED)

            );
        }
    }
}
