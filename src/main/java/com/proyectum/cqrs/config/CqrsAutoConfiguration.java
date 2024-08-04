package com.proyectum.cqrs.config;

import com.proyectum.cqrs.commad.Command;
import com.proyectum.cqrs.commad.CommandBus;
import com.proyectum.cqrs.commad.CommandHandler;
import com.proyectum.cqrs.command.CommandBusImpl;
import com.proyectum.cqrs.query.Query;
import com.proyectum.cqrs.query.QueryBus;
import com.proyectum.cqrs.query.QueryBusImpl;
import com.proyectum.cqrs.query.QueryHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

@Configuration
@ConditionalOnClass({CommandBus.class, QueryBus.class})
public class CqrsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public CommandBus commandBus(final List<CommandHandler> commandHandlers) {
        var map = new HashMap<Class<? extends Command>, CommandHandler>();
        for (var commandHandler : commandHandlers) {
            var commandType = getGenericType(commandHandler.getClass(), CommandHandler.class);
            map.put((Class<? extends Command>) commandType, commandHandler);
        }
        return new CommandBusImpl(map);
    }

    @Bean
    @ConditionalOnMissingBean
    public QueryBus queryBus(final List<QueryHandler> queryHandlers) {
        var map = new HashMap<Class<? extends Query>, QueryHandler>();
        for (var queryHandler : queryHandlers) {
            var commandType = getGenericType(queryHandler.getClass(), QueryHandler.class);
            map.put((Class<? extends Query>) commandType, queryHandler);
        }
        return new QueryBusImpl(map);
    }

    private Class<?> getGenericType(Class<?> clazz, Class<?> handlerClazz) {
        Type[] genericInterfaces = clazz.getGenericInterfaces();

        for (Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
                Type rawType = parameterizedType.getRawType();

                if (rawType instanceof Class && handlerClazz.isAssignableFrom((Class<?>) rawType)) {
                    Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];

                    if (actualTypeArgument instanceof Class) {
                        return (Class<?>) actualTypeArgument;
                    }
                }
            }
        }
        return null;
    }

}
