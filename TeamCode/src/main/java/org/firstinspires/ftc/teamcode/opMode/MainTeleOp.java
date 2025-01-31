package org.firstinspires.ftc.teamcode.opMode;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.low_level.SetRobotState;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.ToggleOuttakeClawCommand;
import org.firstinspires.ftc.teamcode.managers.DriveManager;

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
                new InstantCommand(() -> stateMachine.SetSubsystemState(DriveManager._DriveState.LOCKED)),
                new InstantCommand(() -> stateMachine.SetSubsystemState(DriveManager._DriveState.UNLOCKED)));

        //* Toggle between intake and transfer
        gamepad_codriver.getGamepadButton(gamepad_codriver.leftBumper)
            .toggleWhenPressed(
                    new SetRobotState(stateMachine, StateMachine._RobotState.INTAKE),
                    new SetRobotState(stateMachine, StateMachine._RobotState.TRANSFER));

        //* Go to deposit settings
        gamepad_codriver.getGamepadButton(gamepad_codriver.rightBumper)
            .whenPressed(
                    new SetRobotState(stateMachine, StateMachine._RobotState.DEPOSIT));

        //* Reset IMU
        gamepad_driver.getGamepadButton(gamepad_driver.options)
            .whenPressed(
                    new InstantCommand(() -> drive.pinpoint.resetPosAndIMU()));

        //* Toggle outtake claw
        gamepad_codriver.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)
            .whenPressed(new ToggleOuttakeClawCommand(outtakeManager, gamepad_codriver, gamepad_driver));

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
        CommandScheduler.getInstance().schedule(new SetRobotState(stateMachine, StateMachine._RobotState.HOME));
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
