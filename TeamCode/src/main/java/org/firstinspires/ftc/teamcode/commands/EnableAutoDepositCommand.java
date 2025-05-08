package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandBase;
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

import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetLiftPositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeClawStateCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakePitchServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeTiltServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeYawServoCommand;
import org.firstinspires.ftc.teamcode.managers.DriveManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

public class EnableAutoDepositCommand extends CommandBase {

    private DriveManager driveManager;
    private OuttakeManager outtakeManager;

    private Follower follower;
    private int pathState;
    private Timer pathTimer;

    private final Pose startPose = new Pose(7.4, 63, Math.toRadians(0));

    private final Pose[] scorePoses = {
            new Pose(7.4, 63, Math.toRadians(0)),
            new Pose(8.0, 62, Math.toRadians(5)),
            new Pose(9.1, 61, Math.toRadians(10)),
            new Pose(10.3, 60, Math.toRadians(15)),
            new Pose(11.2, 59, Math.toRadians(20)),
            new Pose(12.0, 58, Math.toRadians(25)),
            new Pose(13.1, 57, Math.toRadians(30)),
            new Pose(14.0, 56, Math.toRadians(35)),
            new Pose(15.2, 55, Math.toRadians(40)),
            new Pose(16.0, 54, Math.toRadians(45))
    };

    private final Pose[] grabPoses = {
            new Pose(17.0, 53, Math.toRadians(50)),
            new Pose(18.0, 52, Math.toRadians(55)),
            new Pose(19.0, 51, Math.toRadians(60)),
            new Pose(20.0, 50, Math.toRadians(65)),
            new Pose(21.0, 49, Math.toRadians(70)),
            new Pose(22.0, 48, Math.toRadians(75)),
            new Pose(23.0, 47, Math.toRadians(80)),
            new Pose(24.0, 46, Math.toRadians(85)),
            new Pose(25.0, 45, Math.toRadians(90)),
            new Pose(26.0, 44, Math.toRadians(95))
    };


    private PathChain[] scorePaths = new PathChain[10];
    private PathChain[] grabPaths = new PathChain[10];

    public EnableAutoDepositCommand(DriveManager driveManager, OuttakeManager outtakeManager) {
        this.outtakeManager = outtakeManager;
        this.driveManager = driveManager;

    }

    @Override
    public void initialize(){
        driveManager.Lock();
        pathTimer = new Timer();
        buildPaths();
    }

    private void buildPaths() {

        scorePaths[0] = buildLinePath(startPose, scorePoses[0]);
        for (int i = 1; i < 10; i++) {
            scorePaths[i] = buildLinePath(grabPoses[i - 1], scorePoses[i]);
            grabPaths[i] = buildCurvePath(scorePoses[i - 1], grabPoses[i]);
        }
    }

    private PathChain buildLinePath(Pose start, Pose end) {
        return follower.pathBuilder()
                .addPath(new BezierLine(new Point(start), new Point(end)))
                .setPathEndTimeoutConstraint(100)
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .build();
    }

    private PathChain buildCurvePath(Pose start, Pose end) {
        return follower.pathBuilder()
                .addPath(new BezierCurve(new Point(start), new Point(19, 65, Point.CARTESIAN), new Point(end)))
                .setPathEndVelocityConstraint(0.02)
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .build();
    }

    private Command createDepositSequence(int scoreIndex, int nextState) {
        return new SequentialCommandGroup(
                new SetOuttakeClawStateCommand(outtakeManager, OuttakeManager._OuttakeClawServoState.CLOSED),
                new WaitCommand(150),
                new InstantCommand(() -> follower.followPath(scorePaths[scoreIndex], true)),
                new InstantCommand(() -> setPathState(nextState)),
                new SetLiftPositionCommand(outtakeManager, OuttakeManager._LiftState.HIGH_CHAMBER),
                new SetOuttakeTiltServoCommand(outtakeManager, OuttakeManager._OuttakeTiltServoState.DEPOSIT_SPECIMEN),
                new SetOuttakePitchServoCommand(outtakeManager, OuttakeManager._PitchServoState.DEPOSIT_SPECIMEN),
                new SetOuttakeYawServoCommand(outtakeManager, OuttakeManager._OuttakeYawServoState.HORIZONTAL_Deposit)
        );
    }

    private Command createGrabSequence(int grabIndex, int nextState) {
        return new SequentialCommandGroup(
                new SetOuttakeClawStateCommand(outtakeManager, OuttakeManager._OuttakeClawServoState.OPEN),
                new SetOuttakeTiltServoCommand(outtakeManager, OuttakeManager._OuttakeTiltServoState.DEPOSIT_CLEARED),
                new InstantCommand(() -> follower.followPath(grabPaths[grabIndex], true)),
                new InstantCommand(() -> setPathState(nextState)),
                new WaitCommand(200),
                new SetOuttakeYawServoCommand(outtakeManager, OuttakeManager._OuttakeYawServoState.HORIZONTAL_Pickup),
                new SetOuttakePitchServoCommand(outtakeManager, OuttakeManager._PitchServoState.PICKUP),
                new SetOuttakeTiltServoCommand(outtakeManager, OuttakeManager._OuttakeTiltServoState.PICKUP),
                new SetLiftPositionCommand(outtakeManager, OuttakeManager._LiftState.ZERO)
        );
    }

    public void PathUpdate() {
        switch (pathState){
            case 0:
                CommandScheduler.getInstance().schedule(createDepositSequence(0, 1));
                break;
            case 1:
                if (!follower.isBusy()) {
                    CommandScheduler.getInstance().schedule(createGrabSequence(1, 2));
                }
                break;
            case 2:
                CommandScheduler.getInstance().schedule(createDepositSequence(1, 3));
                break;
            case 3:
                if (!follower.isBusy()) {
                    CommandScheduler.getInstance().schedule(createGrabSequence(2, 4));
                }
                break;
        }
    }

    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    @Override
    public void execute(){
        follower.update();
        PathUpdate();
    }
}
