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
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakePitchServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeSlidePositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeTiltServoPosCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeYawServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetLiftPositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeClawStateCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakePitchServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeTiltServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeYawServoCommand;
import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

@Autonomous(name = "Secondary Auto Red Sample", group = "..OpMode", preselectTeleOp = "Main TeleOp")
public class MainAutoRedSample extends OpModeTemplate {

    private Follower follower;
    private Timer pathTimer, opmodeTimer;

    private int pathState;

    private final Pose startPose = new Pose(8, 20, Math.toRadians(0));

    private final Pose scorePosePreload = new Pose(10, 22, Math.toRadians(170));
    private final Pose grab1Pose = new Pose(15, 23, Math.toRadians(180));
    private final Pose grab2Pose = new Pose(7.3, 29, Math.toRadians(0));
    private final Pose grab3Pose = new Pose(7.1, 29, Math.toRadians(0));
    private final Pose grab4Pose = new Pose(6.8, 29, Math.toRadians(0));

    private final Pose parkPose = new Pose(7, 29, Math.toRadians(0));

    private PathChain scorePreload, score1, backFromChamber, grab1, score2, score3, score4, park, grab2, grab3;

    public void buildPaths() {

        //! -> 1
        scorePreload = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(startPose),
                                new Point(scorePosePreload)
                        )
                )
                .setConstantHeadingInterpolation(scorePosePreload.getHeading())
                .build();

        //! -> 5
        grab1 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(scorePosePreload),
                                new Point(grab1Pose)
                        )
                )
                .setPathEndVelocityConstraint(0.02)
                .setConstantHeadingInterpolation(grab1Pose.getHeading())
                .build();

    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                CommandScheduler.getInstance().schedule(
                        new SequentialCommandGroup(
                                new SequentialCommandGroup(
                                        new InstantCommand(() -> follower.followPath(scorePreload, true)),
                                        new InstantCommand(() -> setPathState(1))
                                )

                        )
                );

                break;
            case 1:
                if(!follower.isBusy()) {
                    CommandScheduler.getInstance().schedule(
                            new SequentialCommandGroup(
                                    new SetOuttakeYawServoCommand(outtakeManager, OuttakeManager._OuttakeYawServoState.SAMPLE_Deposit),
                                    new SetOuttakeTiltServoCommand(outtakeManager, OuttakeManager._OuttakeTiltServoState.DEPOSIT_SAMPLE),
                                    new SetOuttakePitchServoCommand(outtakeManager, OuttakeManager._PitchServoState.DEPOSIT_SAMPLE),
                                    new SetLiftPositionCommand(outtakeManager, OuttakeManager._LiftState.HIGH_BASKET),
                                    new WaitCommand(1200),
                                    new SetOuttakeClawStateCommand(outtakeManager, OuttakeManager._OuttakeClawServoState.OPEN),
                                    new WaitCommand(150),
                                    new InstantCommand(() -> follower.followPath(grab1,true)),
                                    new InstantCommand(() -> setPathState(2)),
                                    new WaitCommand(500),
                                    new SetOuttakePitchServoCommand(outtakeManager, OuttakeManager._PitchServoState.TRANSFER),
                                    new SetLiftPositionCommand(outtakeManager, OuttakeManager._LiftState.TRANSFER),
                                    new SetOuttakeTiltServoCommand(outtakeManager, OuttakeManager._OuttakeTiltServoState.TRANSFER),
                                    new SetIntakeSlidePositionCommand(intakeManager, IntakeManager._SlideState.EXTENDED),
                                    new SetIntakeYawServoCommand(intakeManager, IntakeManager._YawServoState.AIMING),
                                    new SetIntakePitchServoCommand(intakeManager, IntakeManager._PitchServoState.AIMING),
                                    new SetIntakeTiltServoPosCommand(intakeManager, IntakeManager._TiltServoState.AIMING)
                            )
                    );
                }
                break;

            case 2:
                if (!follower.isBusy()){
                    CommandScheduler.getInstance().schedule(
                        new SequentialCommandGroup(
                                new WaitCommand(10000),
                                new InstantCommand(() -> follower.followPath(grab1,true)),
                                new InstantCommand(() -> setPathState(3))
                        )
                    );
                    follower.followPath(score1,true);
                    setPathState(2);
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
        intakeManager.loop();

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