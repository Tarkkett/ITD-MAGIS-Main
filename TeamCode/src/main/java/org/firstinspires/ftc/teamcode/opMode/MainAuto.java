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
    private TranslationalVelConstraint translationalVelConstraint = new TranslationalVelConstraint(50);

    private Action trajToPickup1st;
    private Action trajToPickup3rd;
    private Action trajToPickup2nd;
    private Action initialTraj;
    private Action trajToHang1st;
    private Action trajToHang2nd;
    private Action trajToHang3rd;
    private Action trajToHang4th;
    private Action longTraj;


    private final float DIST_X_CHAMBER = 29.5f;
    private final float DIST_Y_CHAMBER = -3f;



    private Action mainAutonomous;



    @Override
    public void init() {
        initSystems(true);

        initialTraj = drive.actionBuilder(beginPose).strafeToConstantHeading(new Vector2d(DIST_X_CHAMBER,DIST_Y_CHAMBER)).build();

        trajToHang1st = drive.actionBuilder(new Pose2d(0, -35, 0))
                .strafeToConstantHeading(new Vector2d(DIST_X_CHAMBER,0))
                .build();
        trajToHang2nd = drive.actionBuilder(new Pose2d(0, -35, 0))
                .strafeToConstantHeading(new Vector2d(DIST_X_CHAMBER,2.5))
                .build();
        trajToHang3rd = drive.actionBuilder(new Pose2d(0, -35, 0))
                .strafeToConstantHeading(new Vector2d(DIST_X_CHAMBER,5))
                .build();
        trajToHang4th = drive.actionBuilder(new Pose2d(0, -35, 0))
                .strafeToConstantHeading(new Vector2d(DIST_X_CHAMBER,7.5))
                .build();

        trajToPickup1st = drive.actionBuilder(new Pose2d(DIST_X_CHAMBER, 0, 0))
                .strafeToConstantHeading(new Vector2d(0,-35))
                .build();
        trajToPickup2nd = drive.actionBuilder(new Pose2d(DIST_X_CHAMBER, 2.5, 0))
                .strafeToConstantHeading(new Vector2d(0,-35))
                .build();
        trajToPickup3rd = drive.actionBuilder(new Pose2d(DIST_X_CHAMBER, 5, 0))
                .strafeToConstantHeading(new Vector2d(0,-35))
                .build();

        longTraj = drive.actionBuilder(new Pose2d(DIST_X_CHAMBER, DIST_Y_CHAMBER, 0))
                .strafeToConstantHeading(new Vector2d(DIST_X_CHAMBER, -20))
//                .splineToConstantHeading(new Vector2d(45, -25), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(50, -30), Math.toRadians(-80))
                .splineToConstantHeading(new Vector2d(15, -35), Math.toRadians(-180))
                //Nustumtas 1
                .splineToConstantHeading(new Vector2d(53.5, -39), Math.toRadians(-80))
                .splineToConstantHeading(new Vector2d(15, -50), Math.toRadians(-180))
                //Nustumtas 2
                .splineToConstantHeading(new Vector2d(54, -55), Math.toRadians(-80))
                .splineToConstantHeading(new Vector2d(15, -55), Math.toRadians(-180))
                //Nustumtas 3
                .splineToConstantHeading(new Vector2d(0, -35), Math.toRadians(135))
                //Stovim paimt speciment is HP
                .build();

        //!MAIN
        mainAutonomous = new SequentialAction(
            new ParallelAction(
                    outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.AUTO_DEPOSIT),
                    outtakeManager.DriveLift((int) OuttakeManager._LiftState.HIGH_CHAMBER.getPosition()),
                    intakeManager.GripAction(IntakeManager._GripState.RELEASE),
                    initialTraj
            ),
//            outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.DEPOSIT_FORWARDPUSH),
            outtakeManager.DriveLift((int) OuttakeManager._LiftState.HIGH_CHAMBER_LOWER.getPosition()),
//            outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.DEPOSIT_FORWARDPUSH),
            new SleepAction(0.34),
            outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.RELEASE),
                new SleepAction(0.15),
            //!-----------------------------

            new ParallelAction(
                    longTraj,
                    new SequentialAction(
                            outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.GRIP),
                            outtakeManager.TiltAction(OuttakeManager._OuttakeTiltServoState.PICKUP),
                            new SleepAction(0.5),
                            outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.PICKUP),
                            new SleepAction(0.3),
                            outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.RELEASE),
                            outtakeManager.DriveLift((int) OuttakeManager._LiftState.ZERO.getPosition())
                    )
            ),
            new SleepAction(0.2f),
            outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.GRIP),

            //DriveToHangAndBack
            new ParallelAction(
                    new SequentialAction(
                            new SleepAction(0.1f),
                            trajToHang1st
                    ),
                    new SequentialAction(
                            outtakeManager.DriveLift((int) OuttakeManager._LiftState.HIGH_CHAMBER.getPosition()),
                            outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.AUTO_DEPOSIT),
                            new SleepAction(0.3f),
                            outtakeManager.TiltAction(OuttakeManager._OuttakeTiltServoState.DEPOSIT),
                            new SleepAction(0.5f),
                            outtakeManager.YawAction(OuttakeManager._OuttakeYawServoState.HORIZONTAL_ServoUp)
                    )
            ),
