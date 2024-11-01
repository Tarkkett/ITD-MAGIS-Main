package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.commands.low_level.SetBroomStateCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.SetIntakeSlidePositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.SetTiltServoPosCommand;
import org.firstinspires.ftc.teamcode.managers.IntakeManager;

public class SetIntakeStateCommand extends SequentialCommandGroup {

    IntakeManager manager;

    public SetIntakeStateCommand(IntakeManager._IntakeState intakeState, IntakeManager manager) {

        this.manager = manager;

        if (intakeState == IntakeManager._IntakeState.HOME){
            addCommands(
                    new SetIntakeSlidePositionCommand(manager, IntakeManager._SlideState.RETRACTED),
                    new SetBroomStateCommand(manager, IntakeManager._BroomState.STOPPED),
                    new SetTiltServoPosCommand(manager, IntakeManager._TiltServoState.RAISED)
            );
        } else if (intakeState == IntakeManager._IntakeState.PICKUP) {
            addCommands(
                    new SetIntakeSlidePositionCommand(manager, IntakeManager._SlideState.EXTENDED),
                    new SetBroomStateCommand(manager, IntakeManager._BroomState.INTAKEING),
                    new SetTiltServoPosCommand(manager, IntakeManager._TiltServoState.LOWERED)
            );
        } else if (intakeState == IntakeManager._IntakeState.TRANSFER) {
            addCommands(
                    new SetIntakeSlidePositionCommand(manager, IntakeManager._SlideState.TRANSFER),
                    new SetBroomStateCommand(manager, IntakeManager._BroomState.TRANSFERING),
                    new SetTiltServoPosCommand(manager, IntakeManager._TiltServoState.RAISED)
            );
        }
    }
}
