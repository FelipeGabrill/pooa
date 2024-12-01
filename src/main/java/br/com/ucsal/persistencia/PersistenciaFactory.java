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
                    Class<?> implClass = fieldType;

                    if (fieldType.isInterface()) {
                        Type iAnnotation = field.getAnnotation(Type.class);
                        if (iAnnotation != null) {
                            implClass = iAnnotation.value();
                        } else {
                            throw new RuntimeException("Cannot inject dependency for interface " + fieldType.getName() + " without @ImplementedBy annotation");
                        }
                    }

                    if (implClass.isAnnotationPresent(Singleton.class)) {
                        dependency = getInstance(implClass);
                    } else {
                        dependency = implClass.getDeclaredConstructor().newInstance();
                        injectDependencies(dependency); // Recursive injection
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
        if (instances.containsKey(clazz)) {
            return (T) instances.get(clazz);
        }

        if (clazz.isAnnotationPresent(Singleton.class)) {
            try {
                Constructor<T> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                T instance = constructor.newInstance();
                instances.put(clazz, instance);
                return instance;
            } catch (Exception e) {
                throw new RuntimeException("Erro ao criar a instância do Singleton", e);
            }
        }

        throw new IllegalArgumentException("A classe " + clazz.getName() + " não está anotada com @Singleton.");
    }
}
