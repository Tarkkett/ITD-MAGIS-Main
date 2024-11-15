package org.firstinspires.ftc.teamcode.commands.selectors;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.commands.low_level.LowerLiftSomeBit;
import org.firstinspires.ftc.teamcode.commands.low_level.SetLiftPositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.SetSpecimentServoPositionCommand;
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
        manager.selectingProcess = true;
    }

    @Override
    public void execute(){

        manager.selectingProcess = true;

        if (gamepad_driver.gamepad.b){
            CommandScheduler.getInstance().schedule(new SetLiftPositionCommand(manager, OuttakeManager._LiftState.HIGH_CHAMBER));
            isSelected = true;
            manager.selectingProcess = false;
        }
        else if (gamepad_driver.gamepad.x){
            CommandScheduler.getInstance().schedule(new SetLiftPositionCommand(manager, OuttakeManager._LiftState.HOME));
            isSelected = true;
            manager.selectingProcess = false;
        }
        else if (gamepad_driver.gamepad.y){
            CommandScheduler.getInstance().schedule(new SetLiftPositionCommand(manager, OuttakeManager._LiftState.HIGH_BUCKET));
            isSelected = true;
            manager.selectingProcess = false;
        }
        else if (gamepad_driver.gamepad.left_stick_button){

            isSelected = true;
            manager.selectingProcess = false;
        }
        else if (gamepad_driver.gamepad.dpad_up){
            CommandScheduler.getInstance().schedule(new LowerLiftSomeBit(manager, 50));
        }
        else if (gamepad_driver.gamepad.dpad_down){
            CommandScheduler.getInstance().schedule(new LowerLiftSomeBit(manager, -50));
        }
        if (gamepad_driver.gamepad.cross){
            //Create dedicated command
            CommandScheduler.getInstance().schedule(
                    new SequentialCommandGroup(
                            new LowerLiftSomeBit(manager, -550)
                    )
            );
            isSelected = true;
            manager.selectingProcess = false;
        }
    }

    @Override
    public boolean isFinished(){
        return isSelected;
    }

}
