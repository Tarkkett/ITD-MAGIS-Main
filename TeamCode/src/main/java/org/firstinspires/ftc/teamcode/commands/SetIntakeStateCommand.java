package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.commands.low_level.SetGripStateCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.SetIntakeSlidePositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.SetTiltServoPosCommand;
import org.firstinspires.ftc.teamcode.commands.selectors.IntakePositionSelector;
import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.util.GamepadPlus;

public class SetIntakeStateCommand extends SequentialCommandGroup {

    IntakeManager manager;

    public SetIntakeStateCommand(IntakeManager._IntakeState intakeState, IntakeManager manager, GamepadPlus gamepad_driver) {

        this.manager = manager;

        if (intakeState == IntakeManager._IntakeState.PICKUP) {
            addCommands(
                    new ParallelCommandGroup(
                            new SetIntakeSlidePositionCommand(manager, IntakeManager._SlideState.EXTENDED),
                            new SetTiltServoPosCommand(manager, IntakeManager._TiltServoState.AIMING),
                            new SetGripStateCommand(manager, IntakeManager._GripState.RELEASE)
                    ),
                    new IntakePositionSelector(manager, gamepad_driver),
                    new InstantCommand(() -> manager.isSelectingIntakePosition = false)
            );
        }
    }
}
