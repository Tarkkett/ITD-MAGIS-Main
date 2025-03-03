package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeClawStateCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakePitchServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeTiltServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetLiftPositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeYawServoCommand;
import org.firstinspires.ftc.teamcode.commands.selectors.DepositPositionSelector;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;
import org.firstinspires.ftc.teamcode.util.GamepadPlus;

public class SetOuttakeStateCommand extends SequentialCommandGroup {

    public SetOuttakeStateCommand(OuttakeManager._OuttakeState targetState, OuttakeManager manager, GamepadPlus gamepad_driver, GamepadPlus gamepad_codriver) {

        manager.managerState = targetState;

        switch (targetState){
            case HOME:
                addCommands(
                    new SetOuttakePitchServoCommand(manager, OuttakeManager._PitchServoState.PICKUP),
                    new SetOuttakeTiltServoCommand(manager, OuttakeManager._OuttakeTiltServoState.HOME),
                    new SetOuttakeClawStateCommand(manager, OuttakeManager._OuttakeClawServoState.OPEN),
                    new SetOuttakeYawServoCommand(manager, OuttakeManager._OuttakeYawServoState.HORIZONTAL_ServoUp),
                    new SetLiftPositionCommand(manager, OuttakeManager._LiftState.HOME)
                );
                break;
            case TRANSFER:
                addCommands(
                    new SetLiftPositionCommand(manager, OuttakeManager._LiftState.TRANSFER),
                    new SetOuttakeTiltServoCommand(manager, OuttakeManager._OuttakeTiltServoState.PICKUP),
                    new SetOuttakePitchServoCommand(manager, OuttakeManager._PitchServoState.TRANSFER)
                );
                break;
            case PICKUP:
                addCommands(
                        new SetOuttakeYawServoCommand(manager, OuttakeManager._OuttakeYawServoState.HORIZONTAL_ServoUp),
                        new SetOuttakeTiltServoCommand(manager, OuttakeManager._OuttakeTiltServoState.HOME),
                        new SetLiftPositionCommand(manager, OuttakeManager._LiftState.ZERO),
                        new SetOuttakePitchServoCommand(manager, OuttakeManager._PitchServoState.PICKUP),
                        new SetOuttakeClawStateCommand(manager, OuttakeManager._OuttakeClawServoState.OPEN),
                        new WaitCommand( 1000),
                        new SetOuttakeTiltServoCommand(manager, OuttakeManager._OuttakeTiltServoState.PICKUP),
                        new DepositPositionSelector(gamepad_driver, gamepad_codriver, manager)
                );
                break;
        }
    }
}
