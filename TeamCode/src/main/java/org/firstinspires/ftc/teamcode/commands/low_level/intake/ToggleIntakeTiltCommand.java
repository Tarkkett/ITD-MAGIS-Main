package org.firstinspires.ftc.teamcode.commands.low_level.intake;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.CommandScheduler;

import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;
import org.firstinspires.ftc.teamcode.util.GamepadPlus;

public class ToggleIntakeTiltCommand extends CommandBase {
    IntakeManager intakeManager;
    OuttakeManager outtakeManager;
    GamepadPlus gamepad_driver;
    public ToggleIntakeTiltCommand(IntakeManager intakeManager, OuttakeManager outtakeManager, GamepadPlus gamepad_driver) {
        this.intakeManager = intakeManager;
        this.gamepad_driver = gamepad_driver;
        this.outtakeManager = outtakeManager;
    }

    @Override
    public void initialize(){
        if (!intakeManager.isLowered() && outtakeManager.GetLiftCurrentPos() > 450){
            CommandScheduler.getInstance().schedule(
                    new SetIntakeTiltServoPosCommand(intakeManager, IntakeManager._TiltServoState.AIMING)
            );
        }
        else {
            if (outtakeManager.GetLiftCurrentPos() > 450) {
                CommandScheduler.getInstance().schedule(
                        new SetIntakeTiltServoPosCommand(intakeManager, IntakeManager._TiltServoState.PACKED)
                );
            }
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
