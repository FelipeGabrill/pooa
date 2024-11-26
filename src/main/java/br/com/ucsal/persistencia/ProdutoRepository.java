package br.com.ucsal.persistencia;

import java.util.List;

import br.com.ucsal.model.Produto;

public interface ProdutoRepository<T,I> {
	
    void adicionar(T entidade);
    
    void remover(I id);
    
    void atualizar(T entidade);
    
    List<T> listar();
    
    Produto obterPorID(I id);

}
