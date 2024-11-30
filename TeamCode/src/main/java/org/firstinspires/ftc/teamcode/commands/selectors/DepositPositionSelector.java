package org.firstinspires.ftc.teamcode.commands.selectors;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.commands.low_level.outtake.MoveLiftSomeBit;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetLiftPositionCommand;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;
import org.firstinspires.ftc.teamcode.util.GamepadPlus;

public class DepositPositionSelector extends CommandBase {

    GamepadPlus gamepad_driver;
    GamepadPlus gamepad_codriver;
    boolean isSelected = false;
    OuttakeManager manager;

    public DepositPositionSelector(GamepadPlus gamepad_driver, GamepadPlus gamepad_codriver, OuttakeManager manager){
        this.gamepad_driver = gamepad_driver;
        this.gamepad_codriver = gamepad_codriver;
        this.manager = manager;
    }

    @Override
    public void initialize(){
        manager.selectingProcess = true;
    }

    @Override
    public void execute(){

        if (manager.selectingProcess){
            if (gamepad_codriver.gamepad.circle){
                CommandScheduler.getInstance().schedule(new SetLiftPositionCommand(manager, OuttakeManager._LiftState.HIGH_CHAMBER));
                isSelected = true;
                manager.selectingProcess = false;
            }
            else if (gamepad_codriver.gamepad.square){
                CommandScheduler.getInstance().schedule(new SetLiftPositionCommand(manager, OuttakeManager._LiftState.HOME));
                isSelected = true;
                manager.selectingProcess = false;
            }
            else if (gamepad_codriver.gamepad.triangle){
                CommandScheduler.getInstance().schedule(new SetLiftPositionCommand(manager, OuttakeManager._LiftState.HIGH_BUCKET));
                isSelected = true;
                manager.selectingProcess = false;
            }
            else if (gamepad_codriver.gamepad.left_stick_button){
                //!OVERRIDE
                isSelected = true;
                manager.selectingProcess = false;
            }
            else if (gamepad_codriver.gamepad.dpad_up){
                CommandScheduler.getInstance().schedule(new MoveLiftSomeBit(manager, 50));
            }
            else if (gamepad_codriver.gamepad.dpad_down){
                CommandScheduler.getInstance().schedule(new MoveLiftSomeBit(manager, -50));
            }
        }
    }

    @Override
    public boolean isFinished(){
        return isSelected;
    }

}
