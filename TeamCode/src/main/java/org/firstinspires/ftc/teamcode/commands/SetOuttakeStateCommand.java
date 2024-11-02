package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.SelectCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.commands.low_level.SetBucketPositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.SetLiftPositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.SetSpecimentServoPositionCommand;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.Supplier;

public class SetOuttakeStateCommand extends SequentialCommandGroup {

    OuttakeManager manager;

    public SetOuttakeStateCommand(OuttakeManager._OuttakeState targetState, OuttakeManager outtakeManager, GamepadEx gamepad_driver) {

        manager = outtakeManager;

        if (targetState == OuttakeManager._OuttakeState.HOME){
            addCommands(
                    new SetLiftPositionCommand(manager, OuttakeManager._LiftState.HOME),
                    new SetBucketPositionCommand(manager, OuttakeManager._BucketServoState.LOW),
                    new SetSpecimentServoPositionCommand(manager, OuttakeManager._SpecimentServoState.OPEN)
            );
        }

        if (targetState == OuttakeManager._OuttakeState.DEPOSIT){
            addCommands(
                    new DepositPositionSelector(gamepad_driver, manager),
                    
                    new SetBucketPositionCommand(manager, OuttakeManager._BucketServoState.LOW),
                    new SetSpecimentServoPositionCommand(manager, OuttakeManager._SpecimentServoState.CLOSED)
            );
        }
    }
}
