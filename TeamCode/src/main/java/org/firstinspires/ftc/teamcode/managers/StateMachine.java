package org.firstinspires.ftc.teamcode.managers;

import org.firstinspires.ftc.robotcore.external.Telemetry;
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
    
    

    Telemetry tel;


    public StateMachine(OuttakeManager outtake, IntakeManager intake, DriveManager drive, Telemetry tel){
        this.outtakeManager = outtake;
        this.intakeManager = intake;
        this.tel = tel;
    }

    public static StateMachine getInstance(OuttakeManager outtake, IntakeManager intake, DriveManager drive, Telemetry tel) {
        if (instance == null) {
            instance = new StateMachine(outtake, intake, drive, tel);
        }
        return instance;
    }

    @Override
    public void InitializeStateTransitionActions() {

    }

    @Override
    public void SetSubsystemState(_RobotState state) {

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
            case UNLOCKED:
                driveManager.onUnlocked();
        }
    }

    public enum _RobotState{
        INTAKE,
        DEPOSIT,
        TRANSFER,
        HOME
    }
}
