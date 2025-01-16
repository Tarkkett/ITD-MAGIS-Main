package org.firstinspires.ftc.teamcode.commands.low_level.intake;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.managers.IntakeManager._GripState
import org.firstinspires.ftc.teamcode.managers.IntakeManager._TiltServoState

class SetIntakeTiltServoPosCommand( manager: IntakeManager, _TiltServoState: _TiltServoState) : InstantCommand({manager.update(_TiltServoState)})
