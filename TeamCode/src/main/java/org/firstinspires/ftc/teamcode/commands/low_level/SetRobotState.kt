package org.firstinspires.ftc.teamcode.commands.low_level;

import com.arcrobotics.ftclib.command.InstantCommand;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager

import org.firstinspires.ftc.teamcode.opMode.StateMachine;

class SetRobotState(machine: StateMachine, state: StateMachine._RobotState) : InstantCommand({machine.SetSubsystemState(state)})
