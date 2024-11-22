package org.firstinspires.ftc.teamcode.managers;

import android.graphics.Color;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HardwareManager{

    private static HardwareManager instance;

    List<LynxModule> hubs;
    Collection<Blinker.Step> blinks;
    Blinker.Step blinkBlue = new Blinker.Step();
    Blinker.Step blinkRed = new Blinker.Step();

    DcMotorEx frontLeft;
    DcMotorEx legMotor;
    DcMotorEx frontRight;
    DcMotorEx backLeft;
    DcMotorEx backRight;

    DcMotorEx liftRight;
    DcMotorEx liftLeft;

    DcMotorEx intake;

    public Servo gripServo;
    public Servo intakeTiltServo;
    public Servo yawServo;

    public Servo outtakeTiltServo;
    public Servo specimentServo;
    public Servo outtakeClawServo;

    public static int IMU_DATA_SAMPLING_RATE = 10;

    private final HardwareMap hmap;

    public HardwareManager(HardwareMap hmap){
        this.hmap = hmap;
    }

    public static HardwareManager getInstance(HardwareMap hmap) {
        if (instance == null) {
            instance = new HardwareManager(hmap);
        }
        return instance;
    }

    public void InitHw(){

        //?===============================SETUP================================//

        hubs = this.hmap.getAll(LynxModule.class);

        frontLeft = this.hmap.get(DcMotorEx.class, "frontLeft");
        frontRight = this.hmap.get(DcMotorEx.class, "frontRight");
        backLeft = this.hmap.get(DcMotorEx.class, "backLeft");
        backRight = this.hmap.get(DcMotorEx.class, "backRight");

        liftLeft = this.hmap.get(DcMotorEx.class, "liftLeft");
        liftRight = this.hmap.get(DcMotorEx.class, "liftRight");

        intake = this.hmap.get(DcMotorEx.class, "intakeMotor");

        legMotor = this.hmap.get(DcMotorEx.class, "legMotor");

        gripServo = this.hmap.get(Servo.class, "gripServo");
        intakeTiltServo = this.hmap.get(Servo.class, "intakeTiltServo");
        yawServo = this.hmap.get(Servo.class, "yawServo");

        outtakeTiltServo = this.hmap.get(Servo.class, "outtakeTiltServo");
        specimentServo = this.hmap.get(Servo.class, "specimentServo");
        outtakeClawServo = this.hmap.get(Servo.class, "outtakeClawServo");

        //! Odometry setup in Pinpoint class!

        //?===========================CONFIGURATION================================//

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        intake.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        liftRight.setDirection(DcMotorSimple.Direction.REVERSE);
        liftLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        liftLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        liftRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        legMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //?========================QUALITY FUNCTIONS===============================//

        setupBulkReading();
        blink();

    }

    private void setupBulkReading() {
        for(LynxModule hub : hubs){
            //! Auto for now -> setup manual in the near future!
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

    public void lockDrivetrain() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }
}
