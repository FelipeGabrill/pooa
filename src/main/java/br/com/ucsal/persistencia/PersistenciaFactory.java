package br.com.ucsal.persistencia;

import java.lang.reflect.Method;

import br.com.ucsal.annotations.Singleton;

public class PersistenciaFactory {

	public static final int MEMORIA = 0;
	public static final int HSQL = 1;
	
	 public static ProdutoRepository<?, ?> getProdutoRepository(int type) {
	        switch (type) {
	            case MEMORIA:
	                return carregarSingleton(MemoriaProdutoRepository.class);
	            case HSQL:
	                return new HSQLProdutoRepository();
	            default:
	                throw new IllegalArgumentException("Tipo de persistência inesperado: " + type);
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
