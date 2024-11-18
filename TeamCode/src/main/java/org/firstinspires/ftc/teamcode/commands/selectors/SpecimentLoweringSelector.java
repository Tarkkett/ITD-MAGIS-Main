package org.firstinspires.ftc.teamcode.commands.selectors;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.commands.low_level.outtake.MoveLiftSomeBit;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetSpecimentServoPositionCommand;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

public class SpecimentLoweringSelector extends CommandBase {

    GamepadEx gamepad_driver;
    boolean isSelected = false;
    OuttakeManager manager;

    public SpecimentLoweringSelector(GamepadEx gamepad, OuttakeManager manager){
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

        if (gamepad_driver.gamepad.a){
            CommandScheduler.getInstance().schedule(
                    new SequentialCommandGroup(
                            new MoveLiftSomeBit(manager, -800),
                            new WaitCommand(500),
                            new SetSpecimentServoPositionCommand(manager, OuttakeManager._SpecimentServoState.OPEN)
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
