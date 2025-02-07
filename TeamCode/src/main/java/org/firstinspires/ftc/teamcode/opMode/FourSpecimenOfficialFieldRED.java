package org.firstinspires.ftc.teamcode.opMode;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.TurnConstraints;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;
import org.threeten.bp.zone.ZoneRules;

@Autonomous(name = "Main Auto RED", group = "OpMode", preselectTeleOp = "Main TeleOp")
public class FourSpecimenOfficialFieldRED extends OpModeTemplate {

    private Pose2d beginPose = new Pose2d(0, 0, 0);
    private TurnConstraints turnConstraints = new TurnConstraints(2.3, -2.3, 2.7);
    private TranslationalVelConstraint maxVel = new TranslationalVelConstraint(60);
    private TranslationalVelConstraint minVel = new TranslationalVelConstraint(45);

    private Action trajToPickup1st;

    private Action trajToPickup2nd;
    private Action initialTraj;
    private Action trajToHang1st;
    private Action trajToHang2nd;
    private Action trajToHang3rd;

    private Action longTraj;
    private Action endTraj;
    private Action testTraj;

    private final float DIST_X_CHAMBER = 11.5f;
    private final float DIST_Y_CHAMBER = -32.9f;
    private final float DIST_Y_ZONE = -64.3f;

    private Action mainAutonomous;

