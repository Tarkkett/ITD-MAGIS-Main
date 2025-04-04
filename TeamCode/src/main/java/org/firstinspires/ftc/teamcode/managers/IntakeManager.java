package org.firstinspires.ftc.teamcode.managers;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.drivers.C_PID;
import org.firstinspires.ftc.teamcode.util.GamepadPlus;
import org.firstinspires.ftc.teamcode.util.PID_PARAMS;

@SuppressWarnings("rawtypes")
@Config
public class IntakeManager implements Manager<IntakeManager._IntakeState> {

    public boolean isLowered() {
        return hardwareManager.intakeTiltSrv.getPosition() > 0.5f;
    }

    public _SlideState GetSlideState() {
        if (encoderPos < 100){
            return _SlideState.RETRACTED;
        }
        else return _SlideState.EXTENDED;
    }

    public _YawMode GetYawMode() {
        return yawMode;
    }
    public void SetYawMode(_YawMode mode) {
        yawMode = mode;
    }

    public interface Positionable {
        float getPosition();
    }

    private static final int MIN_SLIDE_RETRACT = 0;
    private static final int MAX_SLIDE_EXTEND = 750;

    private final HardwareManager hardwareManager;
    private Telemetry telemetry;

    private GamepadPlus gamepad_driver;
    private GamepadPlus gamepad_codriver;

    public int targetPosition;
    public int encoderPos = 0;

    public _IntakeState managerState = _IntakeState.HOME;
    private _YawMode yawMode = _YawMode.MANUAL;

    public PID_PARAMS params = new PID_PARAMS(0.01, 0.002, 0.0004, 5);
    C_PID controller = new C_PID(params);

    public IntakeManager(HardwareManager hardwareManager, Telemetry telemetry, GamepadPlus gamepadDriver, GamepadPlus gamepadCodriver) {
        this.gamepad_driver = gamepadDriver;
        this.telemetry = telemetry;
        this.hardwareManager = hardwareManager;
        this.gamepad_codriver = gamepadCodriver;
    }

    private void updateTelemetry() {
        telemetry.addData("CurrentPos", encoderPos);
        telemetry.addData("TargetPos", targetPosition);
        telemetry.update();
    }

    @Override
    public void loop() {
        controller.tune(params);

        FtcDashboard dashboard = FtcDashboard.getInstance();
        telemetry = dashboard.getTelemetry();
        encoderPos = hardwareManager.intake.getCurrentPosition();

        updateTelemetry();

        double power = controller.update(targetPosition, encoderPos);
        hardwareManager.intake.setPower(power);
    }

    @Override
    public _IntakeState GetManagerState() {
        if (encoderPos > 150 && managerState == _IntakeState.INTAKE){
            return _IntakeState.INTAKE;
        }
        if (managerState == _IntakeState.HOME && encoderPos < 150){
            return  _IntakeState.HOME;
        }
        else{
            return _IntakeState.IDLE;
        }
    }

    public void update(_SlideState targetState) {
        targetPosition = (int) targetState.getPosition();
    }

    public void update(_ClawState targetState) {
        hardwareManager.intakeGripSrv.setPosition(targetState.getPosition());
    }

    public void update(_TiltServoState targetState){
        hardwareManager.intakeTiltSrv.setPosition(targetState.getPosition());
    }

    public void update(_PitchServoState targetState){
        hardwareManager.intakePitchSrv.setPosition(targetState.getPosition());
    }

    public void update(_YawServoState targetState){
        if (GetYawMode() == _YawMode.AUTO) {
            hardwareManager.intakeYawSrv.setPosition(targetState.getPosition());
        }
    }

    public void moveSlide(int i) {
        int newPos = targetPosition + i;
        if (newPos < MAX_SLIDE_EXTEND && newPos > MIN_SLIDE_RETRACT){
            targetPosition = newPos;
        }
        else{
            gamepad_codriver.gamepad.rumbleBlips(3);
        }
    }

    public void controlYawAngle(double yawServoAngle) {
        if (GetYawMode() == _YawMode.MANUAL) {
            hardwareManager.intakeYawSrv.setPosition(yawServoAngle);
        }
    }

    public enum _SlideState implements Positionable {
        EXTENDED    (650),
        TRANSFER    (380),
        RETRACTED   (50),
        TRANSFER_WAIT(580);

        private final float position;

        _SlideState(float position) {
            this.position = position;
        }

        @Override
        public float getPosition() {
            return position;
        }
    }
    public enum _ClawState implements Positionable {
        OPEN(0.88f),
        CLOSED(0.55f);

        private final float position;

        _ClawState(float position) {
            this.position = position;
        }

        @Override
        public float getPosition() {
            return position;
        }
    }
    public enum _TiltServoState implements Positionable{
        TRANSFER(0.45f),
        LOWERED (0f),
        AIMING(0.1f),
        PACKED(0.7f),
        VERTICAL(0.3f);

        private final float position;

        _TiltServoState(float position) {
            this.position = position;
        }

        @Override
        public float getPosition() {
            return position;
        }

    }

    public enum _PitchServoState implements Positionable{
        TRANSFER(0.84f),
        LOWERED (0.18f),
        AIMING(0.1f),
        PACKED(0.3f);
        private final float position;

        _PitchServoState(float position) {
            this.position = position;
        }

        @Override
        public float getPosition() {
            return position;
        }

    }

    public enum _YawServoState implements Positionable{
        TRANSFER(0.36f),
        HOME(0.75f);

        private final float position;

        _YawServoState(float position) {
            this.position = position;
        }

        @Override
        public float getPosition() {
            return position;
        }

    }

    public enum _YawMode{
        MANUAL,
        AUTO
    }

    public enum _IntakeState{
        INTAKE,
        IDLE,
        TRANSFER,
        CALIBRATION,
        HOME
    }
}