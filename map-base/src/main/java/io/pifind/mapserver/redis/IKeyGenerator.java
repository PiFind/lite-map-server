package io.pifind.mapserver.redis;

public interface IKeyGenerator<T,S> {

    S generate(T...t);

    boolean matches(S s);
}
