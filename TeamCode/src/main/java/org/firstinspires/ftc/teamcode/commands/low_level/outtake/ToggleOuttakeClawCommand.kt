package org.firstinspires.ftc.teamcode.commands.low_level.outtake

import com.arcrobotics.ftclib.command.CommandBase
import com.arcrobotics.ftclib.command.CommandScheduler
import org.firstinspires.ftc.teamcode.managers.OuttakeManager
import org.firstinspires.ftc.teamcode.util.GamepadPlus

/**
 *
 * @property outtakeManager OuttakeManager
 * @property gamepadCodriver GamepadPlus
 * @property gamepadDriver GamepadPlus
 * @constructor
 */
class ToggleOuttakeClawCommand(
    private val outtakeManager: OuttakeManager,
    private val gamepadCodriver: GamepadPlus,
    private val gamepadDriver: GamepadPlus
) : CommandBase() {

    override fun initialize() {
        if (!outtakeManager.GetClawState()) {

            // Notify driver tp boost forward
            gamepadDriver.rumble(200)

            CommandScheduler.getInstance().schedule(
                SetOuttakeClawStateCommand(
                    outtakeManager,
                    OuttakeManager._OuttakeClawServoState.GRIP
                )
            )
        } else {
            CommandScheduler.getInstance().schedule(
                SetOuttakeClawStateCommand(
                    outtakeManager,
                    OuttakeManager._OuttakeClawServoState.RELEASE
                )
            )
        }
    }

    override fun isFinished(): Boolean {
        return true
    }
}
