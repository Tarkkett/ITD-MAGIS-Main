package org.firstinspires.ftc.teamcode.commands.low_level;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.QuickCommand;
import org.firstinspires.ftc.teamcode.managers.LiftManager;

public class SetLiftPositionCommand extends QuickCommand {

    public SetLiftPositionCommand(LiftManager liftManager, LiftManager.StateEnum stateEnum, Telemetry telemetry) {
        super(() -> {
            liftManager.SetSubsystemState(stateEnum);
            if (telemetry != null) {
                telemetry.addData("Lift State", stateEnum);
                telemetry.update();
            }
        });
    }
}
