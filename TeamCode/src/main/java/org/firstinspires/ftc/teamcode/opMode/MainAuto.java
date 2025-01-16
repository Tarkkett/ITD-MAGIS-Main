package org.firstinspires.ftc.teamcode.opMode;

import com.acmerobotics.roadrunner.AccelConstraint;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.AngularVelConstraint;
import com.acmerobotics.roadrunner.MinMax;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.TurnConstraints;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.arcrobotics.ftclib.trajectory.constraint.CentripetalAccelerationConstraint;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

@Autonomous(name = "Main Auto", group = "OpMode", preselectTeleOp = "Main TeleOp")
public class MainAuto extends OpModeTemplate {

    private Pose2d beginPose = new Pose2d(0, 0, 0);
    private TurnConstraints turnConstraints = new TurnConstraints(2.3, -2.3, 2.7);
    private TranslationalVelConstraint maxVel = new TranslationalVelConstraint(60);
    private TranslationalVelConstraint minVel = new TranslationalVelConstraint(45);

    private Action trajToPickup1st;
    private Action trajToPickup3rd;
    private Action trajToPickup2nd;
    private Action initialTraj;
    private Action trajToHang1st;
    private Action trajToHang2nd;
    private Action trajToHang3rd;
    private Action trajToHang4th;
    private Action longTraj;
    private Action endTraj;
    private Action testTraj;

    private final float DIST_X_CHAMBER = 10;
    private final float DIST_Y_CHAMBER = -36f;

    private Action mainAutonomous;

