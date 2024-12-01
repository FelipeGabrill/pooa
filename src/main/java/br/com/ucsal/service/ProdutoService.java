package br.com.ucsal.service;

import java.util.List;

import br.com.ucsal.annotations.Inject;
import br.com.ucsal.annotations.Type;
import br.com.ucsal.model.Produto;
import br.com.ucsal.persistencia.HSQLProdutoRepository;
import br.com.ucsal.persistencia.ProdutoRepository;

public class ProdutoService {

	@Inject
	@Type(HSQLProdutoRepository.class)
	private ProdutoRepository<Produto, Integer> produtoRepository;

	public ProdutoService() {}
		
	public void adicionarProduto(String nome, double preco) {
		Produto produto = new Produto(null, nome, preco);
		produtoRepository.adicionar(produto);
	}

	public void removerProduto(Integer id) {
		produtoRepository.remover(id);
	}

	public Produto obterProdutoPorId(Integer id) {
		return produtoRepository.obterPorID(id);
	}

	public void atualizarProduto(Produto p) {
		produtoRepository.atualizar(p);
	}

	public List<Produto> listarProdutos() {
		return produtoRepository.listar();
	}
}
