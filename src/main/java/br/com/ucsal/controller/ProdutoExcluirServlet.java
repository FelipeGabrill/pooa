package br.com.ucsal.controller;

import java.io.IOException;

import br.com.ucsal.annotations.CommandRota;
import br.com.ucsal.annotations.Inject;
import br.com.ucsal.annotations.Rota;
import br.com.ucsal.service.ProdutoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@CommandRota
public class ProdutoExcluirServlet implements Command {
	private static final long serialVersionUID = 1L;
	
	@Inject
	 private ProdutoService produtoService;

	@Override
	@Rota("/excluirProduto")
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Lógica de exclusão
		Integer id = Integer.parseInt(request.getParameter("id"));
		produtoService.removerProduto(id);
		response.sendRedirect("listarProdutos");
	}

}
