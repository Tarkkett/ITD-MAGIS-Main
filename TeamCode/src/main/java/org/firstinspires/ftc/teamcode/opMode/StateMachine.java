package org.firstinspires.ftc.teamcode.opMode;

import com.acmerobotics.dashboard.FtcDashboard;
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
import org.firstinspires.ftc.teamcode.managers.testing.HardwareTestManager;
import org.firstinspires.ftc.teamcode.util.GamepadPlus;
import org.firstinspires.ftc.teamcode.util.State;

public class StateMachine implements State<StateMachine._RobotState> {
    public _RobotState robotState = _RobotState.HOME;

    private OuttakeManager outtakeManager;
    private IntakeManager intakeManager;
    private DriveManager driveManager;
    private HardwareTestManager testManager;

    IntakeManager._IntakeState intakeState;
    OuttakeManager._OuttakeState outtakeState;
    DriveManager._DriveState driveState;

    GamepadPlus gamepad_driver;
    GamepadPlus gamepad_codriver;

    HardwareManager hw;

    Telemetry tel;

    public StateMachine(OuttakeManager outtake, IntakeManager intake, DriveManager drive, Telemetry tel, GamepadPlus gamepad_driver, GamepadPlus gamepad_codriver, HardwareManager hw, HardwareTestManager testManager){
        this.outtakeManager = outtake;
        this.intakeManager = intake;
        this.tel = tel;
        this.gamepad_codriver = gamepad_codriver;
        this.gamepad_driver = gamepad_driver;
        this.driveManager = drive;
        this.hw = hw;
        this.testManager = testManager;
    }

    @Override
    public void SetSubsystemState(_RobotState newState) {

        switch (newState) {
            case DEPOSIT:
                if (robotState != _RobotState.DEPOSIT){
                    robotState = _RobotState.DEPOSIT;
                    CommandScheduler.getInstance().schedule(
                            new ParallelCommandGroup(
                                    new SetIntakeStateCommand(IntakeManager._IntakeState.HOME, intakeManager, outtakeManager, this, gamepad_driver, gamepad_codriver),
                                    new SetOuttakeStateCommand(OuttakeManager._OuttakeState.PICKUP, outtakeManager, gamepad_driver, gamepad_codriver, driveManager, hw, this, intakeManager)
                            )
                    );
                } else {
                    gamepad_codriver.Warn();
                }
                break;
            case INTAKE:
                if (robotState != _RobotState.INTAKE) {
                    robotState = _RobotState.INTAKE;
                    CommandScheduler.getInstance().schedule(
                            new ParallelCommandGroup(
                                    new SetOuttakeStateCommand(OuttakeManager._OuttakeState.HOME, outtakeManager, gamepad_driver, gamepad_codriver, driveManager, hw, this, intakeManager),
                                    new SetIntakeStateCommand(IntakeManager._IntakeState.INTAKE, intakeManager, outtakeManager, this, gamepad_driver, gamepad_codriver)
                            )
                    );
                } else {
                    gamepad_codriver.Warn();
                }
                break;
            case TRANSFER:
                if (robotState == _RobotState.INTAKE) {
                    robotState = _RobotState.TRANSFER;
                    CommandScheduler.getInstance().schedule(
                            new TransferCommand(intakeManager, outtakeManager)
                    );
                } else {
                    gamepad_codriver.Warn();
                }
                break;
            case HOME:
                robotState = _RobotState.HOME;
                CommandScheduler.getInstance().schedule(
                        new ParallelCommandGroup(
                                new SetIntakeStateCommand(IntakeManager._IntakeState.HOME, intakeManager, outtakeManager, this, gamepad_driver, gamepad_codriver),
                                new SetOuttakeStateCommand(OuttakeManager._OuttakeState.HOME, outtakeManager, gamepad_driver, gamepad_codriver, driveManager, hw, this, intakeManager)
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
    public _RobotState GetSystemState() {
        return robotState;
    }

    @Override
    public void loop() {
        StateCheck();
        if (robotState == _RobotState.CALIBRATION) {
            FtcDashboard dashboard = FtcDashboard.getInstance();
            Telemetry telemetry = dashboard.getTelemetry();
            telemetry.addData("Intake feels like:", intakeState);
            telemetry.addData("Outtake feels like:", outtakeState);
            telemetry.addData("Drivetrain feels like:", driveState);
            telemetry.addData("Robot feels like:", GetSystemState());
            telemetry.addData("Lift target pos", outtakeManager.GetLiftTargetPos());
            telemetry.update();

            testManager.loop();
        }
    }

    private void StateCheck() {
        intakeState = intakeManager.GetManagerState();
        outtakeState = outtakeManager.GetManagerState();
        driveState = driveManager.GetManagerState();
    }

    public void SetDrivetrainState(DriveManager._DriveState driveState) {
        switch (driveState){
            case LOCKED:
                driveManager.Lock();
                break;
            case UNLOCKED:
                driveManager.Unlock();
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
