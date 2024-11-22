package org.firstinspires.ftc.teamcode.opMode;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.SetIntakeStateCommand;
import org.firstinspires.ftc.teamcode.commands.SetOuttakeStateCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeTiltServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetSpecimentServoPositionCommand;
import org.firstinspires.ftc.teamcode.commands.TransferCommand;
import org.firstinspires.ftc.teamcode.managers.DriveManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;
import org.firstinspires.ftc.teamcode.managers.StateMachine;

@TeleOp(name = "Main TeleOp", group = "OpMode")
public class MainTeleOp extends OpModeTemplate {

    //! "Better Comments" plugin is advised
    @Override
    public void init() {

        initSystems(false);

        telemetry.setAutoClear(true);

        //* Toggle drivetrain lock
        gamepad_driver.getGamepadButton(gamepad_driver.rightStick)
                .toggleWhenActive(
                    new InstantCommand(() -> stateMachine.SetSubsystemState(DriveManager._DriveState.LOCKED)),
                    new InstantCommand(() -> stateMachine.SetSubsystemState(DriveManager._DriveState.UNLOCKED)));

        //* Toggle between intake and transfer
        gamepad_driver.getGamepadButton(gamepad_driver.leftBumper)
                .toggleWhenPressed(
                        new InstantCommand(() -> stateMachine.SetSubsystemState(StateMachine._RobotState.INTAKE)),
                        new InstantCommand(() -> stateMachine.SetSubsystemState(StateMachine._RobotState.TRANSFER)));

        //* Go to deposit settings
        gamepad_driver.getGamepadButton(gamepad_driver.rightBumper)
                .whenPressed(
                        new InstantCommand(() -> stateMachine.SetSubsystemState(StateMachine._RobotState.DEPOSIT)));

        //* Toggle bucket position
        gamepad_codriver.getGamepadButton(gamepad_driver.square)
                .toggleWhenActive(
                        new SetOuttakeTiltServoCommand(outtakeManager, OuttakeManager._OuttakeTiltServoState.HIGH),
                        new SetOuttakeTiltServoCommand(outtakeManager, OuttakeManager._OuttakeTiltServoState.LOW));

        //* Toggle speciment claw
        gamepad_codriver.getGamepadButton(gamepad_driver.cross)
                .toggleWhenActive(
                        new SetSpecimentServoPositionCommand(outtakeManager, OuttakeManager._SpecimentServoState.OPEN),
                        new SetSpecimentServoPositionCommand(outtakeManager, OuttakeManager._SpecimentServoState.CLOSED));

        //* Reset IMU
        gamepad_driver.getGamepadButton(gamepad_driver.options)
                .whenPressed(
                        new InstantCommand(() -> drive.pinpoint.resetPosAndIMU()));

    }

    @Override
    public void loop() {

        CommandScheduler.getInstance().run();
        intakeManager.loop();
        outtakeManager.loop();
        ascentManager.loop();
        stateMachine.loop();
    }

    @Override
    public void stop() {

        driveManager.stopMovementControlThread();

        super.stop();
    }
}
