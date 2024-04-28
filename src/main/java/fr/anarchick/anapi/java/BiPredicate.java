package fr.anarchick.anapi.java;

@FunctionalInterface
public interface BiPredicate<T, U> {
    boolean test(T t, U u);
}
