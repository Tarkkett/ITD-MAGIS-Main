package org.firstinspires.ftc.teamcode.commands.low_level.outtake;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.CommandScheduler;

import org.firstinspires.ftc.teamcode.managers.OuttakeManager;
import org.firstinspires.ftc.teamcode.util.GamepadPlus;

public class ToggleOuttakeClawCommand extends CommandBase {

    OuttakeManager outtakeManager;
    GamepadPlus gamepad_codriver;
    public ToggleOuttakeClawCommand(OuttakeManager outtakeManager, GamepadPlus gamepad_codriver) {
        this.outtakeManager = outtakeManager;
        this.gamepad_codriver = gamepad_codriver;
    }

    @Override
    public void initialize(){
        if (!outtakeManager.isClosed()){
            gamepad_codriver.rumble(200);
            CommandScheduler.getInstance().schedule(
                    new SetOuttakeClawStateCommand(outtakeManager, OuttakeManager._OuttakeClawServoState.CLOSED)
            );
        }
        else {
            CommandScheduler.getInstance().schedule(
                    new SetOuttakeClawStateCommand(outtakeManager, OuttakeManager._OuttakeClawServoState.OPEN)
            );
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
