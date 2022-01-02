package fr.theogiraudet.tp.spring.utils;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class Loading<T> {

    private final byte state;
    private final T value;

    private Loading(T value, byte state) {
        this.state = state;
        this.value = value;
    }

    public static <T> Loading<T> loadNullable(T value) {
        return (value == null) ? notExist() : loaded(value);
    }

    public static <T> Loading<T> loaded(T value) {
        return new Loading<>(Objects.requireNonNull(value), (byte) 0);
    }

    public static <T> Loading<T> loading() {
        return new Loading<>(null, (byte) 1);
    }

    public static <T> Loading<T> notExist() {
        return new Loading<>(null, (byte) 2);
    }

    public static <T> Loading<T> fromOptional(Optional<T> optional) {
        return optional.map(Loading::loaded).orElseGet(Loading::notExist);
    }

    public boolean loaded() {
        return state == 0;
    }

    public boolean inProgress() {
        return state == 1;
    }

    public boolean exists() {
        return state == 2;
    }

    public T get() {
        if(state != 0)
            throw new NoSuchElementException("No value present");
        return value;
    }

    public Loading<T> loadIfAbsent(boolean load) {
        if(state == 2 && load)
            return loading();
        else
            return this;
    }

    public <U> Loading<U> map(Function<? super T, ? extends U> function) {
        if(loaded())
            return new Loading<>(function.apply(value), state);
        return new Loading<>(null, state);
    }

    public Loading<T> ifPresent(Consumer<? super T> function) {
        if(loaded())
            function.accept(value);
        return this;
    }

    public Loading<T> ifLoading(Runnable function) {
        if(inProgress())
            function.run();
        return this;
    }
}
