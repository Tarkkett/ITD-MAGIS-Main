package pedroPathing.constants;

import com.pedropathing.localization.Localizers;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.util.CustomFilteredPIDFCoefficients;
import com.pedropathing.util.CustomPIDFCoefficients;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class FConstants {
    static {
        FollowerConstants.localizers = Localizers.PINPOINT;

        FollowerConstants.leftFrontMotorName = "frontLeft";
        FollowerConstants.leftRearMotorName = "backLeft";
        FollowerConstants.rightFrontMotorName = "frontRight";
        FollowerConstants.rightRearMotorName = "backRight";

        FollowerConstants.leftFrontMotorDirection = DcMotorSimple.Direction.FORWARD;
        FollowerConstants.leftRearMotorDirection = DcMotorSimple.Direction.FORWARD;
        FollowerConstants.rightFrontMotorDirection = DcMotorSimple.Direction.REVERSE;
        FollowerConstants.rightRearMotorDirection = DcMotorSimple.Direction.REVERSE;

        FollowerConstants.mass = 11.81;

        FollowerConstants.xMovement = 68.12;
        FollowerConstants.yMovement = 55.6;

        FollowerConstants.forwardZeroPowerAcceleration = -35.16;
        FollowerConstants.lateralZeroPowerAcceleration = -81.8;

        FollowerConstants.translationalPIDFCoefficients.setCoefficients(0.155,0,0.035,0);
        FollowerConstants.useSecondaryTranslationalPID = true;
        FollowerConstants.secondaryTranslationalPIDFCoefficients.setCoefficients(0.24,0,0.035,0);

        FollowerConstants.headingPIDFCoefficients.setCoefficients(-2,0,-0.05,0);
        FollowerConstants.useSecondaryHeadingPID = true;
        FollowerConstants.secondaryHeadingPIDFCoefficients.setCoefficients(-2,0,-0.045,0);

        FollowerConstants.drivePIDFCoefficients.setCoefficients(0.008,0,0.000002,0.6,0);
        FollowerConstants.useSecondaryDrivePID = true;
//        FollowerConstants.drivePIDFFeedForward = 0.004;
        FollowerConstants.secondaryDrivePIDFCoefficients.setCoefficients(0.0055,0,0.00002,0.8,0);
//        FollowerConstants.secondaryDrivePIDFFeedForward = -0.03;

//        FollowerConstants.headingPIDFFeedForward = 0.01;
//        FollowerConstants.secondaryHeadingPIDFFeedForward = 0.004;

        FollowerConstants.zeroPowerAccelerationMultiplier = 3.2;
        FollowerConstants.centripetalScaling = 0.0005;

        FollowerConstants.pathEndTimeoutConstraint = 100;
        FollowerConstants.pathEndTValueConstraint = 0.995;
        FollowerConstants.pathEndVelocityConstraint = 0.1;
        FollowerConstants.pathEndTranslationalConstraint = 0.1;
        FollowerConstants.pathEndHeadingConstraint = 0.007;
    }
}
