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

    public DepositPositionSelector(GamepadPlus gamepad_driver, GamepadPlus gamepad_codriver, OuttakeManager manager, DriveManager driveManager, HardwareManager hardwareManager){
        this.gamepad_driver = gamepad_driver;
        this.gamepad_codriver = gamepad_codriver;
        this.manager = manager;
        this.driveManager = driveManager;
        this.hw = hardwareManager;
    }

    @Override
    public void execute(){

            if (gamepad_codriver.isDown(gamepad_codriver.circle)){
                goToSpecimenDepositPosition();
            }

            else if (gamepad_codriver.isDown(gamepad_codriver.square)){
                goToSpecimenPickupPosition();
            }

            else if (gamepad_codriver.leftTrigger() > 0.7 && gamepad_codriver.rightTrigger() > 0.7) {
                goToHangPrePosition();
            }

            else if (gamepad_codriver.isDown(gamepad_codriver.triangle)){
                goToSampleDepositPosition();
            }

            else if (gamepad_codriver.isDown(gamepad_codriver.cross)) {
                releaseScoredSpecimen();
            }

            else if (gamepad_codriver.isDown(gamepad_codriver.share)) {
                goToHangPosition();
            }

            else if (gamepad_codriver.gamepad.dpad_up){
                CommandScheduler.getInstance().schedule(new MoveLiftCommand(manager, 50));
            }
            else if (gamepad_codriver.gamepad.dpad_down){
                CommandScheduler.getInstance().schedule(new MoveLiftCommand(manager, -50));
            }
    }

    private void goToSampleDepositPosition() {
            CommandScheduler.getInstance().schedule(
                    new SequentialCommandGroup(
                            new SetOuttakeClawStateCommand(manager, OuttakeManager._OuttakeClawServoState.CLOSED),
                            new SetLiftPositionCommand(manager, OuttakeManager._LiftState.HIGH_BASKET),
                            new SetOuttakeTiltServoCommand(manager, OuttakeManager._OuttakeTiltServoState.DEPOSIT_SAMPLE),
                            new SetOuttakePitchServoCommand(manager, OuttakeManager._PitchServoState.DEPOSIT_SAMPLE)),
                            new SetOuttakeYawServoCommand(manager, OuttakeManager._OuttakeYawServoState.VERTICAL),
                            new InstantCommand(() -> manager.setCanPickup(true))
            );
    }

    private void goToSpecimenPickupPosition() {
        if (manager.canPickup()) {
            CommandScheduler.getInstance().schedule(
                    new SequentialCommandGroup(
                            new SetOuttakeYawServoCommand(manager, OuttakeManager._OuttakeYawServoState.HORIZONTAL_Pickup),
                            new SetOuttakeTiltServoCommand(manager, OuttakeManager._OuttakeTiltServoState.PICKUP),
                            new WaitCommand(300),
                            new SetLiftPositionCommand(manager, OuttakeManager._LiftState.ZERO),
                            new SetOuttakePitchServoCommand(manager, OuttakeManager._PitchServoState.PICKUP),
                            new SetOuttakeClawStateCommand(manager, OuttakeManager._OuttakeClawServoState.OPEN),
                            new WaitUntilCommand(() -> !manager.isInProximity()).interruptOn(() -> gamepad_codriver.gamepad.dpad_left),
                            new SetOuttakeClawStateCommand(manager, OuttakeManager._OuttakeClawServoState.CLOSED)
                    )
            );
        }
    }

    private void goToSpecimenDepositPosition() {
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
    private void goToHangPrePosition(){
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        new SetLiftPositionCommand(manager, OuttakeManager._LiftState.HANG_READY),
                        new SequentialCommandGroup(
                                new SetOuttakeClawStateCommand(manager, OuttakeManager._OuttakeClawServoState.CLOSED),
                                new SetOuttakePitchServoCommand(manager, OuttakeManager._PitchServoState.PICKUP),
                                new WaitCommand(550),
                                new SetOuttakeTiltServoCommand(manager, OuttakeManager._OuttakeTiltServoState.PICKUP)
                        )

                )
        );
    }
    private void goToHangPosition() {
        if (manager.canHang()){
            CommandScheduler.getInstance().schedule(
                    new SetLiftPositionCommand(manager, OuttakeManager._LiftState.HANG_DOWN)
            );
        }
        else gamepad_codriver.rumble(200);
    }
    private void releaseScoredSpecimen(){
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        new SetOuttakeClawStateCommand(manager, OuttakeManager._OuttakeClawServoState.OPEN),
                        new WaitCommand(200),
                        new SetOuttakeTiltServoCommand(manager, OuttakeManager._OuttakeTiltServoState.DEPOSIT_CLEARED),
                        new WaitCommand(150),
                        new InstantCommand(() -> manager.setCanPickup(true))
                )
        );
    }


    @Override
    public void end(boolean interrupted){
        gamepad_codriver.rumble(200);
    }

}
