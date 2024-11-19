package org.firstinspires.ftc.teamcode.commands.selectors;

import com.acmerobotics.dashboard.config.Config;
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

@Config
public class IntakePositionSelector extends CommandBase {

    private static final int DISTANCE = 5;
    IntakeManager manager;
    GamepadPlus gamepad_driver;
    GamepadPlus gamepad_codriver;

    public static double shiftAngleCustom = 0;

    public IntakePositionSelector(IntakeManager manager, GamepadPlus gamepad_driver, GamepadPlus gamepad_codriver) {
        this.manager = manager;
        this.gamepad_driver = gamepad_driver;
        this.gamepad_codriver = gamepad_codriver;
    }

    @Override
    public void initialize() {
        manager.isSelectingIntakePosition = true;
    }

    @Override
    public void execute() {

        manager.isSelectingIntakePosition = true;

        if (gamepad_driver.isDown(gamepad_driver.dpad_Up)) {
            CommandScheduler.getInstance().schedule(
                    new MoveIntakeSomeBit(manager, DISTANCE)
            );
        } else if (gamepad_driver.isDown(gamepad_driver.dpad_Down)) {
            CommandScheduler.getInstance().schedule(
                    new MoveIntakeSomeBit(manager, -DISTANCE)
            );
        } else if (gamepad_driver.isDown(gamepad_driver.square)) {
            CommandScheduler.getInstance().schedule(
                    new SequentialCommandGroup(
                            new SetIntakeTiltServoPosCommand(manager, IntakeManager._TiltServoState.AIMING),
                            new WaitCommand(100),
                            new SetIntakeGripStateCommand(manager, IntakeManager._GripState.RELEASE)
                    )
            );
        } else if (gamepad_driver.isDown(gamepad_driver.circle)) {
            CommandScheduler.getInstance().schedule(
                    new SequentialCommandGroup(
                            new SetIntakeTiltServoPosCommand(manager, IntakeManager._TiltServoState.LOWERED),
                            new WaitCommand(500),
                            new SetIntakeGripStateCommand(manager, IntakeManager._GripState.GRIP)
                    )
            );
        }
        double yawServoAngle = Math.atan2(gamepad_codriver.getRightX(), gamepad_codriver.getRightY());
        double shiftAngle = yawServoAngle+ Math.toRadians(shiftAngleCustom);
        double wrapped_angle = Math.atan2(Math.sin(shiftAngle), Math.cos(shiftAngle));
        double normalized_angle = (wrapped_angle + Math.PI) / (2 * Math.PI);
        manager.controlYawAngle(normalized_angle);



    }

}

