package org.firstinspires.ftc.teamcode.opMode;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.EnableAutoDepositCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.ReInitialiseIMU;
import org.firstinspires.ftc.teamcode.commands.low_level.ResetHeadingCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.SetRobotState;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.ToggleIntakeTiltCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.ToggleOuttakeClawCommand;
import org.firstinspires.ftc.teamcode.managers.DriveManager;

@TeleOp(name = "Main TeleOp", group = "OpMode")
public class MainTeleOp extends OpModeTemplate {

    @Override
    public void init() {
        initSystems(false);
        telemetry.setAutoClear(true);

        //* Toggle drivetrain lock
        gamepad_driver.getGamepadButton(gamepad_driver.share)
                .toggleWhenActive(
                        new InstantCommand(() -> stateMachine.SetDrivetrainState(DriveManager._DriveState.LOCKED)),
                        new InstantCommand(() -> stateMachine.SetDrivetrainState(DriveManager._DriveState.UNLOCKED)));

        //* Enable intake settings
        gamepad_codriver.getGamepadButton(gamepad_codriver.share)
                .whenPressed(new SetRobotState(stateMachine, StateMachine._RobotState.INTAKE));

        //* Go to deposit settings
        gamepad_codriver.getGamepadButton(gamepad_codriver.options)
                .whenPressed(new SetRobotState(stateMachine, StateMachine._RobotState.DEPOSIT));

        //* Reset IMU
        gamepad_driver.getGamepadButton(gamepad_driver.options)
                .whenPressed(new ResetHeadingCommand(hardwareManager, driveManager));

        gamepad_driver.getGamepadButton(gamepad_driver.dpad_Left)
                .whenPressed(new ReInitialiseIMU(hardwareManager));

        //* Toggle outtake claw
        gamepad_codriver.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)
                .whenPressed(new ToggleOuttakeClawCommand(outtakeManager, gamepad_codriver));

        progress(45);
    }

    @Override
    public void start() {
        telemetry.clearAll();
        CommandScheduler.getInstance().cancelAll();
        CommandScheduler.getInstance().schedule(new SequentialCommandGroup(
                new ResetHeadingCommand(hardwareManager, driveManager),
                new SetRobotState(stateMachine, StateMachine._RobotState.HOME),
                new InstantCommand(() -> driveManager.Unlock())
        ));
    }

    @Override
    public void loop() {
        long startTime = System.nanoTime();

        CommandScheduler.getInstance().run();
        intakeManager.loop();
        outtakeManager.loop();
        stateMachine.loop();
        driveManager.loop();

        long endTime = System.nanoTime();
        long loopDuration = (endTime - startTime) / 1_000_000;
    }

}
