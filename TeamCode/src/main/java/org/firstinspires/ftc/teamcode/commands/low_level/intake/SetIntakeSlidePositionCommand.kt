package org.firstinspires.ftc.teamcode.commands.low_level.intake;

import com.arcrobotics.ftclib.command.InstantCommand;
import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.managers.IntakeManager._GripState
import org.firstinspires.ftc.teamcode.managers.IntakeManager._SlideState

class SetIntakeSlidePositionCommand( manager: IntakeManager, _SlideState: _SlideState) : InstantCommand({manager.update(_SlideState)})