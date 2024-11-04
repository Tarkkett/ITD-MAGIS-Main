package org.firstinspires.ftc.teamcode.managers;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.State;
import org.firstinspires.ftc.teamcode.drivers.C_PID;

@Config
public class NavigationManager implements State<NavigationManager._AutoState> {

    public _AutoState state;

    public Telemetry telemetry;

    HardwareManager hardwareManager;

    public double xEncoder;
    public double yEncoder;
    public double headingIMU;

    public static double x;
    public static double y;
    public double theta;

    public C_PID xController = new C_PID(0.002,0,0);
    public C_PID yController = new C_PID(0.002,0,0);
    public C_PID headingController = new C_PID(0,0,0);
    private double t;

    //TESTING
    public static double xKp;
    public static double xKi;
    public static double xKd;

    public static double yKp;
    public static double yKi;
    public static double yKd;


    public NavigationManager(HardwareManager hardwareManager, Telemetry telemetry){
        this.hardwareManager = hardwareManager;
        this.telemetry = telemetry;
    }

    @Override
    public void InitializeStateTransitionActions() {

    }

    @Override
    public void SetSubsystemState(_AutoState state) {

    }

    @Override
    public _AutoState GetSubsystemState() {
        return state;
    }

    @Override
    public void loop() {

        FtcDashboard dashboard = FtcDashboard.getInstance();
        telemetry = dashboard.getTelemetry();

        telemetry.addData("CurrentPos", xEncoder);
        telemetry.addData("TargetPos", x);
        telemetry.update();

        xController.tune(xKp, xKi, xKd);
        yController.tune(yKp, yKi, yKd);

        headingIMU = hardwareManager.odo.getHeading();
        xEncoder = hardwareManager.odo.getPosX();
        yEncoder = hardwareManager.odo.getPosY();

        double x_rotated = x * Math.cos(theta) - y * Math.sin(theta);
        double y_rotated = x * Math.sin(theta) + y * Math.cos(theta);

        hardwareManager.frontLeft.setPower(x_rotated + y_rotated + theta);
        hardwareManager.backLeft.setPower(x_rotated - y_rotated + theta);
        hardwareManager.frontRight.setPower(x_rotated - y_rotated - theta);
        hardwareManager.backRight.setPower(x_rotated + y_rotated - theta);

    }

    public void Drive(float xTarget, float yTarget, double headingTarget) {
        x = xController.update(xTarget, xEncoder);
        y = yController.update(yTarget, yEncoder);
        t = headingController.update(headingTarget, headingIMU);

    }

    public enum _AutoState{

    }
}
