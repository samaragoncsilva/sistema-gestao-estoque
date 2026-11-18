import java.io.Serializable;
import java.util.ArrayList;

public class Produto implements Serializable {
    private static final long serialVersionUID = 1L;

    private static ArrayList<Produto> produtos = new ArrayList<>();
    private static int contador = 0;

    private String id;
    private String nome;
    private String descricao;
    private double preco;
    private int quantidade;
    private int estoqueMinimo;

    private Produto(String nome, String descricao, double preco, int quantidade, int estoqueMinimo) {
        contador++;
        this.id = "P" + contador;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidade = quantidade;
        this.estoqueMinimo = estoqueMinimo;
    }

    public static Produto criarProduto(String nome, String descricao, double preco, int quantidade, int estoqueMinimo) {
        validar(nome, preco, estoqueMinimo);
        if (quantidade < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa.");
        }
        Produto produto = new Produto(nome, descricao, preco, quantidade, estoqueMinimo);
        produtos.add(produto);
        return produto;
    }

    public void editarProduto(String nome, String descricao, double preco, int estoqueMinimo) {
        validar(nome, preco, estoqueMinimo);
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.estoqueMinimo = estoqueMinimo;
    }

    public static boolean excluirProduto(String id) {
        Produto produto = buscarPorId(id);
        if (produto == null) {
            return false;
        }
        return produtos.remove(produto);
    }

    public static Produto buscarPorId(String id) {
        for (Produto produto : produtos) {
            if (produto.id.equalsIgnoreCase(id)) {
                return produto;
            }
        }
        return null;
    }

    public static ArrayList<Produto> buscarPorNome(String nome) {
        ArrayList<Produto> encontrados = new ArrayList<>();
        for (Produto produto : produtos) {
            if (produto.nome.toLowerCase().contains(nome.toLowerCase())) {
                encontrados.add(produto);
            }
        }
        return encontrados;
    }

    public static ArrayList<Produto> listarProdutos() {
        return new ArrayList<>(produtos);
    }

    public void adicionarEstoque(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
        this.quantidade += quantidade;
    }

    public void retirarEstoque(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
        if (quantidade > this.quantidade) {
            throw new IllegalArgumentException("Estoque insuficiente. Disponível: " + this.quantidade);
        }
        this.quantidade -= quantidade;
    }

    public boolean verificarEstoqueMinimo() {
        return quantidade <= estoqueMinimo;
    }

    public static ArrayList<Produto> listarEstoqueBaixo() {
        ArrayList<Produto> baixos = new ArrayList<>();
        for (Produto produto : produtos) {
            if (produto.verificarEstoqueMinimo()) {
                baixos.add(produto);
            }
        }
        return baixos;
    }

    public static String gerarRelatorio() {
        int totalItens = 0;
        double valorTotal = 0;
        for (Produto produto : produtos) {
            totalItens += produto.quantidade;
            valorTotal += produto.quantidade * produto.preco;
        }
        return "Produtos cadastrados: " + produtos.size()
                + "\nItens em estoque: " + totalItens
                + "\nValor total do estoque: R$ " + String.format("%.2f", valorTotal)
                + "\nProdutos com estoque baixo: " + listarEstoqueBaixo().size();
    }

    public static ArrayList<Produto> getProdutos() {
        return produtos;
    }

    public static void setProdutos(ArrayList<Produto> lista) {
        produtos = lista;
        contador = 0;
        for (Produto produto : lista) {
            int numero = Integer.parseInt(produto.id.substring(1));
            if (numero > contador) {
                contador = numero;
            }
        }
    }

    private static void validar(String nome, double preco, int estoqueMinimo) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome é obrigatório.");
        }
        if (preco < 0) {
            throw new IllegalArgumentException("O preço não pode ser negativo.");
        }
        if (estoqueMinimo < 0) {
            throw new IllegalArgumentException("O estoque mínimo não pode ser negativo.");
        }
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getPreco() {
        return preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public int getEstoqueMinimo() {
        return estoqueMinimo;
    }

    @Override
    public String toString() {
        return id + " | " + nome + " | R$ " + String.format("%.2f", preco)
                + " | Qtd: " + quantidade + " | Mín: " + estoqueMinimo
                + (verificarEstoqueMinimo() ? " | ESTOQUE BAIXO" : "");
    }
}
