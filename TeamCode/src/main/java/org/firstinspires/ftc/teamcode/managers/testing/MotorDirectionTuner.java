package org.firstinspires.ftc.teamcode.managers.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.opMode.OpModeTemplate;

@Config
@TeleOp(name = "Motor Direction Tuner", group = "Tuning")
public class MotorDirectionTuner extends OpModeTemplate {

    /**
     * This is a simple teleop routine for debugging your motor configuration.
     * Pressing each of the buttons will power its respective motor.
     *
     * Button Mappings:
     *
     * Xbox/PS4 Button - Motor
     *   X / ▢         - Front Left
     *   Y / Δ         - Front Right
     *   B / O         - Rear  Right
     *   A / X         - Rear  Left
     *                                    The buttons are mapped to match the wheels spatially if you
     *                                    were to rotate the gamepad 45deg°. x/square is the front left
     *                    ________        and each button corresponds to the wheel as you go clockwise
     *                   / ______ \
     *     ------------.-'   _  '-..+              Front of Bot
     *              /   _  ( Y )  _  \                  ^
     *             |  ( X )  _  ( B ) |     Front Left   \    Front Right
     *        ___  '.      ( A )     /|       Wheel       \      Wheel
     *      .'    '.    '-._____.-'  .'       (x/▢)        \     (Y/Δ)
     *     |       |                 |                      \
     *      '.___.' '.               |          Rear Left    \   Rear Right
     *               '.             /             Wheel       \    Wheel
     *                \.          .'              (A/X)        \   (B/O)
     *                  \________/
     *
     */
        public static double MOTOR_POWER = 0.15;

    @Override
    public void init() {
        initSystems(false);

        Telemetry telemetry = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());

        telemetry.addLine("Press play to begin the debugging opmode");
        telemetry.update();

        telemetry.clearAll();
        telemetry.setDisplayFormat(Telemetry.DisplayFormat.HTML);
    }

    @Override
    public void loop() {
        telemetry.addLine("Press each button to turn on its respective motor");
        telemetry.addLine();
        telemetry.addLine("<font face=\"monospace\">Xbox/PS4 Button - Motor</font>");
        telemetry.addLine("<font face=\"monospace\">&nbsp;&nbsp;X / ▢&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Front Left</font>");
        telemetry.addLine("<font face=\"monospace\">&nbsp;&nbsp;Y / Δ&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Front Right</font>");
        telemetry.addLine("<font face=\"monospace\">&nbsp;&nbsp;B / O&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Rear&nbsp;&nbsp;Right</font>");
        telemetry.addLine("<font face=\"monospace\">&nbsp;&nbsp;A / X&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Rear&nbsp;&nbsp;Left</font>");
        telemetry.addLine();

        if(gamepad1.x) {
            hardwareManager.frontLeft.setPower(MOTOR_POWER);
            telemetry.addLine("Running Motor: Front Left");
        } else if(gamepad1.y) {
            hardwareManager.frontRight.setPower(MOTOR_POWER);
            telemetry.addLine("Running Motor: Front Right");
        } else if(gamepad1.b) {
            hardwareManager.backRight.setPower(MOTOR_POWER);
            telemetry.addLine("Running Motor: Rear Right");
        } else if(gamepad1.a) {
            hardwareManager.backLeft.setPower(MOTOR_POWER);
            telemetry.addLine("Running Motor: Rear Left");
        } else {
            telemetry.addLine("Running Motor: None");
        }

        telemetry.update();
    }
}
