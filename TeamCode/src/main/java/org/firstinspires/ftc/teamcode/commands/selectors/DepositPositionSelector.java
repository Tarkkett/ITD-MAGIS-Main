package org.firstinspires.ftc.teamcode.commands.selectors;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.commands.low_level.outtake.MoveLiftSomeBit;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetLiftPositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeClawStateCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeExtendoServoCommand;
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
            //!Up to Down
//            if (gamepad_codriver.gamepad.circle){
//                CommandScheduler.getInstance().schedule(
//                        new ParallelCommandGroup(
//                                new SetLiftPositionCommand(manager, OuttakeManager._LiftState.HIGH_CHAMBER),
//                                new SequentialCommandGroup(
//                                        new WaitCommand(200),
//                                        new SetOuttakeExtendoServoCommand(manager, OuttakeManager._ExtendoServoState.DEPOSIT),
//                                        new WaitCommand(500),
//                                        new SetOuttakeTiltServoCommand(manager, OuttakeManager._OuttakeTiltServoState.DEPOSIT_SPECIMEN),
//                                        new WaitCommand(400),
//                                        new SetOuttakeYawServoCommand(manager, OuttakeManager._OuttakeYawServoState.HORIZONTAL_ServoUp),
//                                        new SetOuttakeExtendoServoCommand(manager, OuttakeManager._ExtendoServoState.DEPOSIT_BACK)
//                                )
//
//                        )
//                );
//            }
            //! Down to Up
            if (gamepad_codriver.gamepad.circle){
                CommandScheduler.getInstance().schedule(
                        new ParallelCommandGroup(
                                new SetOuttakeClawStateCommand(manager, OuttakeManager._OuttakeClawServoState.GRIP),
                                new SetLiftPositionCommand(manager, OuttakeManager._LiftState.HIGH_CHAMBER_LOWER_REVERSED),
                                new SequentialCommandGroup(
                                        new WaitCommand(200),
                                        new SetOuttakeExtendoServoCommand(manager, OuttakeManager._ExtendoServoState.DEPOSIT),
                                        new WaitCommand(500),
                                        new SetOuttakeTiltServoCommand(manager, OuttakeManager._OuttakeTiltServoState.DEPOSIT_SPECIMEN),
                                        new WaitCommand(400),
                                        new SetOuttakeExtendoServoCommand(manager, OuttakeManager._ExtendoServoState.DEPOSIT_BACK)
                                )

                        )
                );
            }
            if (gamepad_codriver.gamepad.square){
                if (manager.canHome()){
                    CommandScheduler.getInstance().schedule(
                            new SequentialCommandGroup(
                                    new SetOuttakeYawServoCommand(manager, OuttakeManager._OuttakeYawServoState.HORIZONTAL_ServoUp),
                                    new SetLiftPositionCommand(manager, OuttakeManager._LiftState.CLEARED_ALL),
                                    new SetOuttakeExtendoServoCommand(manager, OuttakeManager._ExtendoServoState.DEPOSIT_FORWARDPUSH),
                                    new WaitCommand(250),
                                    new SetOuttakeTiltServoCommand(manager, OuttakeManager._OuttakeTiltServoState.PICKUP),
                                    new WaitCommand(850),
                                    new SetLiftPositionCommand(manager, OuttakeManager._LiftState.ZERO),
                                    new SetOuttakeExtendoServoCommand(manager, OuttakeManager._ExtendoServoState.PICKUP),
                                    new WaitCommand(300),
                                    new SetOuttakeClawStateCommand(manager, OuttakeManager._OuttakeClawServoState.RELEASE)
                            )
                    );
                }
                else {gamepad_codriver.rumble(400);}
            }

            //*Initially for high basket -> now for hang
            else if (gamepad_codriver.gamepad.triangle){
                CommandScheduler.getInstance().schedule(
                        new SequentialCommandGroup(
                                new SetLiftPositionCommand(manager, OuttakeManager._LiftState.HANG_READY),
                                new WaitCommand(1000),
                                new SequentialCommandGroup(
                                        new SetOuttakeClawStateCommand(manager, OuttakeManager._OuttakeClawServoState.GRIP),
                                        new SetOuttakeExtendoServoCommand(manager, OuttakeManager._ExtendoServoState.DEPOSIT),
                                        new WaitCommand(550),
                                        new SetOuttakeTiltServoCommand(manager, OuttakeManager._OuttakeTiltServoState.DEPOSIT_SPECIMEN)
                                )

                        )
                );
            }
            else if (gamepad_codriver.gamepad.dpad_up){
                CommandScheduler.getInstance().schedule(new MoveLiftSomeBit(manager, 50));
            }
            else if (gamepad_codriver.gamepad.dpad_down){
                CommandScheduler.getInstance().schedule(new MoveLiftSomeBit(manager, -50));
            }
            //! Up to Down
//            else if (gamepad_codriver.gamepad.cross) {
//                CommandScheduler.getInstance().schedule(
//                        new SequentialCommandGroup(
//                                new SetLiftPositionCommand(manager, OuttakeManager._LiftState.HIGH_CHAMBER_LOWER),
//                                new WaitCommand(750),
//                                new SetOuttakeClawStateCommand(manager, OuttakeManager._OuttakeClawServoState.RELEASE)
//                        )
//                );
//
//            }
            //! Down to up
            else if (gamepad_codriver.gamepad.cross) {
                CommandScheduler.getInstance().schedule(
                        new SequentialCommandGroup(
                                new SetLiftPositionCommand(manager, OuttakeManager._LiftState.HIGH_CHAMBER_REVERSED),
                                new WaitCommand(550),
                                new SetOuttakeClawStateCommand(manager, OuttakeManager._OuttakeClawServoState.RELEASE)
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
