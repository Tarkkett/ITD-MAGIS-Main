package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.commands.low_level.intake.AdjustYawServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeGripStateCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeSlidePositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeTiltServoPosCommand;
import org.firstinspires.ftc.teamcode.commands.selectors.IntakePositionSelector;
import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.opMode.StateMachine;
import org.firstinspires.ftc.teamcode.util.GamepadPlus;

public class SetIntakeStateCommand extends SequentialCommandGroup {


    public SetIntakeStateCommand(IntakeManager._IntakeState targetState, IntakeManager manager, StateMachine states, GamepadPlus gamepad_driver, GamepadPlus gamepad_codriver) {

        manager.managerState = targetState;

        switch (targetState){
            case INTAKE:
                addCommands(
                        new ParallelCommandGroup(
                                new SetIntakeSlidePositionCommand(manager, IntakeManager._SlideState.EXTENDED),
                                new SetIntakeGripStateCommand(manager, IntakeManager._ClawState.OPEN),
                                new SequentialCommandGroup(
                                        new WaitCommand(300),
                                        new SetIntakeTiltServoPosCommand(manager, IntakeManager._TiltServoState.AIMING)
                                )

                        ),
                        new IntakePositionSelector(manager, gamepad_driver, gamepad_codriver).interruptOn(() -> states.robotState == StateMachine._RobotState.DEPOSIT)
                );
                break;
            case HOME:
                if (states.robotState == StateMachine._RobotState.DEPOSIT){
                    addCommands(
                            new SetIntakeTiltServoPosCommand(manager, IntakeManager._TiltServoState.PACKED),
                            new SetIntakeGripStateCommand(manager, IntakeManager._ClawState.OPEN),
                            new AdjustYawServoCommand(manager, IntakeManager._YawServoState.HOME),
                            new WaitCommand(500),
                            new SetIntakeSlidePositionCommand(manager, IntakeManager._SlideState.RETRACTED)
                    );
                }
                else{
                    addCommands(
                            new SetIntakeSlidePositionCommand(manager, IntakeManager._SlideState.RETRACTED),
                            new AdjustYawServoCommand(manager, IntakeManager._YawServoState.HOME),
                            new WaitCommand(400),
                            new SetIntakeTiltServoPosCommand(manager, IntakeManager._TiltServoState.PACKED),
                            new SetIntakeGripStateCommand(manager, IntakeManager._ClawState.OPEN)
                    );
                }
                break;
        }
    }
}
