package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.commands.low_level.SetRobotState;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.AdjustYawServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeGripStateCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeTiltServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeSlidePositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetLiftPositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeClawStateCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeTiltServoPosCommand;
import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;
import org.firstinspires.ftc.teamcode.opMode.StateMachine;


public class TransferCommand extends SequentialCommandGroup {
    public TransferCommand(IntakeManager intake, OuttakeManager outtake){
        if (!intake.selectingProcess && !outtake.selectingProcess) {
            addCommands(
                    new SetIntakeGripStateCommand(intake, IntakeManager._GripState.GRIP),
                    new WaitCommand(250), //300
                    new SetIntakeTiltServoPosCommand(intake, IntakeManager._TiltServoState.TRANSFER),
                    new WaitCommand(300),
                    new ParallelCommandGroup(
                            new SetIntakeSlidePositionCommand(intake, IntakeManager._SlideState.TRANSFER_WAIT),
                            new SetIntakeGripStateCommand(intake, IntakeManager._GripState.GRIP),
                            new AdjustYawServoCommand(intake, IntakeManager._YawServoState.TRANSFER, 0),
                            new SetOuttakeClawStateCommand(outtake, OuttakeManager._OuttakeClawServoState.RELEASE),
                            new SetOuttakeTiltServoCommand(outtake, OuttakeManager._OuttakeTiltServoState.LOW)
                    ),
                    new WaitCommand(200), //400
                    new SetLiftPositionCommand(outtake, OuttakeManager._LiftState.TRANSFER),
                    new WaitUntilCommand(outtake::isTransfer),
                    new WaitCommand(200), //400
                    new SequentialCommandGroup(
                            new SetIntakeSlidePositionCommand(intake, IntakeManager._SlideState.TRANSFER),
                            new WaitCommand(300), //700
                            new SetOuttakeClawStateCommand(outtake, OuttakeManager._OuttakeClawServoState.GRIP),
                            new WaitCommand(200), //500
                            new SetIntakeGripStateCommand(intake, IntakeManager._GripState.RELEASE),
                            new SetIntakeTiltServoPosCommand(intake, IntakeManager._TiltServoState.PACKED),
                            new WaitCommand(300),
                            new SetOuttakeTiltServoCommand(outtake, OuttakeManager._OuttakeTiltServoState.MID)
//                            new SetRobotState(stateMachine, StateMachine._RobotState.DEPOSIT, true)
                    )
            );
        }
    }
}
