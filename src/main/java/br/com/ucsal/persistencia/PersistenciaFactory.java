package br.com.ucsal.persistencia;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import br.com.ucsal.annotations.DatabaseType;
import br.com.ucsal.annotations.Inject;
import br.com.ucsal.annotations.Singleton;


public class PersistenciaFactory {

	public static final int MEMORIA = 0;
	public static final int HSQL = 1;
	
	public static void injectDependencies(Object target) {
        Class<?> clazz = target.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                Inject inject = field.getAnnotation(Inject.class);
                DatabaseType databaseType = inject.type();

                try {
                    Object instance = createRepository(databaseType, field.getType());
                    field.setAccessible(true);
                    field.set(target, instance);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Erro ao injetar dependência", e);
                }
            }
        }
    }

    private static Object createRepository(DatabaseType databaseType, Class<?> repositoryType) {
        switch (databaseType) {
            case HSQLDB:
                return new HSQLProdutoRepository();
            case MEMORIA:
                return carregarSingleton(MemoriaProdutoRepository.class);
            default:
                throw new IllegalArgumentException("Tipo de persistência inesperado: " + databaseType);
        }
    }
	
	 private static ProdutoRepository<?, ?> carregarSingleton(Class<?> clazz) {
	        if (clazz.isAnnotationPresent(Singleton.class)) {
	            try {
	                Method getInstancia = clazz.getMethod("getInstancia");
	                return (ProdutoRepository<?, ?>) getInstancia.invoke(null);
	            } catch (Exception e) {
	                throw new RuntimeException("Erro ao carregar Singleton para a classe: " + clazz.getName(), e);
	            }
	        }
	        throw new IllegalArgumentException("Classe não está anotada com @Singleton: " + clazz.getName());
	    }
}
