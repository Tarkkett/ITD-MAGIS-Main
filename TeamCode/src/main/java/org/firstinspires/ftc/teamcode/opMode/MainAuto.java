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
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.commands.low_level.intake.AdjustYawServoCommand;
import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

@Autonomous(name = "Main Auto", group = "OpMode", preselectTeleOp = "Main TeleOp")
public class MainAuto extends OpModeTemplate {

    Pose2d beginPose = new Pose2d(0, 0, 0);

    TurnConstraints turnConstraints = new TurnConstraints(2.3, -2.3, 2.7);

    Action trajToFirstSample;
    Action trajFinish;
    Action trajToTurnFirst;
    Action trajToTurnScnd;
    Action trajToTurnThrd;
    Action trajToTurnFirst_back;
    Action trajToTurnScnd_back;
    Action testTrajectory;
    Action trajToPickupFirstSpreciment;
    Action trajToPickup1st;
    Action trajToPickup3rd;
    Action trajToPickup2nd;
    Action initialTraj;
    Action trajToHang1st;
    Action trajToHang2nd;
    Action trajToHang3rd;

    @Override
    public void init() {
        initSystems(true);
    }
    @Override
    public void start(){

        initialTraj = drive.actionBuilder(beginPose).strafeToConstantHeading(new Vector2d(-29.8,3)).build();

        trajToFirstSample = drive.actionBuilder(new Pose2d(-29.8,3, 0))
                .strafeToLinearHeading(new Vector2d(-20.4, 41), Math.toRadians(176))
                .build();
        trajToTurnFirst = drive.actionBuilder(new Pose2d(-20.4,41, 176))
                .turnTo(Math.toRadians(20), turnConstraints)
                .build();
        trajToTurnFirst_back = drive.actionBuilder(new Pose2d(-20.4,41, 20))
                .turnTo(Math.toRadians(150.5), turnConstraints)
                .build();
        trajToTurnScnd = drive.actionBuilder(new Pose2d(-20.4,41, 151))
                .turnTo(Math.toRadians(20), turnConstraints)
                .build();
        trajToTurnScnd_back = drive.actionBuilder(new Pose2d(-20.4,41, 20))
                .turnTo(Math.toRadians(151), turnConstraints)
                .strafeToConstantHeading(new Vector2d(-20.4, 54))
                .build();

        trajToTurnThrd = drive.actionBuilder(new Pose2d(-20.4,54, 150.5))
                .strafeToLinearHeading(new Vector2d(-4, 35), Math.toRadians(90), new AngularVelConstraint(0.9))
                .build();

        trajToPickupFirstSpreciment = drive.actionBuilder(new Pose2d(-20.4,41, 20))
                .strafeToLinearHeading(new Vector2d(-1.9, 40), Math.toRadians(180), new TranslationalVelConstraint(15))
                .build();

        trajToHang1st = drive.actionBuilder(new Pose2d(-1.9, 40, 180))
                .strafeToLinearHeading(new Vector2d(-29.8,-2), Math.toRadians(0))
                .build();
        trajToHang2nd = drive.actionBuilder(new Pose2d(-2, 35, 180))
                .strafeToLinearHeading(new Vector2d(-29.8,-5), Math.toRadians(0))
                .build();
        trajToHang3rd = drive.actionBuilder(new Pose2d(-2, 35, 180))
                .strafeToLinearHeading(new Vector2d(-29.8,-8), Math.toRadians(0))
                .build();

        testTrajectory = drive.actionBuilder(new Pose2d(0,0,0))
            .splineToConstantHeading(new Vector2d(-20,0), 0)
            .splineToConstantHeading(new Vector2d(0, 15), 0)
            .splineToConstantHeading(new Vector2d(0, -15), 0)
            .splineToConstantHeading(new Vector2d(-20, 0), 0)
            .build();

        trajToPickup1st = drive.actionBuilder(new Pose2d(-29.8, -2, 0))
                .strafeToLinearHeading(new Vector2d(-2,35), Math.toRadians(180))
                .build();
        trajToPickup2nd = drive.actionBuilder(new Pose2d(-29.8, -5, 0))
                .strafeToLinearHeading(new Vector2d(-2,35), Math.toRadians(180))
                .build();
        trajToPickup3rd = drive.actionBuilder(new Pose2d(-29.8, -8, 0))
                .strafeToLinearHeading(new Vector2d(-2,35), Math.toRadians(180))
                .build();

        trajFinish = drive.actionBuilder(new Pose2d(-29.8, -8, 0))
                .strafeToLinearHeading(new Vector2d(-25, 0), Math.toRadians(180))
                .build();

        //!This blocks everything -> :C
        Actions.runBlocking(
            new ParallelAction(

                outtakeManager.LoopLift(),
                intakeManager.LoopLift(),

                new SequentialAction(

                    new ParallelAction(
                            outtakeManager.DriveLift(1520),//1520
                            intakeManager.GripAction(IntakeManager._GripState.RELEASE),
                            initialTraj

                    ),
                    outtakeManager.DriveLift(950),
                    intakeManager.DriveLift(90),
                    new SleepAction(0.2),
                    outtakeManager.SpecimentAction(OuttakeManager._SpecimentServoState.OPEN),
                    new SleepAction(0.1),
                    new SequentialAction(
                            new SleepAction(0.2),
                            outtakeManager.DriveLift(5),
                            intakeManager.TiltAction(IntakeManager._TiltServoState.CLEARED)
                    ),
                    new ParallelAction(
                            trajToFirstSample,
                            intakeManager.DriveLift(200),
                            intakeManager.YawAction(IntakeManager._YawServoState.AUTO_1),
                            intakeManager.TiltAction(IntakeManager._TiltServoState.AIMING)
                    ),
                    intakeManager.TiltAction(IntakeManager._TiltServoState.LOWERED),
                    new SleepAction(0.2),
                    intakeManager.GripAction(IntakeManager._GripState.GRIP),
                    new SleepAction(0.2),
                    intakeManager.TiltAction(IntakeManager._TiltServoState.AIMING),
                    new ParallelAction(
                        trajToTurnFirst,
                        intakeManager.DriveLift(400)
                    ),
                    intakeManager.YawAction(IntakeManager._YawServoState.AUTO_2),
                    intakeManager.GripAction(IntakeManager._GripState.RELEASE),
                    new SleepAction(0.4),
                    new ParallelAction(
                            trajToTurnFirst_back
                    ),
                    intakeManager.TiltAction(IntakeManager._TiltServoState.LOWERED),
                    new SleepAction(0.2),
                    intakeManager.GripAction(IntakeManager._GripState.GRIP),
                    new SleepAction(0.35),
                    intakeManager.TiltAction(IntakeManager._TiltServoState.AIMING),
                    trajToTurnScnd,
                    intakeManager.GripAction(IntakeManager._GripState.RELEASE),
                    new SleepAction(0.3f),
                    intakeManager.DriveLift(50),
                    trajToPickupFirstSpreciment,
                        outtakeManager.SpecimentAction(OuttakeManager._SpecimentServoState.CLOSED),
                    new SleepAction(0.3),
                    new ParallelAction(
                            outtakeManager.DriveLift(1520),
                            trajToHang1st
                    ),
                    outtakeManager.DriveLift(950),
                    new SleepAction(0.2),
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
                    new SleepAction(0.2),
                    outtakeManager.SpecimentAction(OuttakeManager._SpecimentServoState.OPEN),
                    new SleepAction(0.1),
                    outtakeManager.DriveLift(5),
                    new SleepAction(0.1),
                    new ParallelAction(
                            outtakeManager.DriveLift(5),
                            trajToPickup3rd
                    ),
                    outtakeManager.SpecimentAction(OuttakeManager._SpecimentServoState.CLOSED),
                    new SleepAction(0.3),
                    outtakeManager.DriveLift(1520),
                    intakeManager.TiltAction(IntakeManager._TiltServoState.TRANSFER),
                    trajToHang3rd,
                    outtakeManager.DriveLift(950),
                    new SleepAction(0.2),
                    outtakeManager.SpecimentAction(OuttakeManager._SpecimentServoState.OPEN),
                    new SleepAction(0.1),
                    outtakeManager.DriveLift(5),

                    //!Super important!
                    outtakeManager.StopLift(),
                    intakeManager.StopLift(),
                    trajFinish


                )
            )
        );
    }

    @Override
    public void loop() {

    }
}
