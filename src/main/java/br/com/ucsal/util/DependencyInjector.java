package br.com.ucsal.util;

import java.lang.reflect.Field;

import br.com.ucsal.annotations.Inject;
import br.com.ucsal.persistencia.PersistenciaFactory;
import br.com.ucsal.persistencia.ProdutoRepository;

public class DependencyInjector {

	public class Injector {

	    // Método que cria instâncias com injeção de dependências
	    public static <T> T getInstance(Class<T> clazz) {
	        try {
	            T instance = clazz.getDeclaredConstructor().newInstance();

	            // Verificando e injetando dependências
	            for (Field field : clazz.getDeclaredFields()) {
	                if (field.isAnnotationPresent(Inject.class)) {
	                    field.setAccessible(true);

	                    // Definindo o tipo de banco a ser utilizado (H2 ou Memória)
	                    int tipoBanco = getTipoBanco();  // Aqui você pode definir dinamicamente, ex: de configuração ou parâmetro
	                    Object dependency = createDependency(field.getType(), tipoBanco);
	                    field.set(instance, dependency);
	                }
	            }
	            return instance;
	        } catch (Exception e) {
	            throw new RuntimeException("Erro ao injetar dependências para a classe: " + clazz.getName(), e);
	        }
	    }

	    // Método para criar a dependência dependendo do tipo do banco
	    private static Object createDependency(Class<?> type, int tipoBanco) {
	        if (ProdutoRepository.class.isAssignableFrom(type)) {
	            return PersistenciaFactory.getProdutoRepository(tipoBanco);
	        }
	        throw new IllegalArgumentException("Tipo de dependência não suportado: " + type.getName());
	    }

	    // Aqui você pode definir logicamente de onde vem a configuração (ex: arquivo de propriedades, variáveis de ambiente, etc)
	    private static int getTipoBanco() {
	        // Exemplo de configuração simples
	        String banco = System.getProperty("tipoBanco", "H2"); // Pode ser H2 ou Memoria
	        if ("H2".equalsIgnoreCase(banco)) {
	            return PersistenciaFactory.HSQL;
	        } else {
	            return PersistenciaFactory.MEMORIA;
	        }
	    }
	}
}
