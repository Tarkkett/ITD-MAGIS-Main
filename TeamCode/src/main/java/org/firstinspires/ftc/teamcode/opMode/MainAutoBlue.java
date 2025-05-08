package org.firstinspires.ftc.teamcode.opMode;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Constants;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetLiftPositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeClawStateCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakePitchServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeTiltServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeYawServoCommand;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

@Autonomous(name = "Main Auto Blue", group = "..OpMode", preselectTeleOp = "Main TeleOp")
public class MainAutoBlue extends OpModeTemplate {

    private Follower follower;
    private Timer pathTimer, opmodeTimer;

    private int pathState;

    private final Pose startPose = new Pose(8, 63, Math.toRadians(0));

    private final Pose scorePosePreload = new Pose(40, 63, Math.toRadians(0));
    private final Pose backFromChamberPose = new Pose(30, 42, Math.toRadians(0));

    private final Pose scorePose1 = new Pose(37.5, 66, Math.toRadians(0));
    private final Pose scorePose2 = new Pose(37.5, 69, Math.toRadians(0));
    private final Pose scorePose3 = new Pose(37.5, 75, Math.toRadians(0));
    private final Pose scorePose4 = new Pose(37.5, 72, Math.toRadians(0));
    private final Pose pickup1Pose = new Pose(60, 24, Math.toRadians(0));
    private final Pose push1Pose = new Pose(24, 24, Math.toRadians(0));
    private final Pose pickup2Pose = new Pose(60, 15, Math.toRadians(0));
    private final Pose push2Pose = new Pose(24, 15, Math.toRadians(0));
    private final Pose pickup3Pose = new Pose(61, 6.4, Math.toRadians(0));
    private final Pose push3Pose = new Pose(24, 6.4, Math.toRadians(0));

    private final Pose grab1Pose = new Pose(7.4, 29, Math.toRadians(0));
    private final Pose grab2Pose = new Pose(7.3, 29, Math.toRadians(0));
    private final Pose grab3Pose = new Pose(7.1, 29, Math.toRadians(0));
    private final Pose grab4Pose = new Pose(6.8, 29, Math.toRadians(0));

    private final Pose parkPose = new Pose(7, 29, Math.toRadians(0));

    private PathChain scorePreload, score1, backFromChamber, grabPreload, grab1, score2, score3, score4, park, grab2, grab3;

