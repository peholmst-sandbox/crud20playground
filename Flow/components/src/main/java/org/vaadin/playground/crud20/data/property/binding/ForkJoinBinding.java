package org.vaadin.playground.crud20.data.property.binding;

import com.vaadin.flow.function.SerializableConsumer;
import jakarta.annotation.Nonnull;
import org.vaadin.playground.crud20.data.property.Property;
import org.vaadin.playground.crud20.data.property.PropertyValueChangeEvent;
import org.vaadin.playground.crud20.data.property.WritableProperty;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.requireNonNull;

// The naming of this class should probably be changed. It is intended to be extended by users.
public abstract class ForkJoinBinding<T> implements Serializable {

    private final WritableProperty<T> joinProperty;
    private final WritableProperty<JoinState> state = WritableProperty.create(new JoinState.Incomplete());
    @SuppressWarnings("FieldCanBeLocal")
    private final SerializableConsumer<PropertyValueChangeEvent<T>> joinPropertyChangeListener = (event) -> doFork();
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final Set<SerializableConsumer<? extends PropertyValueChangeEvent<?>>> forkPropertyChangeListeners = new HashSet<>();

    public ForkJoinBinding(@Nonnull WritableProperty<T> joinProperty) {
        this.joinProperty = requireNonNull(joinProperty);
        joinProperty.addWeakListener(joinPropertyChangeListener);
    }

    // This class has no getters for the state variables, only Properties. It does not follow the same convention
    // as ConvertedProperty. Which convention makes more sense is up for debate.

    public final @Nonnull Property<JoinState> state() {
        return state;
    }

    protected final <F> void registerForkProperty(@Nonnull WritableProperty<F> forkProperty) {
        var forkPropertyChangeListener = (SerializableConsumer<PropertyValueChangeEvent<F>>) (event) -> doAttemptJoin();
        forkProperty.addWeakListener(forkPropertyChangeListener);
        forkPropertyChangeListeners.add(forkPropertyChangeListener);
        doFork();
    }

    private void doFork() {
        fork(joinProperty);
    }

    protected abstract void fork(@Nonnull Property<T> joinProperty);

    @SuppressWarnings("unchecked")
    private void doAttemptJoin() {
        var result = attemptJoin(joinProperty);
        state.set(result);
        if (result instanceof JoinState.Complete<?> complete) {
            joinProperty.set((T) complete.joinValue());
        }
    }

    protected abstract @Nonnull JoinState attemptJoin(@Nonnull Property<T> joinProperty);

    public sealed interface JoinState {

        default boolean isError() {
            return this instanceof Failure;
        }

        default boolean isIncomplete() {
            return this instanceof Incomplete;
        }

        default boolean isComplete() {
            return this instanceof Complete<?>;
        }

        record Incomplete() implements JoinState {
        }

        record Complete<T>(T joinValue) implements JoinState {
        }

        record Failure(String errorMessage) implements JoinState {
        }
    }
}
