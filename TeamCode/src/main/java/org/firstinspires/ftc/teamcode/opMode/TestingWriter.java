package org.firstinspires.ftc.teamcode.opMode;

import android.net.Uri;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.managers.TelemetryManagerAP;

@TeleOp(name = "USB Test")
public class TestingWriter extends OpMode {


    TelemetryManagerAP telemetryManager = TelemetryManagerAP.getInstance();

    private final Uri DirURI = Uri.parse("content://com.android.externalstorage.documents/tree/761696AF9B%3A");

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

    @Override
    public void stop() {
        telemetryManager.stopLogging();

    }
}
