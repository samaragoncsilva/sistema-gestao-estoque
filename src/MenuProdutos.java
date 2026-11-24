import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class MenuProdutos {

    public static void exibir(Scanner sc, Funcionario funcionario) {
        NivelAcesso nivel = funcionario.getNivelAcesso();
        String opcao;
        do {
            System.out.println("\n=== PRODUTOS E ESTOQUE ===");
            System.out.println("1 - Cadastrar produto");
            System.out.println("2 - Listar produtos");
            System.out.println("3 - Buscar por ID");
            System.out.println("4 - Buscar por nome");
            System.out.println("5 - Editar produto");
            System.out.println("6 - Excluir produto");
            System.out.println("7 - Entrada de estoque");
            System.out.println("8 - Saída de estoque");
            System.out.println("9 - Produtos com estoque baixo");
            System.out.println("10 - Relatório de estoque");
            System.out.println("0 - Voltar");
            System.out.print("Opção: ");
            opcao = sc.nextLine().trim();

            try {
                switch (opcao) {
                    case "1" -> {
                        exigir(podeCadastrarEditar(nivel));
                        cadastrar(sc);
                    }
                    case "2" -> {
                        exigir(podeConsultar(nivel));
                        mostrar(Produto.listarProdutos());
                    }
                    case "3" -> {
                        exigir(podeConsultar(nivel));
                        encontrar(sc).verProduto();
                    }
                    case "4" -> {
                        exigir(podeConsultar(nivel));
                        System.out.print("Nome: ");
                        mostrar(Produto.buscarProdutosPorNome(sc.nextLine()));
                    }
                    case "5" -> {
                        exigir(podeCadastrarEditar(nivel));
                        editar(sc);
                    }
                    case "6" -> {
                        exigir(nivel == NivelAcesso.ADM_TOTAL);
                        Produto.excluirProduto(encontrar(sc));
                        salvar();
                        System.out.println("Produto excluído.");
                    }
                    case "7" -> {
                        exigir(podeDarEntrada(nivel));
                        Produto produto = encontrar(sc);
                        produto.adicionarEstoque(lerInt(sc, "Quantidade de entrada: "));
                        salvar();
                        produto.verProduto();
                    }
                    case "8" -> {
                        exigir(podeDarSaida(nivel));
                        Produto produto = encontrar(sc);
                        produto.retirarEstoque(lerInt(sc, "Quantidade de saída: "));
                        salvar();
                        produto.verProduto();
                    }
                    case "9" -> {
                        exigir(podeConsultar(nivel));
                        mostrar(Produto.listarProdutosComEstoqueBaixo());
                    }
                    case "10" -> {
                        exigir(podeGerarRelatorio(nivel));
                        mostrar(Produto.gerarRelatorioEstoque());
                    }
                    case "0" -> { }
                    default -> System.out.println("Opção inválida.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        } while (!opcao.equals("0"));
    }

    private static boolean podeConsultar(NivelAcesso nivel) {
        return List.of(NivelAcesso.ADM_PADRAO, NivelAcesso.ADM_AVANCADO, NivelAcesso.ADM_TOTAL, NivelAcesso.GERENTE).contains(nivel);
    }

    private static boolean podeCadastrarEditar(NivelAcesso nivel) {
        return List.of(NivelAcesso.ADM_PADRAO, NivelAcesso.ADM_AVANCADO, NivelAcesso.ADM_TOTAL).contains(nivel);
    }

    private static boolean podeDarEntrada(NivelAcesso nivel) {
        return List.of(NivelAcesso.ADM_AVANCADO, NivelAcesso.ADM_TOTAL, NivelAcesso.ESTOQUISTA).contains(nivel);
    }

    private static boolean podeDarSaida(NivelAcesso nivel) {
        return List.of(NivelAcesso.ADM_AVANCADO, NivelAcesso.ADM_TOTAL, NivelAcesso.ESTOQUISTA, NivelAcesso.VENDEDOR).contains(nivel);
    }

    private static boolean podeGerarRelatorio(NivelAcesso nivel) {
        return List.of(NivelAcesso.ADM_AVANCADO, NivelAcesso.ADM_TOTAL, NivelAcesso.GERENTE).contains(nivel);
    }

    private static void exigir(boolean permitido) {
        if (!permitido) {
            throw new IllegalArgumentException("Você não tem permissão para esta operação.");
        }
    }

    private static void cadastrar(Scanner sc) {
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("Descrição: ");
        String descricao = sc.nextLine();
        double precoCompra = lerDouble(sc, "Preço de compra: ");
        double precoVenda = lerDouble(sc, "Preço de venda: ");
        int quantidade = lerInt(sc, "Quantidade inicial: ");
        int estoqueMinimo = lerInt(sc, "Estoque mínimo: ");
        Produto produto = Produto.criarProduto(nome, descricao, precoCompra, precoVenda, quantidade, estoqueMinimo);
        salvar();
        produto.verProduto();
    }

    private static void editar(Scanner sc) {
        Produto produto = encontrar(sc);
        System.out.print("Novo nome: ");
        String nome = sc.nextLine();
        System.out.print("Nova descrição: ");
        String descricao = sc.nextLine();
        double precoCompra = lerDouble(sc, "Novo preço de compra: ");
        double precoVenda = lerDouble(sc, "Novo preço de venda: ");
        int estoqueMinimo = lerInt(sc, "Novo estoque mínimo: ");
        Produto.editarProduto(produto, nome, descricao, precoCompra, precoVenda, estoqueMinimo);
        salvar();
        produto.verProduto();
    }

    private static void salvar() {
        try {
            Persistencia.salvarProdutos();
        } catch (IOException e) {
            System.out.println("Erro ao salvar produtos: " + e.getMessage());
        }
    }

    private static Produto encontrar(Scanner sc) {
        System.out.print("ID do produto: ");
        Produto produto = Produto.buscarProdutoPorID(sc.nextLine().trim());
        if (produto == null) {
            throw new IllegalArgumentException("Produto não encontrado.");
        }
        return produto;
    }

    private static void mostrar(List<Produto> lista) {
        if (lista.isEmpty()) {
            System.out.println("Nenhum produto encontrado.");
        }
        for (Produto produto : lista) {
            produto.verProduto();
        }
    }

    private static int lerInt(Scanner sc, String mensagem) {
        System.out.print(mensagem);
        return Integer.parseInt(sc.nextLine().trim());
    }

    private static double lerDouble(Scanner sc, String mensagem) {
        System.out.print(mensagem);
        return Double.parseDouble(sc.nextLine().trim().replace(",", "."));
    }
}
