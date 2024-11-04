package org.firstinspires.ftc.teamcode.opMode;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.commands.auto.DriveToPositionCommand;

@Autonomous(name = "Main Auto", group = "OpMode")
@SuppressWarnings("unused")
public class MainAuto extends OpModeTemplate{

    @Override
    public void init() {
        initSystems(true);
        telemetry.setAutoClear(true);
    }

    @Override
    public void start(){
        CommandScheduler.getInstance().schedule(new DriveToPositionCommand(50, 0, 0, navigationManager));
    }

    @Override
    public void loop() {
        navigationManager.loop();
        hardwareManager.odo.update();
    }
}
