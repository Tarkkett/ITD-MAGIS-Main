package org.firstinspires.ftc.teamcode.commands.selectors;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.commands.low_level.AdjustYawServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.MoveIntakeSomeBit;
import org.firstinspires.ftc.teamcode.commands.low_level.SetGripStateCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.SetTiltServoPosCommand;
import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.util.GamepadPlus;

public class IntakePositionSelector extends CommandBase {

    private static final int DISTANCE = 5;
    IntakeManager manager;
    GamepadPlus gamepad;
    private boolean isSelected = false;

    public IntakePositionSelector(IntakeManager manager, GamepadPlus gamepad_driver) {
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

        if (gamepad.isDown(gamepad.dpad_Up)){
            CommandScheduler.getInstance().schedule(
                    new MoveIntakeSomeBit(manager, DISTANCE)
            );
        }
        else if (gamepad.isDown(gamepad.dpad_Down)){
            CommandScheduler.getInstance().schedule(
                    new MoveIntakeSomeBit(manager, -DISTANCE)
            );
        }
        else if (gamepad.isDown(gamepad.square)){
            CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                    new SetTiltServoPosCommand(manager, IntakeManager._TiltServoState.AIMING),
                    new WaitCommand(100),
                    new SetGripStateCommand(manager, IntakeManager._GripState.RELEASE)
                )
            );
        }
        else if (gamepad.isDown(gamepad.circle)){
            CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                    new SetTiltServoPosCommand(manager, IntakeManager._TiltServoState.LOWERED),
                    new WaitCommand(500),
                    new SetGripStateCommand(manager, IntakeManager._GripState.GRIP)
                )
            );
        }
        else if (gamepad.leftTrigger() > 0.9) {
            CommandScheduler.getInstance().schedule(
            new AdjustYawServoCommand(manager, IntakeManager._YawServoState.MANUAL, -0.05f));
        }
        else if (gamepad.rightTrigger() > 0.9) {
            CommandScheduler.getInstance().schedule(
            new AdjustYawServoCommand(manager, IntakeManager._YawServoState.MANUAL, 0.05f));
        }

//        else if (gamepad.isDown(gamepad.dpad_Left)) {
//            CommandScheduler.getInstance().schedule(
//            new AdjustYawServoCommand(manager, IntakeManager._YawServoState.TRANSFER, 0)
//            );
//        }
//        else if (gamepad.isDown(gamepad.dpad_Right)) {
//            new AdjustYawServoCommand(manager, IntakeManager._YawServoState.PICKUP_DEFAULT, 0);
//        }
    }

    @Override
    public boolean isFinished() {
        if (isSelected){
            manager.isSelectingIntakePosition = false;
        }
        return isSelected;
    }
}
