package org.firstinspires.ftc.teamcode.commands.low_level;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.commands.low_level.intake.AdjustYawServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeTiltServoPosCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeTiltServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetSpecimentServoPositionCommand;
import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

public class SetServosToDefaultsCommand extends SequentialCommandGroup {
    public SetServosToDefaultsCommand(OuttakeManager outtakeManager, IntakeManager intakeManager) {
        addCommands(
                new SetSpecimentServoPositionCommand(outtakeManager, OuttakeManager._SpecimentServoState.CLOSED),
                new SetOuttakeTiltServoCommand(outtakeManager, OuttakeManager._OuttakeTiltServoState.HIGH),
                new WaitCommand(300),
                new AdjustYawServoCommand(intakeManager, IntakeManager._YawServoState.TRANSFER),
                new SetIntakeTiltServoPosCommand(intakeManager, IntakeManager._TiltServoState.PACKED),
                new WaitCommand(300),
                new SetOuttakeTiltServoCommand(outtakeManager, OuttakeManager._OuttakeTiltServoState.LOW)
        );

    }
}
