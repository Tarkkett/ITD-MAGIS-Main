package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeGripStateCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeSlidePositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeTiltServoPosCommand;
import org.firstinspires.ftc.teamcode.commands.selectors.IntakePositionSelector;
import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.util.GamepadPlus;

public class SetIntakeStateCommand extends SequentialCommandGroup {

    IntakeManager manager;

    public SetIntakeStateCommand(IntakeManager._IntakeState intakeState, IntakeManager manager, GamepadPlus gamepad_driver, GamepadPlus gamepad_codriver) {

        this.manager = manager;

        if (intakeState == IntakeManager._IntakeState.PICKUP) {
            addCommands(
                    new ParallelCommandGroup(
                            new SetIntakeSlidePositionCommand(manager, IntakeManager._SlideState.EXTENDED),
                            new SetIntakeTiltServoPosCommand(manager, IntakeManager._TiltServoState.AIMING),
                            new SetIntakeGripStateCommand(manager, IntakeManager._GripState.RELEASE)
                    ),
                    new IntakePositionSelector(manager, gamepad_driver, gamepad_codriver),
                    new InstantCommand(() -> manager.isSelectingIntakePosition = false)
            );
        } else if (intakeState == IntakeManager._IntakeState.HOME) {
            //FIXME: add this functionality
        }
    }
}
