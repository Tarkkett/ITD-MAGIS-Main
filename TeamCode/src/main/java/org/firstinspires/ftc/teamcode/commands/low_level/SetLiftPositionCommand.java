package org.firstinspires.ftc.teamcode.commands.low_level;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

public class SetLiftPositionCommand extends InstantCommand {

    public SetLiftPositionCommand(OuttakeManager liftManager, OuttakeManager._LiftState state) {
        super(() -> {
            liftManager.update(state);
        });
    }
}
