package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.ParallelDeadlineGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.commands.low_level.SetBroomStateCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.SetIntakeSlidePositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.SetTiltServoPosCommand;
import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

public class SetIntakeStateCommand extends SequentialCommandGroup {

    IntakeManager manager;

    public SetIntakeStateCommand(IntakeManager._IntakeState intakeState, IntakeManager manager, OuttakeManager outtakeManager) {

        this.manager = manager;

        if (intakeState == IntakeManager._IntakeState.HOME){
            manager.SetSubsystemState(IntakeManager._IntakeState.HOME);
            addCommands(
                    new SetIntakeSlidePositionCommand(manager, IntakeManager._SlideState.RETRACTED),
                    new SetBroomStateCommand(manager, IntakeManager._BroomState.STOPPED),
                    new SetTiltServoPosCommand(manager, IntakeManager._TiltServoState.RAISED)
            );
        } else if (intakeState == IntakeManager._IntakeState.PICKUP) {
            manager.SetSubsystemState(IntakeManager._IntakeState.PICKUP);
            addCommands(
                    new SetIntakeSlidePositionCommand(manager, IntakeManager._SlideState.EXTENDED),
                    new SetBroomStateCommand(manager, IntakeManager._BroomState.INTAKEING),
                    new SetTiltServoPosCommand(manager, IntakeManager._TiltServoState.LOWERED)
            );
        } else if (intakeState == IntakeManager._IntakeState.TRANSFER) {
            manager.SetSubsystemState(IntakeManager._IntakeState.TRANSFER);
            addCommands(
                    new SequentialCommandGroup(
                            new WaitUntilCommand(outtakeManager::isTransfer),
                            new SequentialCommandGroup(
                                    new WaitCommand(200),
                                    new SetIntakeSlidePositionCommand(manager, IntakeManager._SlideState.TRANSFER),
                                    new SetBroomStateCommand(manager, IntakeManager._BroomState.TRANSFERING),
                                    new SetTiltServoPosCommand(manager, IntakeManager._TiltServoState.RAISED)
                            )

                    ).withTimeout(300)

            );
        }
    }
}
