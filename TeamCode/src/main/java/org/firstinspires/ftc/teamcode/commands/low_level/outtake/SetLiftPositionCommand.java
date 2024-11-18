package org.firstinspires.ftc.teamcode.commands.low_level.outtake;

import com.arcrobotics.ftclib.command.InstantCommand;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

public class SetLiftPositionCommand extends InstantCommand {

    public SetLiftPositionCommand(OuttakeManager manager, OuttakeManager._LiftState state) {
        super(() -> manager.update(state));
    }
}
