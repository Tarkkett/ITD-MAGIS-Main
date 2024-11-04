package org.firstinspires.ftc.teamcode.managers;

import java.util.Objects;

public class Transition<S> {
    private final S fromState;
    private final S toState;

    public Transition(S fromState, S toState) {
        this.fromState = fromState;
        this.toState = toState;
    }

    @SuppressWarnings("unused")
    public S getFromState() {
        return fromState;
    }

    @SuppressWarnings("unused")
    public S getToState() {
        return toState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transition)) return false;
        Transition<?> transition = (Transition<?>) o;
        return Objects.equals(fromState, transition.fromState) &&
                Objects.equals(toState, transition.toState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromState, toState);
    }
}

