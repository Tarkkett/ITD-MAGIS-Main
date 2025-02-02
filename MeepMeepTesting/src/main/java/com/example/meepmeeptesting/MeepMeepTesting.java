package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {



    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);

        float DIST_Y_CHAMBER = -36f;
        float DIST_X_CHAMBER = 10;

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(65, 65, Math.toRadians(180), Math.toRadians(180), 14.33)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(9, -62, Math.toRadians(-270)))
                        .strafeToConstantHeading(new Vector2d(9, -5))

                        .splineToConstantHeading(new Vector2d(35 , -35), Math.toRadians(-90))
                        .splineToConstantHeading(new Vector2d(0 , -60), Math.toRadians(-180))
                        .splineToConstantHeading(new Vector2d(-35 , -35), Math.toRadians(-270))
                        .splineToConstantHeading(new Vector2d(9 , -5), Math.toRadians(-90))
//                .strafeToConstantHeading(new Vector2d(DIST_X_CHAMBER, DIST_Y_CHAMBER))
//                .waitSeconds(0.2f)
//
//
//                .strafeToConstantHeading(new Vector2d(30, -36))
//                .splineToConstantHeading(new Vector2d(46, -8), Math.toRadians(-20))
//                .splineToConstantHeading(new Vector2d(48, -10), Math.toRadians(-90))
//                .strafeToConstantHeading(new Vector2d(48, -55), new TranslationalVelConstraint(80), new ProfileAccelConstraint(-32, 1))
//
//                .splineToConstantHeading(new Vector2d(43, -55), Math.toRadians(50), new TranslationalVelConstraint(20))
//                .splineToConstantHeading(new Vector2d(56, -10), Math.toRadians(0))
//                .splineToConstantHeading(new Vector2d(58, -20), Math.toRadians(-90))
//                .strafeToConstantHeading(new Vector2d(58, -55), null, new ProfileAccelConstraint(-32, 1))
//
//                .splineToConstantHeading(new Vector2d(53, -50), Math.toRadians(50), new TranslationalVelConstraint(20))
//                .splineToConstantHeading(new Vector2d(63, -10), Math.toRadians(0))
//                .splineToConstantHeading(new Vector2d(65, -20), Math.toRadians(-90), new TranslationalVelConstraint(25))
//                .strafeToConstantHeading(new Vector2d(65, -50))
//
//                .splineToConstantHeading(new Vector2d(50, -62), Math.toRadians(-90))
//
//
//                //Cycle >>>>>>> 1
//                .waitSeconds(0.3f)
//                .splineToConstantHeading(new Vector2d(8, -36), Math.toRadians(90))
//
//                .waitSeconds(0.3f)
//                .setReversed(true)
//                .splineToConstantHeading(new Vector2d(43, -60), Math.toRadians(-90))
//                //>>>>>>>>>>>>>>
//                //Cycle >>>>>>> 2
//                .waitSeconds(0.3f)
//                .splineToConstantHeading(new Vector2d(6, -36), Math.toRadians(90))
//
//                .waitSeconds(0.3f)
//                .setReversed(true)
//                .splineToConstantHeading(new Vector2d(43, -60), Math.toRadians(-90))
//                //>>>>>>>>>>>>>>
//                //Cycle >>>>>>> 3
//                .waitSeconds(0.3f)
//                .splineToConstantHeading(new Vector2d(4, -36), Math.toRadians(90))
//
//                .waitSeconds(0.3f)
//                .setReversed(true)
//                .splineToConstantHeading(new Vector2d(43, -60), Math.toRadians(-90))
//                //>>>>>>>>>>>>>>
//                //Cycle >>>>>>> 4
//                .waitSeconds(0.3f)
//                .splineToConstantHeading(new Vector2d(2, -36), Math.toRadians(90))
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}