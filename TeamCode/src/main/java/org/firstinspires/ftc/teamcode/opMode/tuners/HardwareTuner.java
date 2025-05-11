package org.firstinspires.ftc.teamcode.opMode.tuners;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.managers.testing.HardwareTestManager;
import org.firstinspires.ftc.teamcode.opMode.OpModeTemplate;
import org.firstinspires.ftc.teamcode.opMode.StateMachine;

@TeleOp(name = "Hardware Test", group = "Testing")
public class HardwareTuner extends OpModeTemplate {

    HardwareTestManager hardwareTestManager;

    @Override
    public void init() {

        initSystems(false);
        hardwareTestManager = new HardwareTestManager(hardwareManager, outtakeManager, intakeManager, driveManager, telemetry);

        stateMachine.SetSubsystemState(StateMachine._RobotState.CALIBRATION);

    }

    @Override
    public void loop() {
        hardwareTestManager.loop();
        intakeManager.loop();
        outtakeManager.loop();
        CommandScheduler.getInstance().run();
    }
}
