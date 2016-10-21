package moklev.compiler.util;

import java.util.Objects;

/**
 * @author Moklev Vyacheslav
 */
@FunctionalInterface
public interface QuadriConsumer<A, B, C, D> {
    void accept(A a, B b, C c, D d);

    default QuadriConsumer<A, B, C, D> andThen(QuadriConsumer<? super A, ? super B, ? super C, ? super D> after) {
        Objects.requireNonNull(after);

        return (a, b, c, d) -> {
            accept(a, b, c, d);
            after.accept(a, b, c, d);
        };
    }
}
