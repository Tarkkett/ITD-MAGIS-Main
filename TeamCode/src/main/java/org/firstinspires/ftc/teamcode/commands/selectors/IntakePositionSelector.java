package org.firstinspires.ftc.teamcode.commands.selectors;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.commands.low_level.intake.AdjustYawServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.MoveIntakeSomeBit;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeGripStateCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeTiltServoPosCommand;
import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.util.GamepadPlus;

public class IntakePositionSelector extends CommandBase {

    private static final int DISTANCE = 5;
    IntakeManager manager;
    GamepadPlus gamepad;

    public IntakePositionSelector(IntakeManager manager, GamepadPlus gamepad_driver) {
        this.manager = manager;
        gamepad = gamepad_driver;
    }

    @Override
    public void initialize() {
        manager.isSelectingIntakePosition = true;
    }

    @Override
    public void execute() {

        manager.isSelectingIntakePosition = true;

        if (gamepad.isDown(gamepad.dpad_Up)) {
            CommandScheduler.getInstance().schedule(
                    new MoveIntakeSomeBit(manager, DISTANCE)
            );
        } else if (gamepad.isDown(gamepad.dpad_Down)) {
            CommandScheduler.getInstance().schedule(
                    new MoveIntakeSomeBit(manager, -DISTANCE)
            );
        } else if (gamepad.isDown(gamepad.square)) {
            CommandScheduler.getInstance().schedule(
                    new SequentialCommandGroup(
                            new SetIntakeTiltServoPosCommand(manager, IntakeManager._TiltServoState.AIMING),
                            new WaitCommand(100),
                            new SetIntakeGripStateCommand(manager, IntakeManager._GripState.RELEASE)
                    )
            );
        } else if (gamepad.isDown(gamepad.circle)) {
            CommandScheduler.getInstance().schedule(
                    new SequentialCommandGroup(
                            new SetIntakeTiltServoPosCommand(manager, IntakeManager._TiltServoState.LOWERED),
                            new WaitCommand(500),
                            new SetIntakeGripStateCommand(manager, IntakeManager._GripState.GRIP)
                    )
            );
        } else if (gamepad.leftTrigger() > 0.9) {
            CommandScheduler.getInstance().schedule(
                    new AdjustYawServoCommand(manager, IntakeManager._YawServoState.MANUAL, -0.01f));
        } else if (gamepad.rightTrigger() > 0.9) {
            CommandScheduler.getInstance().schedule(
                    new AdjustYawServoCommand(manager, IntakeManager._YawServoState.MANUAL, 0.01f));
        }

    }

}

