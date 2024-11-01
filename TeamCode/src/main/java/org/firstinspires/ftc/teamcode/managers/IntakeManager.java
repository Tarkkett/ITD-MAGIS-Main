package org.firstinspires.ftc.teamcode.managers;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.gamepad.GamepadEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.State;
import org.firstinspires.ftc.teamcode.drivers.C_PID;

import java.util.HashMap;
import java.util.Map;

@Config
public class IntakeManager implements State<IntakeManager._IntakeState> {

    private final HardwareManager hardwareManager;
    private Telemetry telemetry;
    private final GamepadEx gamepad_driver;

    public static int targetPos = 100;
    public static int currentPos = 0;

    private _IntakeState state = _IntakeState.HOME;
    private _IntakeState previousState = _IntakeState.HOME;

    private int RETRACTED_POS = 0;
    private int EXTENDED_POS = 800;
    private int TRANSFER_POS = 550;

    C_PID controller = new C_PID(0.02, 0.0004, 0.002);

    private final Map<Transition, Runnable> stateTransitionActions = new HashMap<>();

    public IntakeManager(HardwareManager hardwareManager, Telemetry telemetry, GamepadEx gamepadDriver) {
        this.gamepad_driver = gamepadDriver;
        this.telemetry = telemetry;
        this.hardwareManager = hardwareManager;
    }

    @Override
    public void SetSubsystemState(_IntakeState state) {

    }

    @Override
    public _IntakeState GetSubsystemState() {
        return state;
    }

    @Override
    public void loop() {
        FtcDashboard dashboard = FtcDashboard.getInstance();
        telemetry = dashboard.getTelemetry();
        currentPos = hardwareManager.intake.getCurrentPosition();
        telemetry.addData("CurrentPos", currentPos);
        telemetry.addData("TargetPos", targetPos);
        telemetry.update();

        double power = controller.update(targetPos, currentPos);
        hardwareManager.intake.setPower(power);

    }

    @Override
    public void InitializeStateTransitionActions() {
        stateTransitionActions.put(new Transition(_IntakeState.HOME, _IntakeState.PICKUP), this::onExtend);
    }

    private void onExtend() {

    }

    public void update(_SlideState targetState) {
        switch (targetState){
            case EXTENDED:
                targetPos = EXTENDED_POS;
                break;
            case TRANSFER:
                targetPos = TRANSFER_POS;
                break;
            case RETRACTED:
                targetPos = RETRACTED_POS;
                break;
        }
    }

    public void update(_BroomState targetState) {
        switch (targetState){
            case STOPPED:
                hardwareManager.broomServo.setPosition(0.5);
                break;
            case INTAKEING:
                hardwareManager.broomServo.setPosition(1);
                break;
            case TRANSFERING:
                hardwareManager.broomServo.setPosition(-1);
                break;
        }
    }

    public void update(_TiltServoState targetState){
        switch (targetState){
            case RAISED:
                hardwareManager.intakeTiltServo.setPosition(0.65f);
                break;
            case LOWERED:
                hardwareManager.intakeTiltServo.setPosition(0f);
                break;
        }
    }

    public enum _IntakeState {
        PICKUP,
        HOME,
        TRANSFER,
    }
    public enum _SlideState {
        EXTENDED,
        RETRACTED,
        TRANSFER,
    }
    public enum _BroomState {
        INTAKEING,
        TRANSFERING,
        STOPPED
    }
    public enum _TiltServoState{
        RAISED,
        LOWERED
    }
}