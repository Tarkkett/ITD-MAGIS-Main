package org.firstinspires.ftc.teamcode.opMode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.PinpointDrive;
import org.firstinspires.ftc.teamcode.commands.low_level.SetServosToDefaultsCommand;
import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.managers.DriveManager;
import org.firstinspires.ftc.teamcode.managers.HardwareManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;
import org.firstinspires.ftc.teamcode.util.GamepadPlus;
import org.firstinspires.ftc.teamcode.kotlin.TelemetryManager;

public abstract class OpModeTemplate extends OpMode {

    //!Kotlin

    protected TelemetryManager telemetryManager;

    //!Java

    protected boolean teamSelected = false;
    protected boolean sideSelected = false;


    protected GamepadPlus gamepad_driver;
    protected GamepadPlus gamepad_codriver;

    protected StateMachine stateMachine;
    protected HardwareManager hardwareManager;
    protected DriveManager driveManager;
    protected OuttakeManager outtakeManager;
    protected IntakeManager intakeManager;

    PinpointDrive drive;
    private final Pose2d startingPos = new Pose2d(new Vector2d(0,0), 0);

    protected boolean isAuto = false;

    protected Alliance team = Alliance.UNKNOWN;
    protected Side side = Side.UNKNOWN;

    protected void initSystems(boolean isAuto){

        this.isAuto = isAuto;

        drive = new PinpointDrive(hardwareMap, startingPos);

        hardwareManager = new HardwareManager(hardwareMap);
        hardwareManager.InitHw();

        gamepad_driver = new GamepadPlus(gamepad1);
        gamepad_codriver = new GamepadPlus(gamepad2);

        intakeManager = new IntakeManager(hardwareManager, telemetry, gamepad_driver, gamepad_codriver);
        outtakeManager = new OuttakeManager(hardwareManager, telemetry, intakeManager);

        driveManager = new DriveManager(hardwareManager, telemetry, gamepad_driver, drive, outtakeManager);

        telemetryManager = new TelemetryManager(this);

        stateMachine = new StateMachine(outtakeManager, intakeManager, driveManager, telemetry, gamepad_driver, gamepad_codriver, hardwareManager);

        if (isAuto) SetSystemDefaults();

    }

    protected void SetSystemDefaults() {
        CommandScheduler.getInstance().schedule(
                new SetServosToDefaultsCommand(outtakeManager, intakeManager)
        );
    }

    private void SetupAuto() {

        //...
        //* Continue in init_loop()
    }

    @Override
    public void init_loop() {
        CommandScheduler.getInstance().run();
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

    @Override
    public void stop() {
        CommandScheduler.getInstance().reset();
        driveManager.stopMovementControlThread();

    }
}
