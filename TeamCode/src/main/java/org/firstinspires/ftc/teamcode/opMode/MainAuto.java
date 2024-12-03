package org.firstinspires.ftc.teamcode.opMode;

import com.acmerobotics.roadrunner.AccelConstraint;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.AngularVelConstraint;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.TurnConstraints;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.VelConstraint;
import com.acmerobotics.roadrunner.ftc.Actions;

import com.arcrobotics.ftclib.command.ParallelRaceGroup;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

@Autonomous(name = "Main Auto", group = "OpMode", preselectTeleOp = "Main TeleOp")
public class MainAuto extends OpModeTemplate {

    Pose2d beginPose = new Pose2d(0, 0, 0);

    Action trajToFirstSample;
    Action trajToTurnFirst;
    Action trajToTurnScnd;
    Action trajToTurnThrd;
    Action trajToTurnFirst_back;
    Action trajToTurnScnd_back;
    Action testTrajectory;
    Action trajToPickupFirstSpreciment;
    Action trajToPickup1st;
    Action trajToPickup2nd;
    Action initialTraj;
    Action trajToHang1st;
    Action trajToHang2nd;

    @Override
    public void init() {
        initSystems(true);
    }
    @Override
    public void start(){

        initialTraj = drive.actionBuilder(beginPose).strafeToConstantHeading(new Vector2d(-29.6,0)).build();

        trajToFirstSample = drive.actionBuilder(new Pose2d(-29.6,0, 0))
                .strafeToLinearHeading(new Vector2d(-20.4, 28), Math.toRadians(141))
                .build();
        trajToTurnFirst = drive.actionBuilder(new Pose2d(-20.4,28, 141))
                .turnTo(Math.toRadians(20), new TurnConstraints(4, -5, 2.5))
                .build();
        trajToTurnFirst_back = drive.actionBuilder(new Pose2d(-20.4,28, 20))
                .strafeToLinearHeading(new Vector2d(-20.4, 41), Math.toRadians(145.5), new AngularVelConstraint(0.9))
                .build();
        trajToTurnScnd = drive.actionBuilder(new Pose2d(-20.4,41, 145.5))
                .turnTo(Math.toRadians(20), new TurnConstraints(4, -5, 2.5))
                .build();
        trajToTurnScnd_back = drive.actionBuilder(new Pose2d(-20.4,41, 20))
                .turnTo(Math.toRadians(150.5), new TurnConstraints(4, -5, 2.5))
                .strafeToConstantHeading(new Vector2d(-20.4, 54))
                .build();

        trajToTurnThrd = drive.actionBuilder(new Pose2d(-20.4,54, 150.5))
                .strafeToLinearHeading(new Vector2d(-4, 35), Math.toRadians(90), new AngularVelConstraint(0.9))
                .build();

        trajToPickupFirstSpreciment = drive.actionBuilder(new Pose2d(-4,35, 90))
                .turnTo(Math.toRadians(180), new TurnConstraints(4, -5, 2.5))
                .strafeToConstantHeading(new Vector2d(-2, 35))
                .build();

        trajToHang1st = drive.actionBuilder(new Pose2d(-2, 35, 180))
                .strafeToLinearHeading(new Vector2d(-29.6,-3), Math.toRadians(0))
                .build();
        trajToHang2nd = drive.actionBuilder(new Pose2d(-2, 35, 180))
                .strafeToLinearHeading(new Vector2d(-29.6,-6), Math.toRadians(0))
                .build();

        testTrajectory = drive.actionBuilder(new Pose2d(0,0,0))
            .splineToConstantHeading(new Vector2d(-20,0), 0)
            .splineToConstantHeading(new Vector2d(0, 15), 0)
            .splineToConstantHeading(new Vector2d(0, -15), 0)
            .splineToConstantHeading(new Vector2d(-20, 0), 0)
            .build();

        trajToPickup1st = drive.actionBuilder(new Pose2d(-29.6, -3, 0))
                .strafeToLinearHeading(new Vector2d(-2,35), Math.toRadians(180))
                .build();
        trajToPickup2nd = drive.actionBuilder(new Pose2d(-29.6, -6, 0))
                .strafeToLinearHeading(new Vector2d(-2,35), Math.toRadians(180))
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
                        intakeManager.GripAction(IntakeManager._GripState.RELEASE),
                        initialTraj

                    ),
                    //Score Points
                    outtakeManager.DriveLift(950),
                    intakeManager.DriveLift(90),
                    new SleepAction(0.2),//0.3
                    outtakeManager.SpecimentAction(OuttakeManager._SpecimentServoState.OPEN),
                    new SleepAction(0.1),
                    new ParallelAction(
                        //Home Lift
                        new SequentialAction(
                            new SleepAction(0.2),
                            outtakeManager.DriveLift(5),
                            intakeManager.TiltAction(IntakeManager._TiltServoState.CLEARED)
                        ),
                        //Drive to pick up 2nd speciment
                        new SequentialAction( //-20, 26.8
                                new ParallelAction(
                                        trajToFirstSample,
                                        new SequentialAction(
                                                new SleepAction(1),
                                                outtakeManager.SpecimentAction(OuttakeManager._SpecimentServoState.OPEN),
                                                intakeManager.GripAction(IntakeManager._GripState.RELEASE),
                                                intakeManager.YawAction(IntakeManager._YawServoState.AUTO_1),
                                                intakeManager.DriveLift(570),
//                                                intakeManager.TiltAction(IntakeManager._TiltServoState.AIMING),
//                                                new SleepAction(0.5),
                                                intakeManager.TiltAction(IntakeManager._TiltServoState.LOWERED),
                                                new SleepAction(0.4),
                                                intakeManager.GripAction(IntakeManager._GripState.GRIP),
                                                new SleepAction(0.4),
                                                intakeManager.TiltAction(IntakeManager._TiltServoState.AIMING),
                                                trajToTurnFirst,
                                                intakeManager.GripAction(IntakeManager._GripState.RELEASE),
                                                new SleepAction(0.3),
                                                intakeManager.DriveLift(400),
                                                trajToTurnFirst_back,
                                                new SleepAction(0.15),
                                                intakeManager.TiltAction(IntakeManager._TiltServoState.LOWERED),
                                                new SleepAction(0.2),
                                                intakeManager.GripAction(IntakeManager._GripState.GRIP),
                                                new SleepAction(0.2),
                                                intakeManager.TiltAction(IntakeManager._TiltServoState.AIMING),
                                                trajToTurnScnd,
                                                intakeManager.GripAction(IntakeManager._GripState.RELEASE),
                                                new SleepAction(0.5),
                                                intakeManager.DriveLift(320),
                                                trajToTurnScnd_back,
                                                new SleepAction(0.15),
                                                intakeManager.TiltAction(IntakeManager._TiltServoState.LOWERED),
                                                new SleepAction(0.2),
                                                intakeManager.GripAction(IntakeManager._GripState.GRIP),
                                                new SleepAction(0.2),
                                                intakeManager.TiltAction(IntakeManager._TiltServoState.CLEARED),
                                                intakeManager.DriveLift(0),
                                                trajToTurnThrd,
                                                intakeManager.TiltAction(IntakeManager._TiltServoState.AIMING),
                                                new SleepAction(0.2),
                                                intakeManager.GripAction(IntakeManager._GripState.RELEASE),
                                                new SleepAction(0.3),
                                                trajToPickupFirstSpreciment,
                                                outtakeManager.SpecimentAction(OuttakeManager._SpecimentServoState.CLOSED),
                                                new SleepAction(0.3),
                                                new ParallelAction(
                                                        outtakeManager.DriveLift(1520),
                                                        trajToHang1st
                                                ),
                                                outtakeManager.DriveLift(950),
                                                outtakeManager.SpecimentAction(OuttakeManager._SpecimentServoState.OPEN),
                                                new SleepAction(0.1),
                                                new ParallelAction(
                                                        outtakeManager.DriveLift(5),
                                                        trajToPickup1st
                                                ),
                                                outtakeManager.SpecimentAction(OuttakeManager._SpecimentServoState.CLOSED),
                                                new SleepAction(0.3),
                                                outtakeManager.DriveLift(1520),
                                                trajToHang2nd,
                                                outtakeManager.DriveLift(950),
                                                outtakeManager.SpecimentAction(OuttakeManager._SpecimentServoState.OPEN),
                                                new SleepAction(0.1),
                                                outtakeManager.DriveLift(5),
                                                trajToPickup2nd


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
