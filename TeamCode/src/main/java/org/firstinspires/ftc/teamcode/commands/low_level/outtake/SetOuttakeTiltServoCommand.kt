package org.firstinspires.ftc.teamcode.commands.low_level.outtake;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

class SetOuttakeTiltServoCommand(manager: OuttakeManager, state: OuttakeManager._OuttakeTiltServoState) : InstantCommand({manager.update(state)})