package br.com.ucsal.annotations.Implements;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import br.com.ucsal.annotations.Singleton;

public class SingletonRegistry {

    private static final Map<Class<?>, Object> instances = new HashMap<>();

    public static <T> T getInstance(Class<T> clazz) {
        if (!clazz.isAnnotationPresent(Singleton.class)) {
            throw new IllegalArgumentException("A classe " + clazz.getName() + " não está anotada com @Singleton.");
        }

        return (T) instances.computeIfAbsent(clazz, SingletonRegistry::createInstance);
    }

    private static <T> T createInstance(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("A classe " + clazz.getName() + " deve ter um construtor sem argumentos.", e);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar a instância do Singleton para a classe " + clazz.getName(), e);
        }
    }
}
