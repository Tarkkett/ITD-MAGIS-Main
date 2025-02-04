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

        float DIST_Y_CHAMBER = -31f;
        float DIST_X_CHAMBER = 10;

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(55, 55, Math.toRadians(180), Math.toRadians(180), 14.33)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(9, -62, Math.toRadians(-270)))

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
                .splineToConstantHeading(new Vector2d(43, -62), Math.toRadians(-90), null, new ProfileAccelConstraint(-28, 55))
                //>>>>>>>>>>>>>>
                //Cycle >>>>>>> 2
                .waitSeconds(0.3f)
                .strafeToConstantHeading(new Vector2d(6, DIST_Y_CHAMBER), null, new ProfileAccelConstraint(-28, 55))

                .waitSeconds(0.3f)
                .setReversed(true)
                .strafeToConstantHeading(new Vector2d(28, -40))

                .splineToConstantHeading(new Vector2d(43, -62), Math.toRadians(-90), null, new ProfileAccelConstraint(-28, 55))

                //>>>>>>>>>>>>>>
                //Cycle >>>>>>> 3
                .waitSeconds(0.3f)
                .strafeToConstantHeading(new Vector2d(4, DIST_Y_CHAMBER), null, new ProfileAccelConstraint(-28, 55))

                .waitSeconds(0.3f)
                .setReversed(true)
                .strafeToConstantHeading(new Vector2d(26, -40))

                .splineToConstantHeading(new Vector2d(43, -62), Math.toRadians(-90), null, new ProfileAccelConstraint(-28, 55))

                //>>>>>>>>>>>>>>
                //Cycle >>>>>>> 4
                .waitSeconds(0.3f)
                .strafeToConstantHeading(new Vector2d(2, DIST_Y_CHAMBER), null, new ProfileAccelConstraint(-28, 55))

                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}