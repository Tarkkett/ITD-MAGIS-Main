package org.firstinspires.ftc.teamcode.commands.low_level;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.commands.low_level.intake.AdjustYawServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.intake.SetIntakeTiltServoPosCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeClawStateCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeTiltServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeExtendoServoCommand;
import org.firstinspires.ftc.teamcode.commands.low_level.outtake.SetOuttakeYawServoCommand;
import org.firstinspires.ftc.teamcode.managers.HardwareManager;
import org.firstinspires.ftc.teamcode.managers.IntakeManager;
import org.firstinspires.ftc.teamcode.managers.OuttakeManager;

public class SetServosToDefaultsCommand extends SequentialCommandGroup {
    public SetServosToDefaultsCommand(OuttakeManager outtakeManager, IntakeManager intakeManager, HardwareManager hwmanager) {
        addCommands(

                new SetOuttakeClawStateCommand(outtakeManager, OuttakeManager._OuttakeClawServoState.GRIP),
                new WaitCommand(400),

                //?Deposit
                new SetOuttakeExtendoServoCommand(outtakeManager, OuttakeManager._ExtendoServoState.DEPOSIT_BACK),
                new SetOuttakeYawServoCommand(outtakeManager, OuttakeManager._OuttakeYawServoState.HORIZONTAL_ServoUp),


                //?Intake
                new AdjustYawServoCommand(intakeManager, IntakeManager._YawServoState.TRANSFER),
                new SetIntakeTiltServoPosCommand(intakeManager, IntakeManager._TiltServoState.PACKED),
                new SetOuttakeTiltServoCommand(outtakeManager, OuttakeManager._OuttakeTiltServoState.DEPOSIT_SPECIMEN)

                );

    }
}
