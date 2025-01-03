package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeClawStateCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeTiltServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetLiftPositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeExtendoServoCommand;
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
                    new SetOuttakeExtendoServoCommand(manager, OuttakeManager._ExtendoServoState.PICKUP),
                    new SetOuttakeTiltServoCommand(manager, OuttakeManager._OuttakeTiltServoState.DEPOSIT),
                    new SetOuttakeClawStateCommand(manager, OuttakeManager._OuttakeClawServoState.GRIP),
                    new SetOuttakeYawServoCommand(manager, OuttakeManager._OuttakeYawServoState.VERTICAL),
                    new SetLiftPositionCommand(manager, OuttakeManager._LiftState.HOME)
                );
                break;
            case TRANSFER:
                addCommands(
                    new SetLiftPositionCommand(manager, OuttakeManager._LiftState.TRANSFER),
                    new SetOuttakeTiltServoCommand(manager, OuttakeManager._OuttakeTiltServoState.PICKUP),
                    new SetOuttakeExtendoServoCommand(manager, OuttakeManager._ExtendoServoState.TRANSFER)
                );
                break;
            case DEPOSIT:
                addCommands(
                    new SetLiftPositionCommand(manager, OuttakeManager._LiftState.CLEARED),
                    new SetOuttakeExtendoServoCommand(manager, OuttakeManager._ExtendoServoState.DEPOSIT),
                    new WaitCommand(1000),
                    new SetOuttakeTiltServoCommand(manager, OuttakeManager._OuttakeTiltServoState.PICKUP),
                    new WaitCommand(1000),
                    new SetLiftPositionCommand(manager, OuttakeManager._LiftState.ZERO),
                    new SetOuttakeExtendoServoCommand(manager, OuttakeManager._ExtendoServoState.PICKUP),
                    new WaitCommand(1000),
                    new SetOuttakeClawStateCommand(manager, OuttakeManager._OuttakeClawServoState.RELEASE),
                    new SetOuttakeYawServoCommand(manager, OuttakeManager._OuttakeYawServoState.HORIZONTAL_ServoDown),
                    new DepositPositionSelector(gamepad_driver, gamepad_codriver, manager)
                );
                break;
            case PICKUP:
                addCommands(
                    new SetLiftPositionCommand(manager, OuttakeManager._LiftState.CLEARED),
                    new WaitCommand(300),
                    new SetOuttakeExtendoServoCommand(manager, OuttakeManager._ExtendoServoState.DEPOSIT),
                    new WaitCommand(300),
                    new SetOuttakeTiltServoCommand(manager, OuttakeManager._OuttakeTiltServoState.DEPOSIT),
                    new SetOuttakeClawStateCommand(manager, OuttakeManager._OuttakeClawServoState.GRIP),
                    new SetOuttakeYawServoCommand(manager, OuttakeManager._OuttakeYawServoState.VERTICAL),
                    new SetLiftPositionCommand(manager, OuttakeManager._LiftState.HOME)
                );
                break;
        }
    }
}
