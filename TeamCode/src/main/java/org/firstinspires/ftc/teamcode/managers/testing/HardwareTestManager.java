package org.firstinspires.ftc.teamcode.managers.testing;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.CommandScheduler;

import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetLiftPositionCommand;
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

    public static double OUTTAKE_Kp = 0.012;
    public static double OUTTAKE_Ki = 0;
    public static double OUTTAKE_Kd = 0.0003;
    public static double INTAKE_Kp = 0.02;
    public static double INTAKE_Ki = 0;
    public static double INTAKE_Kd = 0.0006;

    //?Intake servos
    public static float intakeTiltServoPos = 0.25f;
    public static float intakeClawServoPos = 0.5f;
    public static float intakeYawServoPos = 0.36f;

    //?Outtake servos
    public static float outtakeClawServoPos = 0.25f;
    public static float outtakeTiltServoPos = 0.65f;
    public static float outtakeExtendoServoPos = 0f;
    public static float outtakeYawServoPos = 0f;


    public static int outtakeTargetPos = 400;
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
        hardware.outtakeExtendoServo.setPosition(outtakeExtendoServoPos);
        hardware.outtakeYawServo.setPosition(outtakeYawServoPos);

        CommandScheduler.getInstance().schedule(new SetLiftPositionCommand(outtake, null, outtakeTargetPos));

        CommandScheduler.getInstance().run();
    }
}
