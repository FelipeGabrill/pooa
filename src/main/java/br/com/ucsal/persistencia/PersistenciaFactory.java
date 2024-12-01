package br.com.ucsal.persistencia;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import br.com.ucsal.annotations.Inject;
import br.com.ucsal.annotations.Singleton;
import br.com.ucsal.annotations.Type;

public class PersistenciaFactory {
	
    private static final Map<Class<?>, Object> instances = new HashMap<>();

    public static void injectDependencies(Object target) {
        Class<?> clazz = target.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                if (field.isAnnotationPresent(Inject.class)) {
                    Object dependency;

                    Class<?> fieldType = field.getType();
                    Class<?> iClass = fieldType;

                    if (fieldType.isInterface()) {
                        Type iAnnotation = field.getAnnotation(Type.class);
                        if (iAnnotation != null) {
                            iClass = iAnnotation.value();
                        } else {
                            throw new RuntimeException("Cannot inject dependency for interface " + fieldType.getName() + " without @ImplementedBy annotation");
                        }
                    }

                    if (iClass.isAnnotationPresent(Singleton.class)) {
                        dependency = getInstance(iClass);
                    } else {
                        dependency = iClass.getDeclaredConstructor().newInstance();
                        injectDependencies(dependency); 
                    }

                    field.setAccessible(true);
                    field.set(target, dependency);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
	
    public static <T> T getInstance(Class<T> clazz) {
        if (!clazz.isAnnotationPresent(Singleton.class)) {
            throw new IllegalArgumentException("A classe " + clazz.getName() + " não está anotada com @Singleton.");
        }

        return (T) instances.computeIfAbsent(clazz, PersistenciaFactory::createInstance);
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
