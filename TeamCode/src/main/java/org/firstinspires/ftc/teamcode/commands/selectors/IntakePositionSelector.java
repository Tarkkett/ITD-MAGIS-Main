package org.firstinspires.ftc.teamcode.commands.selectors;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;

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

    private boolean isSelected = false;

    public static double shiftAngleCustom = 0;

    public IntakePositionSelector(IntakeManager manager, GamepadPlus gamepad_driver, GamepadPlus gamepad_codriver) {
        this.manager = manager;
        this.gamepad_driver = gamepad_driver;
        this.gamepad_codriver = gamepad_codriver;
    }

    @Override
    public void initialize() {
        manager.selectingProcess = true;
    }

    @Override
    public void execute() {

        if (manager.selectingProcess) {
            if (gamepad_codriver.isDown(gamepad_driver.dpad_Up)) {
                CommandScheduler.getInstance().schedule(
                        new MoveIntakeSomeBit(manager, DISTANCE)
                );
            } else if (gamepad_codriver.isDown(gamepad_driver.dpad_Down)) {
                CommandScheduler.getInstance().schedule(
                        new MoveIntakeSomeBit(manager, -DISTANCE)
                );
            } else if (gamepad_codriver.leftTrigger() > 0.2) {
                CommandScheduler.getInstance().schedule(
                        new SequentialCommandGroup(
                                new SetIntakeGripStateCommand(manager, IntakeManager._GripState.GRIP),
                                new WaitCommand(400),
                                new SetIntakeTiltServoPosCommand(manager, IntakeManager._TiltServoState.AIMING_UPPER)
                        )
                );

            } else if (gamepad_codriver.isDown(gamepad_driver.triangle)) {
                CommandScheduler.getInstance().schedule(
                        new SequentialCommandGroup(
                                new SetIntakeGripStateCommand(manager, IntakeManager._GripState.RELEASE),
                                new SetIntakeTiltServoPosCommand(manager, IntakeManager._TiltServoState.AIMING)
                        )

                );
            } else if (gamepad_codriver.rightTrigger() > 0.2) {
                CommandScheduler.getInstance().schedule(
                        new SequentialCommandGroup(
                                new SetIntakeGripStateCommand(manager, IntakeManager._GripState.RELEASE),
                                new WaitCommand(100),
                                new SetIntakeTiltServoPosCommand(manager, IntakeManager._TiltServoState.LOWERED)
                        )
                );
            }
            ControlYawManually(gamepad_codriver.getRightX(), gamepad_codriver.getRightY());
        }

    }

    private void ControlYawManually(double rightX, double rightY) {

        double yawServoAngle = Math.atan2(rightX, rightY);
        double shiftAngle = yawServoAngle+ Math.toRadians(shiftAngleCustom);
        double wrapped_angle = Math.atan2(Math.sin(shiftAngle), Math.cos(shiftAngle));
        double normalized_angle = (wrapped_angle + Math.PI) / (2 * Math.PI);
        manager.controlYawAngle(normalized_angle);
    }

}

