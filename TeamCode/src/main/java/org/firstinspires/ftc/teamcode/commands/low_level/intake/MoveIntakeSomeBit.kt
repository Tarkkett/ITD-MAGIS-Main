package org.firstinspires.ftc.teamcode.commands.low_level.intake

import com.arcrobotics.ftclib.command.InstantCommand
import org.firstinspires.ftc.teamcode.managers.IntakeManager

class MoveIntakeSomeBit(manager: IntakeManager, i: Int) : InstantCommand({ manager.moveSlide(i) })