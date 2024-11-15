package org.firstinspires.ftc.teamcode.opMode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.PinpointDrive;
import org.firstinspires.ftc.teamcode.managers.AscentManager;
import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.managers.DriveManager;
import org.firstinspires.ftc.teamcode.managers.HardwareManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;
import org.firstinspires.ftc.teamcode.util.GamepadPlus;

public abstract class OpModeTemplate extends OpMode {

    protected boolean teamSelected = false;
    protected boolean sideSelected = false;

    protected GamepadPlus gamepad_driver;
    protected GamepadPlus gamepad_codriver;

    protected HardwareManager hardwareManager;
    protected DriveManager driveManager;
    protected OuttakeManager outtakeManager;
    protected IntakeManager intakeManager;
    protected AscentManager ascentManager;

    PinpointDrive drive;
    private final Pose2d staringPos = new Pose2d(new Vector2d(0,0), 0);

    protected boolean isAuto = false;

    protected Alliance team = Alliance.UNKNOWN;
    protected Side side = Side.UNKNOWN;

    protected void initSystems(boolean isAuto){

        drive = new PinpointDrive(hardwareMap, staringPos);

        this.isAuto = isAuto;

        hardwareManager = HardwareManager.getInstance(hardwareMap);
        hardwareManager.InitHw();

        gamepad_driver = new GamepadPlus(gamepad1);
        gamepad_codriver = new GamepadPlus(gamepad2);

        intakeManager = new IntakeManager(hardwareManager, telemetry, gamepad_driver);
        outtakeManager = new OuttakeManager(hardwareManager, telemetry, intakeManager);
        ascentManager = new AscentManager(hardwareManager, telemetry, gamepad_codriver, outtakeManager, intakeManager);

        if (isAuto){
            SetupAuto();
        } else {
            driveManager = new DriveManager(hardwareManager, telemetry, gamepad_driver, drive);
        }

    }

    private void SetupAuto() {

        //...
        //* Continue in init_loop()
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
        UNKNOWN
    }
    public enum Side {
        RIGHT,
        LEFT,
        UNKNOWN
    }
}
