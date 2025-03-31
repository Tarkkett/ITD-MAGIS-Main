package org.firstinspires.ftc.teamcode.commands.low_level;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.commands.low_level.intake.AdjustYawServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakePitchServoCommand;
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
                new SetOuttakeYawServoCommand(outtakeManager, OuttakeManager._OuttakeYawServoState.HORIZONTAL_Pickup),
                new SetOuttakeTiltServoCommand(outtakeManager, OuttakeManager._OuttakeTiltServoState.HOME),
                new SetOuttakeClawStateCommand(outtakeManager, OuttakeManager._OuttakeClawServoState.CLOSED),

                //?Intake
                new AdjustYawServoCommand(intakeManager, IntakeManager._YawServoState.TRANSFER),
                new SetIntakeTiltServoPosCommand(intakeManager, IntakeManager._TiltServoState.PACKED),
                new SetIntakePitchServoCommand(intakeManager, IntakeManager._PitchServoState.PACKED),

                new WaitCommand(400),
                new SetOuttakePitchServoCommand(outtakeManager, OuttakeManager._PitchServoState.ZERO)

        );

    }
}
