package org.firstinspires.ftc.teamcode.drivers;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class C_LoadingBar {
    private static final int MAX_BAR_WIDTH = 15;
    private Telemetry telemetry;

    public C_LoadingBar(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    public void updateProgress(int progress) {
        int filledWidth = (int) (MAX_BAR_WIDTH * (progress / 100.0));
        int emptyWidth = MAX_BAR_WIDTH - filledWidth;

        StringBuilder progressBar = new StringBuilder();

        for (int i = 0; i < filledWidth; i++) {
            progressBar.append("#");
        }

        for (int i = 0; i < emptyWidth; i++) {
            progressBar.append(" ");
        }

        telemetry.addLine("Loading...");
        telemetry.addData("Progress", progressBar.toString() + " " + progress + "%");
        telemetry.update();
    }

    public void complete() {
        updateProgress(100);
        telemetry.addLine("Complete!");
        telemetry.update();
    }
}
