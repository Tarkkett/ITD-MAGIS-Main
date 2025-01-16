package org.firstinspires.ftc.teamcode.commands.low_level.outtake;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

class MoveLiftSomeBit(manager: OuttakeManager, i: Int) : InstantCommand({manager.lowerLiftPosition(i)})