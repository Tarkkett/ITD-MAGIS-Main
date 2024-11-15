package org.firstinspires.ftc.teamcode.opMode;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.PinpointDrive;
import org.firstinspires.ftc.teamcode.commands.ActionCommand;
import org.firstinspires.ftc.teamcode.commands.auto.DriveToPositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.SetLiftPositionCommand;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

@Autonomous(name = "Main Auto", group = "OpMode", preselectTeleOp = "Main TeleOp")
@SuppressWarnings("unused")
public class MainAuto extends OpModeTemplate {

    Pose2d beginPose = new Pose2d(0, 0, 0);

    Action trajectoryFromChamberToPushingPos;
    Action demoTrajectory;

    @Override
    public void init() {
        initSystems(true);
    }
    @Override
    public void start(){
        trajectoryFromChamberToPushingPos = drive.actionBuilder(new Pose2d(-29.5,0,0))
                .splineToConstantHeading(new Vector2d(-27,0), 0)
                .splineToLinearHeading(new Pose2d(-27,23, Math.toRadians(90)),0)
                .splineToConstantHeading(new Vector2d(-37, 35), 0)
                .splineToConstantHeading(new Vector2d(-48, 42), 0)
                .build();

        demoTrajectory = drive.actionBuilder(new Pose2d(0,0,0))
                .splineToConstantHeading(new Vector2d(-20,0), 0)
                .splineToConstantHeading(new Vector2d(0, 15), 0)
                .splineToConstantHeading(new Vector2d(0, -15), 0)
                .splineToConstantHeading(new Vector2d(-20, 0), 0)
                .build();

        Actions.runBlocking(
            new ParallelAction(

                outtakeManager.loopLift(),

                new SequentialAction(

                    //Drive to Deposit Speciment from start pos
                    new ParallelAction(
                        outtakeManager.driveLift(1500),
                        drive.actionBuilder(beginPose).strafeToConstantHeading(new Vector2d(-29.6,0)).build()

                    ),
                    //Score Points
                    outtakeManager.driveLift(920),
                    new SleepAction(0.5),
                    outtakeManager.OpenCloseSpeciment(true),
                    new SleepAction(0.2),
                    new ParallelAction(
                            //Home Lift
                            new SequentialAction(
                                    new SleepAction(0.2),
                                    outtakeManager.driveLift(5)
                            ),
                            //Drive to pick up 2nd speciment
                            new SequentialAction(
                                    drive.actionBuilder(new Pose2d(-29.6,0, 0)).strafeToLinearHeading(new Vector2d(-10, 40), Math.toRadians(180))
                                            .strafeToConstantHeading(new Vector2d(-2.2, 40))
                                            .build(),

                                    //Grab Speciment
                                    outtakeManager.OpenCloseSpeciment(false),
                                    new SleepAction(0.7),
                                    //Raise Lift
                                    outtakeManager.driveLift(1500),
                                    new SleepAction(0.5),
                                    //Drive to Chamber 2nd time
                                    drive.actionBuilder(new Pose2d(-2.2, 40, Math.toRadians(180))).strafeToLinearHeading(new Vector2d(-29.7, -4), Math.toRadians(0)).build()
                            )
                    ),
                    //Score 2nd speciment
                    new SleepAction(1),
                    outtakeManager.driveLift(920),
                    new SleepAction(0.5),
                    outtakeManager.OpenCloseSpeciment(true),
                    //Go park
                        outtakeManager.driveLift(10),
                        drive.actionBuilder(new Pose2d(-29.7, -4, 0))
                                .strafeToLinearHeading(new Vector2d(-28, -4), Math.toRadians(90))
                                .strafeToConstantHeading(new Vector2d(-28, 28))
                                .strafeToConstantHeading(new Vector2d(-55, 28))
                                .strafeToConstantHeading(new Vector2d(-55, 43))
                                .strafeToConstantHeading(new Vector2d(-5, 43))
                                .turnTo(Math.toRadians(180))
//                                .strafeToConstantHeading(new Vector2d(-30, 43))
//                                .strafeToConstantHeading(new Vector2d(-2, 43))

                                        .build(),
//                    drive.actionBuilder(new Pose2d(-29.6, -4, 0))
//                            .strafeToLinearHeading(new Vector2d(-5, 45), Math.toRadians(180))
//                            .build(),
//                        outtakeManager.OpenCloseSpeciment(false),
//                    outtakeManager.driveLift(1500),
//                        new SleepAction(0.5),
//                        //Drive to Chamber 2nd time
//                        drive.actionBuilder(new Pose2d(-2.2, 40, Math.toRadians(180))).strafeToLinearHeading(new Vector2d(-29.7, -4), Math.toRadians(0)).build(),
                    outtakeManager.stop()

                )
            )
        );

    }

    @Override
    public void loop() {

    }
}
