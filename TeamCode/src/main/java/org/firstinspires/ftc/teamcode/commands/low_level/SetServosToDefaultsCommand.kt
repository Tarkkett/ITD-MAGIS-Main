package org.firstinspires.ftc.teamcode.commands.low_level;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.commands.low_level.intake.AdjustYawServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeTiltServoPosCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeClawStateCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeTiltServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeExtendoServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeYawServoCommand;
import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

class SetServosToDefaultsCommand(
        outtakeManager: OuttakeManager,
        intakeManager: IntakeManager
) : SequentialCommandGroup(

        // Deposit
        SetOuttakeExtendoServoCommand(outtakeManager, OuttakeManager._ExtendoServoState.TRANSFER),
        SetOuttakeTiltServoCommand(outtakeManager, OuttakeManager._OuttakeTiltServoState.DEPOSIT),
        SetOuttakeYawServoCommand(outtakeManager, OuttakeManager._OuttakeYawServoState.HORIZONTAL_ServoDown),
        SetOuttakeClawStateCommand(outtakeManager, OuttakeManager._OuttakeClawServoState.GRIP),

        // Intake
        AdjustYawServoCommand(intakeManager, IntakeManager._YawServoState.TRANSFER),
        SetIntakeTiltServoPosCommand(intakeManager, IntakeManager._TiltServoState.PACKED)
)
