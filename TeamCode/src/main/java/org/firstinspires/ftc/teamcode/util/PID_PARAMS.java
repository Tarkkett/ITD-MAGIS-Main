package org.firstinspires.ftc.teamcode.util;

public class PID_PARAMS {
    public double kP;
    public double kI;
    public double kD;
    public double tolerance;

    public PID_PARAMS(double kP, double kI, double kD, double tolerance) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.tolerance = tolerance;
    }
}