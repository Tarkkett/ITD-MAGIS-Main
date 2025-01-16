package org.firstinspires.ftc.teamcode.commands.low_level.outtake;

import com.arcrobotics.ftclib.command.InstantCommand;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

class SetLiftPositionCommand(
        manager: OuttakeManager,
        state: OuttakeManager._LiftState,
        targetPosition: Int? = null
) : InstantCommand({
    manager.update(state, targetPosition ?: 0)
})