    public void buildPaths() {

        //! -> 1
        scorePreload = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(startPose),
                                new Point(scorePosePreload)
                        )
                )
                .setZeroPowerAccelerationMultiplier(5)
                .setPathEndTimeoutConstraint(100)
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .build();

        //! -> 2
        backFromChamber = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(scorePosePreload), new Point(25, 65, Point.CARTESIAN), new Point(backFromChamberPose)))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .addPath(new BezierCurve(new Point(backFromChamberPose), new Point(64, 30.5), new Point(pickup1Pose)))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .addParametricCallback(0.5, () -> CommandScheduler.getInstance().schedule(
                        new SequentialCommandGroup(
                                new SetLiftPositionCommand(outtakeManager, OuttakeManager._LiftState.ZERO),
                                new SetOuttakePitchServoCommand(outtakeManager, OuttakeManager._PitchServoState.PICKUP),
                                new SetOuttakeTiltServoCommand(outtakeManager, OuttakeManager._OuttakeTiltServoState.PICKUP)
                        )
                    )
                )
                .addPath(new BezierLine(new Point(pickup1Pose), new Point(push1Pose)))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .addPath(new BezierCurve(new Point(push1Pose), new Point(64, 32), new Point(pickup2Pose)))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .addPath(new BezierLine(new Point(pickup2Pose), new Point(push2Pose)))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .addPath(new BezierCurve(new Point(push2Pose), new Point(61, 17.5), new Point(pickup3Pose)))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .addPath(new BezierLine(new Point(pickup3Pose), new Point(push3Pose)))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .addPath(new BezierCurve(new Point(push3Pose), new Point(11.5, 15.5, Point.CARTESIAN), new Point(grab2Pose)))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .build();

        //! -> 5
        grab1 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(scorePose1), new Point(19, 65, Point.CARTESIAN), new Point(grab1Pose)))
                .setPathEndVelocityConstraint(0.02)
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .build();

        //! -> 7
        grab2 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(scorePose2), new Point(19, 65, Point.CARTESIAN), new Point(grab3Pose)))
                .setPathEndVelocityConstraint(0.02)
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .build();

        //! -> 9
        grab3 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(scorePose3), new Point(19, 65, Point.CARTESIAN), new Point(grab4Pose)))
                .setPathEndVelocityConstraint(0.02)
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .build();

        //! -> 4
        score1 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(grab1Pose), new Point(18, 66, Point.CARTESIAN), new Point(scorePose1)))
                .setPathEndTimeoutConstraint(100)
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .build();

        //! -> 6
        score2 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(grab2Pose), new Point(18, 66, Point.CARTESIAN), new Point(scorePose2)))
                .setPathEndTimeoutConstraint(100)
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .build();

        //! -> 8
        score3 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(grab3Pose), new Point(18, 66, Point.CARTESIAN), new Point(scorePose3)))
                .setPathEndTimeoutConstraint(100)
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .build();

        //! -> 10
        score4 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(grab4Pose), new Point(26, 63, Point.CARTESIAN), new Point(scorePose4)))
                .setPathEndTimeoutConstraint(100)
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .build();

        //! -> 11
        park = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scorePose4), new Point(parkPose)))

                .setPathEndTimeoutConstraint(100)
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .build();

    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                CommandScheduler.getInstance().schedule(
                        new SequentialCommandGroup(
                                new SetLiftPositionCommand(outtakeManager, OuttakeManager._LiftState.HIGH_CHAMBER),
                                new SetOuttakeTiltServoCommand(outtakeManager, OuttakeManager._OuttakeTiltServoState.DEPOSIT_SPECIMEN),
                                new SetOuttakePitchServoCommand(outtakeManager, OuttakeManager._PitchServoState.DEPOSIT_SPECIMEN),
                                new SetOuttakeYawServoCommand(outtakeManager, OuttakeManager._OuttakeYawServoState.HORIZONTAL_Deposit),
                                new WaitCommand(150),
                                new InstantCommand(() -> follower.followPath(scorePreload)),
                                new InstantCommand(() -> setPathState(1))
                        )
                );

                break;
            case 1:
                if(!follower.isBusy()) {
                    CommandScheduler.getInstance().schedule(
                            new SetOuttakeClawStateCommand(outtakeManager, OuttakeManager._OuttakeClawServoState.OPEN),
                            new SetOuttakeTiltServoCommand(outtakeManager, OuttakeManager._OuttakeTiltServoState.DEPOSIT_CLEARED)
                    );
                    follower.followPath(backFromChamber,true);
                    setPathState(3);
                }
                break;
            case 3:
                if(!follower.isBusy()) {
                    CommandScheduler.getInstance().schedule(
                            new SequentialCommandGroup(
                                    new SetOuttakeClawStateCommand(outtakeManager, OuttakeManager._OuttakeClawServoState.CLOSED),
                                    new SequentialCommandGroup(
                                            new WaitCommand(150),
                                            new InstantCommand(() -> follower.followPath(score1,true)),
                                            new InstantCommand(() -> setPathState(4))),
                                            new SetLiftPositionCommand(outtakeManager, OuttakeManager._LiftState.HIGH_CHAMBER),
                                            new SetOuttakeTiltServoCommand(outtakeManager, OuttakeManager._OuttakeTiltServoState.DEPOSIT_SPECIMEN),
                                            new SetOuttakePitchServoCommand(outtakeManager, OuttakeManager._PitchServoState.DEPOSIT_SPECIMEN),
                                            new SetOuttakeYawServoCommand(outtakeManager, OuttakeManager._OuttakeYawServoState.HORIZONTAL_Pickup) //Unconventional
                                    )
                            );
                }
                break;
            case 4:
                if(!follower.isBusy()) {

                    CommandScheduler.getInstance().schedule(
                        new SequentialCommandGroup(
                                new SetOuttakeClawStateCommand(outtakeManager, OuttakeManager._OuttakeClawServoState.OPEN),
                                new SetOuttakeTiltServoCommand(outtakeManager, OuttakeManager._OuttakeTiltServoState.DEPOSIT_CLEARED)
                        ));

                    follower.followPath(grab1 ,true);
                    setPathState(5);

                    CommandScheduler.getInstance().schedule(
                            new SequentialCommandGroup(
                                    new WaitCommand(200),
                                    new SetOuttakePitchServoCommand(outtakeManager, OuttakeManager._PitchServoState.PICKUP),
                                    new SetOuttakeTiltServoCommand(outtakeManager, OuttakeManager._OuttakeTiltServoState.PICKUP),
                                    new SetLiftPositionCommand(outtakeManager, OuttakeManager._LiftState.ZERO)
                            ));
                }
                break;
            case 5:
                if(!follower.isBusy()) {
                    CommandScheduler.getInstance().schedule(
                            new SequentialCommandGroup(
                                    new SetOuttakeClawStateCommand(outtakeManager, OuttakeManager._OuttakeClawServoState.CLOSED),
                                    new SequentialCommandGroup(
                                            new WaitCommand(150),
                                            new InstantCommand(() -> follower.followPath(score2,true)),
                                            new InstantCommand(() -> setPathState(6))),
                                    new SetLiftPositionCommand(outtakeManager, OuttakeManager._LiftState.HIGH_CHAMBER),
                                    new SetOuttakeTiltServoCommand(outtakeManager, OuttakeManager._OuttakeTiltServoState.DEPOSIT_SPECIMEN),
                                    new SetOuttakePitchServoCommand(outtakeManager, OuttakeManager._PitchServoState.DEPOSIT_SPECIMEN),
                                    new SetOuttakeYawServoCommand(outtakeManager, OuttakeManager._OuttakeYawServoState.HORIZONTAL_Deposit) //Conventional
                            )
                    );
                }
                break;
            case 6:
                if(!follower.isBusy()) {

                    CommandScheduler.getInstance().schedule(
                            new SequentialCommandGroup(
                                    new SetOuttakeClawStateCommand(outtakeManager, OuttakeManager._OuttakeClawServoState.OPEN),
                                    new SetOuttakeTiltServoCommand(outtakeManager, OuttakeManager._OuttakeTiltServoState.DEPOSIT_CLEARED)
                            ));

                    follower.followPath(grab2 ,true);
                    setPathState(7);

                    CommandScheduler.getInstance().schedule(
                            new SequentialCommandGroup(
                                    new WaitCommand(200),
                                    new SetOuttakeYawServoCommand(outtakeManager, OuttakeManager._OuttakeYawServoState.HORIZONTAL_Pickup),
                                    new SetOuttakePitchServoCommand(outtakeManager, OuttakeManager._PitchServoState.PICKUP),
                                    new SetOuttakeTiltServoCommand(outtakeManager, OuttakeManager._OuttakeTiltServoState.PICKUP),
                                    new SetLiftPositionCommand(outtakeManager, OuttakeManager._LiftState.ZERO)
                            ));
                }
                break;
            case 7:
                if(!follower.isBusy()) {

                    CommandScheduler.getInstance().schedule(
                            new SequentialCommandGroup(
                                    new SetOuttakeClawStateCommand(outtakeManager, OuttakeManager._OuttakeClawServoState.CLOSED),
                                    new SequentialCommandGroup(
                                            new WaitCommand(150),
                                            new InstantCommand(() -> follower.followPath(score3,true)),
                                            new InstantCommand(() -> setPathState(8))),
                                    new SetLiftPositionCommand(outtakeManager, OuttakeManager._LiftState.HIGH_CHAMBER),
                                    new SetOuttakeTiltServoCommand(outtakeManager, OuttakeManager._OuttakeTiltServoState.DEPOSIT_SPECIMEN),
                                    new SetOuttakePitchServoCommand(outtakeManager, OuttakeManager._PitchServoState.DEPOSIT_SPECIMEN),
                                    new SetOuttakeYawServoCommand(outtakeManager, OuttakeManager._OuttakeYawServoState.HORIZONTAL_Deposit) //Conventional
                            )
                    );
                }
                break;
            case 8:
                if(!follower.isBusy()) {
                    CommandScheduler.getInstance().schedule(
                            new SequentialCommandGroup(
                                    new SetOuttakeClawStateCommand(outtakeManager, OuttakeManager._OuttakeClawServoState.OPEN),
                                    new SetOuttakeTiltServoCommand(outtakeManager, OuttakeManager._OuttakeTiltServoState.DEPOSIT_CLEARED)
                            ));

                    follower.followPath(grab3 ,true);
                    setPathState(9);

                    CommandScheduler.getInstance().schedule(
                            new SequentialCommandGroup(
                                    new WaitCommand(200),
                                    new SetOuttakeYawServoCommand(outtakeManager, OuttakeManager._OuttakeYawServoState.HORIZONTAL_Pickup),
                                    new SetOuttakePitchServoCommand(outtakeManager, OuttakeManager._PitchServoState.PICKUP),
                                    new SetOuttakeTiltServoCommand(outtakeManager, OuttakeManager._OuttakeTiltServoState.PICKUP),
                                    new SetLiftPositionCommand(outtakeManager, OuttakeManager._LiftState.ZERO)
                            ));
                }
                break;

            case 9:
                if (!follower.isBusy()){
                    CommandScheduler.getInstance().schedule(
                            new SequentialCommandGroup(
                                    new SetOuttakeClawStateCommand(outtakeManager, OuttakeManager._OuttakeClawServoState.CLOSED),
                                    new SequentialCommandGroup(
                                            new WaitCommand(150),
                                            new InstantCommand(() -> follower.followPath(score4,true)),
                                            new InstantCommand(() -> setPathState(10))),
                                    new SetLiftPositionCommand(outtakeManager, OuttakeManager._LiftState.HIGH_CHAMBER),
                                    new SetOuttakeTiltServoCommand(outtakeManager, OuttakeManager._OuttakeTiltServoState.DEPOSIT_SPECIMEN),
                                    new SetOuttakePitchServoCommand(outtakeManager, OuttakeManager._PitchServoState.DEPOSIT_SPECIMEN),
                                    new SetOuttakeYawServoCommand(outtakeManager, OuttakeManager._OuttakeYawServoState.HORIZONTAL_Deposit) //Conventional
                            )
                    );
                }
                break;

            case 10:
                if (!follower.isBusy()){

                    CommandScheduler.getInstance().schedule(
                            new SequentialCommandGroup(
                                    new SetOuttakeClawStateCommand(outtakeManager, OuttakeManager._OuttakeClawServoState.OPEN),
                                    new SetOuttakeTiltServoCommand(outtakeManager, OuttakeManager._OuttakeTiltServoState.DEPOSIT_CLEARED),
                                    new SetLiftPositionCommand(outtakeManager, OuttakeManager._LiftState.CLEARED)
                            ));
                    follower.followPath(park, true);
                    CommandScheduler.getInstance().schedule(
                            new WaitCommand(300)
                    );

                    setPathState(-1);
                }
                break;
        }
    }

    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    @Override
    public void loop() {

        follower.update();
        outtakeManager.loop();
        autonomousPathUpdate();

        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();

        CommandScheduler.getInstance().run();

    }

    @Override
    public void init() {
        initSystems(true);

        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        follower.setStartingPose(startPose);
        buildPaths();
    }

    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);
    }

}