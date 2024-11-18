package org.firstinspires.ftc.teamcode.commands.low_level.intake;

import com.arcrobotics.ftclib.command.InstantCommand;
import org.firstinspires.ftc.teamcode.managers.IntakeManager;

public class SetIntakeSlidePositionCommand extends InstantCommand {
    public SetIntakeSlidePositionCommand(IntakeManager manager, IntakeManager._SlideState targetState){
        super(() -> manager.update(targetState));
    }
}
