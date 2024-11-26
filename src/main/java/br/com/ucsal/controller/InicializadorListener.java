package br.com.ucsal.controller;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class InicializadorListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Carregue suas classes ou inicialize recursos aqui
        System.out.println("Inicializando recursos na inicialização da aplicação");
    }


}