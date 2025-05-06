package org.firstinspires.ftc.teamcode.opMode;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.commands.low_level.SetServosToDefaultsCommand;
import org.firstinspires.ftc.teamcode.drivers.C_LoadingBar;
import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.managers.DriveManager;
import org.firstinspires.ftc.teamcode.managers.HardwareManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;
import org.firstinspires.ftc.teamcode.managers.testing.HardwareTestManager;
import org.firstinspires.ftc.teamcode.util.GamepadPlus;

public abstract class OpModeTemplate extends OpMode {

    private C_LoadingBar loadingBar;
    private int progress = 0;

    protected boolean teamSelected = false;
    protected boolean sideSelected = false;
    protected Alliance team = Alliance.UNKNOWN;
    protected Side side = Side.UNKNOWN;



    protected GamepadPlus gamepad_driver;
    protected GamepadPlus gamepad_codriver;

    protected StateMachine stateMachine;
    protected HardwareManager hardwareManager;
    protected DriveManager driveManager;
    protected OuttakeManager outtakeManager;
    protected IntakeManager intakeManager;

    protected HardwareTestManager testManager;

    protected boolean isAuto = false;

    protected void initSystems(boolean isAuto){

        if (loadingBar == null) {
            loadingBar = new C_LoadingBar(telemetry);
        }

        progress(10);

        this.isAuto = isAuto;

        hardwareManager = new HardwareManager(hardwareMap, isAuto);
        hardwareManager.InitHw(isAuto);

        outtakeManager = new OuttakeManager(hardwareManager, telemetry, intakeManager, stateMachine);
        intakeManager = new IntakeManager(hardwareManager, telemetry, gamepad_driver, gamepad_codriver, stateMachine);

        progress(15);

        if (!isAuto) {
            gamepad_driver = new GamepadPlus(gamepad1);
            gamepad_codriver = new GamepadPlus(gamepad2);

            driveManager = new DriveManager(hardwareManager, telemetry, gamepad_driver, outtakeManager);

            testManager = new HardwareTestManager(hardwareManager, outtakeManager, intakeManager, driveManager, telemetry);

            stateMachine = new StateMachine(outtakeManager, intakeManager, driveManager, telemetry, gamepad_driver, gamepad_codriver, hardwareManager, testManager);

            progress(13);
        }
        if (isAuto) SetSystemDefaults();

        progress(17);

    }

    public void progress(int i) {
        progress += i;
    }

    protected void SetSystemDefaults() {
        CommandScheduler.getInstance().schedule(
                new SetServosToDefaultsCommand(outtakeManager, intakeManager, hardwareManager)
        );
        progress(13);
    }

    @Override
    public void init_loop() {

        if (progress <= 100){
            loadingBar.updateProgress(progress);
        } else { loadingBar.complete(); }

        CommandScheduler.getInstance().run();

        handleTeamSelection();
        if (isAuto) handleSideSelection();

        telemetry.addData("Alliance Info", "Team: " + team.name() + ", Side: " + side.name());
        telemetry.update();
    }

    private void handleSideSelection() {
        if (sideSelected){
            telemetry.clearAll();
        }
        else {
            telemetry.addLine("▲ for LEFT, ⵝ for RIGHT;");
            if (gamepad1.triangle){
                selectSide(Side.LEFT);
            }
            else if (gamepad1.cross) {
                selectSide(Side.RIGHT);
            }
        }
    }

    private void handleTeamSelection() {
        if (teamSelected) {
            telemetry.clearAll();
        } else {
            telemetry.addLine("■ for BLUE, ● for RED;");
            if (gamepad1.square) {
                selectTeam(Alliance.BLUE, 0xFF0000FF);
            } else if (gamepad1.circle) {
                selectTeam(Alliance.RED, 0xFFFF0000);
            }
        }
    }

    private void selectTeam(Alliance team, int ledColor) {
        this.team = team;
        hardwareManager.setIndicatorLEDs(ledColor);
        teamSelected = true;
    }
    private void selectSide(Side side) {
        this.side = side;
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


    //! Stop OpMode procedure
    @Override
    public void stop() {
        CommandScheduler.getInstance().reset();
        driveManager.stopMovementControlThread();
    }
}
