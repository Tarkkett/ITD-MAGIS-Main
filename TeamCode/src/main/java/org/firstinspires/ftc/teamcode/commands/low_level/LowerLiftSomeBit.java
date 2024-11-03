package org.firstinspires.ftc.teamcode.commands.low_level;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

public class LowerLiftSomeBit extends InstantCommand {
    public LowerLiftSomeBit(OuttakeManager manager, int i) {
        manager.lowerLiftPosition(i);
    }
}
