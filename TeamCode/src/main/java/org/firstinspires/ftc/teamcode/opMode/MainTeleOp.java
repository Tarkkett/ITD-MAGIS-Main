package org.firstinspires.ftc.teamcode.opMode;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.SetIntakeStateCommand;
import org.firstinspires.ftc.teamcode.commands.SetOuttakeStateCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.SetBroomStateCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.SetServosToDefaultsCommand;
import org.firstinspires.ftc.teamcode.commands.selectors.SpecimentLoweringSelector;
import org.firstinspires.ftc.teamcode.commands.low_level.SetBucketPositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.SetSpecimentServoPositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.SetTiltServoPosCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.TransferCommand;
import org.firstinspires.ftc.teamcode.managers.DriveManager;
import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

@SuppressWarnings("unused")
@TeleOp(name = "Main TeleOp", group = "OpMode")
public class MainTeleOp extends OpModeTemplate {

    //! "Better Comments" plugin is advised
    @Override
    public void init() {

        initSystems(false);

        telemetry.setAutoClear(true);

        //* Toggle drivetrain lock
        gamepad_driver.getGamepadButton(gamepad_driver.rightStick)
                .toggleWhenActive(new InstantCommand(() -> driveManager.SetSubsystemState(DriveManager.DriveState.LOCKED)),
                    new InstantCommand(() -> driveManager.SetSubsystemState(DriveManager.DriveState.UNLOCKED)));

        //* Intake home command
        gamepad_driver.getGamepadButton(gamepad_driver.dpad_Down)
                .whenPressed(() -> CommandScheduler.getInstance()
                        .schedule(new SetIntakeStateCommand(IntakeManager._IntakeState.HOME, intakeManager, gamepad_driver)
                                .andThen(new SetTiltServoPosCommand(intakeManager, IntakeManager._TiltServoState.LOWERED))));

        //* Toggle between intake and transfer
        gamepad_driver.getGamepadButton(gamepad_driver.leftBumper)
                .toggleWhenPressed(
                        new SetIntakeStateCommand(IntakeManager._IntakeState.PICKUP, intakeManager, gamepad_driver),
                        new TransferCommand(intakeManager, outtakeManager));

        //* Toggle bucket position
        gamepad_codriver.getGamepadButton(gamepad_driver.square)
                .toggleWhenActive(new SetBucketPositionCommand(outtakeManager, OuttakeManager._BucketServoState.HIGH),
                        new SetBucketPositionCommand(outtakeManager, OuttakeManager._BucketServoState.LOW));

        //* Toggle speciment claw
        gamepad_codriver.getGamepadButton(gamepad_driver.cross)
                .toggleWhenActive(new SetSpecimentServoPositionCommand(outtakeManager, OuttakeManager._SpecimentServoState.OPEN), new SetSpecimentServoPositionCommand(outtakeManager, OuttakeManager._SpecimentServoState.CLOSED));

        //* Go to deposit settings
        gamepad_driver.getGamepadButton(gamepad_driver.rightBumper)
                .whenPressed(() -> CommandScheduler.getInstance().schedule(new SetOuttakeStateCommand(OuttakeManager._OuttakeState.DEPOSIT, outtakeManager, gamepad_driver)));

        //* Reset IMU
        gamepad_driver.getGamepadButton(gamepad_driver.options)
                .whenPressed(new InstantCommand(() -> drive.pinpoint.resetPosAndIMU()));
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
        ascentManager.loop();
    }

    @Override
    public void stop() {

        driveManager.stopMovementControlThread();

        super.stop();
    }
}
