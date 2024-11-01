package org.firstinspires.ftc.teamcode.opMode;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.SetIntakeStateCommand;
import org.firstinspires.ftc.teamcode.managers.DriveManager;
import org.firstinspires.ftc.teamcode.managers.IntakeManager;

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

        gamepad_driver.getGamepadButton(GamepadKeys.Button.START).whenPressed(new InstantCommand(() -> hardwareManager.recalibrateIMU()));
        gamepad_driver.getGamepadButton(GamepadKeys.Button.RIGHT_STICK_BUTTON).toggleWhenActive(new InstantCommand(() -> driveManager.SetSubsystemState(DriveManager.DriveState.LOCKED)),
                new InstantCommand(() -> driveManager.SetSubsystemState(DriveManager.DriveState.UNLOCKED)));

        gamepad_driver.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenPressed(() -> CommandScheduler.getInstance().schedule(new SetIntakeStateCommand(IntakeManager._IntakeState.PICKUP, intakeManager)));
        gamepad_driver.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenPressed(() -> CommandScheduler.getInstance().schedule(new SetIntakeStateCommand(IntakeManager._IntakeState.TRANSFER, intakeManager)));
        gamepad_driver.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenPressed(() -> CommandScheduler.getInstance().schedule(new SetIntakeStateCommand(IntakeManager._IntakeState.HOME, intakeManager)));

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
//        if (gamepad2.a){ commandScheduler.schedule(new SetLiftPositionCommand(liftManager, LiftManager.StateEnum.HIGH_RUNG, telemetry)); }
//        if (gamepad2.b){ commandScheduler.schedule(new SetLiftPositionCommand(liftManager, LiftManager.StateEnum.LOW_CHAMBER, telemetry)); }

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
