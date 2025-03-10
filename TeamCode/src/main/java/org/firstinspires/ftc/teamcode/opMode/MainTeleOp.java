package org.firstinspires.ftc.teamcode.opMode;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.low_level.SetRobotState;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.ToggleIntakeTiltCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakePitchServoCommand;
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
                    new SetRobotState(stateMachine, StateMachine._RobotState.INTAKE));

        //* Transfer Sequence
        gamepad_codriver.getGamepadButton(gamepad_codriver.dpad_Right)
                .whenPressed(
                        new SetRobotState(stateMachine, StateMachine._RobotState.TRANSFER));

        //* Go to deposit settings
        gamepad_codriver.getGamepadButton(gamepad_codriver.rightBumper)
            .whenPressed(
                    new SetRobotState(stateMachine, StateMachine._RobotState.DEPOSIT));

        //* Reset IMU
        gamepad_driver.getGamepadButton(gamepad_driver.options)
            .whenPressed(
                    new InstantCommand(() -> hardwareManager.pinpointDriver.resetPosAndIMU()));

        //* Switch to CALIBRATION mode
        gamepad_codriver.getGamepadButton(gamepad_codriver.options)
                .whenPressed(
                        new SetRobotState(stateMachine, StateMachine._RobotState.CALIBRATION));

        //* Toggle outtake claw
        gamepad_codriver.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)
            .whenPressed(new ToggleOuttakeClawCommand(outtakeManager, gamepad_codriver));

        //* Toggle intake tilt servo
        gamepad_driver.getGamepadButton(GamepadKeys.Button.X)
                .whenPressed(new ToggleIntakeTiltCommand(intakeManager, outtakeManager, gamepad_driver));
    }

    @Override
    public void start(){
        CommandScheduler.getInstance().cancelAll();
        CommandScheduler.getInstance().schedule(new SequentialCommandGroup(
                new SetRobotState(stateMachine, StateMachine._RobotState.HOME),
//                new InstantCommand(() -> hardwareManager.pinpointDriver.resetPosAndIMU()),
                new InstantCommand(() -> driveManager.Unlock())
        ));
    }

    @Override
    public void loop() {
        CommandScheduler.getInstance().run();
        intakeManager.loop();
        outtakeManager.loop();
        stateMachine.loop();
    }
}
