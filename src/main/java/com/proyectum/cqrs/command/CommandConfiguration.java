package com.proyectum.cqrs.command;

import com.proyectum.cqrs.commad.Command;
import com.proyectum.cqrs.commad.CommandBus;
import com.proyectum.cqrs.commad.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class CommandConfiguration {

    @Bean
    public CommandBus commandBus(final List<CommandHandler> commandHandlers) {
        var map = new HashMap<Class<? extends Command>, CommandHandler>();
        for (var commandHandler : commandHandlers) {
            var commandType = getGenericType(commandHandler.getClass());
            map.put((Class<? extends Command>) commandType, commandHandler);
        }
        return new CommandBusImpl(map);
    }

    private Class<?> getGenericType(Class<?> clazz) {
        Type[] genericInterfaces = clazz.getGenericInterfaces();

        for (Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
                Type rawType = parameterizedType.getRawType();

                if (rawType instanceof Class && CommandHandler.class.isAssignableFrom((Class<?>) rawType)) {
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
