import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Produto implements Serializable {
    private static List<Produto> produtos = new ArrayList<>();
    private static int contador = 0;

    private String id;
    private String nome;
    private String descricao;
    private double precoCompra;
    private double precoVenda;
    private int quantidade;
    private int estoqueMinimo;

    public void verProduto() {
        System.out.println(id + " | " + nome + " | " + descricao
                + " | Compra: R$ " + String.format("%.2f", precoCompra)
                + " | Venda: R$ " + String.format("%.2f", precoVenda)
                + " | Qtd: " + quantidade + " | Mín: " + estoqueMinimo
                + (verificarEstoqueMinimo() ? " | ESTOQUE BAIXO" : ""));
    }

    public static Produto criarProduto(String nome, String descricao, double precoCompra, double precoVenda, int quantidade, int estoqueMinimo) {
        validar(nome, precoCompra, precoVenda, estoqueMinimo);
        if (quantidade < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa.");
        }
        Produto produto = new Produto();
        contador++;
        produto.id = "P" + contador;
        produto.nome = nome;
        produto.descricao = descricao;
        produto.precoCompra = precoCompra;
        produto.precoVenda = precoVenda;
        produto.quantidade = quantidade;
        produto.estoqueMinimo = estoqueMinimo;
        produtos.add(produto);
        return produto;
    }

    public static void editarProduto(Produto produto, String nome, String descricao, double precoCompra, double precoVenda, int estoqueMinimo) {
        validar(nome, precoCompra, precoVenda, estoqueMinimo);
        produto.nome = nome;
        produto.descricao = descricao;
        produto.precoCompra = precoCompra;
        produto.precoVenda = precoVenda;
        produto.estoqueMinimo = estoqueMinimo;
    }

    public static void excluirProduto(Produto produto) {
        produtos.remove(produto);
    }

    public static List<Produto> listarProdutos() {
        return produtos;
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

    public static List<Produto> buscarProdutosPorNome(String nome) {
        List<Produto> encontrados = new ArrayList<>();
        for (Produto produto : produtos) {
            if (produto.nome.toLowerCase().contains(nome.toLowerCase())) {
                encontrados.add(produto);
            }
        }
        return encontrados;
    }

    public static Produto buscarProdutoPorID(String id) {
        for (Produto produto : produtos) {
            if (produto.id.equalsIgnoreCase(id)) {
                return produto;
            }
        }
        return null;
    }

    public static List<Produto> listarProdutosComEstoqueBaixo() {
        List<Produto> baixos = new ArrayList<>();
        for (Produto produto : produtos) {
            if (produto.verificarEstoqueMinimo()) {
                baixos.add(produto);
            }
        }
        return baixos;
    }

    public static List<Produto> gerarRelatorioEstoque() {
        List<Produto> relatorio = new ArrayList<>(produtos);
        relatorio.sort((a, b) -> a.quantidade - b.quantidade);
        return relatorio;
    }

    public static void setProdutos(List<Produto> lista) {
        produtos = lista;
        contador = 0;
        for (Produto produto : lista) {
            int numero = Integer.parseInt(produto.id.substring(1));
            if (numero > contador) {
                contador = numero;
            }
        }
    }

    private static void validar(String nome, double precoCompra, double precoVenda, int estoqueMinimo) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome é obrigatório.");
        }
        if (precoCompra < 0 || precoVenda < 0) {
            throw new IllegalArgumentException("Os preços não podem ser negativos.");
        }
        if (estoqueMinimo < 0) {
            throw new IllegalArgumentException("O estoque mínimo não pode ser negativo.");
        }
    }
}
