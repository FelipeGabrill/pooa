package br.com.ucsal.controller;

import java.io.IOException;
import java.util.List;

import br.com.ucsal.annotations.Rota;
import br.com.ucsal.model.Produto;
import br.com.ucsal.service.ProdutoService;
import br.com.ucsal.util.DependencyInjector.Injector;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class ProdutoListarServlet implements Command {
    private static final long serialVersionUID = 1L;
	private ProdutoService produtoService;

	public ProdutoListarServlet() {
	     this.produtoService = Injector.getInstance(ProdutoService.class);
	}
	

	@Rota("/listarProdutos")
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtém a lista de produtos
        List<Produto> produtos = produtoService.listarProdutos();
        
        // Define a lista de produtos como atributo da requisição
        request.setAttribute("produtos", produtos);
        
        // Encaminha para a página JSP que exibe a lista de produtos
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/produtolista.jsp");
        dispatcher.forward(request, response);
    }

}