package org.firstinspires.ftc.teamcode.managers.testing;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.CommandScheduler;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetLiftPositionCommand;
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

    Telemetry tel;

    public static double OUTTAKE_Kp = 0.012;
    public static double OUTTAKE_Ki = 0;
    public static double OUTTAKE_Kd = 0.0003;
    public static double INTAKE_Kp = 0.01;
    public static double INTAKE_Ki = 0.002;
    public static double INTAKE_Kd = 0.0004;

    //?Intake servos
    public static float intakeTiltServoPos = 0.25f;
    public static float intakeClawServoPos = 0.5f;
    public static float intakeYawServoPos = 0.36f;

    //?Outtake servos
    public static float outtakeClawServoPos = 0.25f;
    public static float outtakeTiltServoPos = 0.65f;
    public static float outtakePitchServoPos = 1f;
    public static float outtakeYawServoPos = 0f;

    public static int outtakeTargetPos = 400;
    public static int intakeTargetPos = 0;


    public HardwareTestManager(HardwareManager hardware, OuttakeManager outtake, IntakeManager intake, DriveManager drive, Telemetry tel){
        this.outtake = outtake;
        this.intake = intake;
        this.drive = drive;
        this.hardware = hardware;
        this.tel = tel;
    }

    public void loop(){

        tel.addLine("Please, do not touch the controller while in calibration mode!");
        tel.update();

        intake.params = new PID_PARAMS(INTAKE_Kp, INTAKE_Ki, INTAKE_Kd, 5);
        outtake.params = new PID_PARAMS(OUTTAKE_Kp, OUTTAKE_Ki, OUTTAKE_Kd, 5);

        //? Intake

        hardware.intakeTiltSrv.setPosition(intakeTiltServoPos);
        hardware.intakeGripSrv.setPosition(intakeClawServoPos);
        hardware.intakeYawSrv.setPosition(intakeYawServoPos);

        intake.targetPosition = intakeTargetPos;

        //? Outtake
        hardware.outtakeDepositorClawSrv.setPosition(outtakeClawServoPos);
        hardware.setOuttakeArmTiltServos(outtakeTiltServoPos);
        hardware.outtakeDepositorPitchSrv.setPosition(outtakePitchServoPos);
        hardware.outtakeDepositorYawSrv.setPosition(outtakeYawServoPos);

        CommandScheduler.getInstance().schedule(new SetLiftPositionCommand(outtake, null, outtakeTargetPos));
    }
}
