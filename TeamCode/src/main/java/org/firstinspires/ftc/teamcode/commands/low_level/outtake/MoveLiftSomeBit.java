package org.firstinspires.ftc.teamcode.commands.low_level.outtake;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

public class MoveLiftSomeBit extends InstantCommand {
    public MoveLiftSomeBit(OuttakeManager manager, int i) {
        manager.lowerLiftPosition(i);
    }
}
