package org.firstinspires.ftc.teamcode.commands.low_level;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.commands.low_level.intake.AdjustYawServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeTiltServoPosCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeClawStateCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakePitchServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeTiltServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeYawServoCommand;
import org.firstinspires.ftc.teamcode.managers.HardwareManager;
import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

public class SetServosToDefaultsCommand extends SequentialCommandGroup {
    public SetServosToDefaultsCommand(OuttakeManager outtakeManager, IntakeManager intakeManager, HardwareManager hwmanager) {
        addCommands(

                //?Deposit
                new SetOuttakePitchServoCommand(outtakeManager, OuttakeManager._PitchServoState.HOME),
                new SetOuttakeYawServoCommand(outtakeManager, OuttakeManager._OuttakeYawServoState.HORIZONTAL_ServoUp),
                new SetOuttakeTiltServoCommand(outtakeManager, OuttakeManager._OuttakeTiltServoState.HOME),
                new SetOuttakeClawStateCommand(outtakeManager, OuttakeManager._OuttakeClawServoState.CLOSED),


                //?Intake
                new AdjustYawServoCommand(intakeManager, IntakeManager._YawServoState.TRANSFER),
                new SetIntakeTiltServoPosCommand(intakeManager, IntakeManager._TiltServoState.PACKED),
                new SetOuttakeTiltServoCommand(outtakeManager, OuttakeManager._OuttakeTiltServoState.DEPOSIT_SPECIMEN)

                );

    }
}
