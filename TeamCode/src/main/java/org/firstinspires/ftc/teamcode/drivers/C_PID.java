package org.firstinspires.ftc.teamcode.drivers;

import android.sax.StartElementListener;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config
public class C_PID {

    private double Kp = 0;
    private double Ki = 0;
    private double Kd = 0;

    private double integralSum = 0;

    private ElapsedTime timer = new ElapsedTime();
    private double lastError = 0;

    public C_PID(){}

    public C_PID(double Kp, double Ki, double Kd) {
        tune(Kp, Ki, Kd);
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

        double output = (error * Kp) + (derivative * Kd) + (integralSum * Ki);
        return output;

    }
}
