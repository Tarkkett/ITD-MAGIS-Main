package org.firstinspires.ftc.teamcode.commands.selectors;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.commands.TransferCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeClawStateCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakePitchServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeSlidePositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeTiltServoPosCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeYawServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetLiftPositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeClawStateCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakePitchServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeTiltServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeYawServoCommand;
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

    public static double shiftAngleCustom = 45; //* Degrees

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
        commandButtonBindings.put(GamepadKeys.Button.START, this::DoTransfer);
        commandButtonBindings.put(GamepadKeys.Button.DPAD_UP, this::ScoreSample);
        commandButtonBindings.put(GamepadKeys.Button.DPAD_DOWN, this::HomeLift);
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

        ControlYawManually(-gamepad_codriver.getLeftY(), gamepad_codriver.getLeftX());
    }



    private void DoRetrySampleGrab() {
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        new SetIntakeClawStateCommand(manager, IntakeManager._ClawState.CLOSED),
                        new SetIntakeTiltServoPosCommand(manager, IntakeManager._TiltServoState.AIMING),
                        new SetIntakePitchServoCommand(manager, IntakeManager._PitchServoState.AIMING),
                        new SetIntakeSlidePositionCommand(manager, IntakeManager._SlideState.EXTENDED)
                )
        );
    }

    private void DoGrabSample() {
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        new SetIntakeClawStateCommand(manager, IntakeManager._ClawState.CLOSED),
                        new WaitCommand(400),
                        new SetIntakeTiltServoPosCommand(manager, IntakeManager._TiltServoState.AIMING),
                        new SetIntakePitchServoCommand(manager, IntakeManager._PitchServoState.VERTICAL),
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
                        new SetIntakePitchServoCommand(manager, IntakeManager._PitchServoState.AIMING),
                        new SetIntakeYawServoCommand(manager, IntakeManager._YawServoState.AIMING),
                        new SetIntakeClawStateCommand(manager, IntakeManager._ClawState.OPEN)
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
                        new SetIntakePitchServoCommand(manager, IntakeManager._PitchServoState.LOWERED),
                        new SetIntakeYawServoCommand(manager, IntakeManager._YawServoState.LOWERED)
                )
        );
    }

    private void ControlYawManually(double rightX, double rightY) {

        double yawServoAngle = Math.atan2(rightX, rightY);
        double shiftAngle = yawServoAngle+ Math.toRadians(shiftAngleCustom);
        double wrapped_angle = Math.atan2(Math.sin(shiftAngle), Math.cos(shiftAngle));
        double normalized_angle = (wrapped_angle + Math.PI) / (2 * Math.PI);
        if (manager.yawState == IntakeManager._YawServoState.LOWERED){
            manager.controlYawAngle(normalized_angle + 0.1); //* Differential offset
        }
        else {manager.controlYawAngle(normalized_angle);}

    }

    private void ScoreSample(){
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        new SetOuttakeClawStateCommand(outtakeManager, OuttakeManager._OuttakeClawServoState.CLOSED),
                        new SetLiftPositionCommand(outtakeManager, OuttakeManager._LiftState.HIGH_BASKET),
                        new SetOuttakeTiltServoCommand(outtakeManager, OuttakeManager._OuttakeTiltServoState.DEPOSIT_SAMPLE),
                        new SetOuttakePitchServoCommand(outtakeManager, OuttakeManager._PitchServoState.DEPOSIT_SAMPLE))
        );
    }

    private void HomeLift(){
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        new SetOuttakeClawStateCommand(outtakeManager, OuttakeManager._OuttakeClawServoState.OPEN),
                        new SetLiftPositionCommand(outtakeManager, OuttakeManager._LiftState.TRANSFER),
                        new SetOuttakeTiltServoCommand(outtakeManager, OuttakeManager._OuttakeTiltServoState.TRANSFER),
                        new SetOuttakePitchServoCommand(outtakeManager, OuttakeManager._PitchServoState.TRANSFER)),
                new SetOuttakeYawServoCommand(outtakeManager, OuttakeManager._OuttakeYawServoState.HORIZONTAL_Pickup)
        );
    }

    @Override
    public void end(boolean interrupted){
        gamepad_codriver.Warn();
    }



}

