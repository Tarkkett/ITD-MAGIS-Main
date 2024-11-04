package org.firstinspires.ftc.teamcode.commands.auto;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.managers.NavigationManager;

public class DriveToPositionCommand extends InstantCommand {
    public DriveToPositionCommand(float xTarget, float yTarget, double headingHarget, NavigationManager nav) {
        super(() -> nav.Drive(xTarget, yTarget, headingHarget));
    }
}
