package br.com.ucsal.controller;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import br.com.ucsal.annotations.Rota;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/view/*") // Mapeia todas as requisições com "/view/*"
public class ProdutoController extends HttpServlet {

    private final Map<String, Method> rotaMethods = new HashMap<>();
    private final Map<String, Object> controllers = new HashMap<>();

    @Override
    public void init() throws ServletException {
        try {
        	System.out.println("aaa");
            registerRoutes();
        } catch (Exception e) {
            throw new ServletException("Erro ao registrar rotas.", e);
        }
    }

    private void registerRoutes() throws Exception {
    	System.out.println("aaaaaaaaaaaa");

        Reflections reflections = new Reflections("br.com.ucsal.controller");

        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(WebServlet.class);

        for (Class<?> controllerClass : controllerClasses) {
            Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
            controllers.put(controllerClass.getName(), controllerInstance);

            for (Method method : controllerClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Rota.class)) {
                    String path = method.getAnnotation(Rota.class).value();
                    rotaMethods.put(path, method);
                }
            }
        }		
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
 	
    	String path = request.getPathInfo();
        Method method = rotaMethods.get(path);

        if (method != null) {
            try {
                Object controller = controllers.get(method.getDeclaringClass().getName());
                method.invoke(controller, request, response);
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao processar a requisição.");
                e.printStackTrace();
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Página não encontrada");
        }
    }
}
