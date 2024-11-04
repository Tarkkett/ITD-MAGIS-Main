package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.commands.low_level.SetBroomStateCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.SetIntakeSlidePositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.SetTiltServoPosCommand;
import org.firstinspires.ftc.teamcode.commands.selectors.IntakePositionSelector;
import org.firstinspires.ftc.teamcode.managers.IntakeManager;

public class SetIntakeStateCommand extends SequentialCommandGroup {

    IntakeManager manager;

    public SetIntakeStateCommand(IntakeManager._IntakeState intakeState, IntakeManager manager, GamepadEx gamepad_driver) {

        this.manager = manager;

        if (intakeState == IntakeManager._IntakeState.PICKUP) {
            addCommands(
                    new ParallelCommandGroup(
                            new SetIntakeSlidePositionCommand(manager, IntakeManager._SlideState.EXTENDED),
                            new SetTiltServoPosCommand(manager, IntakeManager._TiltServoState.LOWERED),
                            new SetBroomStateCommand(manager, IntakeManager._BroomState.INTAKEING)
                    ),
                    new IntakePositionSelector(manager, gamepad_driver).withTimeout(5000)
            );
        }
    }
}
