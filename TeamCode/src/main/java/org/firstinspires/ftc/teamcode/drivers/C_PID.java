package org.firstinspires.ftc.teamcode.drivers;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.util.PID_PARAMS;

@Config
public class C_PID {

    private double Kp = 0;
    private double Ki = 0;
    private double Kd = 0;

    private double integralSum = 0;

    private final ElapsedTime timer = new ElapsedTime();
    private double lastError = 0;

    @SuppressWarnings("unused")
    public C_PID(){}

    public C_PID(double Kp, double Ki, double Kd) {
        tune(Kp, Ki, Kd);
    }

    public C_PID(PID_PARAMS params){
        tune(params.kP, params.kI, params.kD);
    }

    public void tune(double Kp, double Ki, double Kd){
        this.Kp = Kp;
        this.Ki = Ki;
        this.Kd = Kd;
    }

    public double update(double target, double state) {

        double currentTime = timer.seconds();
        double error = target - state;
        integralSum += error * currentTime;
        double derivative = (error - lastError) / currentTime;
        lastError = error;

        timer.reset();

        return (error * Kp) + (derivative * Kd) + (integralSum * Ki);

    }

    public void tune(PID_PARAMS params) {
        this.Kp = params.kP;
        this.Ki = params.kI;
        this.Kd = params.kD;
    }
}
