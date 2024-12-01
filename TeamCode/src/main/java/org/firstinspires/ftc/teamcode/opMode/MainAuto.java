package org.firstinspires.ftc.teamcode.opMode;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.managers.IntakeManager;

@Autonomous(name = "Main Auto", group = "OpMode", preselectTeleOp = "Main TeleOp")
public class MainAuto extends OpModeTemplate {

    Pose2d beginPose = new Pose2d(0, 0, 0);

    Action trajToFirstSample;
    Action trajToTurnFirst;
    Action trajToTurnFirst_back;
    Action testTrajectory;
    Action initialTraj;

    @Override
    public void init() {
        initSystems(true);
    }
    @Override
    public void start(){
        trajToFirstSample = drive.actionBuilder(new Pose2d(-29.6,0, 0))
                .strafeToLinearHeading(new Vector2d(-20.6, 26.8), Math.toRadians(141.5))
                .build();
        trajToTurnFirst = drive.actionBuilder(new Pose2d(-20.6,26.8, 141.5))
                .strafeToLinearHeading(new Vector2d(-20.6, 30.8), Math.toRadians(41.5))
                .build();
        trajToTurnFirst_back = drive.actionBuilder(new Pose2d(-20.6,30.8, 41.5))
                .strafeToLinearHeading(new Vector2d(-20.6, 30.9), Math.toRadians(140))
                .build();

        initialTraj = drive.actionBuilder(beginPose).strafeToConstantHeading(new Vector2d(-29.6,0)).build();

        testTrajectory = drive.actionBuilder(new Pose2d(0,0,0))
            .splineToConstantHeading(new Vector2d(-20,0), 0)
            .splineToConstantHeading(new Vector2d(0, 15), 0)
            .splineToConstantHeading(new Vector2d(0, -15), 0)
            .splineToConstantHeading(new Vector2d(-20, 0), 0)
            .build();

        //!This blocks everything -> :C
        Actions.runBlocking(
            new ParallelAction(

                outtakeManager.LoopLift(),
                intakeManager.LoopLift(),

                new SequentialAction(

                    //Drive to Deposit Speciment from start pos
                    new ParallelAction(
                        outtakeManager.DriveLift(1520),
                        initialTraj

                    ),
                    //Score Points
                    outtakeManager.DriveLift(950),
                    new SleepAction(0.3),
                    outtakeManager.OpenCloseSpeciment(true),
                    new SleepAction(0.1),
                    new ParallelAction(
                        //Home Lift
                        new SequentialAction(
                            new SleepAction(0.2),
                            outtakeManager.DriveLift(5)
                        ),
                        //Drive to pick up 2nd speciment
                        new SequentialAction( //-20, 26.8
                                new ParallelAction(
                                        trajToFirstSample,
                                        new SequentialAction(
                                                new SleepAction(1),
                                                intakeManager.GripAction(IntakeManager._GripState.RELEASE),
                                                intakeManager.YawAction(IntakeManager._YawServoState.AUTO_1),
                                                intakeManager.DriveLift(600),
                                                intakeManager.TiltAction(IntakeManager._TiltServoState.AIMING),
                                                new SleepAction(0.5),
                                                intakeManager.TiltAction(IntakeManager._TiltServoState.LOWERED),
                                                new SleepAction(0.2),
                                                intakeManager.GripAction(IntakeManager._GripState.GRIP),
                                                new SleepAction(0.4),
                                                intakeManager.TiltAction(IntakeManager._TiltServoState.AIMING),
                                                intakeManager.DriveLift(815),
                                                trajToTurnFirst,
                                                intakeManager.GripAction(IntakeManager._GripState.RELEASE),
                                                new SleepAction(0.5),
                                                trajToTurnFirst_back,

                                                outtakeManager.StopLift(),
                                                intakeManager.StopLift()


                                        )
                                )
                        )
                    ),

                    //!Super important!
                    outtakeManager.StopLift(),
                    intakeManager.StopLift()


                )
            )
        );
    }

    @Override
    public void loop() {

    }
}
