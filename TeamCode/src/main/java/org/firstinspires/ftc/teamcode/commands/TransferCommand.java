package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.commands.low_level.intake.AdjustYawServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeGripStateCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakePitchServoCommand;
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
                    new SetIntakeGripStateCommand(intake, IntakeManager._ClawState.CLOSED),
                    new AdjustYawServoCommand(intake, IntakeManager._YawServoState.TRANSFER),
                    new SetIntakeTiltServoPosCommand( intake, IntakeManager._TiltServoState.TRANSFER),
                    new SetIntakeSlidePositionCommand(intake, IntakeManager._SlideState.TRANSFER_WAIT),
                    new SetLiftPositionCommand(outtake, OuttakeManager._LiftState.ZERO)

                    //!Finish
            );
        }
    }
}
