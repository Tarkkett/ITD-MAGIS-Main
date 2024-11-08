package org.firstinspires.ftc.teamcode.managers;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.State;
import org.firstinspires.ftc.teamcode.commands.low_level.SetTiltServoPosCommand;
import org.firstinspires.ftc.teamcode.drivers.C_PID;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
@Config
public class IntakeManager implements State<IntakeManager._IntakeState> {

    private static final int MIN_SLIDE_RETRACT = 500;
    private static final int MAX_SLIDE_EXTEND = 950;

    private final HardwareManager hardwareManager;
    public boolean isSelectingIntakePosition = false;
    private Telemetry telemetry;
    private final GamepadEx gamepad_driver;

    public static int targetPos = 100;
    public static int currentPos = 0;

    private _IntakeState state = _IntakeState.HOME;

    C_PID controller = new C_PID(0.02, 0.0004, 0.002);

    private final Map<Transition, Runnable> stateTransitionActions = new HashMap<>();

    public IntakeManager(HardwareManager hardwareManager, Telemetry telemetry, GamepadEx gamepadDriver) {
        this.gamepad_driver = gamepadDriver;
        this.telemetry = telemetry;
        this.hardwareManager = hardwareManager;

        targetPos = (int) _SlideState.RETRACTED.getPosition();
    }

    @Override
    public void SetSubsystemState(_IntakeState newState) {
        if (state != newState) {
            _IntakeState previousState = state;
            state = newState;

            Transition transition = new Transition(previousState, newState);
            Runnable action = stateTransitionActions.get(transition);
            if (action != null) {
                action.run();
            }

            telemetry.addData("Intake State Changed:", newState);
            telemetry.update();
        }
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
                targetPos = (int) _SlideState.EXTENDED.getPosition();
                break;
            case TRANSFER:
                targetPos = (int) _SlideState.TRANSFER.getPosition();
                break;
            case RETRACTED:
                targetPos = (int) _SlideState.RETRACTED.getPosition();
                break;
        }
    }

    public void update(_BroomState targetState) {
        switch (targetState){
            case STOPPED:
                hardwareManager.broomServo.setPosition(_BroomState.STOPPED.getPosition());
                break;
            case INTAKEING:
                hardwareManager.broomServo.setPosition(_BroomState.INTAKEING.getPosition());
                break;
            case TRANSFERING:
                hardwareManager.broomServo.setPosition(_BroomState.TRANSFERING.getPosition());
                break;
        }
    }

    public void update(_TiltServoState targetState){
        switch (targetState){
            case RAISED:
                hardwareManager.intakeTiltServo.setPosition(_TiltServoState.RAISED.getPosition());
                break;
            case LOWERED:
                hardwareManager.intakeTiltServo.setPosition(_TiltServoState.LOWERED.getPosition());
                break;
            case MID:
                hardwareManager.intakeTiltServo.setPosition(_TiltServoState.MID.getPosition());
                break;
            case PACKED:
                hardwareManager.intakeTiltServo.setPosition(_TiltServoState.PACKED.getPosition());
                break;
        }
    }

    public void moveSlide(int i) {
        int newPos = targetPos + i;
        if (newPos < MAX_SLIDE_EXTEND && newPos > MIN_SLIDE_RETRACT){
            targetPos = newPos;
        }
        else{
            gamepad_driver.gamepad.rumbleBlips(3);
        }
    }

    @SuppressWarnings("unused")
    public enum _IntakeState {
        PICKUP,
        HOME,
        TRANSFER
    }
    public enum _SlideState {
        EXTENDED    (550),
        TRANSFER    (330),
        RETRACTED   (10);

        private final float position;

        _SlideState(float position) {
            this.position = position;
        }

        public float getPosition() {
            return position;
        }
    }
    public enum _BroomState {
        INTAKEING   (1),
        TRANSFERING (-1),
        STOPPED     (0.5f);

        private final float position;

        _BroomState(float position) {
            this.position = position;
        }

        public float getPosition() {
            return position;
        }
    }
    public enum _TiltServoState{
        RAISED  (0.65f),
        LOWERED (0f),
        MID (0.25f),
        PACKED(0.75f);

        private final float position;

        _TiltServoState(float position) {
            this.position = position;
        }

        public float getPosition() {
            return position;
        }

    }
}