    @Override
    public void init() {
        initSystems(true);

        initialTraj = drive.actionBuilder(new Pose2d(new Vector2d(9, -62), Math.toRadians(-270)))
                .strafeToConstantHeading(new Vector2d(DIST_X_CHAMBER, DIST_Y_CHAMBER))
                .build();

        trajToHang1st = drive.actionBuilder(new Pose2d(50, -62, Math.toRadians(-270)))
                .strafeToConstantHeading(new Vector2d(8, -36))
                .build();
        trajToHang2nd = drive.actionBuilder(new Pose2d(43, -62, Math.toRadians(-270)))
                .strafeToConstantHeading(new Vector2d(6, -36))
                .build();
        trajToHang3rd = drive.actionBuilder(new Pose2d(43, -62, Math.toRadians(-270)))
                .strafeToConstantHeading(new Vector2d(4, -36))
                .build();
        trajToHang4th = drive.actionBuilder(new Pose2d(43, -62, Math.toRadians(-270)))
                .strafeToConstantHeading(new Vector2d(2, -36))
                .build();

        trajToPickup1st = drive.actionBuilder(new Pose2d(8, -36, Math.toRadians(-270)))
                .setReversed(true) //Pickup 1
                .splineToConstantHeading(new Vector2d(43, -62), Math.toRadians(-90))
                .build();
        trajToPickup2nd = drive.actionBuilder(new Pose2d(6, -36,Math.toRadians(-270)))
                .setReversed(true)
                .splineToConstantHeading(new Vector2d(43, -62), Math.toRadians(-90))
                .build();
        trajToPickup3rd = drive.actionBuilder(new Pose2d(4, -36, Math.toRadians(-270)))
                .setReversed(true)
                .splineToConstantHeading(new Vector2d(43, -62), Math.toRadians(-90))
                .build();

        endTraj = drive.actionBuilder(new Pose2d(DIST_X_CHAMBER, 5.25f, 0))
                .strafeToConstantHeading(new Vector2d(10, -35))
                .build();

        testTraj = drive.actionBuilder(new Pose2d(new Vector2d(9, -62), Math.toRadians(-270)))
                .strafeToConstantHeading(new Vector2d(DIST_X_CHAMBER, DIST_Y_CHAMBER))
                .waitSeconds(0.2f)

                .strafeToConstantHeading(new Vector2d(30, -36))
                .splineToConstantHeading(new Vector2d(46, -10), Math.toRadians(-20))
                .splineToConstantHeading(new Vector2d(50, -20), Math.toRadians(-90))
                .strafeToConstantHeading(new Vector2d(50, -55), new TranslationalVelConstraint(80), new ProfileAccelConstraint(-32, 1))

                .splineToConstantHeading(new Vector2d(43, -55), Math.toRadians(50), new TranslationalVelConstraint(20))
                .splineToConstantHeading(new Vector2d(56, -10), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(58, -20), Math.toRadians(-90))
                .strafeToConstantHeading(new Vector2d(58, -55), null, new ProfileAccelConstraint(-32, 1))

                .splineToConstantHeading(new Vector2d(53, -50), Math.toRadians(50), new TranslationalVelConstraint(20))
                .splineToConstantHeading(new Vector2d(63, -10), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(65, -20), Math.toRadians(-90), new TranslationalVelConstraint(25))
                .strafeToConstantHeading(new Vector2d(65, -50))

                .splineToConstantHeading(new Vector2d(50, -62), Math.toRadians(-90))

                //Cycle >>>>>>> 1
                .waitSeconds(0.12f) //Hang 1
                .splineToConstantHeading(new Vector2d(8, -36), Math.toRadians(90))

                .waitSeconds(0.12f)
                .setReversed(true) //Pickup 1
                .splineToConstantHeading(new Vector2d(43, -60), Math.toRadians(-90))
                //>>>>>>>>>>>>>>
                //Cycle >>>>>>> 2
                .waitSeconds(0.12f) //Hang 2
                .splineToConstantHeading(new Vector2d(6, -36), Math.toRadians(90))

                .waitSeconds(0.12f) //Pickup 2
                .setReversed(true)
                .splineToConstantHeading(new Vector2d(43, -60), Math.toRadians(-90))
                //>>>>>>>>>>>>>>
                //Cycle >>>>>>> 3
                .waitSeconds(0.12f) //Hang 3
                .splineToConstantHeading(new Vector2d(4, -36), Math.toRadians(90))

                .waitSeconds(0.12f) //Pickup 3
                .setReversed(true)
                .splineToConstantHeading(new Vector2d(43, -60), Math.toRadians(-90))
                //>>>>>>>>>>>>>>
                //Cycle >>>>>>> 4
                .waitSeconds(0.12f) //Hang 4
                .splineToConstantHeading(new Vector2d(2, -36), Math.toRadians(90))
                .build();

        longTraj = drive.actionBuilder(new Pose2d(DIST_X_CHAMBER, DIST_Y_CHAMBER, Math.toRadians(-270)))
                .strafeToConstantHeading(new Vector2d(30, -36))
                .splineToConstantHeading(new Vector2d(46, -8), Math.toRadians(-20))
                .splineToConstantHeading(new Vector2d(48, -10), Math.toRadians(-90))
                .strafeToConstantHeading(new Vector2d(48, -55), new TranslationalVelConstraint(80), new ProfileAccelConstraint(-32, 1))

                .splineToConstantHeading(new Vector2d(43, -55), Math.toRadians(50), new TranslationalVelConstraint(20))
                .splineToConstantHeading(new Vector2d(56, -10), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(58, -20), Math.toRadians(-90))
                .strafeToConstantHeading(new Vector2d(58, -55), null, new ProfileAccelConstraint(-32, 1))

                .splineToConstantHeading(new Vector2d(53, -50), Math.toRadians(50), new TranslationalVelConstraint(20))
                .splineToConstantHeading(new Vector2d(63, -10), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(65, -20), Math.toRadians(-90), new TranslationalVelConstraint(25))
                .strafeToConstantHeading(new Vector2d(65, -50))

                .splineToConstantHeading(new Vector2d(50, -62), Math.toRadians(-90))
                .build();


        //!MAIN
        mainAutonomous = new SequentialAction(

            new ParallelAction(
                outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.AUTO_DEPOSIT),
                outtakeManager.DriveLift((int) OuttakeManager._LiftState.HIGH_CHAMBER.getPosition()),
                intakeManager.GripAction(IntakeManager._GripState.RELEASE),
                initialTraj
            ),
            outtakeManager.DriveLift((int) OuttakeManager._LiftState.HIGH_CHAMBER_LOWER.getPosition()),
            new SleepAction(0.34),
            outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.RELEASE),
            new SleepAction(0.15),
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
            new SleepAction(0.15f),

