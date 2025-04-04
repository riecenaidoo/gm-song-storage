package com.bobo.storage.core.domain;

import org.junit.jupiter.api.BeforeEach;

import java.util.function.Supplier;

/**
 * A {@link Supplier} of a type of {@code Domain} object.
 * <p>
 * A {@code Mother} is knowledgeable about the needs (invariants) of her children,
 * and will produce well-formed objects for the purpose of testing, unless otherwise instructed.
 * <p>
 * A {@code Mother} is allowed to over-step the boundaries of her children;
 * she may be asked to expose {@code protected} members of a child,
 * but is respectful enough to never expose {@code private} members.
 * <p>
 * A {@code Mother} is not guaranteed to be immutable; instances should not be reused between tests.
 *
 * @param <T> the type of children ({@code Domain} objects) of this {@code Mother}.
 * @see BeforeEach
 */
public interface Mother<T> extends Supplier<T> {

}
