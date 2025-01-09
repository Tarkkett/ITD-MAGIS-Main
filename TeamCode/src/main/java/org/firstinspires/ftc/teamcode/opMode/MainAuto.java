package org.firstinspires.ftc.teamcode.opMode;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.AngularVelConstraint;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.TurnConstraints;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

@Autonomous(name = "Main Auto", group = "OpMode", preselectTeleOp = "Main TeleOp")
public class MainAuto extends OpModeTemplate {

    private Pose2d beginPose = new Pose2d(0, 0, 0);
    private TurnConstraints turnConstraints = new TurnConstraints(2.3, -2.3, 2.7);

    private Action trajToFirstSample;
    private Action trajFinish;
    private Action trajToTurnFirst;
    private Action trajToTurnScnd;
    private Action trajToSecondSample;
    private Action trajToTurnThrd;
    private Action trajToTurnFirst_back;
    private Action trajToTurnScnd_back;
    private Action trajToThirdSample;
    private Action trajToPickupFirstSpreciment;
    private Action trajToPickup1st;
    private Action trajToPickup3rd;
    private Action trajToPickup4th;
    private Action trajToPickup2nd;
    private Action initialTraj;
    private Action trajToHang1st;
    private Action trajToHang2nd;
    private Action trajToHang3rd;
    private Action trajToHang4th;
    private Action longTraj;

    Action trajToPushFirstSample;

    private final float DIST_X_CHAMBER = 29.6f;
    private final float DIST_Y_CHAMBER = -3f;

    private final float COORD_X_SAMPLE_1st = 21.25f;
    private final float COORD_Y_SAMPLE_1st = -41.9f;

    private final float COORD_Y_SAMPLE_2nd = -52.1f;

    private final float COORD_X_SAMPLE_1st_BACK = 9f;

    private final float COORD_X_SAMPLE_3rd = 24f;
    private final float COORD_Y_SAMPLE_3rd = -52.6f;
    private final double HEADING_Y_SAMPLE_3rd = Math.toRadians(-34);



