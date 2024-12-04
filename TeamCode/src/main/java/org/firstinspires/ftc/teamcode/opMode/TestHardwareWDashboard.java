package org.firstinspires.ftc.teamcode.opMode;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.managers.testing.HardwareTestManager;

@TeleOp(name = "TestAllHardware", group = "Testing")
public class TestHardwareWDashboard extends OpModeTemplate {

    HardwareTestManager hardwareTestManager;

    @Override
    public void init() {

        initSystems(false);
        hardwareTestManager = new HardwareTestManager(hardwareManager, outtakeManager, intakeManager, driveManager);

        stateMachine.SetSubsystemState(StateMachine._RobotState.CALIBRATION);

    }

    @Override
    public void loop() {

        telemetry.addLine("Please, do not touch the controller while in calibration mode!");
        telemetry.update();

        hardwareTestManager.loop();
        intakeManager.loop();
        outtakeManager.loop();
        CommandScheduler.getInstance().run();
    }
}
