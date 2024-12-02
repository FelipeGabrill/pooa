# Sistema de Roteamento Dinâmico com Injeção de Dependências e Padrões de Projeto

O sistema descrito visa implementar um roteamento dinâmico com o uso do padrão de projeto Command e injeção de dependências. Este sistema utiliza as anotações `@Rota`, `@CommandRota`, `@Inject`, `@Singleton`, `@Type` e o padrão **Factory Method** para gerenciamento de repositórios e controle de dependências.

A seguir, estão descritos os principais componentes e seu funcionamento:
# 1. Anotações Criadas e Seus Propósitos

### @Rota
```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Rota {
    String value(); // Define o caminho da rota
}
```
- **Objetivo**: Marca métodos que estão responsáveis por tratar requisições para uma rota específica.
- **Funcionamento**: Durante o escaneamento das classes de controle, métodos anotados com `@Rota("/caminho")` são mapeados para a rota correspondente.

### @CommandRota
```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandRota {
}
```
- **Objetivo**: Marca uma classe como responsável por comandos ou lógicas de uma rota específica.
- **Funcionamento**: Classes anotadas com `@CommandRota` são detectadas e processadas, registrando os métodos anotados com `@Rota` e associando-os às rotas correspondentes.


### @Inject
```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Inject {
}
```
- **Objetivo**: Marca campos que precisam ter suas dependências injetadas.
- **Funcionamento**: Através de reflexão, o sistema identifica campos anotados com `@Inject` e injeta a dependência correta, como repositórios ou outros serviços.

### @Singleton
```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Singleton {
}
```
- **Objetivo**: Marca classes que devem ser gerenciadas como Singleton.
- **Funcionamento**: Garante que uma única instância de classes anotadas com `@Singleton` seja criada e reutilizada durante toda a execução do sistema.

### @Type
```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Type {
    Class<?> value();
}
```
- **Objetivo**: Especifica qual implementação de uma interface ou classe abstrata deve ser injetada quando o campo está anotado com `@Inject` e o tipo é uma interface.
- **Funcionamento**: Através de reflexão, identifica a classe de implementação especificada pela anotação `@Type` e a injeta no campo correspondente.


## 2. Funcionamento do Sistema

### Parte 1: Roteamento com Command

#### 1. Servlet Unificado com Command
- Um servlet único é responsável por capturar todas as requisições e, com base no caminho (rota), despachar a requisição para o comando correspondente. Esse dispatcher utiliza o padrão Command, permitindo desacoplamento entre as rotas e os comandos.

#### 2. Mapeamento Dinâmico de Rotas
- O sistema usa reflexão para escanear as classes de controle em busca de métodos anotados com `@Rota`. Esses métodos são mapeados para suas respectivas rotas, permitindo que a execução de comandos seja dinâmica.

- **Exemplo**:
```java
@CommandRota
public class ProdutoAdicionarServlet {
    @Rota("/adicionarProduto")
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        // Lógica para exibir o produto
    }
}
```

## Parte 2: Injeção de Dependências

### 1. Repositórios em Memória e HSQLDB
- O sistema possui implementações de repositórios em memória (Singletons) e um repositório persistente utilizando HSQLDB. A troca entre essas implementações é gerenciada pela Factory Method.

### 2. Injeção de Dependências
- O sistema usa a anotação `@Inject` para marcar os campos onde as dependências devem ser injetadas. A classe `InjectDependencies` usa reflexão para identificar essas anotações e injetar as instâncias apropriadas, como repositórios ou outros serviços.

## Parte 3: Implementação de Singleton para Repositórios em Memória

### 1. Singleton para Repositórios em Memória
- As classes de repositório em memória são implementadas como Singletons, garantindo uma única instância para toda a execução do sistema.

### 2. Registry de Singleton
- A classe `SingletonRegistry` gerencia a criação e recuperação de instâncias Singleton. Quando uma classe anotada com `@Singleton` é solicitada, o registro retorna a mesma instância para garantir que não haja múltiplas instâncias da classe.

## 3. Descrição do Funcionamento do Sistema

### A) Anotações
As anotações desempenham papéis chave para tornar o sistema altamente flexível e desacoplado:
- **@Rota**: Marca métodos que serão executados quando uma rota correspondente for acessada.
- **@CommandRota**: Identifica as classes responsáveis por lidar com comandos e rotas no sistema.
- **@Inject**: Permite a injeção automática de dependências em campos.
- **@Singleton**: Garante que uma classe tenha uma única instância em toda a aplicação.
- **@Type**: Resolve a implementação correta de interfaces quando necessário.

### B) Reflexão para Carga Dinâmica de Rotas e Dependências
O sistema usa reflexão para detectar anotações e carregar as rotas e dependências de maneira dinâmica. Por exemplo, ao escanear as classes, ele identifica métodos anotados com `@Rota` e os associa automaticamente às rotas solicitadas. Da mesma forma, os campos anotados com `@Inject` têm suas dependências injetadas, e a injeção de classes `@Singleton` é gerida pelo `SingletonRegistry`.

### C) Factory Method e Troca de Repositórios
A troca entre repositórios é feita com o uso do Factory Method. O `RepositórioFactory` decide qual implementação de repositório será utilizada, seja em memória ou persistente (HSQLDB), dependendo da configuração do sistema. A implementação específica é determinada pela anotação @Type, que associa a interface ProdutoRepository a uma classe concreta (como ProdutoRepositoryEmMemoria ou ProdutoRepositoryHSQLDB), permitindo a seleção dinâmica da implementação adequada.

### D) Singleton para Repositórios em Memória
A implementação do padrão Singleton assegura que os repositórios em memória sejam criados uma única vez e reutilizados durante toda a execução do sistema. Isso é fundamental para garantir a consistência e a eficiência de acesso aos dados em memória.

## Resumo Final

Este sistema implementa um modelo de roteamento dinâmico e injeção de dependências utilizando anotações e reflexão. Ele é composto por:
- **Servlet Unificado**: Despacha requisições dinamicamente para comandos registrados.
- **Injeção de Dependências**: Usa reflexão para injetar automaticamente as instâncias corretas de repositórios e serviços.
- **Singletons**: Garantem que os repositórios em memória sejam instanciados uma única vez.
- **Factory Method**: Seleciona dinamicamente entre diferentes tipos de repositórios (em memória ou persistente).

Com esse modelo, o sistema se torna flexível, extensível e desacoplado, facilitando a manutenção e adição de novas funcionalidades de forma simples e eficiente.

