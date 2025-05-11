package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetLiftPositionCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeClawStateCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakePitchServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeTiltServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeYawServoCommand;
import org.firstinspires.ftc.teamcode.managers.DriveManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;
import org.firstinspires.ftc.teamcode.util.GamepadPlus;

import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

public class EnableAutoDepositCommand extends CommandBase {

    private DriveManager driveManager;
    private OuttakeManager outtakeManager;
    private HardwareMap hmap;

    private Follower follower;
    private int pathState;
    private Timer pathTimer;

    GamepadPlus gamepadDriver;

    private final Pose startPose = new Pose(7.4, 29, Math.toRadians(0)); // Grab 1

    private final Pose[] scorePoses = {
            new Pose(37.5, 66, Math.toRadians(0)), //* Score 1
            new Pose(37.5, 66, Math.toRadians(0)), //* Score 2
            new Pose(37.5, 66, Math.toRadians(0)), //* Score 3
            new Pose(37.5, 66, Math.toRadians(0)), //* Score 4
            new Pose(37.5, 66, Math.toRadians(0)), //* Score 5
            new Pose(37.5, 66, Math.toRadians(0)), //* Score 6
            new Pose(37.5, 66, Math.toRadians(0)), //* Score 7
            new Pose(37.5, 66, Math.toRadians(0)), //* Score 8
            new Pose(37.5, 66, Math.toRadians(0)), //* Score 9
            new Pose(37.5, 66, Math.toRadians(0)) //* Score 10
    };

    private final Pose[] grabPoses = {
            new Pose(7.4, 29, Math.toRadians(0)), // Grab 2
            new Pose(7.4, 29, Math.toRadians(0)), // Grab 3
            new Pose(7.4, 29, Math.toRadians(0)), // Grab 4
            new Pose(7.4, 29, Math.toRadians(0)), // Grab 5
            new Pose(7.4, 29, Math.toRadians(0)), // Grab 6
            new Pose(7.4, 29, Math.toRadians(0)), // Grab 7
            new Pose(7.4, 29, Math.toRadians(0)), // Grab 8
            new Pose(7.4, 29, Math.toRadians(0)), // Grab 9
            new Pose(7.4, 29, Math.toRadians(0)), // Grab 10
            new Pose(7.4, 29, Math.toRadians(0)), // Grab 1?
    };

    private PathChain[] scorePaths = new PathChain[10];
    private PathChain[] grabPaths = new PathChain[10];

    public EnableAutoDepositCommand(DriveManager driveManager, OuttakeManager outtakeManager, HardwareMap hardwareMap, GamepadPlus gamepadDriver) {
        this.outtakeManager = outtakeManager;
        this.driveManager = driveManager;
        this.hmap = hardwareMap;
        this.gamepadDriver = gamepadDriver;

    }

    @Override
    public void initialize(){
        driveManager.Lock();
        pathTimer = new Timer();

        follower = new Follower(hmap, FConstants.class, LConstants.class);
        follower.setStartingPose(startPose);

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
                CommandScheduler.getInstance().schedule(
                        new SequentialCommandGroup(
                                new WaitCommand(500),
                                createDepositSequence(0, 1)
                        )
                );
                break;
            case 1:
                if (!follower.isBusy()) {
                    CommandScheduler.getInstance().schedule(createGrabSequence(1, 2));
                }
                break;
            case 2:
                if (!follower.isBusy()) {
                    CommandScheduler.getInstance().schedule(createDepositSequence(1, 3));
                }
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

        if (gamepadDriver.isDown(GamepadKeys.Button.DPAD_LEFT)) {
            follower.breakFollowing();
            driveManager.Unlock();
            gamepadDriver.rumble(500);
        }
    }
}
