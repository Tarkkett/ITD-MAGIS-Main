package org.firstinspires.ftc.teamcode.opMode;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.low_level.SetRobotState;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.ToggleIntakeTiltCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetLiftPositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeExtendoServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.ToggleOuttakeClawCommand;
import org.firstinspires.ftc.teamcode.managers.DriveManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

@TeleOp(name = "Main TeleOp", group = "OpMode")
public class MainTeleOp extends OpModeTemplate {

    //! "Better Comments" plugin is advised
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
        gamepad_codriver.getGamepadButton(gamepad_codriver.leftBumper)
            .whenPressed(
                    new SetRobotState(stateMachine, StateMachine._RobotState.INTAKE, false));

        //* Transfer Sequence
        gamepad_codriver.getGamepadButton(gamepad_codriver.dpad_Right)
                .whenPressed(
                        new SetRobotState(stateMachine, StateMachine._RobotState.TRANSFER, false));

        //* Go to deposit settings
        gamepad_codriver.getGamepadButton(gamepad_codriver.rightBumper)
            .whenPressed(
                    new SetRobotState(stateMachine, StateMachine._RobotState.DEPOSIT, false));

        //* Reset IMU
        gamepad_driver.getGamepadButton(gamepad_driver.options)
            .whenPressed(
                    new InstantCommand(() -> drive.pinpoint.resetPosAndIMU()));

        gamepad_codriver.getGamepadButton(gamepad_codriver.options)
                .whenPressed(
                        new SetRobotState(stateMachine, StateMachine._RobotState.CALIBRATION, false));

        //* Toggle outtake claw
        gamepad_codriver.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)
            .whenPressed(new ToggleOuttakeClawCommand(outtakeManager, gamepad_codriver));

        //* Toggle intake tilt servo
        gamepad_driver.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(new ToggleIntakeTiltCommand(intakeManager, outtakeManager, gamepad_driver));

        //* Morse code?
        gamepad_driver.getGamepadButton(GamepadKeys.Button.LEFT_STICK_BUTTON).whenPressed(
            new InstantCommand(() -> gamepad_codriver.rumble(200))
        );
        gamepad_codriver.getGamepadButton(GamepadKeys.Button.LEFT_STICK_BUTTON).whenPressed(
            new InstantCommand(() -> gamepad_driver.rumble(200))
        );
    }

    @Override
    public void start(){
        CommandScheduler.getInstance().cancelAll();
        CommandScheduler.getInstance().schedule(new SequentialCommandGroup(

                new SetRobotState(stateMachine, StateMachine._RobotState.HOME, false),
                new InstantCommand(() -> drive.pinpoint.recalibrateIMU()),
                new SetOuttakeExtendoServoCommand(outtakeManager, OuttakeManager._ExtendoServoState.AUTO_DEPOSIT)
        ));

        driveManager.onUnlocked();
    }

    @Override
    public void loop() {
        CommandScheduler.getInstance().run();
        intakeManager.loop();
        outtakeManager.loop();
        stateMachine.loop();
    }
}
