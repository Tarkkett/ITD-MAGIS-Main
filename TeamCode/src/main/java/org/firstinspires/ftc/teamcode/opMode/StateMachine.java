package org.firstinspires.ftc.teamcode.opMode;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.commands.SetIntakeStateCommand;
import org.firstinspires.ftc.teamcode.commands.SetOuttakeStateCommand;
import org.firstinspires.ftc.teamcode.commands.TransferCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeTiltServoCommand;
import org.firstinspires.ftc.teamcode.managers.DriveManager;
import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;
import org.firstinspires.ftc.teamcode.managers.Transition;
import org.firstinspires.ftc.teamcode.util.GamepadPlus;
import org.firstinspires.ftc.teamcode.util.State;

import java.util.HashMap;
import java.util.Map;

public class StateMachine implements State<StateMachine._RobotState> {

    private static StateMachine instance;

    public _RobotState robotState = _RobotState.HOME;
    public _RobotState previousState;

    private OuttakeManager outtakeManager;
    private IntakeManager intakeManager;
    private DriveManager driveManager;

    IntakeManager._IntakeState intakeState;
    OuttakeManager._OuttakeState outtakeState;
    DriveManager._DriveState driveState;

    GamepadPlus gamepad_driver;
    GamepadPlus gamepad_codriver;

    private final Map<Transition, Runnable> stateTransitionActions = new HashMap<>();

    Telemetry tel;



    public StateMachine(OuttakeManager outtake, IntakeManager intake, DriveManager drive, Telemetry tel, GamepadPlus gamepad_driver, GamepadPlus gamepad_codriver){
        this.outtakeManager = outtake;
        this.intakeManager = intake;
        this.tel = tel;
        this.gamepad_codriver = gamepad_codriver;
        this.gamepad_driver = gamepad_driver;
        this.driveManager = drive;
    }

    public static StateMachine getInstance(OuttakeManager outtake, IntakeManager intake, DriveManager drive, Telemetry tel, GamepadPlus gamepad_driver, GamepadPlus gamepad_codriver) {
        if (instance == null) {
            instance = new StateMachine(outtake, intake, drive, tel, gamepad_driver, gamepad_codriver);
        }
        return instance;
    }

    @Override
    public void InitializeStateTransitionActions() {
//        stateTransitionActions.put(new Transition(_RobotState.HOME, _RobotState.INTAKE), this::onIntake);
//        stateTransitionActions.put(new Transition(_RobotState.TRANSFER, _RobotState.INTAKE), this::onIntake);
//        stateTransitionActions.put(new Transition(_RobotState.DEPOSIT, _RobotState.INTAKE), this::onIntake);
//        stateTransitionActions.put(new Transition(_RobotState.HOME, _RobotState.DEPOSIT), this::onOuttake);
//        stateTransitionActions.put(new Transition(_RobotState.TRANSFER, _RobotState.DEPOSIT), this::onOuttake);
//        stateTransitionActions.put(new Transition(_RobotState.INTAKE, _RobotState.DEPOSIT), this::onOuttake);
    }

//    private void onOuttake() {
//
//        gamepad_driver.gamepad.rumble(200);
//        gamepad_driver.gamepad.setLedColor(1, 0, 0, 3000);
//        gamepad_codriver.gamepad.setLedColor(1, 0, 0, 3000);
//    }
//
//    private void onIntake() {
//
//        CommandScheduler.getInstance().schedule(new SetOuttakeTiltServoCommand(outtakeManager, OuttakeManager._OuttakeTiltServoState.HIGH));
//        gamepad_driver.gamepad.setLedColor(0, 1, 0, 3000);
//        gamepad_codriver.gamepad.setLedColor(0, 1, 0, 3000);
//    }

    @Override
    public void SetSubsystemState(_RobotState newState) {

        if (robotState != newState) {
            robotState = newState;


            switch (newState) {
                case DEPOSIT:
                    intakeManager.managerState = IntakeManager._IntakeState.IDLE;
                    intakeManager.selectingProcess = false;
                    CommandScheduler.getInstance().schedule(
                            new SequentialCommandGroup(
                                    new SetOuttakeStateCommand(OuttakeManager._OuttakeState.DEPOSIT, outtakeManager, gamepad_driver, gamepad_codriver),
                                    new SetIntakeStateCommand(IntakeManager._IntakeState.HOME, intakeManager, gamepad_driver, gamepad_codriver)
                            )
                    );
                    break;
                case INTAKE:
                    intakeManager.managerState = IntakeManager._IntakeState.INTAKE;
                    outtakeManager.selectingProcess = false;
                    CommandScheduler.getInstance().schedule(
                            new SequentialCommandGroup(
                                    new SetIntakeStateCommand(IntakeManager._IntakeState.INTAKE, intakeManager, gamepad_driver, gamepad_codriver),
                                    new SetOuttakeStateCommand(OuttakeManager._OuttakeState.HOME, outtakeManager, gamepad_driver, gamepad_codriver)


                            )
                    );
                    break;
                case TRANSFER:
                    intakeManager.managerState = IntakeManager._IntakeState.TRANSFER;
                    intakeManager.selectingProcess = false;
                    outtakeManager.selectingProcess = false;
                    CommandScheduler.getInstance().schedule(
                            new SequentialCommandGroup(
                                    new TransferCommand(intakeManager, outtakeManager)
                            )
                    );
                    break;
                case HOME:
                    CommandScheduler.getInstance().schedule(
                            new SequentialCommandGroup(
                                    new SetIntakeStateCommand(IntakeManager._IntakeState.HOME, intakeManager, gamepad_driver, gamepad_codriver),
                                    new SetOuttakeStateCommand(OuttakeManager._OuttakeState.HOME, outtakeManager, gamepad_driver, gamepad_codriver)
                            )
                    );
                    break;
            }

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
        tel.addData("Robot feels like:", robotState);
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
        HOME
    }
}
