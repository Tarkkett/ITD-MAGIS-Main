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

    float DIST_Y_CHAMBER = -31f;
    float DIST_X_CHAMBER = 10;

    @Override
    public void init() {
        initSystems(true);


        longTraj = drive.actionBuilder(new Pose2d(9, -62, Math.toRadians(-270)))
                .strafeToConstantHeading(new Vector2d(DIST_X_CHAMBER, DIST_Y_CHAMBER), new TranslationalVelConstraint(50))
                .waitSeconds(0.2f)


                .strafeToConstantHeading(new Vector2d(30, -36), new TranslationalVelConstraint(30))
                .splineToConstantHeading(new Vector2d(48, -15), Math.toRadians(-20), new TranslationalVelConstraint(30))
                .splineToConstantHeading(new Vector2d(50, -25), Math.toRadians(-90), new TranslationalVelConstraint(18))
                .strafeToConstantHeading(new Vector2d(50, -50), new TranslationalVelConstraint(50))

                .splineToConstantHeading(new Vector2d(50, -51), Math.toRadians(-90), new TranslationalVelConstraint(7), new ProfileAccelConstraint(-20, 5))
                .splineToConstantHeading(new Vector2d(50, -50), Math.toRadians(-270), new TranslationalVelConstraint(7), new ProfileAccelConstraint(-20, 5))

                .strafeToConstantHeading(new Vector2d(50, -20), new TranslationalVelConstraint(50), new ProfileAccelConstraint(-100, 20))

                .splineToConstantHeading(new Vector2d(58, -20), Math.toRadians(-90), new TranslationalVelConstraint(15))
                .strafeToConstantHeading(new Vector2d(58, -50), new TranslationalVelConstraint(50))

                .splineToConstantHeading(new Vector2d(58, -51), Math.toRadians(-90), new TranslationalVelConstraint(7), new ProfileAccelConstraint(-20, 5))
                .splineToConstantHeading(new Vector2d(58, -50), Math.toRadians(-270), new TranslationalVelConstraint(7), new ProfileAccelConstraint(-20, 5))

                .strafeToConstantHeading(new Vector2d(58, -20), new TranslationalVelConstraint(50), new ProfileAccelConstraint(-20, 15))

                .splineToConstantHeading(new Vector2d(65, -20), Math.toRadians(-90), new TranslationalVelConstraint(15))
                .strafeToConstantHeading(new Vector2d(65, -50), new TranslationalVelConstraint(50))
//
                .splineToConstantHeading(new Vector2d(50, -62), Math.toRadians(-90), new TranslationalVelConstraint(20), new ProfileAccelConstraint(-25, 55))


                //Cycle >>>>>>> 1
                .waitSeconds(0.3f)
                .strafeToConstantHeading(new Vector2d(8, DIST_Y_CHAMBER), null, new ProfileAccelConstraint(-28, 55))

                .waitSeconds(0.3f)
                .setReversed(true)
                .strafeToConstantHeading(new Vector2d(30, -40))
                .splineToConstantHeading(new Vector2d(43, -62), Math.toRadians(-90), null, new ProfileAccelConstraint(-26, 55))
                //>>>>>>>>>>>>>>
                //Cycle >>>>>>> 2
                .waitSeconds(0.3f)
                .strafeToConstantHeading(new Vector2d(6, DIST_Y_CHAMBER), null, new ProfileAccelConstraint(-28, 55))

                .waitSeconds(0.3f)
                .setReversed(true)
                .strafeToConstantHeading(new Vector2d(28, -40))

                .splineToConstantHeading(new Vector2d(43, -62), Math.toRadians(-90), null, new ProfileAccelConstraint(-26, 55))

                //>>>>>>>>>>>>>>
                //Cycle >>>>>>> 3
                .waitSeconds(0.3f)
                .strafeToConstantHeading(new Vector2d(4, DIST_Y_CHAMBER), null, new ProfileAccelConstraint(-28, 55))

                .waitSeconds(0.3f)
                .setReversed(true)
                .strafeToConstantHeading(new Vector2d(26, -40))

                .splineToConstantHeading(new Vector2d(43, -62), Math.toRadians(-90), null, new ProfileAccelConstraint(-26, 55))

                //>>>>>>>>>>>>>>
                //Cycle >>>>>>> 4
                .waitSeconds(0.3f)
                .strafeToConstantHeading(new Vector2d(2, DIST_Y_CHAMBER), null, new ProfileAccelConstraint(-28, 55))

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