package org.firstinspires.ftc.teamcode.commands.selectors;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;

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

import java.util.HashMap;
import java.util.Map;

public class DepositPositionSelector extends CommandBase {

    private static final int LIFT_MANUAL_TICKS = 50;
    GamepadPlus gamepad_driver;
    GamepadPlus gamepad_codriver;
    OuttakeManager manager;

    private final Map<GamepadKeys.Button, Runnable> commandButtonBindings = new HashMap<>();
    private final Map<GamepadKeys.Trigger, Runnable> commandTriggerBindings = new HashMap<>();

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
    public void initialize(){
        commandButtonBindings.put(GamepadKeys.Button.DPAD_UP, () -> CommandScheduler.getInstance().schedule(new MoveLiftCommand(manager, LIFT_MANUAL_TICKS)));
        commandButtonBindings.put(GamepadKeys.Button.DPAD_UP, () -> CommandScheduler.getInstance().schedule(new MoveLiftCommand(manager, -LIFT_MANUAL_TICKS)));
        commandButtonBindings.put(GamepadKeys.Button.Y, this::goToSampleDepositPosition);
        commandButtonBindings.put(GamepadKeys.Button.A, this::releaseScoredSpecimen);
        commandButtonBindings.put(GamepadKeys.Button.X, this::goToSpecimenPickupPosition);
        commandButtonBindings.put(GamepadKeys.Button.B, this::goToSpecimenDepositPosition);
        commandButtonBindings.put(GamepadKeys.Button.START, this::goToHangPosition);
        commandButtonBindings.put(GamepadKeys.Button.BACK, this::goToHangPrePosition);
    }

    @Override
    public void execute(){

        for (Map.Entry<GamepadKeys.Button, Runnable> entry : commandButtonBindings.entrySet()) {
            if (gamepad_codriver.isDown(entry.getKey())) {
                entry.getValue().run();
            }
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
                            new SetOuttakeTiltServoCommand(manager, OuttakeManager._OuttakeTiltServoState.PICKUP_TELEOP),
                            new WaitCommand(300),
                            new SetLiftPositionCommand(manager, OuttakeManager._LiftState.ZERO),
                            new SetOuttakePitchServoCommand(manager, OuttakeManager._PitchServoState.PICKUP),
                            new SetOuttakeClawStateCommand(manager, OuttakeManager._OuttakeClawServoState.OPEN)
                    )
            );
        }
    }

    private void goToSpecimenDepositPosition() {
        CommandScheduler.getInstance().schedule(
                new SequentialCommandGroup(
                        new SetOuttakeClawStateCommand(manager, OuttakeManager._OuttakeClawServoState.CLOSED),
                        new WaitCommand(240),
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
                        new SetLiftPositionCommand(manager, OuttakeManager._LiftState.CLEARED),
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
