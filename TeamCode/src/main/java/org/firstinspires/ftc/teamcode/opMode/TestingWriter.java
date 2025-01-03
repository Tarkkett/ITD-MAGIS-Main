package org.firstinspires.ftc.teamcode.opMode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.managers.TelemetryManager;

@TeleOp(name = "USB Test")
public class TestingWriter extends OpMode {


    TelemetryManager telemetryManager = TelemetryManager.getInstance();

    @Override
    public void init() {
        telemetryManager.setTelemetry(telemetry);

        telemetryManager.startLogging();
    }

    @Override
    public void loop() {
        telemetryManager.addData("Status", "Running");
        telemetryManager.addData("Battery Voltage", String.valueOf(hardwareMap.voltageSensor.iterator().next().getVoltage()));
        telemetryManager.updateTelemetry();
    }
}
