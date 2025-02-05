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

@Autonomous(name = "FiveSpecimenUnstable", group = "OpMode", preselectTeleOp = "Main TeleOp")
public class FiveSpecimenUnstable extends OpModeTemplate {

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

    private final float DIST_X_CHAMBER = 10;
    private final float DIST_Y_CHAMBER = -31f;

    private Action mainAutonomous;
    private float demon_multiplier = 1.4f;
    private Action trajToPickup3rd;
    private Action trajToHang4th;


    @Override
    public void init() {
        initSystems(true);

        initialTraj = drive.actionBuilder(new Pose2d(new Vector2d(9, -62), Math.toRadians(-270)))
                .strafeToConstantHeading(new Vector2d(DIST_X_CHAMBER, DIST_Y_CHAMBER), new TranslationalVelConstraint(55 * demon_multiplier), new ProfileAccelConstraint(-24 * demon_multiplier, 56 * demon_multiplier))
                .build();

        trajToHang1st = drive.actionBuilder(new Pose2d(50, -62, Math.toRadians(-270)))
                .splineToConstantHeading(new Vector2d(7, DIST_Y_CHAMBER), Math.toRadians(-270), null, new ProfileAccelConstraint(-24 * demon_multiplier, 56 * demon_multiplier))
                .build();

        trajToHang2nd = drive.actionBuilder(new Pose2d(38, -62, Math.toRadians(-270)))
                .splineToConstantHeading(new Vector2d(5, DIST_Y_CHAMBER), Math.toRadians(-270),null, new ProfileAccelConstraint(-24 * demon_multiplier, 56 * demon_multiplier))
                .build();

        trajToHang3rd = drive.actionBuilder(new Pose2d(38, -62, Math.toRadians(-270)))
                .splineToConstantHeading(new Vector2d(3, DIST_Y_CHAMBER), Math.toRadians(-270),null, new ProfileAccelConstraint(-24 * demon_multiplier, 56 * demon_multiplier))
                .build();

        trajToHang4th = drive.actionBuilder(new Pose2d(38, -62, Math.toRadians(-270)))
                .splineToConstantHeading(new Vector2d(1, DIST_Y_CHAMBER), Math.toRadians(-270),null, new ProfileAccelConstraint(-24 * demon_multiplier, 56 * demon_multiplier))
                .build();

        trajToPickup1st = drive.actionBuilder(new Pose2d(7, DIST_Y_CHAMBER, Math.toRadians(-270)))
                .setReversed(true) //Pickup 1
                .splineToConstantHeading(new Vector2d(38, -62), Math.toRadians(-90), null, new ProfileAccelConstraint(-24 * 1.1, 56 * 1.2))
                .build();
        trajToPickup2nd = drive.actionBuilder(new Pose2d(5, DIST_Y_CHAMBER, Math.toRadians(-270)))
                .setReversed(true)
                .splineToConstantHeading(new Vector2d(38, -62), Math.toRadians(-90), null, new ProfileAccelConstraint(-24 * 1.1, 56 * 1.2))
                .build();
        trajToPickup3rd = drive.actionBuilder(new Pose2d(3, DIST_Y_CHAMBER, Math.toRadians(-270)))
                .setReversed(true)
                .splineToConstantHeading(new Vector2d(38, -62), Math.toRadians(-90), null, new ProfileAccelConstraint(-24 * 1.1, 56 * 1.2))
                .build();

        endTraj = drive.actionBuilder(new Pose2d(3, DIST_Y_CHAMBER, Math.toRadians(-270)))
                .strafeToConstantHeading(new Vector2d(55, -60), new TranslationalVelConstraint(101), new ProfileAccelConstraint(-40, 55))
                .build();

        longTraj = drive.actionBuilder(new Pose2d(DIST_X_CHAMBER, DIST_Y_CHAMBER, Math.toRadians(-270)))
                .strafeToConstantHeading(new Vector2d(30, -36), new TranslationalVelConstraint(36 * demon_multiplier))
                .splineToConstantHeading(new Vector2d(48, -10), Math.toRadians(-20), new TranslationalVelConstraint(31 * demon_multiplier))
                .splineToConstantHeading(new Vector2d(50, -20), Math.toRadians(-90), new TranslationalVelConstraint(19 * demon_multiplier))
                .strafeToConstantHeading(new Vector2d(50, -50), new TranslationalVelConstraint(51 * 1.2))

                .splineToConstantHeading(new Vector2d(50, -51), Math.toRadians(-90), new TranslationalVelConstraint(16 * 1.2), new ProfileAccelConstraint(-26 * demon_multiplier, 11 * demon_multiplier))
                .splineToConstantHeading(new Vector2d(50, -50), Math.toRadians(-270), new TranslationalVelConstraint(16), new ProfileAccelConstraint(-26 * demon_multiplier, 11 * demon_multiplier))

                .strafeToConstantHeading(new Vector2d(50, -15), new TranslationalVelConstraint(52 * 1.3), new ProfileAccelConstraint(-101 * demon_multiplier, 21 * demon_multiplier))

                .splineToConstantHeading(new Vector2d(58, -15), Math.toRadians(-90), new TranslationalVelConstraint(16 * demon_multiplier))
                .strafeToConstantHeading(new Vector2d(58, -50), new TranslationalVelConstraint(53 * 1.3))

                .splineToConstantHeading(new Vector2d(58, -51), Math.toRadians(-90), new TranslationalVelConstraint(16 * demon_multiplier), new ProfileAccelConstraint(-26 * demon_multiplier, 11 * demon_multiplier))
                .splineToConstantHeading(new Vector2d(58, -50), Math.toRadians(-270), new TranslationalVelConstraint(16 * demon_multiplier), new ProfileAccelConstraint(-26 * demon_multiplier, 11 * demon_multiplier))

                .strafeToConstantHeading(new Vector2d(58, -15), new TranslationalVelConstraint(53 * 1.3), new ProfileAccelConstraint(-31 * demon_multiplier, 21 * demon_multiplier))

                .splineToConstantHeading(new Vector2d(67, -15), Math.toRadians(-90), new TranslationalVelConstraint(16 * demon_multiplier))
                .strafeToConstantHeading(new Vector2d(67, -50), new TranslationalVelConstraint(53 * 1.2))

                .splineToConstantHeading(new Vector2d(50, -62), Math.toRadians(-90), new TranslationalVelConstraint(24 * 1.2), new ProfileAccelConstraint(-26 * 1.1, 56 * 1.2))
                .build();


        //!MAIN
        mainAutonomous = new SequentialAction(
                new ParallelAction(
                        outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.AUTO_DEPOSIT),
                        outtakeManager.DriveLift((int) OuttakeManager._LiftState.HIGH_CHAMBER_LOWER_REVERSED.getPosition()),
                        intakeManager.GripAction(IntakeManager._GripState.RELEASE),
                        initialTraj,
                        new SequentialAction(
                                new SleepAction(1.7f),
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
                                new SleepAction(0.5),
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
                                new SleepAction(0.4f),
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
                                new SleepAction(0.4f),
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
                new ParallelAction(
                        trajToPickup3rd,
                        new SequentialAction(
                                outtakeManager.YawAction(OuttakeManager._OuttakeYawServoState.HORIZONTAL_ServoUp),
                                outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.GRIP),
                                outtakeManager.TiltAction(OuttakeManager._OuttakeTiltServoState.PICKUP),
                                new SleepAction(0.4f),
                                outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.ZERO),
                                outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.RELEASE),
                                outtakeManager.DriveLift((int) OuttakeManager._LiftState.ZERO.getPosition())
                        )
                ),
                outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.GRIP),
                new SleepAction(0.05),
                new ParallelAction(
                        new SequentialAction(
                                new SleepAction(0.05f),
                                trajToHang4th
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
                new SleepAction(0.1f)




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