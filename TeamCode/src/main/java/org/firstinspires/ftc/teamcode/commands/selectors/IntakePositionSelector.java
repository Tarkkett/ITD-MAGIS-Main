package org.firstinspires.ftc.teamcode.commands.selectors;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.commands.TransferCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeClawStateCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakePitchServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeSlidePositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeTiltServoPosCommand;
import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;
import org.firstinspires.ftc.teamcode.util.GamepadPlus;

import java.util.HashMap;
import java.util.Map;

@Config
public class IntakePositionSelector extends CommandBase {

    IntakeManager manager;
    OuttakeManager outtakeManager;
    GamepadPlus gamepad_codriver;

    private final Map<GamepadKeys.Button, Runnable> commandButtonBindings = new HashMap<>();
    private final Map<GamepadKeys.Trigger, Runnable> commandTriggerBindings = new HashMap<>();

    public static double shiftAngleCustom = 0;

    public IntakePositionSelector(IntakeManager manager, OuttakeManager outtakeManager, GamepadPlus gamepad_codriver) {
        this.manager = manager;
        this.gamepad_codriver = gamepad_codriver;
        this.outtakeManager = outtakeManager;
    }

    @Override
    public void initialize() {
        commandTriggerBindings.put(GamepadKeys.Trigger.LEFT_TRIGGER, this::DoGrabSample);
        commandTriggerBindings.put(GamepadKeys.Trigger.RIGHT_TRIGGER, this::LowerClawIntoSample);
        commandButtonBindings.put(GamepadKeys.Button.Y, this::DoRetrySampleGrab);
        commandButtonBindings.put(GamepadKeys.Button.A, this::DoReleaseSample);
        commandButtonBindings.put(GamepadKeys.Button.B, this::DoExtendAndAim);
        commandButtonBindings.put(GamepadKeys.Button.LEFT_BUMPER, this::DoTransfer);
    }

    @Override
    public void execute() {

        for (Map.Entry<GamepadKeys.Trigger, Runnable> entry : commandTriggerBindings.entrySet()) {
            GamepadKeys.Trigger trigger = entry.getKey();
            Runnable action = entry.getValue();

            double value = 0.0;
            if (trigger == GamepadKeys.Trigger.LEFT_TRIGGER) {
                value = gamepad_codriver.leftTrigger();
            } else if (trigger == GamepadKeys.Trigger.RIGHT_TRIGGER) {
                value = gamepad_codriver.rightTrigger();
            }

            if (value > 0.2) {
                action.run();
            }
        }

        for (Map.Entry<GamepadKeys.Button, Runnable> entry : commandButtonBindings.entrySet()) {
            if (gamepad_codriver.isDown(entry.getKey())) {
                entry.getValue().run();
            }
        }

        ControlYawManually(gamepad_codriver.getRightY(), gamepad_codriver.getRightX());
    }



    private void DoRetrySampleGrab() {
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        new SetIntakeClawStateCommand(manager, IntakeManager._ClawState.OPEN),
                        new SetIntakeTiltServoPosCommand(manager, IntakeManager._TiltServoState.AIMING),
                        new SetIntakePitchServoCommand(manager, IntakeManager._PitchServoState.AIMING)
                )
        );
    }

    private void DoGrabSample() {
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        new SetIntakeClawStateCommand(manager, IntakeManager._ClawState.CLOSED),
                        new WaitCommand(400),
                        new SetIntakeTiltServoPosCommand(manager, IntakeManager._TiltServoState.VERTICAL),
                        new WaitCommand(400),
                        new SetIntakeSlidePositionCommand(manager, IntakeManager._SlideState.RETRACTED)
                )
        );
    }

    private void DoReleaseSample() {
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        new SetIntakeClawStateCommand(manager, IntakeManager._ClawState.OPEN),
                        new WaitCommand(200),
                        new SetIntakeSlidePositionCommand(manager, IntakeManager._SlideState.RETRACTED)
                )
        );
    }

    private void DoExtendAndAim() {
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        new SetIntakeSlidePositionCommand(manager, IntakeManager._SlideState.EXTENDED),
                        new SetIntakeTiltServoPosCommand(manager, IntakeManager._TiltServoState.AIMING),
                        new SetIntakePitchServoCommand(manager, IntakeManager._PitchServoState.AIMING)
                )
        );
    }

    private void DoTransfer() {
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        new TransferCommand(manager, outtakeManager)
                )
        );
    }

    private void LowerClawIntoSample(){
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        new SetIntakeClawStateCommand(manager, IntakeManager._ClawState.OPEN),
                        new WaitCommand(100),
                        new SetIntakeTiltServoPosCommand(manager, IntakeManager._TiltServoState.LOWERED),
                        new SetIntakePitchServoCommand(manager, IntakeManager._PitchServoState.LOWERED)
                )
        );
    }

    private void ControlYawManually(double rightX, double rightY) {

        double yawServoAngle = Math.atan2(rightX, rightY);
        double shiftAngle = yawServoAngle+ Math.toRadians(shiftAngleCustom);
        double wrapped_angle = Math.atan2(Math.sin(shiftAngle), Math.cos(shiftAngle));
        double normalized_angle = (wrapped_angle + Math.PI) / (2 * Math.PI);
        manager.controlYawAngle(normalized_angle);
    }

    @Override
    public void end(boolean interrupted){
        gamepad_codriver.Warn();
    }

}

