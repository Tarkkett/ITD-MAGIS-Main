package org.firstinspires.ftc.teamcode.commands.selectors;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.commands.low_level.MoveIntakeSomeBit;
import org.firstinspires.ftc.teamcode.commands.low_level.SetTiltServoPosCommand;
import org.firstinspires.ftc.teamcode.managers.IntakeManager;

public class IntakePositionSelector extends CommandBase {

    IntakeManager manager;
    GamepadEx gamepad;
    private boolean isSelected = false;

    public IntakePositionSelector(IntakeManager manager, GamepadEx gamepad_driver) {
        this.manager = manager;
        gamepad = gamepad_driver;
    }

    @Override
    public void execute(){
        if (gamepad.wasJustPressed(GamepadKeys.Button.DPAD_UP)){
            CommandScheduler.getInstance().schedule(
                    new MoveIntakeSomeBit(manager, 50)
            );
        }
        else if (gamepad.wasJustPressed(GamepadKeys.Button.DPAD_DOWN)){
            CommandScheduler.getInstance().schedule(
                    new MoveIntakeSomeBit(manager, -50)
            );
        }
        else if (gamepad.wasJustPressed(GamepadKeys.Button.Y)){
            CommandScheduler.getInstance().schedule(
                    new SetTiltServoPosCommand(manager, IntakeManager._TiltServoState.RAISED)
            );
        }
        else if (gamepad.wasJustPressed(GamepadKeys.Button.A)){
            CommandScheduler.getInstance().schedule(
                    new SetTiltServoPosCommand(manager, IntakeManager._TiltServoState.LOWERED)
            );
        }
        else if (gamepad.wasJustPressed(GamepadKeys.Button.B)){
            isSelected = true;
            gamepad.gamepad.rumbleBlips(2);
        }
    }

    @Override
    public boolean isFinished() {
        return isSelected;
    }
}
