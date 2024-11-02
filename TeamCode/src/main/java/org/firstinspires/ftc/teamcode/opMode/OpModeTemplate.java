package org.firstinspires.ftc.teamcode.opMode;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.State;
import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.managers.DriveManager;
import org.firstinspires.ftc.teamcode.managers.HardwareManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

public abstract class OpModeTemplate extends OpMode {

    protected boolean teamSelected = false;

    protected GamepadEx gamepad_driver;
    protected GamepadEx gamepad_codriver;

    protected HardwareManager hardwareManager;
    protected DriveManager driveManager;
    protected OuttakeManager outtakeManager;
    protected IntakeManager intakeManager;

    protected Alliance team = Alliance.UNKNOWN;

    protected void initSystems(boolean isAuto){

        hardwareManager = HardwareManager.getInstance(hardwareMap, telemetry);
        hardwareManager.InitHw();

        gamepad_driver = new GamepadEx(gamepad1);
        gamepad_codriver = new GamepadEx(gamepad2);

        driveManager = new DriveManager(hardwareManager, telemetry, gamepad_driver);
        intakeManager = new IntakeManager(hardwareManager, telemetry, gamepad_driver);
        outtakeManager = new OuttakeManager(hardwareManager, telemetry);

    }

    @Override
    public void init_loop() {
        PromptUserForAllianceSelection();
    }

    private void PromptUserForAllianceSelection() {
        if(teamSelected){
            telemetry.clearAll();
            telemetry.addData("Team selected", team.name());
        }
        else{
            telemetry.clearAll();
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
}
