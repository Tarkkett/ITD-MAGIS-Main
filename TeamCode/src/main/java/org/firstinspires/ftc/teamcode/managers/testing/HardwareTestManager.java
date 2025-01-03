package org.firstinspires.ftc.teamcode.managers.testing;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.drivers.C_PID;
import org.firstinspires.ftc.teamcode.managers.DriveManager;
import org.firstinspires.ftc.teamcode.managers.HardwareManager;
import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;
import org.firstinspires.ftc.teamcode.util.PID_PARAMS;

@Config
public class HardwareTestManager{
    OuttakeManager outtake;
    IntakeManager intake;
    DriveManager drive;
    HardwareManager hardware;

    C_PID intakeController = new C_PID(0,0,0);
    C_PID outtakeController = new C_PID(0.03,0.5,0.1);

    public static double OUTTAKE_Kp;
    public static double OUTTAKE_Ki;
    public static double OUTTAKE_Kd;
    public static double INTAKE_Kp;
    public static double INTAKE_Ki;
    public static double INTAKE_Kd;

    public static float intakeTiltServoPos = 0.5f;
    public static float intakeClawServoPos = 0.5f;
    public static float outtakeClawServoPos = 0.5f;
    public static float outtakeTiltServoPos = 0.5f;
    public static float specimentServoPos = 0.5f;
    public static float intakeYawServoPos = 0.5f;

    public static int outtakeTargetPos = 0;
    public static int intakeTargetPos = 0;


    public HardwareTestManager(HardwareManager hardware, OuttakeManager outtake, IntakeManager intake, DriveManager drive){
        this.outtake = outtake;
        this.intake = intake;
        this.drive = drive;
        this.hardware = hardware;
    }

    public void loop(){

        intake.params = new PID_PARAMS(INTAKE_Kp, INTAKE_Ki, INTAKE_Kd, 5);
        outtake.params = new PID_PARAMS(OUTTAKE_Kp, OUTTAKE_Ki, OUTTAKE_Kd, 5);

        //? Intake

        hardware.intakeTiltServo.setPosition(intakeTiltServoPos);
        hardware.gripServo.setPosition(intakeClawServoPos);
        hardware.yawServo.setPosition(intakeYawServoPos);

        intake.targetPosition = intakeTargetPos;

        //? Outtake
        hardware.outtakeClawServo.setPosition(outtakeClawServoPos);
        hardware.outtakeTiltServo.setPosition(outtakeTiltServoPos);
        hardware.specimentServo.setPosition(specimentServoPos);

        outtake.targetPosition = outtakeTargetPos;
    }
}
