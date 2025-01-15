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

public class SetServosToDefaultsCommand extends SequentialCommandGroup {
    public SetServosToDefaultsCommand(OuttakeManager outtakeManager, IntakeManager intakeManager) {
        addCommands(

                //?Deposit
                new SetOuttakeExtendoServoCommand(outtakeManager, OuttakeManager._ExtendoServoState.TRANSFER),
                new SetOuttakeTiltServoCommand(outtakeManager, OuttakeManager._OuttakeTiltServoState.DEPOSIT),
                new SetOuttakeYawServoCommand(outtakeManager, OuttakeManager._OuttakeYawServoState.HORIZONTAL_ServoDown),
                new SetOuttakeClawStateCommand(outtakeManager, OuttakeManager._OuttakeClawServoState.GRIP),

                //?Intake
                new AdjustYawServoCommand(intakeManager, IntakeManager._YawServoState.TRANSFER),
                new SetIntakeTiltServoPosCommand(intakeManager, IntakeManager._TiltServoState.PACKED)
        );

    }
}
