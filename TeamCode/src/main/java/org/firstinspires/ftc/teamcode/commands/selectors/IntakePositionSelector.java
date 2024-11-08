package org.firstinspires.ftc.teamcode.commands.selectors;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.commands.low_level.MoveIntakeSomeBit;
import org.firstinspires.ftc.teamcode.commands.low_level.SetTiltServoPosCommand;
import org.firstinspires.ftc.teamcode.managers.IntakeManager;

public class IntakePositionSelector extends CommandBase {

    private static final int DISTANCE = 5;
    IntakeManager manager;
    GamepadEx gamepad;
    private boolean isSelected = false;

    public IntakePositionSelector(IntakeManager manager, GamepadEx gamepad_driver) {
        this.manager = manager;
        gamepad = gamepad_driver;
    }

    @Override
    public void initialize(){
        manager.isSelectingIntakePosition = true;
    }

    @Override
    public void execute(){

        manager.isSelectingIntakePosition = true;

        if (gamepad.gamepad.dpad_up){
            CommandScheduler.getInstance().schedule(
                    new MoveIntakeSomeBit(manager, DISTANCE)
            );
        }
        else if (gamepad.gamepad.dpad_down){
            CommandScheduler.getInstance().schedule(
                    new MoveIntakeSomeBit(manager, -DISTANCE)
            );
        }
        else if (gamepad.gamepad.square){
            CommandScheduler.getInstance().schedule(
                    new SetTiltServoPosCommand(manager, IntakeManager._TiltServoState.RAISED)
            );
        }
        else if (gamepad.gamepad.circle){
            CommandScheduler.getInstance().schedule(
                    new SetTiltServoPosCommand(manager, IntakeManager._TiltServoState.LOWERED)

            );
        }
        else if (gamepad.gamepad.left_stick_button){
            manager.isSelectingIntakePosition = false;
            isSelected = true;
            gamepad.gamepad.rumbleBlips(2);
        }
    }

    @Override
    public boolean isFinished() {
        if (isSelected){
            manager.isSelectingIntakePosition = false;
        }
        return isSelected;
    }
}
