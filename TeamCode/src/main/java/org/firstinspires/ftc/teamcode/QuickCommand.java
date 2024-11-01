package org.firstinspires.ftc.teamcode;

public class QuickCommand implements Command {

    private final Runnable action;
    private boolean isExecuted = false;

    public QuickCommand(Runnable action) {
        this.action = action;
    }

    @Override
    public void initialize() {
        if (action != null) {
            action.run();
        }
        isExecuted = true;
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        return isExecuted;
    }

    @Override
    public void end(boolean interrupted) {

    }
}
