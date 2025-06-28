package org.firstinspires.ftc.teamcode.commands.low_level;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.managers.DriveManager;
import org.firstinspires.ftc.teamcode.managers.HardwareManager;

public class ResetHeadingCommand extends SequentialCommandGroup {
    public ResetHeadingCommand(HardwareManager hardwareManager, DriveManager driveManager) {
        addCommands(
                new InstantCommand(hardwareManager::ResetPosAndIMU),
                new InstantCommand(driveManager::resetHeading)
        );
    }
}
