package pedroPathing.constants;

import com.pedropathing.localization.Localizers;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.util.CustomFilteredPIDFCoefficients;
import com.pedropathing.util.CustomPIDFCoefficients;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class FConstants {
    static {
        FollowerConstants.localizers = Localizers.PINPOINT;

        FollowerConstants.leftFrontMotorName = "backRight";
        FollowerConstants.leftRearMotorName = "frontRight";
        FollowerConstants.rightFrontMotorName = "backLeft";
        FollowerConstants.rightRearMotorName = "frontLeft";

        FollowerConstants.leftFrontMotorDirection = DcMotorSimple.Direction.REVERSE;
        FollowerConstants.leftRearMotorDirection = DcMotorSimple.Direction.REVERSE;
        FollowerConstants.rightFrontMotorDirection = DcMotorSimple.Direction.FORWARD;
        FollowerConstants.rightRearMotorDirection = DcMotorSimple.Direction.FORWARD;

        FollowerConstants.mass = 11.81;

        FollowerConstants.xMovement = 57.0;
        FollowerConstants.yMovement = 48.27;

        FollowerConstants.forwardZeroPowerAcceleration = -33.0;
        FollowerConstants.lateralZeroPowerAcceleration = -66.31;

        FollowerConstants.translationalPIDFCoefficients.setCoefficients(0.155,0,0.035,0);
        FollowerConstants.useSecondaryTranslationalPID = true;
        FollowerConstants.secondaryTranslationalPIDFCoefficients.setCoefficients(0.14,0,0.035,0); // Not being used, @see useSecondaryTranslationalPID

        FollowerConstants.headingPIDFCoefficients.setCoefficients(2,0,0.05,0);
        FollowerConstants.useSecondaryHeadingPID = true;
        FollowerConstants.secondaryHeadingPIDFCoefficients.setCoefficients(2,0,0.045,0); // Not being used, @see useSecondaryHeadingPID

        FollowerConstants.drivePIDFCoefficients.setCoefficients(0.02,0,0.0000002,0.6,0);
        FollowerConstants.useSecondaryDrivePID = true;
//        FollowerConstants.drivePIDFFeedForward = 0.004;
        FollowerConstants.secondaryDrivePIDFCoefficients.setCoefficients(0.02,0,0.0000002,0.6,0); // Not being used, @see useSecondaryDrivePID
//        FollowerConstants.secondaryDrivePIDFFeedForward = -0.03;

//        FollowerConstants.headingPIDFFeedForward = 0.01;
//        FollowerConstants.secondaryHeadingPIDFFeedForward = 0.004;

        FollowerConstants.zeroPowerAccelerationMultiplier = 5;
        FollowerConstants.centripetalScaling = 0.0005;

        FollowerConstants.pathEndTimeoutConstraint = 200;
        FollowerConstants.pathEndTValueConstraint = 0.995;
        FollowerConstants.pathEndVelocityConstraint = 0.1;
        FollowerConstants.pathEndTranslationalConstraint = 0.1;
        FollowerConstants.pathEndHeadingConstraint = 0.007;
    }
}
