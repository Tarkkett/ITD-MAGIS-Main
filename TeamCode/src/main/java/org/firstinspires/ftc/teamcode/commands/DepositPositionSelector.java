package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.commands.low_level.SetLiftPositionCommand;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

public class DepositPositionSelector extends CommandBase {

    GamepadEx gamepad_driver;
    boolean isSelected = false;
    OuttakeManager manager;

    public DepositPositionSelector(GamepadEx gamepad, OuttakeManager manager){
        gamepad_driver = gamepad;
        this.manager = manager;
    }

    @Override
    public void initialize(){

    }

    @Override
    public void execute(){
        if (gamepad_driver.gamepad.a){
            CommandScheduler.getInstance().schedule(new SetLiftPositionCommand(manager, OuttakeManager._LiftState.HIGH_CHAMBER));
            isSelected = true;
        } else if (gamepad_driver.gamepad.b){
            CommandScheduler.getInstance().schedule(new SetLiftPositionCommand(manager, OuttakeManager._LiftState.LOW_CHAMBER));
            isSelected = true;
        }
    }

    @Override
    public boolean isFinished(){
        return isSelected;
    }

}
