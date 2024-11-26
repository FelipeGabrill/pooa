<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="br.com.ucsal.model.Produto" %>
<!DOCTYPE html>
<html>
<head>
    <title>Formulário de Produto</title>
</head>
<body>
<h2>
        <c:choose>
            <c:when test="${not empty produto}">Editar</c:when>
            <c:otherwise>Criar</c:otherwise>
        </c:choose>
</h2>
<%
    Produto produto = (Produto) request.getAttribute("produto");
    String id = "";
    String nome = "";
    String preco = "";
    String action = "adicionarProduto";
    String titulo = "Adicionar Novo Produto";
    String botao = "Adicionar";

    if (produto != null) {
        id = produto.getId().toString();
        nome = produto.getNome();
        preco = String.valueOf(produto.getPreco());
        action = "editarProduto";
        titulo = "Editar Produto";
        botao = "Atualizar";
    }
%>
<h2><%= titulo %></h2>
<form action="<c:choose><c:when test='${not empty produto}'>editarProduto</c:when><c:otherwise>adicionarProduto</c:otherwise></c:choose>" method="post">
        <c:if test="${not empty produto}">
            <input type="hidden" name="id" value="${produto.id}">
        </c:if>
    Nome: <input type="text" name="nome" value="${produto.nome}"/><br/>
    Preço: <input type="text" name="preco" value="${produto.preco}"/><br/>
    <input type="submit" value="Salvar"/>
</form>
</body>
</html>
