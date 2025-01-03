package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeTiltServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetLiftPositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetSpecimentServoPositionCommand;
import org.firstinspires.ftc.teamcode.commands.selectors.DepositPositionSelector;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;
import org.firstinspires.ftc.teamcode.util.GamepadPlus;

public class SetOuttakeStateCommand extends SequentialCommandGroup {

    public SetOuttakeStateCommand(OuttakeManager._OuttakeState targetState, OuttakeManager manager, GamepadPlus gamepad_driver, GamepadPlus gamepad_codriver) {

        switch (targetState){
            case HOME:
                    addCommands(
                            new SetLiftPositionCommand(manager, OuttakeManager._LiftState.HOME),
                            new SetOuttakeTiltServoCommand(manager, OuttakeManager._OuttakeTiltServoState.MID),
                            new SetSpecimentServoPositionCommand(manager, OuttakeManager._SpecimentServoState.OPEN)
                    );

                break;
            case TRANSFER:
                addCommands(
                        new SetLiftPositionCommand(manager, OuttakeManager._LiftState.TRANSFER),
                        new SetOuttakeTiltServoCommand(manager, OuttakeManager._OuttakeTiltServoState.LOW),
                        new SetSpecimentServoPositionCommand(manager, OuttakeManager._SpecimentServoState.CLOSED)
                );
                break;
            case DEPOSIT:
                addCommands(
                        new SetSpecimentServoPositionCommand(manager, OuttakeManager._SpecimentServoState.CLOSED),
                        new DepositPositionSelector(gamepad_driver, gamepad_codriver, manager)

                );
                break;
        }
    }
}
