package org.firstinspires.ftc.teamcode.managers;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.drivers.C_PID;
import org.firstinspires.ftc.teamcode.util.GamepadPlus;
import org.firstinspires.ftc.teamcode.util.PID_PARAMS;

@SuppressWarnings("rawtypes")
@Config
public class IntakeManager implements Manager<IntakeManager._IntakeState> {

    private static final int MIN_SLIDE_RETRACT = 200;
    private static final int MAX_SLIDE_EXTEND = 850;

    private final HardwareManager hardwareManager;
    public boolean selectingProcess = false;
    private Telemetry telemetry;
    private GamepadPlus gamepad_driver;

    public int targetPosition;
    public int encoderPos = 0;

    public _IntakeState managerState = _IntakeState.HOME;

    private boolean isAutoLoop = true;

    public PID_PARAMS params = new PID_PARAMS(0.02, 0.0, 0.0006, 5);
    C_PID controller = new C_PID(params);

    public IntakeManager(HardwareManager hardwareManager, Telemetry telemetry, GamepadPlus gamepadDriver) {
        this.gamepad_driver = gamepadDriver;
        this.telemetry = telemetry;
        this.hardwareManager = hardwareManager;
    }

    @Override
    public void loop() {

        controller.tune(params);

        FtcDashboard dashboard = FtcDashboard.getInstance();
        telemetry = dashboard.getTelemetry();
        encoderPos = hardwareManager.intake.getCurrentPosition();
        telemetry.addData("CurrentPos", encoderPos);
        telemetry.addData("TargetPos", targetPosition);
        telemetry.update();


        double power = controller.update(targetPosition, encoderPos);
        hardwareManager.intake.setPower(power);

    }

    @Override
    public _IntakeState GetManagerState() {
        if (selectingProcess && encoderPos > 150 && managerState == _IntakeState.INTAKE){
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
        switch (targetState){
            case EXTENDED:
                targetPosition = (int) _SlideState.EXTENDED.getPosition();
                break;
            case TRANSFER:
                targetPosition = (int) _SlideState.TRANSFER.getPosition();
                break;
            case TRANSFER_WAIT:
                targetPosition = (int) _SlideState.TRANSFER_WAIT.getPosition();
                break;
            case RETRACTED:
                targetPosition = (int) _SlideState.RETRACTED.getPosition();
                break;
        }
    }

    public void update(_GripState targetState) {
        switch (targetState){
            case GRIP:
                hardwareManager.gripServo.setPosition(_GripState.GRIP.getPosition());
                break;
            case RELEASE:
                hardwareManager.gripServo.setPosition(_GripState.RELEASE.getPosition());
                break;
        }
    }

    public void update(_TiltServoState targetState){
        switch (targetState){
            case TRANSFER:
                hardwareManager.intakeTiltServo.setPosition(_TiltServoState.TRANSFER.getPosition());
                break;
            case LOWERED:
                hardwareManager.intakeTiltServo.setPosition(_TiltServoState.LOWERED.getPosition());
                break;
            case AIMING:
                hardwareManager.intakeTiltServo.setPosition(_TiltServoState.AIMING.getPosition());
                break;
            case PACKED:
                hardwareManager.intakeTiltServo.setPosition(_TiltServoState.PACKED.getPosition());
                break;
            case CLEARED:
                hardwareManager.intakeTiltServo.setPosition(_TiltServoState.CLEARED.getPosition());
                break;
            case AIMING_UPPER:
                hardwareManager.intakeTiltServo.setPosition(_TiltServoState.AIMING_UPPER.getPosition());
                break;

        }
    }

    public void update(_YawServoState targetState, double i){
        switch (targetState){
            case TRANSFER:
                hardwareManager.yawServo.setPosition(_YawServoState.TRANSFER.getPosition());
                break;
            case PICKUP_DEFAULT:
                hardwareManager.yawServo.setPosition(_YawServoState.PICKUP_DEFAULT.getPosition());
                break;
            case AUTO_1:
                hardwareManager.yawServo.setPosition(_YawServoState.AUTO_1.getPosition());
                break;
            case AUTO_2:
                hardwareManager.yawServo.setPosition(_YawServoState.AUTO_2.getPosition());
                break;
            case AUTO_3:
                hardwareManager.yawServo.setPosition(_YawServoState.AUTO_3.getPosition());
                break;
            case MANUAL:
                double currentPos = hardwareManager.yawServo.getPosition();
                hardwareManager.yawServo.setPosition(currentPos + i);
                break;

        }
    }

    public void moveSlide(int i) {
        int newPos = targetPosition + i;
        if (newPos < MAX_SLIDE_EXTEND && newPos > MIN_SLIDE_RETRACT){
            targetPosition = newPos;
        }
        else{
            gamepad_driver.gamepad.rumbleBlips(3);
        }
    }

    public void controlYawAngle(double yawServoAngle) {
        if (selectingProcess) {
            telemetry.addData("Yaw servo", yawServoAngle);
            telemetry.update();
            hardwareManager.yawServo.setPosition(yawServoAngle);
        }
    }

    public enum _SlideState {
        EXTENDED    (550),
        TRANSFER    (400),
        RETRACTED   (10),
        TRANSFER_WAIT(340);

        private final float position;

        _SlideState(float position) {
            this.position = position;
        }

        public float getPosition() {
            return position;
        }
    }
    public enum _GripState {
        RELEASE(0.1f),
        GRIP(0.8f);

        private final float position;

        _GripState(float position) {
            this.position = position;
        }

        public float getPosition() {
            return position;
        }
    }
    public enum _TiltServoState{
        TRANSFER(0.36f),
        LOWERED (0.95f),
        AIMING(0.87f),
        PACKED(0.25f),
        CLEARED(0.5f),
        AIMING_UPPER(0.8f);

        private final float position;

        _TiltServoState(float position) {
            this.position = position;
        }

        public float getPosition() {
            return position;
        }

    }
    public enum _YawServoState{
        TRANSFER(0.33f),
        PICKUP_DEFAULT(0.85f),
        //Increment for manual
        MANUAL(0.1f),
        AUTO_1(0.62f),
        AUTO_2(0.685f),
        AUTO_3(0.74f);

        private final float position;

        _YawServoState(float position) {
            this.position = position;
        }

        public float getPosition() {
            return position;
        }

    }

    public enum _IntakeState{
        INTAKE,
        IDLE,
        TRANSFER,
        CALIBRATION,
        HOME
    }

    public Action LoopLift(){
        return new Action(){
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket){
                if (isAutoLoop) {
                    loop();
                }
                else{ return false;}

                return true;
            }
        };
    }

    public Action DriveLift(int position){
        return new Action(){

            private boolean initialized = false;
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!initialized){
                    targetPosition = position;
                    initialized = true;
                }
                return false;

            }
        };
    }

    public Action StopLift() {
        return new Action(){
            private boolean initialize = false;
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!initialize){
                    isAutoLoop = false;
                    initialize = true;
                }
                return false;
            }
        };
    }

    public Action TiltAction(_TiltServoState state) {
        return new Action(){
            private boolean initialize = false;
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!initialize){
                    update(state);
                    initialize = true;
                }
                return false;
            }
        };
    }
    public Action YawAction(_YawServoState state) {
        return new Action(){
            private boolean initialize = false;
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!initialize){
                    update(state, 0);
                    initialize = true;
                }
                return false;
            }
        };
    }
    public Action GripAction(_GripState state) {
        return new Action(){
            private boolean initialize = false;
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!initialize){
                    update(state);
                    initialize = true;
                }
                return false;
            }
        };
    }
}