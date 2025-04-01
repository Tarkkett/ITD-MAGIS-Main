package org.firstinspires.ftc.teamcode.drivers;

import com.qualcomm.robotcore.util.ElapsedTime;

public class C_MotionProfile {

    private final double maxVelocity;
    private final double maxAcceleration;
    private final double maxDeceleration;
    private double targetPosition;
    private double currentVelocity = 0;
    private double currentPosition = 0;

    private final ElapsedTime timer = new ElapsedTime();

    public C_MotionProfile(double maxVelocity, double maxAcceleration, double maxDeceleration) {
        this.maxVelocity = maxVelocity;
        this.maxAcceleration = maxAcceleration;
        this.maxDeceleration = maxDeceleration;
    }

    public void setTargetPosition(double target) {
        this.targetPosition = target;
        this.currentPosition = 0;
        this.currentVelocity = 0;
        timer.reset();
    }

    public double update() {
        double elapsedTime = timer.seconds();
        timer.reset();

        double remainingDistance = targetPosition - currentPosition;
        double decelerationDistance = (currentVelocity * currentVelocity) / (2 * maxDeceleration);

        if (currentVelocity < maxVelocity && remainingDistance > decelerationDistance) {
            currentVelocity += maxAcceleration * elapsedTime;
            if (currentVelocity > maxVelocity) {
                currentVelocity = maxVelocity;
            }
        }

        else if (remainingDistance <= decelerationDistance) {
            currentVelocity -= maxDeceleration * elapsedTime;
            if (currentVelocity < 0) {
                currentVelocity = 0;
            }
        }

        currentPosition += currentVelocity * elapsedTime;

        if (Math.abs(remainingDistance) < 0.1) {
            currentVelocity = 0;
            currentPosition = targetPosition;
        }

        return currentVelocity;
    }

    public boolean isComplete() {
        return currentVelocity == 0 && Math.abs(targetPosition - currentPosition) < 0.1;
    }
}