//            outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.DEPOSIT_FORWARDPUSH),
            outtakeManager.DriveLift((int) OuttakeManager._LiftState.HIGH_CHAMBER_LOWER.getPosition()),
            new SleepAction(0.3f),
            outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.RELEASE),
            new SleepAction(0.1f),
            new ParallelAction(
                    trajToPickup1st,
                    new SequentialAction(
                            outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.GRIP),
                            outtakeManager.TiltAction(OuttakeManager._OuttakeTiltServoState.PICKUP),
                            new SleepAction(0.4f),
                            outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.PICKUP),
                            outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.RELEASE),
                            outtakeManager.DriveLift((int) OuttakeManager._LiftState.ZERO.getPosition())
                    )
            ),
            new SleepAction(0.2f),
            outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.GRIP),
                //Done2
                new ParallelAction(
                        new SequentialAction(
                                new SleepAction(0.1f),
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
//                outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.DEPOSIT_FORWARDPUSH),
                outtakeManager.DriveLift((int) OuttakeManager._LiftState.HIGH_CHAMBER_LOWER.getPosition()),
                new SleepAction(0.3f),
                outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.RELEASE),
                new SleepAction(0.1f),
                new ParallelAction(
                        trajToPickup2nd,
                        new SequentialAction(
                                outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.GRIP),
                                outtakeManager.TiltAction(OuttakeManager._OuttakeTiltServoState.PICKUP),
                                new SleepAction(0.4f),
                                outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.PICKUP),
                                outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.RELEASE),
                                outtakeManager.DriveLift((int) OuttakeManager._LiftState.ZERO.getPosition())
                        )
                ),
                outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.GRIP),
//Dne 3
                new ParallelAction(
                        new SequentialAction(
                                new SleepAction(0.1f),
                                trajToHang3rd
                        ),
                        new SequentialAction(
                                new SleepAction(0.2f),
                                outtakeManager.DriveLift((int) OuttakeManager._LiftState.HIGH_CHAMBER.getPosition()),
                                outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.AUTO_DEPOSIT),
                                new SleepAction(0.3f),
                                outtakeManager.TiltAction(OuttakeManager._OuttakeTiltServoState.DEPOSIT),
                                new SleepAction(0.5f),
                                outtakeManager.YawAction(OuttakeManager._OuttakeYawServoState.HORIZONTAL_ServoUp)
                        )
                ),
//                outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.DEPOSIT_FORWARDPUSH),
                outtakeManager.DriveLift((int) OuttakeManager._LiftState.HIGH_CHAMBER_LOWER.getPosition()),
                new SleepAction(0.3f),
                outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.RELEASE),
                new SleepAction(0.1f),
                new ParallelAction(
                        trajToPickup3rd,
                        new SequentialAction(
                                outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.GRIP),
                                outtakeManager.TiltAction(OuttakeManager._OuttakeTiltServoState.PICKUP),
                                new SleepAction(0.4f),
                                outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.PICKUP),
                                outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.RELEASE),
                                outtakeManager.DriveLift((int) OuttakeManager._LiftState.ZERO.getPosition())
                        )
                ),
                outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.GRIP),
                //Done 4
                new ParallelAction(
                        new SequentialAction(
                                new SleepAction(0.1f),
                                trajToHang4th
                        ),
                        new SequentialAction(
                                new SleepAction(0.2f),
                                outtakeManager.DriveLift((int) OuttakeManager._LiftState.HIGH_CHAMBER.getPosition()),
                                outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.AUTO_DEPOSIT),
                                new SleepAction(0.3f),
                                outtakeManager.TiltAction(OuttakeManager._OuttakeTiltServoState.DEPOSIT),
                                new SleepAction(0.5f),
                                outtakeManager.YawAction(OuttakeManager._OuttakeYawServoState.HORIZONTAL_ServoDown)
                        )
                ),
//                outtakeManager.OuttakeExtendoAction(OuttakeManager._ExtendoServoState.DEPOSIT_FORWARDPUSH),
                outtakeManager.DriveLift((int) OuttakeManager._LiftState.HIGH_CHAMBER_LOWER.getPosition()),
                new SleepAction(0.3f),
                outtakeManager.ClawAction(OuttakeManager._OuttakeClawServoState.RELEASE),
                new SleepAction(0.1f),
                outtakeManager.DriveLift((int) OuttakeManager._LiftState.ZERO.getPosition())
                //Done 5 :D
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
