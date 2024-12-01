package br.com.ucsal.controller;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;  // Biblioteca para escanear pacotes

import br.com.ucsal.annotations.CommandRota;  // Importa a anotação ComandoRota
import br.com.ucsal.annotations.Rota;
import br.com.ucsal.persistencia.PersistenciaFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/view/*") // Mapeia todas as requisições com "/view/*"
public class ProdutoController extends HttpServlet {

    private Map<String, Command> commands = new HashMap<>();

    @Override
    public void init() {
        // Scaneia as classes no pacote para encontrar as que possuem @ComandoRota
        Reflections reflections = new Reflections("br.com.ucsal.controller");  // Substitua pelo seu pacote
        Set<Class<?>> commandClasses = reflections.getTypesAnnotatedWith(CommandRota.class);

        // Para cada classe anotada com @ComandoRota, registra as rotas
        for (Class<?> commandClass : commandClasses) {
            registerCommands(commandClass);
        }
    }

    private void registerCommands(Class<?> commandClass) {
        for (Method method : commandClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Rota.class)) {
                Rota rotaAnnotation = method.getAnnotation(Rota.class);
                String path = rotaAnnotation.value();

                try {
                    // Criação da instância do comando
                    Object commandInstance = commandClass.getDeclaredConstructor().newInstance();

                    // Injeção das dependências
                    PersistenciaFactory.injectDependencies(commandInstance);

                    // Mapeia a rota e registra o comando no mapa
                    mapsAndInject(path, (Command) commandInstance);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void mapsAndInject(String path, Command command) {
        // Mapeamento da rota e injeção de dependências
        PersistenciaFactory.injectDependencies(command);
        commands.put(path, command);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo();
        System.out.println("Rota chamada: " + path);

        Command command = commands.get(path);

        if (command != null) {
            command.execute(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Página não encontrada");
        }
    }
}
