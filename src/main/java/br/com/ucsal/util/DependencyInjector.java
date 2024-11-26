package br.com.ucsal.util;

import java.lang.reflect.Field;

import br.com.ucsal.annotations.DatabaseType;
import br.com.ucsal.annotations.Inject;
import br.com.ucsal.persistencia.PersistenciaFactory;
import br.com.ucsal.persistencia.ProdutoRepository;

public class DependencyInjector {

//    // Classe interna Injector que realiza a injeção de dependências
//    public static class Injector {
//
//        // Método que cria instâncias com injeção de dependências
//        public static <T> T getInstance(Class<T> clazz) {
//            try {
//                // Criando a instância da classe alvo
//                T instance = clazz.getDeclaredConstructor().newInstance();
//
//                // Verificar e injetar dependências
//                for (Field field : clazz.getDeclaredFields()) {
//                    if (field.isAnnotationPresent(Inject.class)) {
//                        field.setAccessible(true);
//
//                        // Definindo o tipo de banco a ser utilizado
//                        int tipoBanco = getTipoBanco(); 
//
//                        // Criando a dependência baseada no tipo de banco e no tipo de campo
//                        Object dependency = createDependency(field.getType(), tipoBanco);
//                        field.set(instance, dependency);
//                    }
//                }
//
//                return instance;
//            } catch (Exception e) {
//                throw new RuntimeException("Erro ao injetar dependências para a classe: " + clazz.getName(), e);
//            }
//        }
//
//        // Método para criar a dependência dependendo do tipo de banco
//        private static Object createDependency(Class<?> type, DatabaseType tipoBanco) {
//            if (ProdutoRepository.class.isAssignableFrom(type)) {
//                // Usando a PersistenciaFactory para obter o repositório adequado
//                return PersistenciaFactory.getProdutoRepository(tipoBanco);
//            }
//            throw new IllegalArgumentException("Tipo de dependência não suportado: " + type.getName());
//        }
//
//        // Definindo logicamente de onde vem a configuração do tipo de banco
//        private static int getTipoBanco() {
//            String banco = System.getProperty("tipoBanco", "H2"); 
//            if ("H2".equalsIgnoreCase(banco)) {
//                return PersistenciaFactory.HSQL; // HSQLDB
//            } else {
//                return PersistenciaFactory.MEMORIA; // MEMÓRIA
//            }
//        }
//    }
}