    @Override
    public void init() {
        initSystems(true);

        initialTraj = drive.actionBuilder(new Pose2d(new Vector2d(9, DIST_Y_ZONE), Math.toRadians(-270)))
                .strafeToConstantHeading(new Vector2d(DIST_X_CHAMBER, DIST_Y_CHAMBER), new TranslationalVelConstraint(50), new ProfileAccelConstraint(-25, 55))
                .build();

        trajToHang1st = drive.actionBuilder(new Pose2d(50, DIST_Y_ZONE, Math.toRadians(-270)))
                .splineToConstantHeading(new Vector2d(8, DIST_Y_CHAMBER), Math.toRadians(-270), null, new ProfileAccelConstraint(-23, 55))
                .build();

        trajToHang2nd = drive.actionBuilder(new Pose2d(38, DIST_Y_ZONE, Math.toRadians(-270)))
                .splineToConstantHeading(new Vector2d(5, DIST_Y_CHAMBER), Math.toRadians(-270),null, new ProfileAccelConstraint(-23, 55))
                .build();

        trajToHang3rd = drive.actionBuilder(new Pose2d(38, DIST_Y_ZONE, Math.toRadians(-270)))
                .splineToConstantHeading(new Vector2d(3, DIST_Y_CHAMBER), Math.toRadians(-270),null, new ProfileAccelConstraint(-23, 55))
                .build();

        trajToPickup1st = drive.actionBuilder(new Pose2d(8, DIST_Y_CHAMBER, Math.toRadians(-270)))
                .setReversed(true) //Pickup 1
                .splineToConstantHeading(new Vector2d(38, DIST_Y_ZONE), Math.toRadians(-90), null, new ProfileAccelConstraint(-25, 55))
                .build();
        trajToPickup2nd = drive.actionBuilder(new Pose2d(5, DIST_Y_CHAMBER, Math.toRadians(-270)))
                .setReversed(true)
                .splineToConstantHeading(new Vector2d(38, DIST_Y_ZONE), Math.toRadians(-90), null, new ProfileAccelConstraint(-25, 55))
                .build();

        endTraj = drive.actionBuilder(new Pose2d(3, DIST_Y_CHAMBER, Math.toRadians(-270)))
                .strafeToConstantHeading(new Vector2d(55, DIST_Y_ZONE), new TranslationalVelConstraint(100), new ProfileAccelConstraint(-100, 100))
                .build();

        testTraj = drive.actionBuilder(new Pose2d(new Vector2d(9, DIST_Y_ZONE), Math.toRadians(-270)))
                .strafeToConstantHeading(new Vector2d(DIST_X_CHAMBER, DIST_Y_CHAMBER))
                .waitSeconds(0.2f)

                .strafeToConstantHeading(new Vector2d(30, DIST_Y_CHAMBER))
                .splineToConstantHeading(new Vector2d(46, -15), Math.toRadians(-20))
                .splineToConstantHeading(new Vector2d(50, -25), Math.toRadians(-90))
                .strafeToConstantHeading(new Vector2d(50, -55), new TranslationalVelConstraint(80), new ProfileAccelConstraint(-32, 1))

                .splineToConstantHeading(new Vector2d(43, -55), Math.toRadians(50), new TranslationalVelConstraint(20))
                .splineToConstantHeading(new Vector2d(56, -10), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(58, -20), Math.toRadians(-90))
                .strafeToConstantHeading(new Vector2d(58, -55), null, new ProfileAccelConstraint(-32, 1))

                .splineToConstantHeading(new Vector2d(53, -50), Math.toRadians(50), new TranslationalVelConstraint(20))
                .splineToConstantHeading(new Vector2d(63, -10), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(65, -20), Math.toRadians(-90), new TranslationalVelConstraint(25))
                .strafeToConstantHeading(new Vector2d(65, -50))

                .splineToConstantHeading(new Vector2d(50, DIST_Y_ZONE), Math.toRadians(-90))

                //Cycle >>>>>>> 1
                .waitSeconds(0.12f) //Hang 1
                .splineToConstantHeading(new Vector2d(8, DIST_Y_CHAMBER), Math.toRadians(90))

                .waitSeconds(0.12f)
                .setReversed(true) //Pickup 1
                .splineToConstantHeading(new Vector2d(43, -60), Math.toRadians(-90))
                //>>>>>>>>>>>>>>
                //Cycle >>>>>>> 2
                .waitSeconds(0.12f) //Hang 2
                .splineToConstantHeading(new Vector2d(6, DIST_Y_CHAMBER), Math.toRadians(90))

                .waitSeconds(0.12f) //Pickup 2
                .setReversed(true)
                .splineToConstantHeading(new Vector2d(43, -60), Math.toRadians(-90))
                //>>>>>>>>>>>>>>
                //Cycle >>>>>>> 3
                .waitSeconds(0.12f) //Hang 3
                .splineToConstantHeading(new Vector2d(4, DIST_Y_CHAMBER), Math.toRadians(90))

                .waitSeconds(0.12f) //Pickup 3
                .setReversed(true)
                .splineToConstantHeading(new Vector2d(43, -60), Math.toRadians(-90))
                //>>>>>>>>>>>>>>
                //Cycle >>>>>>> 4
                .waitSeconds(0.12f) //Hang 4
                .splineToConstantHeading(new Vector2d(2, -36), Math.toRadians(90))
                .build();

        longTraj = drive.actionBuilder(new Pose2d(DIST_X_CHAMBER, DIST_Y_CHAMBER, Math.toRadians(-270)))
                .strafeToConstantHeading(new Vector2d(30, -36), new TranslationalVelConstraint(35))
                .splineToConstantHeading(new Vector2d(48, -10), Math.toRadians(-20), new TranslationalVelConstraint(30))
                .splineToConstantHeading(new Vector2d(50, -20), Math.toRadians(-90), new TranslationalVelConstraint(18))
                .strafeToConstantHeading(new Vector2d(50, -50), new TranslationalVelConstraint(50))

                .splineToConstantHeading(new Vector2d(50, -51), Math.toRadians(-90), new TranslationalVelConstraint(15), new ProfileAccelConstraint(-25, 10))
                .splineToConstantHeading(new Vector2d(50, -50), Math.toRadians(-270), new TranslationalVelConstraint(15), new ProfileAccelConstraint(-25, 10))

                .strafeToConstantHeading(new Vector2d(50, -15), new TranslationalVelConstraint(52), new ProfileAccelConstraint(-100, 20))

                .splineToConstantHeading(new Vector2d(58, -15), Math.toRadians(-90), new TranslationalVelConstraint(15))
                .strafeToConstantHeading(new Vector2d(58, -50), new TranslationalVelConstraint(52))

                .splineToConstantHeading(new Vector2d(58, -51), Math.toRadians(-90), new TranslationalVelConstraint(15), new ProfileAccelConstraint(-25, 10))
                .splineToConstantHeading(new Vector2d(58, -50), Math.toRadians(-270), new TranslationalVelConstraint(15), new ProfileAccelConstraint(-25, 10))

                .strafeToConstantHeading(new Vector2d(58, -15), new TranslationalVelConstraint(52), new ProfileAccelConstraint(-30, 20))

                .splineToConstantHeading(new Vector2d(65.5, -15), Math.toRadians(-90), new TranslationalVelConstraint(15))
                .strafeToConstantHeading(new Vector2d(65.5, -50), new TranslationalVelConstraint(52))

                .splineToConstantHeading(new Vector2d(50, DIST_Y_ZONE), Math.toRadians(-90), new TranslationalVelConstraint(23), new ProfileAccelConstraint(-25, 55))
                .build();


        //!MAIN
        mainAutonomous = new SequentialAction(
                new ParallelAction(
                        outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.AUTO_DEPOSIT),
                        outtakeManager.DriveLift((int) OuttakeManager._LiftState.HIGH_CHAMBER_LOWER_REVERSED.getPosition()),
                        intakeManager.GripAction(IntakeManager._GripState.RELEASE),
                        initialTraj,
                        new SequentialAction(
                                new SleepAction(1.6f),
                                outtakeManager.DriveLift((int) OuttakeManager._LiftState.HIGH_CHAMBER_REVERSED.getPosition()),
                                new SleepAction(0.35),
                                outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.RELEASE),
                                new SleepAction(0.15)
                        )
                ),

