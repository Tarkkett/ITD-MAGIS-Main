package org.firstinspires.ftc.teamcode.managers;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.gamepad.GamepadEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.State;
import org.firstinspires.ftc.teamcode.commands.low_level.LowerLiftSomeBit;
import org.firstinspires.ftc.teamcode.commands.low_level.PrepLiftForAscentCommand;

public class AscentManager implements State<DriveManager.DriveState> {

    HardwareManager hardwareManager;

    OuttakeManager outtakeManager;

    IntakeManager intakeManager;
    Telemetry telemetry;
    GamepadEx gamepad_codriver;

    public AscentManager(HardwareManager hardwareManager, Telemetry telemetry, GamepadEx gamepadCoDriver, OuttakeManager outtakeManager, IntakeManager intakeManager) {
        this.hardwareManager = hardwareManager;
        this.telemetry = telemetry;
        this.gamepad_codriver = gamepadCoDriver;
        this.outtakeManager = outtakeManager;
        this.intakeManager = intakeManager;
    }

    @Override
    public void InitializeStateTransitionActions() {

    }

    @Override
    public void SetSubsystemState(DriveManager.DriveState state) {

    }

    @Override
    public DriveManager.DriveState GetSubsystemState() {
        return null;
    }

    @Override
    public void loop() {
        if (gamepad_codriver.gamepad.triangle){
            CommandScheduler.getInstance().schedule(
                    new LowerLiftSomeBit(outtakeManager, 150)
            );
        }
        else if (gamepad_codriver.gamepad.cross){
            CommandScheduler.getInstance().schedule(
                    new LowerLiftSomeBit(outtakeManager, -150)
            );
        } else if (gamepad_codriver.gamepad.dpad_down) {
            hardwareManager.legMotor.setPower(-0.6);
        }
        else if (gamepad_codriver.gamepad.dpad_up) {
            hardwareManager.legMotor.setPower(0.6);
        }
        else if(gamepad_codriver.gamepad.circle){
            CommandScheduler.getInstance().schedule(new PrepLiftForAscentCommand(intakeManager, outtakeManager));
        }
        else hardwareManager.legMotor.setPower(0);
    }
}
