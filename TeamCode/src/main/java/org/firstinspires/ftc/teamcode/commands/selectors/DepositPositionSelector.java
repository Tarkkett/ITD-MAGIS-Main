package org.firstinspires.ftc.teamcode.commands.selectors;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.commands.low_level.outtake.MoveLiftCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetLiftPositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeClawStateCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakePitchServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeTiltServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeYawServoCommand;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;
import org.firstinspires.ftc.teamcode.util.GamepadPlus;

public class DepositPositionSelector extends CommandBase {

    GamepadPlus gamepad_driver;
    GamepadPlus gamepad_codriver;
    OuttakeManager manager;

    boolean autoSeq = false;

    public DepositPositionSelector(GamepadPlus gamepad_driver, GamepadPlus gamepad_codriver, OuttakeManager manager){
        this.gamepad_driver = gamepad_driver;
        this.gamepad_codriver = gamepad_codriver;
        this.manager = manager;
//        this.autoSeq = autoSeq;
    }

    @Override
    public void initialize(){
        manager.selectingProcess = true;

        if (autoSeq){
            CommandScheduler.getInstance().schedule(
                    new SequentialCommandGroup(
                            new SetLiftPositionCommand(manager, OuttakeManager._LiftState.HIGH_BUCKET),
                            new WaitCommand(1000),
                            new SetOuttakeTiltServoCommand(manager, OuttakeManager._OuttakeTiltServoState.DEPOSIT_SPECIMEN)
                    )
            );
        }

    }

    @Override
    public void execute(){

        if (manager.selectingProcess){

            //* Go to depositing position
            if (gamepad_codriver.gamepad.circle){
                CommandScheduler.getInstance().schedule(
                        new ParallelCommandGroup(
                                new SetOuttakeClawStateCommand(manager, OuttakeManager._OuttakeClawServoState.CLOSED),
                                new SetLiftPositionCommand(manager, OuttakeManager._LiftState.HIGH_CHAMBER),
                                new SetOuttakeYawServoCommand(manager, OuttakeManager._OuttakeYawServoState.HORIZONTAL_ServoDown),
                                new SetOuttakePitchServoCommand(manager, OuttakeManager._PitchServoState.DEPOSIT_SPECIMEN),
                                new SetOuttakeTiltServoCommand(manager, OuttakeManager._OuttakeTiltServoState.DEPOSIT_SPECIMEN),
                                new InstantCommand(() -> manager.canPickup = true)
                        )
                );
            }

            //* Return to pickup
            if (gamepad_codriver.gamepad.square){
                    CommandScheduler.getInstance().schedule(
                            new WaitUntilCommand(() -> manager.canPickup).withTimeout(1500),
                            new SequentialCommandGroup(
                                    new SetOuttakeYawServoCommand(manager, OuttakeManager._OuttakeYawServoState.HORIZONTAL_ServoUp),
                                    new SetOuttakeTiltServoCommand(manager, OuttakeManager._OuttakeTiltServoState.PICKUP),
                                    new SetLiftPositionCommand(manager, OuttakeManager._LiftState.ZERO),
                                    new SetOuttakePitchServoCommand(manager, OuttakeManager._PitchServoState.PICKUP),
                                    new SetOuttakeClawStateCommand(manager, OuttakeManager._OuttakeClawServoState.OPEN)
                            )
                    );
            }

            //* Prepare for hang
            else if (gamepad_codriver.gamepad.triangle){
                CommandScheduler.getInstance().schedule(
                        new SequentialCommandGroup(
                                new SetLiftPositionCommand(manager, OuttakeManager._LiftState.HANG_READY),
                                new WaitCommand(1000),
                                new SequentialCommandGroup(
                                        new SetOuttakeClawStateCommand(manager, OuttakeManager._OuttakeClawServoState.CLOSED),
                                        new SetOuttakePitchServoCommand(manager, OuttakeManager._PitchServoState.PICKUP),
                                        new WaitCommand(550),
                                        new SetOuttakeTiltServoCommand(manager, OuttakeManager._OuttakeTiltServoState.PICKUP)
                                )

                        )
                );
            }

            else if (gamepad_codriver.gamepad.dpad_up){
                CommandScheduler.getInstance().schedule(new MoveLiftCommand(manager, 50));
            }
            else if (gamepad_codriver.gamepad.dpad_down){
                CommandScheduler.getInstance().schedule(new MoveLiftCommand(manager, -50));
            }

            //* Release scored specimen
            else if (gamepad_codriver.gamepad.cross) {
                CommandScheduler.getInstance().schedule(
                        new WaitUntilCommand(() -> !manager.canPickup).withTimeout(1500),
                        new SequentialCommandGroup(
                                new SetOuttakeClawStateCommand(manager, OuttakeManager._OuttakeClawServoState.OPEN),
                                new WaitCommand(200),
                                new SetOuttakeTiltServoCommand(manager, OuttakeManager._OuttakeTiltServoState.DEPOSIT_CLEARED),
                                new InstantCommand(() -> manager.canPickup = true)
                        )
                );

            }

            else if (gamepad_codriver.gamepad.share) {
                if (manager.canHang()){
                    CommandScheduler.getInstance().schedule(
                            new SetLiftPositionCommand(manager, OuttakeManager._LiftState.HANG_DOWN)
                    );
                }
                else gamepad_codriver.rumble(200);
            }
        }
    }

}
