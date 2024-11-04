package org.firstinspires.ftc.teamcode.commands.low_level;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.managers.IntakeManager;

public class MoveIntakeSomeBit extends InstantCommand {
    public MoveIntakeSomeBit(IntakeManager manager, int i) {
        super(() -> manager.moveSlide(i));
    }
}
