package br.com.ucsal.controller;

import java.io.IOException;

import br.com.ucsal.annotations.Rota;
import br.com.ucsal.model.Produto;
import br.com.ucsal.service.ProdutoService;
import br.com.ucsal.util.DependencyInjector.Injector;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ProdutoEditarServlet {
    private static final long serialVersionUID = 1L;

    private ProdutoService produtoService;

    public ProdutoEditarServlet() {
        this.produtoService = Injector.getInstance(ProdutoService.class);
    }

    @Rota("/editarProduto")
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getMethod();
        
        if ("GET".equalsIgnoreCase(method)) {
            doGet(request, response);
        } else if ("POST".equalsIgnoreCase(method)) {
            doPost(request, response);
        }
    }

    private void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer id = Integer.parseInt(request.getParameter("id"));
        Produto produto = produtoService.obterProdutoPorId(id);
        request.setAttribute("produto", produto);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/produtoformulario.jsp");
        dispatcher.forward(request, response);
    }

    private void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer id = Integer.parseInt(request.getParameter("id"));
        String nome = request.getParameter("nome");
        double preco = Double.parseDouble(request.getParameter("preco"));
        Produto produto = new Produto(id, nome, preco);
        produtoService.atualizarProduto(produto);
        response.sendRedirect("listarProdutos");
    }

}

