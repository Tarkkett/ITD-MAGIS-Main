package org.firstinspires.ftc.teamcode.opMode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.PinpointDrive;
import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.managers.DriveManager;
import org.firstinspires.ftc.teamcode.managers.HardwareManager;
import org.firstinspires.ftc.teamcode.managers.NavigationManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

public abstract class OpModeTemplate extends OpMode {

    protected boolean teamSelected = false;
    protected boolean sideSelected = false;

    protected GamepadEx gamepad_driver;
    protected GamepadEx gamepad_codriver;

    protected HardwareManager hardwareManager;
    protected DriveManager driveManager;
    protected OuttakeManager outtakeManager;
    protected IntakeManager intakeManager;
    protected NavigationManager navigationManager;

    PinpointDrive drive;
    private Pose2d staringPos = new Pose2d(new Vector2d(0,0), 0);

    protected boolean isAuto = false;

    protected Alliance team = Alliance.UNKNOWN;
    protected Side side = Side.UNKNOWN;

    protected void initSystems(boolean isAuto){

        drive = new PinpointDrive(hardwareMap, staringPos);

        this.isAuto = isAuto;

        hardwareManager = HardwareManager.getInstance(hardwareMap, telemetry);
        hardwareManager.InitHw();

        gamepad_driver = new GamepadEx(gamepad1);
        gamepad_codriver = new GamepadEx(gamepad2);

        if (!isAuto) {driveManager = new DriveManager(hardwareManager, telemetry, gamepad_driver, drive);}
        intakeManager = new IntakeManager(hardwareManager, telemetry, gamepad_driver);
        outtakeManager = new OuttakeManager(hardwareManager, telemetry);

        if (isAuto) SetupAuto();

    }

    private void SetupAuto() {
        navigationManager = new NavigationManager(hardwareManager, telemetry);
    }

    @Override
    public void init_loop() {
        PromptUserForAllianceSelection();
        if (isAuto) PromptUserForSideSelection();
    }

    private void PromptUserForSideSelection() {
        if (sideSelected){
            telemetry.clearAll();
            telemetry.addData("Side selected", side.name());
        }
        else {
            telemetry.addLine("▲ for LEFT, ⵝ for RIGHT;");

            if (gamepad1.triangle){
                side = Side.LEFT;
                sideSelected = true;
            }
            else if (gamepad1.cross) {
                side = Side.RIGHT;
                sideSelected = true;
            }
        }
    }

    private void PromptUserForAllianceSelection() {
        if(teamSelected){
            telemetry.clearAll();
            telemetry.addData("Team selected", team.name());
        }
        else{
            telemetry.addLine("■ for BLUE, ● for RED;");

            if (gamepad1.square){
                team = Alliance.BLUE;
                hardwareManager.setIndicatorLEDs(0xFF0000FF);
                teamSelected = true;
            }
            else if (gamepad1.circle) {
                team = Alliance.RED;
                hardwareManager.setIndicatorLEDs(0xFFFF0000);
                teamSelected = true;
            }
        }
    }


    public enum Alliance {
        RED,
        BLUE,
        UNKNOWN;
        public double adjust(double input) {
            return this == RED ? input : -input;
        }
    }
    public enum Side {
        RIGHT,
        LEFT,
        UNKNOWN
    }
}
