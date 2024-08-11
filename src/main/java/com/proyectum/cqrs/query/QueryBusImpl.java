package com.proyectum.cqrs.query;

import com.proyectum.cqrs.exceptions.UnknownQueryHandlerException;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
public class QueryBusImpl implements QueryBus {

    private final Map<Class<? extends Query>, QueryHandler> handlers;

    @Override
    public <R> R ask(Query query) {
        var handler = handlers.get(query.getClass());
        if (Objects.isNull(handler)) {
            throw new UnknownQueryHandlerException("No handler found for query " + query.getClass());
        }
        return (R) handler.ask(query);
    }
}
