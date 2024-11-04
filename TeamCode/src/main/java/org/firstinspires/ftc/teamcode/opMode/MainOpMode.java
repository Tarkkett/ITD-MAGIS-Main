package org.firstinspires.ftc.teamcode.opMode;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.SetIntakeStateCommand;
import org.firstinspires.ftc.teamcode.commands.SetOuttakeStateCommand;
import org.firstinspires.ftc.teamcode.commands.selectors.SpecimentLoweringSelector;
import org.firstinspires.ftc.teamcode.commands.low_level.SetBucketPositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.SetSpecimentServoPositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.SetTiltServoPosCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.TransferCommand;
import org.firstinspires.ftc.teamcode.managers.DriveManager;
import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

@TeleOp(name = "Main TeleOp", group = "OpMode")
public class MainOpMode extends OpModeTemplate {

    @Override
    public void init() {
        initSystems(false);

        telemetry.addData(":","Init passed..!");
        telemetry.setAutoClear(true);

        //Recalibrate IMU on computer
        gamepad_driver.getGamepadButton(GamepadKeys.Button.START)
                .whenPressed(new InstantCommand(() -> hardwareManager.recalibrateIMU()));

        //Toggle drivetrain lock
        gamepad_driver.getGamepadButton(GamepadKeys.Button.RIGHT_STICK_BUTTON)
                .toggleWhenActive(new InstantCommand(() -> driveManager.SetSubsystemState(DriveManager.DriveState.LOCKED)),
                    new InstantCommand(() -> driveManager.SetSubsystemState(DriveManager.DriveState.UNLOCKED)));

        //Go to pickup settings
        gamepad_driver.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(() -> CommandScheduler.getInstance().schedule(new SetIntakeStateCommand(IntakeManager._IntakeState.PICKUP, intakeManager, gamepad_driver)));

        //Intake home command
        gamepad_driver.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .whenPressed(() -> CommandScheduler.getInstance()
                        .schedule(new SetIntakeStateCommand(IntakeManager._IntakeState.HOME, intakeManager, gamepad_driver)
                                .andThen(new SetTiltServoPosCommand(intakeManager, IntakeManager._TiltServoState.LOWERED))));

        //Transfer command
        gamepad_driver.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                .whenPressed(() -> CommandScheduler.getInstance().schedule(new TransferCommand(intakeManager, outtakeManager)));

        //Toggle bucket position
        gamepad_driver.getGamepadButton(GamepadKeys.Button.X)   //Square
                .toggleWhenActive(new SetBucketPositionCommand(outtakeManager, OuttakeManager._BucketServoState.HIGH),
                        new SetBucketPositionCommand(outtakeManager, OuttakeManager._BucketServoState.LOW));

        //Speciment lower sequence command
        gamepad_driver.getGamepadButton(GamepadKeys.Button.Y)   //Triangle
                .whenPressed(() -> CommandScheduler.getInstance().schedule(new SpecimentLoweringSelector(gamepad_driver, outtakeManager)));

        //Toggle speciment claw
        gamepad_driver.getGamepadButton(GamepadKeys.Button.A)   //Cross
                .toggleWhenActive(new SetSpecimentServoPositionCommand(outtakeManager, OuttakeManager._SpecimentServoState.OPEN), new SetSpecimentServoPositionCommand(outtakeManager, OuttakeManager._SpecimentServoState.CLOSED));

        //Go to deposit settings
        gamepad_driver.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(() -> CommandScheduler.getInstance().schedule(new SetOuttakeStateCommand(OuttakeManager._OuttakeState.DEPOSIT, outtakeManager, gamepad_driver)));

    }

    @Override
    public void start(){
        driveManager.SetSubsystemState(DriveManager.DriveState.UNLOCKED);
    }

    @Override
    public void loop() {

        CommandScheduler.getInstance().run();
        intakeManager.loop();
        outtakeManager.loop();

    }

    @Override
    public void stop() {

        driveManager.stopMovementControlThread();

        super.stop();
    }
}
