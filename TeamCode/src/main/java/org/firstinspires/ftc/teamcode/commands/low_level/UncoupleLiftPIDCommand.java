package org.firstinspires.ftc.teamcode.commands.low_level;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

public class UncoupleLiftPIDCommand extends InstantCommand {
    public UncoupleLiftPIDCommand(OuttakeManager manager, boolean isAuto){
        super(()->manager.updateMode(isAuto));
    }
}
