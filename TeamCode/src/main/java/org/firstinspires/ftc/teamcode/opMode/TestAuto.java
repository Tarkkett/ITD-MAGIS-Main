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

@Autonomous(name = "Test Auto", group = "OpMode")
public class TestAuto extends OpModeTemplate {

    private Pose2d beginPose = new Pose2d(0, 0, 0);
    private TurnConstraints turnConstraints = new TurnConstraints(2.3, -2.3, 2.7);
    private TranslationalVelConstraint maxVel = new TranslationalVelConstraint(60);
    private TranslationalVelConstraint minVel = new TranslationalVelConstraint(45);
    private Action longTraj;

    @Override
    public void init() {
        initSystems(true);


        longTraj = drive.actionBuilder(new Pose2d(9, -62, Math.toRadians(-270)))
                .strafeToConstantHeading(new Vector2d(55, -5))
                .strafeToConstantHeading(new Vector2d(9, -62))
                .strafeToConstantHeading(new Vector2d(55, -5))
                .strafeToConstantHeading(new Vector2d(9, -62))
        .build();
    }
    @Override
    public void start(){

        //!This blocks everything -> :C
        Actions.runBlocking(
                new ParallelAction(

                        outtakeManager.LoopLift(),
                        intakeManager.LoopLift(),

                        new SequentialAction(
                                longTraj,

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