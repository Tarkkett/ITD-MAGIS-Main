package org.firstinspires.ftc.teamcode.commands.low_level.intake;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.managers.IntakeManager._GripState

class SetIntakeGripStateCommand( manager: IntakeManager, _GripState: _GripState) : InstantCommand({manager.update(_GripState)})