            //DriveToHangAndBack 1st ===============>
            new ParallelAction(
                    new SequentialAction(
                            new SleepAction(0.05f),
                            trajToHang1st
                    ),
                    new SequentialAction(
                            outtakeManager.DriveLift((int) OuttakeManager._LiftState.HIGH_CHAMBER.getPosition()),
                            outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.AUTO_DEPOSIT),
                            new SleepAction(0.3f),
                            outtakeManager.TiltAction(OuttakeManager._OuttakeTiltServoState.DEPOSIT),
                            new SleepAction(0.5f),
                            outtakeManager.YawAction(OuttakeManager._OuttakeYawServoState.HORIZONTAL_ServoDown)
                    )
            ),
            outtakeManager.DriveLift((int) OuttakeManager._LiftState.HIGH_CHAMBER_LOWER.getPosition()),
            new SleepAction(0.3f),
            outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.RELEASE),
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
            new SleepAction(0.15),

            //DriveToHangAndBack 2nd ===============>
            new ParallelAction(
                new SequentialAction(
                    new SleepAction(0.05f),
                    trajToHang2nd
                ),
                new SequentialAction(
                    outtakeManager.DriveLift((int) OuttakeManager._LiftState.HIGH_CHAMBER.getPosition()),
                    outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.AUTO_DEPOSIT),
                    new SleepAction(0.3f),
                    outtakeManager.TiltAction(OuttakeManager._OuttakeTiltServoState.DEPOSIT),
                    new SleepAction(0.5f),
                    outtakeManager.YawAction(OuttakeManager._OuttakeYawServoState.HORIZONTAL_ServoDown)
                )
            ),
            outtakeManager.DriveLift((int) OuttakeManager._LiftState.HIGH_CHAMBER_LOWER.getPosition()),
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
            new SleepAction(0.15),


                //DriveToHangAndBack 3rd ===============>
            new ParallelAction(
                    new SequentialAction(
                            new SleepAction(0.05f),
                            trajToHang3rd
                    ),
                    new SequentialAction(
                            outtakeManager.DriveLift((int) OuttakeManager._LiftState.HIGH_CHAMBER.getPosition()),
                            outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.AUTO_DEPOSIT),
                            new SleepAction(0.3f),
                            outtakeManager.TiltAction(OuttakeManager._OuttakeTiltServoState.DEPOSIT),
                            new SleepAction(0.5f),
                            outtakeManager.YawAction(OuttakeManager._OuttakeYawServoState.HORIZONTAL_ServoDown)
                    )
            ),
            outtakeManager.DriveLift((int) OuttakeManager._LiftState.HIGH_CHAMBER_LOWER.getPosition()),
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
            new SleepAction(0.15),


                //DriveToHangAndBack 4th ===============>
            new ParallelAction(
                new SequentialAction(
                        new SleepAction(0.05f),
                        trajToHang4th
                ),
                new SequentialAction(
//                                new SleepAction(0.14f),
                        outtakeManager.DriveLift((int) OuttakeManager._LiftState.HIGH_CHAMBER.getPosition()),
                        outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.AUTO_DEPOSIT),
                        new SleepAction(0.3f),
                        outtakeManager.TiltAction(OuttakeManager._OuttakeTiltServoState.DEPOSIT),
                        new SleepAction(0.5f),
                        outtakeManager.YawAction(OuttakeManager._OuttakeYawServoState.HORIZONTAL_ServoDown)
                )
            ),
            outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.RELEASE),
            outtakeManager.DriveLift((int) OuttakeManager._LiftState.HIGH_CHAMBER_LOWER.getPosition()),
            new SleepAction(0.3f),
            outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.RELEASE),
            new SleepAction(0.1f),
            outtakeManager.DriveLift((int) OuttakeManager._LiftState.ZERO.getPosition())

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
