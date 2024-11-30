package org.firstinspires.ftc.teamcode.opMode;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetSpecimentServoPositionCommand;
import org.firstinspires.ftc.teamcode.managers.DriveManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;
import org.firstinspires.ftc.teamcode.util.GamepadPlus;

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
                        new InstantCommand(() -> stateMachine.SetSubsystemState(StateMachine._RobotState.INTAKE)),
                        new InstantCommand(() -> stateMachine.SetSubsystemState(StateMachine._RobotState.TRANSFER)));

        //* Go to deposit settings
        gamepad_codriver.getGamepadButton(gamepad_codriver.rightBumper)
                .whenPressed(
                        new InstantCommand(() -> stateMachine.SetSubsystemState(StateMachine._RobotState.DEPOSIT)));

        //* Toggle speciment claw
        gamepad_codriver.getGamepadButton(gamepad_codriver.cross)
                .toggleWhenActive(
                        new SetSpecimentServoPositionCommand(outtakeManager, OuttakeManager._SpecimentServoState.OPEN),
                        new SetSpecimentServoPositionCommand(outtakeManager, OuttakeManager._SpecimentServoState.CLOSED));

        //* Reset IMU
        gamepad_driver.getGamepadButton(gamepad_driver.options)
                .whenPressed(
                        new InstantCommand(() -> drive.pinpoint.resetPosAndIMU()));
    }

    @Override
    public void start(){
        driveManager.onUnlocked();
    }

    @Override
    public void loop() {

        if (elapsedTime.milliseconds() >= 1000){
            GamepadPlus tempGamepad;
            tempGamepad = gamepad_driver;
            gamepad_driver = gamepad_codriver;
            gamepad_codriver = tempGamepad;
            elapsedTime.reset();

            gamepad_driver.gamepad.setLedColor(0.5, 0.5, 0, 100000);
            gamepad_codriver.gamepad.setLedColor(0, 0.5, 0.5, 100000);
        }

        CommandScheduler.getInstance().run();
        intakeManager.loop();
        outtakeManager.loop();
//        ascentManager.loop();
        stateMachine.loop();
    }

    @Override
    public void stop() {
        driveManager.stopMovementControlThread();
        outtakeManager.selectingProcess = false;
        intakeManager.selectingProcess = false;

        super.stop();
    }
}