    @Override
    public void init() {
        initSystems(true);
    }
    @Override
    public void start(){

        initialTraj = drive.actionBuilder(beginPose).strafeToConstantHeading(new Vector2d(DIST_X_CHAMBER,DIST_Y_CHAMBER)).build();

        trajToPushFirstSample = drive.actionBuilder(new Pose2d(DIST_X_CHAMBER,DIST_Y_CHAMBER, 0))

                .build();

        trajToFirstSample = drive.actionBuilder(new Pose2d(DIST_X_CHAMBER,DIST_Y_CHAMBER, 0))
                .strafeToConstantHeading(new Vector2d(COORD_X_SAMPLE_1st, COORD_Y_SAMPLE_1st))
                .build();
        trajToTurnFirst = drive.actionBuilder(new Pose2d(COORD_X_SAMPLE_1st, COORD_Y_SAMPLE_1st, 0))
                .strafeToConstantHeading(new Vector2d(COORD_X_SAMPLE_1st_BACK, COORD_Y_SAMPLE_1st))
                .build();
//        trajToTurnFirst_back = drive.actionBuilder(new Pose2d(COORD_X_SAMPLE_1st, COORD_Y_SAMPLE_1st, 20))
//                .turnTo(Math.toRadians(150.5), turnConstraints)
//                .build();
        trajToSecondSample = drive.actionBuilder(new Pose2d(COORD_X_SAMPLE_1st_BACK, COORD_Y_SAMPLE_1st, 0))
                .strafeToConstantHeading(new Vector2d(COORD_X_SAMPLE_1st, COORD_Y_SAMPLE_2nd))
                .build();
//        trajToTurnScnd_back = drive.actionBuilder(new Pose2d(COORD_X_SAMPLE_1st, COORD_Y_SAMPLE_1st, 20))
//                .turnTo(Math.toRadians(151), turnConstraints)
//                .strafeToConstantHeading(new Vector2d(COORD_X_SAMPLE_1st, 54))
//                .build();
        trajToTurnScnd = drive.actionBuilder(new Pose2d(COORD_X_SAMPLE_1st, COORD_Y_SAMPLE_2nd, 0))
                .strafeToConstantHeading(new Vector2d(COORD_X_SAMPLE_1st_BACK, COORD_Y_SAMPLE_2nd))
                .build();
        trajToThirdSample = drive.actionBuilder(new Pose2d(COORD_X_SAMPLE_1st_BACK, COORD_Y_SAMPLE_2nd, 0))
                .strafeToLinearHeading(new Vector2d(COORD_X_SAMPLE_3rd, COORD_Y_SAMPLE_3rd), HEADING_Y_SAMPLE_3rd)
                .build();
        trajToTurnThrd = drive.actionBuilder(new Pose2d(COORD_X_SAMPLE_3rd,COORD_Y_SAMPLE_3rd, HEADING_Y_SAMPLE_3rd))
                .strafeToLinearHeading(new Vector2d(COORD_X_SAMPLE_1st_BACK, -35), Math.toRadians(0))
                .build();

        trajToPickupFirstSpreciment = drive.actionBuilder(new Pose2d(COORD_X_SAMPLE_1st_BACK, -35, 0))
                .strafeToConstantHeading(new Vector2d(0.4, -35))
                .build();

        trajToHang1st = drive.actionBuilder(new Pose2d(0.4, -35, 0))
                .strafeToConstantHeading(new Vector2d(DIST_X_CHAMBER,0))
                .build();
        trajToHang2nd = drive.actionBuilder(new Pose2d(0.4, -35, 0))
                .strafeToConstantHeading(new Vector2d(DIST_X_CHAMBER,4))
                .build();
        trajToHang3rd = drive.actionBuilder(new Pose2d(0.4, -35, 0))
                .strafeToConstantHeading(new Vector2d(DIST_X_CHAMBER,8))
                .build();
        trajToHang4th = drive.actionBuilder(new Pose2d(0.4, -35, 0))
                .strafeToConstantHeading(new Vector2d(DIST_X_CHAMBER,12))
                .build();

//        testTrajectory = drive.actionBuilder(new Pose2d(0,0,0))
//            .splineToConstantHeading(new Vector2d(-20,0), 0)
//            .splineToConstantHeading(new Vector2d(0, 15), 0)
//            .splineToConstantHeading(new Vector2d(0, -15), 0)
//            .splineToConstantHeading(new Vector2d(-20, 0), 0)
//            .build();

        trajToPickup1st = drive.actionBuilder(new Pose2d(DIST_X_CHAMBER, 0, 0))
                .strafeToConstantHeading(new Vector2d(0.4,-35))
                .build();
        trajToPickup2nd = drive.actionBuilder(new Pose2d(DIST_X_CHAMBER, 4, 0))
                .strafeToConstantHeading(new Vector2d(0.4,-35))
                .build();
        trajToPickup3rd = drive.actionBuilder(new Pose2d(DIST_X_CHAMBER, 8, 0))
                .strafeToConstantHeading(new Vector2d(0.4,-35))
                .build();
        trajToPickup4th = drive.actionBuilder(new Pose2d(DIST_X_CHAMBER, 12, 0))
                .strafeToConstantHeading(new Vector2d(0.4,-35))
                .build();

        trajFinish = drive.actionBuilder(new Pose2d(DIST_X_CHAMBER, -8, 0))
                .strafeToLinearHeading(new Vector2d(25, 0), Math.toRadians(180))
                .build();

        //!This blocks everything -> :C
        Actions.runBlocking(
            new ParallelAction(

                outtakeManager.LoopLift(),
                intakeManager.LoopLift(),

                new SequentialAction(

                    new ParallelAction(
                            outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.DEPOSIT),
                            outtakeManager.DriveLift((int) OuttakeManager._LiftState.HIGH_CHAMBER.getPosition()),
                            intakeManager.GripAction(IntakeManager._GripState.RELEASE),
                            initialTraj
                    ),
                    outtakeManager.DriveLift((int) OuttakeManager._LiftState.HIGH_CHAMBER_LOWER.getPosition()),
                    new SleepAction(0.5),
                    outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.RELEASE),
                    outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.PICKUP),
                    //-----------------------------
                    outtakeManager.DriveLift((int) OuttakeManager._LiftState.ZERO.getPosition()),
                    longTraj,


//                    outtakeManager.DriveLift((int) OuttakeManager._LiftState.CLEARED.getPosition()),
//                    new ParallelAction(
//                            trajToFirstSample,
//                            new SequentialAction(
//                                    new SleepAction(0.4),
//                                    intakeManager.DriveLift(0),
//                                    intakeManager.YawAction(IntakeManager._YawServoState.AUTO_1),
//                                    intakeManager.TiltAction(IntakeManager._TiltServoState.AIMING),
//                                    new SleepAction(0.2),
//                                    outtakeManager.DriveLift((int) OuttakeManager._LiftState.TRANSFER.getPosition())
//                            )
//                    ),
//
//                    intakeManager.TiltAction(IntakeManager._TiltServoState.LOWERED),
//                    new SleepAction(0.1),
//                    intakeManager.GripAction(IntakeManager._GripState.GRIP),
//                    new SleepAction(0.2),
//                    intakeManager.TiltAction(IntakeManager._TiltServoState.TRANSFER),
//                    new SleepAction(0.3),
//                    new ParallelAction(
//                            intakeManager.DriveLift((int) IntakeManager._SlideState.EXTENDED.getPosition()),
//                            intakeManager.YawAction(IntakeManager._YawServoState.TRANSFER),
//                            outtakeManager.TiltAction(OuttakeManager._OuttakeTiltServoState.TRANSFER),
//                            outtakeManager.YawAction(OuttakeManager._OuttakeYawServoState.VERTICAL)
//                    ),
//                    new SleepAction(0.2),
//
//                    new ParallelAction(
//                            trajToTurnFirst,
//                            new SequentialAction(
//                                    outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.TRANSFER),
//                                    intakeManager.DriveLift((int) IntakeManager._SlideState.TRANSFER.getPosition()),
//                                    new SleepAction(0.5),
//                                    outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.GRIP),
//                                    new SleepAction(0.2),
//                                    intakeManager.GripAction(IntakeManager._GripState.RELEASE),
//                                    new SleepAction(0.3),
//                                    intakeManager.TiltAction(IntakeManager._TiltServoState.PACKED),
//                                    outtakeManager.DriveLift((int) OuttakeManager._LiftState.CLEARED.getPosition()),
//                                    outtakeManager.YawAction(OuttakeManager._OuttakeYawServoState.HORIZONTAL_ServoDown),
//                                    outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.DEPOSIT),
//                                    outtakeManager.TiltAction(OuttakeManager._OuttakeTiltServoState.PICKUP),
//                                    new SleepAction(0.6),
//                                    outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.PICKUP),
//                                    intakeManager.TiltAction(IntakeManager._TiltServoState.AIMING),
//                                    intakeManager.YawAction(IntakeManager._YawServoState.AUTO_1),
//                                    intakeManager.DriveLift(0),
//                                    new SleepAction(0.2),
//                                    outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.RELEASE)
//                            )
//                    ),
//                    new ParallelAction(
//                            trajToSecondSample,
//                            new SequentialAction(
//                                    outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.TRANSFER),
//                                    new SleepAction(0.3),
//                                    outtakeManager.TiltAction(OuttakeManager._OuttakeTiltServoState.TRANSFER)
//                            )
//
//                    ),
//                    outtakeManager.DriveLift((int) OuttakeManager._LiftState.TRANSFER.getPosition()),
//                    intakeManager.TiltAction(IntakeManager._TiltServoState.LOWERED),
//                    new SleepAction(0.2),
//                    intakeManager.GripAction(IntakeManager._GripState.GRIP),
//
//                    //===================================================
//
//                    new SleepAction(0.2),
//                    intakeManager.TiltAction(IntakeManager._TiltServoState.TRANSFER),
//                    new SleepAction(0.3),
//                    new ParallelAction(
//                            intakeManager.DriveLift((int) IntakeManager._SlideState.EXTENDED.getPosition()),
//                            intakeManager.YawAction(IntakeManager._YawServoState.TRANSFER),
//                            outtakeManager.TiltAction(OuttakeManager._OuttakeTiltServoState.TRANSFER),
//                            outtakeManager.YawAction(OuttakeManager._OuttakeYawServoState.VERTICAL)
//                    ),
//                    new SleepAction(0.2),
//
//                    new ParallelAction(
//                            trajToTurnScnd,
//                            new SequentialAction(
//                                    outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.TRANSFER),
//                                    intakeManager.DriveLift((int) IntakeManager._SlideState.TRANSFER.getPosition()),
//                                    new SleepAction(0.5),
//                                    outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.GRIP),
//                                    new SleepAction(0.2),
//                                    intakeManager.GripAction(IntakeManager._GripState.RELEASE),
//                                    new SleepAction(0.3),
//                                    intakeManager.TiltAction(IntakeManager._TiltServoState.PACKED),
//                                    outtakeManager.DriveLift((int) OuttakeManager._LiftState.CLEARED.getPosition()),
//                                    outtakeManager.YawAction(OuttakeManager._OuttakeYawServoState.HORIZONTAL_ServoDown),
//                                    outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.DEPOSIT),
//                                    outtakeManager.TiltAction(OuttakeManager._OuttakeTiltServoState.PICKUP),
//                                    new SleepAction(0.6),
//                                    outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.PICKUP),
//                                    intakeManager.TiltAction(IntakeManager._TiltServoState.AIMING),
//                                    intakeManager.YawAction(IntakeManager._YawServoState.AUTO_2),
//                                    intakeManager.DriveLift(0),
//                                    new SleepAction(0.2),
//                                    outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.RELEASE)
//                            )
//                    ),
//                    new ParallelAction(
//                            trajToThirdSample,
//                            new SequentialAction(
//                                    outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.TRANSFER),
//                                    new SleepAction(0.3),
//                                    outtakeManager.TiltAction(OuttakeManager._OuttakeTiltServoState.TRANSFER)
//                            )
//
//                    ),
//
//                    outtakeManager.DriveLift((int) OuttakeManager._LiftState.TRANSFER.getPosition()),
//                    intakeManager.TiltAction(IntakeManager._TiltServoState.LOWERED),
//                    new SleepAction(0.2),
//                    intakeManager.GripAction(IntakeManager._GripState.GRIP),
//
//                    //========================================== 3 back with transfer
//
//                    new SleepAction(0.2),
//                    intakeManager.TiltAction(IntakeManager._TiltServoState.TRANSFER),
//                    new SleepAction(0.3),
//                    new ParallelAction(
//                            intakeManager.DriveLift((int) IntakeManager._SlideState.EXTENDED.getPosition()),
//                            intakeManager.YawAction(IntakeManager._YawServoState.TRANSFER),
//                            outtakeManager.TiltAction(OuttakeManager._OuttakeTiltServoState.TRANSFER),
//                            outtakeManager.YawAction(OuttakeManager._OuttakeYawServoState.VERTICAL)
//                    ),
//                    new SleepAction(0.2),
//
//                    new ParallelAction(
//                            trajToTurnThrd,
//                            new SequentialAction(
//                                    outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.TRANSFER),
//                                    intakeManager.DriveLift((int) IntakeManager._SlideState.TRANSFER.getPosition()),
//                                    new SleepAction(0.5),
//                                    outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.GRIP),
//                                    new SleepAction(0.2),
//                                    intakeManager.GripAction(IntakeManager._GripState.RELEASE),
//                                    new SleepAction(0.3),
//                                    intakeManager.TiltAction(IntakeManager._TiltServoState.PACKED),
//                                    outtakeManager.DriveLift((int) OuttakeManager._LiftState.CLEARED.getPosition()),
//                                    outtakeManager.YawAction(OuttakeManager._OuttakeYawServoState.HORIZONTAL_ServoDown),
//                                    outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.DEPOSIT),
//                                    outtakeManager.TiltAction(OuttakeManager._OuttakeTiltServoState.PICKUP),
//                                    new SleepAction(0.6),
//                                    outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.PICKUP),
//                                    intakeManager.DriveLift(0),
//                                    new SleepAction(0.2),
//                                    outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.RELEASE),
//                                    outtakeManager.DriveLift(0)
//                            )
//                    ),
//                    outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.ZERO),
//                    trajToPickupFirstSpreciment,
//
//                    new SleepAction(0.25),
//                    outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.GRIP),
//                    new SleepAction(0.15),
//                    outtakeManager.DriveLift((int) OuttakeManager._LiftState.HIGH_CHAMBER.getPosition()),
//                    new ParallelAction(
//                            trajToHang1st,
//                            new SequentialAction(
//                                    outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.DEPOSIT),
//                                    new SleepAction(0.3),
//                                    outtakeManager.TiltAction(OuttakeManager._OuttakeTiltServoState.DEPOSIT),
//                                    new SleepAction(0.5),
//                                    outtakeManager.YawAction(OuttakeManager._OuttakeYawServoState.HORIZONTAL_ServoUp)
//                            )
//                    ),
//                    outtakeManager.DriveLift((int) OuttakeManager._LiftState.HIGH_CHAMBER_LOWER.getPosition()),
//                    new SleepAction(0.5),
//                    outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.RELEASE),
//                    new SleepAction(0.2),
//                    //PICKUP
//                    new ParallelAction(
//                            trajToPickup1st,
//                            new SequentialAction(
//                                    outtakeManager.TiltAction(OuttakeManager._OuttakeTiltServoState.PICKUP),
//                                    new SleepAction(0.5),
//                                    outtakeManager.DriveLift(0),
//                                    outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.ZERO)
//                            )
//                    ),
//                    outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.GRIP),
//                    new SleepAction(0.15),
//                    outtakeManager.DriveLift((int) OuttakeManager._LiftState.HIGH_CHAMBER.getPosition()),
//                    //DEPOSIT
//                    new ParallelAction(
//                            trajToHang2nd,
//                            new SequentialAction(
//                                    outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.DEPOSIT),
//                                    new SleepAction(0.3),
//                                    outtakeManager.TiltAction(OuttakeManager._OuttakeTiltServoState.DEPOSIT),
//                                    new SleepAction(0.5),
//                                    outtakeManager.YawAction(OuttakeManager._OuttakeYawServoState.HORIZONTAL_ServoDown)
//                            )
//                    ),
//                    outtakeManager.DriveLift((int) OuttakeManager._LiftState.HIGH_CHAMBER_LOWER.getPosition()),
//                    new SleepAction(0.5),
//                    outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.RELEASE),
//                    new SleepAction(0.2),
//                    //==
//                        new ParallelAction(
//                                trajToPickup2nd,
//                                new SequentialAction(
//                                        outtakeManager.TiltAction(OuttakeManager._OuttakeTiltServoState.PICKUP),
//                                        new SleepAction(0.5),
//                                        outtakeManager.DriveLift(0),
//                                        outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.ZERO)
//                                )
//                        ),
//                        outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.GRIP),
//                        new SleepAction(0.15),
//                        outtakeManager.DriveLift((int) OuttakeManager._LiftState.HIGH_CHAMBER.getPosition()),
//                    //==
//                        new ParallelAction(
//                                trajToHang3rd,
//                                new SequentialAction(
//                                        outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.DEPOSIT),
//                                        new SleepAction(0.45),
//                                        outtakeManager.TiltAction(OuttakeManager._OuttakeTiltServoState.DEPOSIT),
//                                        new SleepAction(0.5),
//                                        outtakeManager.YawAction(OuttakeManager._OuttakeYawServoState.HORIZONTAL_ServoDown)
//                                )
//                        ),
//                        outtakeManager.DriveLift((int) OuttakeManager._LiftState.HIGH_CHAMBER_LOWER.getPosition()),
//                        new SleepAction(0.5),
//                        outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.RELEASE),
//                        new SleepAction(0.2),



                    //!Super important!
                    outtakeManager.StopLift(),
                    intakeManager.StopLift()


                )
            )
        );
    }

    @Override
    public void loop() {
        //? RunBlocking() blocks this
    }
}
