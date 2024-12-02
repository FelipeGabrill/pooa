package br.com.ucsal.annotations.Implements;

import java.lang.reflect.Field;

import br.com.ucsal.annotations.Inject;
import br.com.ucsal.annotations.Singleton;
import br.com.ucsal.annotations.Type;

public class InjectDependencies {

	public static void injectDependencies(Object target) {
        Field[] fields = target.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Inject.class)) {
                injectField(target, field);
            }
        }
    }

    private static void injectField(Object target, Field field) {
        try {
            Object dependency = resolveDependency(field);
            field.setAccessible(true);
            field.set(target, dependency);
        } catch (Exception e) {
            throw new RuntimeException("Error injecting field: " + field.getName(), e);
        }
    }

    private static Object resolveDependency(Field field) throws Exception {
        Class<?> fieldType = field.getType();
        Class<?> implementationClass = fieldType;

        if (fieldType.isInterface()) {
            Type typeAnnotation = field.getAnnotation(Type.class);
            if (typeAnnotation != null) {
                implementationClass = typeAnnotation.value();
            } else {
                throw new RuntimeException(
                    "Cannot inject dependency for interface " + fieldType.getName() + " without @Type annotation"
                );
            }
        }

        if (implementationClass.isAnnotationPresent(Singleton.class)) {
            return SingletonRegistry.getInstance(implementationClass);
        }

        Object instance = implementationClass.getDeclaredConstructor().newInstance();
        injectDependencies(instance); // Recursão para injetar dependências no objeto criado
        return instance;
    }
}