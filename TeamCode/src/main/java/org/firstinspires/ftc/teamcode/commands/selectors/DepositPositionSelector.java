package org.firstinspires.ftc.teamcode.commands.selectors;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.commands.low_level.outtake.MoveLiftCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetLiftPositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeClawStateCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakePitchServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeTiltServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeYawServoCommand;
import org.firstinspires.ftc.teamcode.managers.DriveManager;
import org.firstinspires.ftc.teamcode.managers.HardwareManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;
import org.firstinspires.ftc.teamcode.util.GamepadPlus;

public class DepositPositionSelector extends CommandBase {

    GamepadPlus gamepad_driver;
    GamepadPlus gamepad_codriver;
    OuttakeManager manager;

    DriveManager driveManager;
    HardwareManager hw;
    private boolean finish = false;

    public DepositPositionSelector(GamepadPlus gamepad_driver, GamepadPlus gamepad_codriver, OuttakeManager manager, DriveManager driveManager, HardwareManager hardwareManager){
        this.gamepad_driver = gamepad_driver;
        this.gamepad_codriver = gamepad_codriver;
        this.manager = manager;
        this.driveManager = driveManager;
        this.hw = hardwareManager;
    }

    @Override
    public void initialize(){
        manager.selectingProcess = true;
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
                                new SetOuttakeYawServoCommand(manager, OuttakeManager._OuttakeYawServoState.HORIZONTAL_Deposit),
                                new SetOuttakePitchServoCommand(manager, OuttakeManager._PitchServoState.DEPOSIT_SPECIMEN),
                                new SetOuttakeTiltServoCommand(manager, OuttakeManager._OuttakeTiltServoState.DEPOSIT_SPECIMEN),
                                new InstantCommand(() -> manager.setCanPickup(false))
                        )
                );
            }

            //* Return to pickup
            if (gamepad_codriver.gamepad.square){
                if (manager.canPickup()) {
                    CommandScheduler.getInstance().schedule(
                            new SequentialCommandGroup(
                                    new SetOuttakeYawServoCommand(manager, OuttakeManager._OuttakeYawServoState.HORIZONTAL_Pickup),
                                    new SetOuttakeTiltServoCommand(manager, OuttakeManager._OuttakeTiltServoState.PICKUP),
                                    new SetLiftPositionCommand(manager, OuttakeManager._LiftState.ZERO),
                                    new SetOuttakePitchServoCommand(manager, OuttakeManager._PitchServoState.PICKUP),
                                    new SetOuttakeClawStateCommand(manager, OuttakeManager._OuttakeClawServoState.OPEN)
                            )
                    );
                }
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
                        new SequentialCommandGroup(
                                new SetOuttakeClawStateCommand(manager, OuttakeManager._OuttakeClawServoState.OPEN),
                                new WaitCommand(200),
                                new SetOuttakeTiltServoCommand(manager, OuttakeManager._OuttakeTiltServoState.DEPOSIT_CLEARED),
//                                new SetDriveVectorCommand(hw, driveManager),
                                new WaitCommand(150),
                                new InstantCommand(() -> manager.setCanPickup(true))
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

    @Override
    public void end(boolean interrupted){
        gamepad_codriver.rumble(200);
    }

}
