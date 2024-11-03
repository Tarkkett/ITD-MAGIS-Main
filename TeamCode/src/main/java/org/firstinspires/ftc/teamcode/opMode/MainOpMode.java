package org.firstinspires.ftc.teamcode.opMode;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.SetIntakeStateCommand;
import org.firstinspires.ftc.teamcode.commands.SetOuttakeStateCommand;
import org.firstinspires.ftc.teamcode.commands.SpecimentLoweringSelector;
import org.firstinspires.ftc.teamcode.commands.low_level.SetBucketPositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.SetSpecimentServoPositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.TransferCommand;
import org.firstinspires.ftc.teamcode.managers.DriveManager;
import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

import java.util.Optional;

@TeleOp(name = "Main TeleOp", group = "OpMode")
public class MainOpMode extends OpModeTemplate {

//    private StateMachine stateMachine;
//    private Thread stateThread;

    @Override
    public void init() {
        initSystems(false);

//        stateMachine = new StateMachine(liftManager, driveManager);
//        stateThread = new Thread(stateMachine);

        telemetry.addData(":","Init passed..!");
        telemetry.setAutoClear(true);

        gamepad_driver.getGamepadButton(GamepadKeys.Button.START)
                .whenPressed(new InstantCommand(() -> hardwareManager.recalibrateIMU()));
        gamepad_driver.getGamepadButton(GamepadKeys.Button.RIGHT_STICK_BUTTON)
                .toggleWhenActive(new InstantCommand(() -> driveManager.SetSubsystemState(DriveManager.DriveState.LOCKED)),
                    new InstantCommand(() -> driveManager.SetSubsystemState(DriveManager.DriveState.UNLOCKED)));

        gamepad_driver.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                .whenPressed(() -> CommandScheduler.getInstance().schedule(new SetIntakeStateCommand(IntakeManager._IntakeState.PICKUP, intakeManager, outtakeManager)));
//        gamepad_driver.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
//                .whenPressed(() -> CommandScheduler.getInstance().schedule(new SetIntakeStateCommand(IntakeManager._IntakeState.TRANSFER, intakeManager, outtakeManager)));
        gamepad_driver.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                .whenPressed(() -> CommandScheduler.getInstance().schedule(new SetIntakeStateCommand(IntakeManager._IntakeState.HOME, intakeManager, outtakeManager)));

        gamepad_driver.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenPressed(() -> CommandScheduler.getInstance().schedule(new TransferCommand(intakeManager, outtakeManager)));

        gamepad_driver.getGamepadButton(GamepadKeys.Button.X)   //Square
                .toggleWhenActive(new SetBucketPositionCommand(outtakeManager, OuttakeManager._BucketServoState.HIGH),
                        new SetBucketPositionCommand(outtakeManager, OuttakeManager._BucketServoState.LOW));

        gamepad_driver.getGamepadButton(GamepadKeys.Button.Y)   //Triangle
                .whenPressed(() -> CommandScheduler.getInstance().schedule(new SpecimentLoweringSelector(gamepad_driver, outtakeManager)));

        gamepad_driver.getGamepadButton(GamepadKeys.Button.A)   //Cross
                .toggleWhenActive(new SetSpecimentServoPositionCommand(outtakeManager, OuttakeManager._SpecimentServoState.OPEN), new SetSpecimentServoPositionCommand(outtakeManager, OuttakeManager._SpecimentServoState.CLOSED));
        gamepad_driver.getGamepadButton(GamepadKeys.Button.B)   //Circle
                .whenPressed(() -> CommandScheduler.getInstance().schedule(new SetOuttakeStateCommand(OuttakeManager._OuttakeState.TRANSFER, outtakeManager, gamepad_driver)));
        gamepad_driver.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenPressed(() -> CommandScheduler.getInstance().schedule(new SetOuttakeStateCommand(OuttakeManager._OuttakeState.DEPOSIT, outtakeManager, gamepad_driver)));

    }

    @Override
    public void start(){
        driveManager.SetSubsystemState(DriveManager.DriveState.UNLOCKED);
//        stateThread.start();
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

//        if (stateMachine != null) {
//            stateMachine.stop();
//        }
//
//        if (stateThread != null) {
//            try {
//                stateThread.join();
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        }
        super.stop();
    }
}