                new ParallelAction(
                        longTraj,
                        new SequentialAction(
                                outtakeManager.YawAction(OuttakeManager._OuttakeYawServoState.HORIZONTAL_ServoUp),
                                outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.GRIP),
                                outtakeManager.TiltAction(OuttakeManager._OuttakeTiltServoState.PICKUP),
                                new SleepAction(0.85),
                                outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.ZERO),
                                new SleepAction(0.3),
                                outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.RELEASE),
                                outtakeManager.DriveLift((int) OuttakeManager._LiftState.ZERO.getPosition())
                        )
                ),
                outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.GRIP),
                new SleepAction(0.05f),

                //DriveToHangAndBack 1st ===============>
                new ParallelAction(
                        new SequentialAction(
                                new SleepAction(0.05f),
                                trajToHang1st
                        ),
                        new SequentialAction(
                                outtakeManager.DriveLift((int) OuttakeManager._LiftState.HIGH_CHAMBER_LOWER_REVERSED.getPosition()),
                                outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.AUTO_DEPOSIT),
                                new SleepAction(0.3f),
                                outtakeManager.TiltAction(OuttakeManager._OuttakeTiltServoState.DEPOSIT_SPECIMEN),
                                new SleepAction(0.5f),
                                outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.DEPOSIT_BACKPUSH)
//                                outtakeManager.YawAction(OuttakeManager._OuttakeYawServoState.HORIZONTAL_ServoDown)
                        ),
                        new SequentialAction(
                                new SleepAction(2.4),
                                outtakeManager.DriveLift((int) OuttakeManager._LiftState.HIGH_CHAMBER_REVERSED.getPosition()),
                                new SleepAction(0.3f),
                                outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.RELEASE)
                        )
                ),

                new ParallelAction(
                        trajToPickup1st,
                        new SequentialAction(
                                outtakeManager.YawAction(OuttakeManager._OuttakeYawServoState.HORIZONTAL_ServoUp),
                                outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.GRIP),
                                outtakeManager.TiltAction(OuttakeManager._OuttakeTiltServoState.PICKUP),
                                new SleepAction(0.85f),
                                outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.ZERO),
                                outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.RELEASE),
                                outtakeManager.DriveLift((int) OuttakeManager._LiftState.ZERO.getPosition())
                        )
                ),
                outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.GRIP),
                new SleepAction(0.05),

                //DriveToHangAndBack 2nd ===============>
                new ParallelAction(
                        new SequentialAction(
                                new SleepAction(0.05f),
                                trajToHang2nd
                        ),
                        new SequentialAction(
                                outtakeManager.DriveLift((int) OuttakeManager._LiftState.HIGH_CHAMBER_LOWER_REVERSED.getPosition()),
                                outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.AUTO_DEPOSIT),
                                new SleepAction(0.3f),
                                outtakeManager.TiltAction(OuttakeManager._OuttakeTiltServoState.DEPOSIT_SPECIMEN),
                                new SleepAction(0.5f),
                                outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.DEPOSIT_BACKPUSH)
                        )
                ),
                outtakeManager.DriveLift((int) OuttakeManager._LiftState.HIGH_CHAMBER_REVERSED.getPosition()),
                new SleepAction(0.3f),
                outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.RELEASE),
                new SleepAction(0.1f),
                new ParallelAction(
                        trajToPickup2nd,
                        new SequentialAction(
                                outtakeManager.YawAction(OuttakeManager._OuttakeYawServoState.HORIZONTAL_ServoUp),
                                outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.GRIP),
                                outtakeManager.TiltAction(OuttakeManager._OuttakeTiltServoState.PICKUP),
                                new SleepAction(0.85f),
                                outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.ZERO),
                                outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.RELEASE),
                                outtakeManager.DriveLift((int) OuttakeManager._LiftState.ZERO.getPosition())
                        )
                ),
                outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.GRIP),
                new SleepAction(0.05),


                //DriveToHangAndBack 3rd ===============>
                new ParallelAction(
                        new SequentialAction(
                                new SleepAction(0.05f),
                                trajToHang3rd
                        ),
                        new SequentialAction(
                                outtakeManager.DriveLift((int) OuttakeManager._LiftState.HIGH_CHAMBER_LOWER_REVERSED.getPosition()),
                                outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.AUTO_DEPOSIT),
                                new SleepAction(0.3f),
                                outtakeManager.TiltAction(OuttakeManager._OuttakeTiltServoState.DEPOSIT_SPECIMEN),
                                new SleepAction(0.5f),
                                outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.DEPOSIT_BACKPUSH)
//                                outtakeManager.YawAction(OuttakeManager._OuttakeYawServoState.HORIZONTAL_ServoDown)
                        )
                ),
                outtakeManager.DriveLift((int) OuttakeManager._LiftState.HIGH_CHAMBER_REVERSED.getPosition()),
                new SleepAction(0.3f),
                outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.RELEASE),
                new SleepAction(0.1f),
                endTraj



        );
    }
    @Override
    public void start(){

        //!This blocks everything -> :C
        Actions.runBlocking(
                new ParallelAction(

                        outtakeManager.LoopLift(),
                        intakeManager.LoopLift(),

                        new SequentialAction(
                                mainAutonomous,

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