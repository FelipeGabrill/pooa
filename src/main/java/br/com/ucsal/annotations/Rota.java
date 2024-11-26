package br.com.ucsal.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // Apenas para métodos
@Retention(RetentionPolicy.RUNTIME) // Disponível em tempo de execução
public @interface Rota {
    String value(); // Path da rota, por exemplo, "/usuario"
}
