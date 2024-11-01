package org.firstinspires.ftc.teamcode.managers;

import android.graphics.Color;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.drivers.GoBildaPinpointDriver;

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
    DcMotorEx frontRight;
    DcMotorEx backLeft;
    DcMotorEx backRight;

    DcMotorEx liftRight;
    DcMotorEx liftLeft;

    DcMotorEx intake;

    Servo broomServo;
    Servo intakeTiltServo;

    Servo bucketServo;
    Servo specimentServo;

    public GoBildaPinpointDriver odo;

    public static int IMU_DATA_SAMPLING_RATE = 10;

    private HardwareMap hmap;
    private Telemetry telemetry;

    public HardwareManager(HardwareMap hmap, Telemetry tel){
        this.hmap = hmap;
        this.telemetry = tel;
    }

    public static HardwareManager getInstance(HardwareMap hmap, Telemetry telemetry) {
        if (instance == null) {
            instance = new HardwareManager(hmap, telemetry);
        }
        return instance;
    }

    public void InitHw(){

        hubs = this.hmap.getAll(LynxModule.class);

        frontLeft = this.hmap.get(DcMotorEx.class, "frontLeft");
        frontRight = this.hmap.get(DcMotorEx.class, "frontRight");
        backLeft = this.hmap.get(DcMotorEx.class, "backLeft");
        backRight = this.hmap.get(DcMotorEx.class, "backRight");

        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        liftLeft = this.hmap.get(DcMotorEx.class, "liftLeft");
        liftRight = this.hmap.get(DcMotorEx.class, "liftRight");

        intake = this.hmap.get(DcMotorEx.class, "intakeMotor");

        broomServo = this.hmap.get(Servo.class, "broomServo");
        intakeTiltServo = this.hmap.get(Servo.class, "intakeTiltServo");

        bucketServo = this.hmap.get(Servo.class, "bucketServo");
        specimentServo = this.hmap.get(Servo.class, "specimentServo");

        odo = this.hmap.get(GoBildaPinpointDriver.class,"pinpoint");

        //========================================================================//

        intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intake.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);


        setupBulkReading();
        setupOdometry();
        blink();

        telemetry.addLine("Hardware initialised!");

    }

    private void setupBulkReading() {
        for(LynxModule hub : hubs){
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
    }

    private void setupOdometry() {

        odo.setOffsets(-123.7, -42.07);
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);
        odo.resetPosAndIMU();
    }

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

    public void recalibrateIMU() {
        odo.recalibrateIMU();
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
