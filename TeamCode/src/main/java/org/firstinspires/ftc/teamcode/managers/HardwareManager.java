package org.firstinspires.ftc.teamcode.managers;

import android.graphics.Color;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.drivers.GoBildaPinpointDriver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import dev.frozenmilk.dairy.cachinghardware.CachingDcMotorEx;
import dev.frozenmilk.dairy.cachinghardware.CachingServo;

public class HardwareManager{

    private static final double MOTOR_CACHING_TOLERANCE = 0.005;
    private static final double SERVO_CACHING_TOLERANCE = 0.001;
    private static HardwareManager instance;
    List<LynxModule> hubs;
    Collection<Blinker.Step> blinks;
    Blinker.Step blinkBlue = new Blinker.Step();
    Blinker.Step blinkRed = new Blinker.Step();

    public GoBildaPinpointDriver pinpointDriver;

    CachingDcMotorEx frontLeft;
    CachingDcMotorEx frontRight;
    CachingDcMotorEx backLeft;
    CachingDcMotorEx backRight;

    CachingDcMotorEx liftRight;
    CachingDcMotorEx liftLeft;

    DcMotorEx intake;

    public CachingServo intakeClawSrv;
    public CachingServo intakeTiltSrv;
    public CachingServo intakePitchSrv;
    public CachingServo intakeYawSrv;

    public CachingServo outtakeArmTiltSrvLeft;
    public CachingServo outtakeArmTiltSrvRight;
    public CachingServo outtakeDepositorPitchSrv;
    public CachingServo outtakeDepositorYawSrv;
    public CachingServo outtakeDepositorClawSrv;

    public DigitalChannel outtakeProximitySensor;

    public static int IMU_DATA_SAMPLING_RATE = 10;

    private final HardwareMap hmap;

    public HardwareManager(HardwareMap hmap, boolean isAuto){
        this.hmap = hmap;
    }

    public static HardwareManager getInstance(HardwareMap hmap, boolean isAuto) {
        if (instance == null) {
            instance = new HardwareManager(hmap, isAuto);
        }
        return instance;
    }

    public void InitHw(boolean isAuto){

        //?===============================SETUP================================//

        hubs = this.hmap.getAll(LynxModule.class);

        frontLeft = new CachingDcMotorEx(this.hmap.get(DcMotorEx.class, "frontLeft"));
        frontRight = new CachingDcMotorEx(this.hmap.get(DcMotorEx.class, "frontRight"));
        backLeft = new CachingDcMotorEx(this.hmap.get(DcMotorEx.class, "backLeft"));
        backRight = new CachingDcMotorEx(this.hmap.get(DcMotorEx.class, "backRight"));

        liftLeft = new CachingDcMotorEx(this.hmap.get(DcMotorEx.class, "liftLeft"));
        liftRight = new CachingDcMotorEx(this.hmap.get(DcMotorEx.class, "liftRight"));

        intake = this.hmap.get(DcMotorEx.class, "intakeMotor");

        outtakeProximitySensor = this.hmap.get(DigitalChannel.class, "outtakeProximitySensor");

        intakeClawSrv = new CachingServo(this.hmap.get(Servo.class, "intakeClawServo"));
        intakeTiltSrv = new CachingServo(this.hmap.get(Servo.class, "intakeTiltServo"));
        intakeYawSrv = new CachingServo(this.hmap.get(Servo.class, "intakeYawServo"));
        intakePitchSrv = new CachingServo(this.hmap.get(Servo.class, "intakePitchServo"));

        outtakeArmTiltSrvLeft = new CachingServo(this.hmap.get(Servo.class, "outtakeTiltLeftServo"));
        outtakeArmTiltSrvRight = new CachingServo(this.hmap.get(Servo.class, "outtakeTiltRightServo"));
        outtakeDepositorPitchSrv = new CachingServo(this.hmap.get(Servo.class, "outtakeDepositorPitchServo"));
        outtakeDepositorYawSrv = new CachingServo(this.hmap.get(Servo.class, "outtakeDepositorYawServo"));
        outtakeDepositorClawSrv = new CachingServo(this.hmap.get(Servo.class, "outtakeDepositorClawServo"));

        if (!isAuto) {pinpointDriver = this.hmap.get(GoBildaPinpointDriver.class, "pinpoint");}

        //?===========================CONFIGURATION================================//

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        intake.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        intake.setDirection(DcMotorSimple.Direction.REVERSE);

        liftRight.setDirection(DcMotorSimple.Direction.REVERSE);
        liftLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        liftLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        liftRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        outtakeArmTiltSrvLeft.setDirection(Servo.Direction.REVERSE);

        outtakeProximitySensor.setMode(DigitalChannel.Mode.INPUT);

//        if (!isAuto) {pinpointDriver.resetPosAndIMU();}

        //?========================QUALITY FUNCTIONS===============================//

        setupBulkReading();
        setHardwareCachingTolerance();
        blink();

    }

    private void setHardwareCachingTolerance() {
        intakePitchSrv.setCachingTolerance(SERVO_CACHING_TOLERANCE);
        intakeYawSrv.setCachingTolerance(SERVO_CACHING_TOLERANCE);
        intakeTiltSrv.setCachingTolerance(SERVO_CACHING_TOLERANCE);
        intakeClawSrv.setCachingTolerance(SERVO_CACHING_TOLERANCE);

        outtakeArmTiltSrvLeft.setCachingTolerance(SERVO_CACHING_TOLERANCE);
        outtakeArmTiltSrvRight.setCachingTolerance(SERVO_CACHING_TOLERANCE);
        outtakeDepositorPitchSrv.setCachingTolerance(SERVO_CACHING_TOLERANCE);
        outtakeDepositorYawSrv.setCachingTolerance(SERVO_CACHING_TOLERANCE);
        outtakeDepositorClawSrv.setCachingTolerance(SERVO_CACHING_TOLERANCE);

        frontLeft.setCachingTolerance(MOTOR_CACHING_TOLERANCE);
        backLeft.setCachingTolerance(MOTOR_CACHING_TOLERANCE);
        frontRight.setCachingTolerance(MOTOR_CACHING_TOLERANCE);
        backRight.setCachingTolerance(MOTOR_CACHING_TOLERANCE);
    }

    private void setupBulkReading() {
        for(LynxModule hub : hubs){
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
    }

    @SuppressWarnings("unused")
    public void clearCache(){
        for(LynxModule hub : hubs){
            hub.clearBulkCache();
        }
    }

    public void setIndicatorLEDs(int color){
        for(LynxModule hub : hubs){
            hub.popPattern();
            hub.setConstant(color);
        }
    }

    public void blink() {
        blinks = new ArrayList<>();

        blinkBlue.setColor(Color.BLUE);
        blinkBlue.setDuration(1, TimeUnit.SECONDS);
        blinkRed.setColor(Color.RED);
        blinkRed.setDuration(1, TimeUnit.SECONDS);

        blinks.add(blinkBlue);
        blinks.add(blinkRed);

        for(LynxModule hub : hubs){
            hub.setPattern(blinks);
        }
    }

    public void stopDriveMotors() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }

    public void setOuttakeArmTiltServos(float pos){
        outtakeArmTiltSrvLeft.setPosition(pos);
        outtakeArmTiltSrvRight.setPosition(pos);
    }

    public void MotorsFullThrottleForOneSec(DriveManager driveManager) {
        ElapsedTime timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

        driveManager.Lock();

        while(timer.milliseconds() <= 30){
            frontLeft.setPower(1);
            frontRight.setPower(1);
            backLeft.setPower(1);
            backRight.setPower(1);
        }

        driveManager.Unlock();

    }
}
