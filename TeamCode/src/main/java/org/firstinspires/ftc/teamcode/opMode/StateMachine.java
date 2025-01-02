package org.firstinspires.ftc.teamcode.opMode;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.commands.SetIntakeStateCommand;
import org.firstinspires.ftc.teamcode.commands.SetOuttakeStateCommand;
import org.firstinspires.ftc.teamcode.commands.TransferCommand;
import org.firstinspires.ftc.teamcode.managers.DriveManager;
import org.firstinspires.ftc.teamcode.managers.HardwareManager;
import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;
import org.firstinspires.ftc.teamcode.util.GamepadPlus;
import org.firstinspires.ftc.teamcode.util.State;

public class StateMachine implements State<StateMachine._RobotState> {

    private static StateMachine instance;
    public _RobotState robotState = _RobotState.HOME;

    private OuttakeManager outtakeManager;
    private IntakeManager intakeManager;
    private DriveManager driveManager;

    IntakeManager._IntakeState intakeState;
    OuttakeManager._OuttakeState outtakeState;
    DriveManager._DriveState driveState;

    GamepadPlus gamepad_driver;
    GamepadPlus gamepad_codriver;

    HardwareManager hw;

    Telemetry tel;

    public StateMachine(OuttakeManager outtake, IntakeManager intake, DriveManager drive, Telemetry tel, GamepadPlus gamepad_driver, GamepadPlus gamepad_codriver, HardwareManager hw){
        this.outtakeManager = outtake;
        this.intakeManager = intake;
        this.tel = tel;
        this.gamepad_codriver = gamepad_codriver;
        this.gamepad_driver = gamepad_driver;
        this.driveManager = drive;
        this.hw = hw;
    }

    public static StateMachine getInstance(OuttakeManager outtake, IntakeManager intake, DriveManager drive, Telemetry tel, GamepadPlus gamepad_driver, GamepadPlus gamepad_codriver, HardwareManager hw) {
        if (instance == null) {
            instance = new StateMachine(outtake, intake, drive, tel, gamepad_driver, gamepad_codriver, hw);
        }
        return instance;
    }

    @Override
    public void SetSubsystemState(_RobotState newState) {

        switch (newState) {
            case DEPOSIT:
                robotState = _RobotState.DEPOSIT;
                intakeManager.selectingProcess = false;
                CommandScheduler.getInstance().schedule(
                    new ParallelCommandGroup(
                        new SetIntakeStateCommand(IntakeManager._IntakeState.HOME, intakeManager, gamepad_driver, gamepad_codriver),
                        new SetOuttakeStateCommand(OuttakeManager._OuttakeState.DEPOSIT, outtakeManager, gamepad_driver, gamepad_codriver)
                    )
                );
                break;
            case INTAKE:
                robotState = _RobotState.INTAKE;
                outtakeManager.selectingProcess = false;
                CommandScheduler.getInstance().schedule(
                    new ParallelCommandGroup(
                        new SetOuttakeStateCommand(OuttakeManager._OuttakeState.PICKUP, outtakeManager, gamepad_driver, gamepad_codriver),
                        new SetIntakeStateCommand(IntakeManager._IntakeState.INTAKE, intakeManager, gamepad_driver, gamepad_codriver)
                    )
                );
                break;
            case TRANSFER:
                robotState = _RobotState.TRANSFER;
                intakeManager.selectingProcess = false;
                outtakeManager.selectingProcess = false;
                CommandScheduler.getInstance().schedule(
                    new TransferCommand(intakeManager, outtakeManager)
                );
                break;
            case HOME:
                robotState = _RobotState.HOME;
                CommandScheduler.getInstance().schedule(
                        new ParallelCommandGroup(
                                new SetIntakeStateCommand(IntakeManager._IntakeState.HOME, intakeManager, gamepad_driver, gamepad_codriver),
                                new SetOuttakeStateCommand(OuttakeManager._OuttakeState.HOME, outtakeManager, gamepad_driver, gamepad_codriver)
                        )
                );
                break;
            case CALIBRATION:
                intakeManager.managerState = IntakeManager._IntakeState.CALIBRATION;
                outtakeManager.managerState = OuttakeManager._OuttakeState.CALIBRATION;
                break;
        }
    }

    @Override
    public _RobotState GetSubsystemState() {
        return robotState;
    }

    @Override
    public void loop() {
        StateCheck();
        tel.addData("Intake feels like:", intakeState);
        tel.addData("Outtake feels like:", outtakeState);
        tel.addData("Drivetrain feels like:", driveState);
        tel.addData("Robot feels like:", GetSubsystemState());
        tel.addData("Speciment servo pos", hw.outtakeExtendoServo.getPosition());
        tel.addData("CanHome", outtakeManager.canHome());
        tel.update();
    }

    private void StateCheck() {
        intakeState = intakeManager.GetManagerState();
        outtakeState = outtakeManager.GetManagerState();
        driveState = driveManager.GetManagerState();
    }

    public void SetSubsystemState(DriveManager._DriveState driveState) {
        switch (driveState){
            case LOCKED:
                driveManager.onLocked();
                break;
            case UNLOCKED:
                driveManager.onUnlocked();
                break;
        }
    }

    public enum _RobotState{
        INTAKE,
        DEPOSIT,
        TRANSFER,
        CALIBRATION,
        HOME
    }
}
