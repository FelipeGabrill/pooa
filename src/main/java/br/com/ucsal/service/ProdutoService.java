package br.com.ucsal.service;

import java.util.List;

import br.com.ucsal.annotations.Inject;
import br.com.ucsal.model.Produto;
import br.com.ucsal.persistencia.PersistenciaFactory;
import br.com.ucsal.persistencia.ProdutoRepository;

public class ProdutoService {

	@Inject
	private ProdutoRepository<Produto, Integer> produtoRepository;

	public ProdutoService(int tipoPersistencia) {
        // Seleciona o repositório com base no tipo
        this.produtoRepository = (ProdutoRepository<Produto, Integer>) PersistenciaFactory.getProdutoRepository(tipoPersistencia);
    }

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
