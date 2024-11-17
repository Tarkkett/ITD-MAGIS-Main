package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.commands.low_level.SetOuttakeTiltServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.SetLiftPositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.SetSpecimentServoPositionCommand;
import org.firstinspires.ftc.teamcode.commands.selectors.DepositPositionSelector;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

public class SetOuttakeStateCommand extends SequentialCommandGroup {

    OuttakeManager manager;

    public SetOuttakeStateCommand(OuttakeManager._OuttakeState targetState, OuttakeManager outtakeManager, GamepadEx gamepad_driver) {

        manager = outtakeManager;

        if (targetState == OuttakeManager._OuttakeState.HOME && !manager.selectingProcess){
            addCommands(
                    new SetLiftPositionCommand(manager, OuttakeManager._LiftState.HOME),
                    new InstantCommand(() -> gamepad_driver.gamepad.rumble(500)),
                    new SetOuttakeTiltServoCommand(manager, OuttakeManager._OuttakeTiltServoState.LOW),
                    new SetSpecimentServoPositionCommand(manager, OuttakeManager._SpecimentServoState.OPEN)
            );
        }

        else if (targetState == OuttakeManager._OuttakeState.DEPOSIT){
            addCommands(
                new SetOuttakeTiltServoCommand(manager, OuttakeManager._OuttakeTiltServoState.LOW),
                new SetSpecimentServoPositionCommand(manager, OuttakeManager._SpecimentServoState.CLOSED),
                new DepositPositionSelector(gamepad_driver, manager)
                    
            );
        }

        else if (targetState == OuttakeManager._OuttakeState.TRANSFER){
            addCommands(
                    new SetLiftPositionCommand(manager, OuttakeManager._LiftState.TRANSFER),
                    new SetOuttakeTiltServoCommand(manager, OuttakeManager._OuttakeTiltServoState.LOW),
                    new SetSpecimentServoPositionCommand(manager, OuttakeManager._SpecimentServoState.CLOSED)
            );
        }
    }
}
