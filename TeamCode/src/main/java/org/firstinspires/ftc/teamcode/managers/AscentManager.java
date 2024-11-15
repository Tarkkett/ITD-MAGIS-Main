package org.firstinspires.ftc.teamcode.managers;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.gamepad.GamepadEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.State;
import org.firstinspires.ftc.teamcode.commands.low_level.LowerLiftSomeBit;
import org.firstinspires.ftc.teamcode.commands.low_level.PrepLiftForAscentCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.UncoupleLiftPIDCommand;

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
            hardwareManager.liftLeft.setPower(1);
            hardwareManager.liftRight.setPower(1);
        }
        else if (gamepad_codriver.gamepad.cross){
            hardwareManager.liftLeft.setPower(-1);
            hardwareManager.liftRight.setPower(-1);
        }
        else if (gamepad_codriver.gamepad.left_bumper){
            CommandScheduler.getInstance().schedule(
                    new UncoupleLiftPIDCommand(outtakeManager, true)
            );
        }
        else if (gamepad_codriver.gamepad.right_bumper){
            CommandScheduler.getInstance().schedule(
                    new UncoupleLiftPIDCommand(outtakeManager, false)
            );
        }
        else if (gamepad_codriver.gamepad.dpad_down) {
            hardwareManager.legMotor.setPower(-1);
        }
        else if (gamepad_codriver.gamepad.dpad_up) {
            hardwareManager.legMotor.setPower(1);
        }
        else if(gamepad_codriver.gamepad.circle){
            CommandScheduler.getInstance().schedule(new PrepLiftForAscentCommand(intakeManager, outtakeManager));
        }
        else{
            if (outtakeManager.mode == OuttakeManager._LiftMode.MANUAL) {
                hardwareManager.legMotor.setPower(0);
                hardwareManager.liftRight.setPower(0);
                hardwareManager.liftLeft.setPower(0);
            }
        }

    }
}
