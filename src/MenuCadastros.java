import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class MenuCadastros {

    public static void exibir(Scanner sc, Funcionario logado) {
        if (logado == null || !podeConsultar(logado.getNivelAcesso())) {
            System.out.println("Acesso negado: seu nível não permite consultar cadastros.");
            return;
        }

        String opcao;
        do {
            System.out.println("\n====== CADASTRO DE CLIENTES E FORNECEDORES ======\n");
            System.out.println("1 - Cadastrar cliente");
            System.out.println("2 - Listar clientes");
            System.out.println("3 - Buscar cliente por documento");
            System.out.println("4 - Editar cliente");
            System.out.println("5 - Desativar cliente");
            System.out.println("6 - Cadastrar fornecedor");
            System.out.println("7 - Listar fornecedores");
            System.out.println("8 - Buscar fornecedor por CNPJ");
            System.out.println("9 - Editar fornecedor");
            System.out.println("10 - Desativar fornecedor");
            System.out.println("0 - Voltar");
            System.out.print("\nEscolha uma opção: ");
            opcao = sc.nextLine().trim();

            try {
                switch (opcao) {
                    case "1":
                        exigir(podeCadastrarEditar(logado.getNivelAcesso()));
                        cadastrarCliente(sc);
                        break;
                    case "2":
                        mostrarClientes(Cliente.listarClientes());
                        break;
                    case "3":
                        buscarCliente(sc).verCliente();
                        break;
                    case "4":
                        exigir(podeCadastrarEditar(logado.getNivelAcesso()));
                        editarCliente(sc);
                        break;
                    case "5":
                        exigir(podeExcluir(logado.getNivelAcesso()));
                        Cliente.excluirCliente(buscarCliente(sc));
                        salvarUsuarios();
                        System.out.println("Cliente desativado.");
                        break;
                    case "6":
                        exigir(podeCadastrarEditar(logado.getNivelAcesso()));
                        cadastrarFornecedor(sc);
                        break;
                    case "7":
                        mostrarFornecedores(Fornecedor.listarFornecedores());
                        break;
                    case "8":
                        buscarFornecedor(sc).verFornecedor();
                        break;
                    case "9":
                        exigir(podeCadastrarEditar(logado.getNivelAcesso()));
                        editarFornecedor(sc);
                        break;
                    case "10":
                        exigir(podeExcluir(logado.getNivelAcesso()));
                        Fornecedor.excluirFornecedor(buscarFornecedor(sc));
                        salvarUsuarios();
                        System.out.println("Fornecedor desativado.");
                        break;
                    case "0":
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        } while (!opcao.equals("0"));
    }

    private static boolean podeConsultar(NivelAcesso nivel) {
        return nivel == NivelAcesso.ADM_PADRAO
                || nivel == NivelAcesso.ADM_AVANCADO
                || nivel == NivelAcesso.ADM_TOTAL;
    }

    private static boolean podeCadastrarEditar(NivelAcesso nivel) {
        return nivel == NivelAcesso.ADM_PADRAO
                || nivel == NivelAcesso.ADM_AVANCADO
                || nivel == NivelAcesso.ADM_TOTAL;
    }

    private static boolean podeExcluir(NivelAcesso nivel) {
        return nivel == NivelAcesso.ADM_TOTAL;
    }

    private static void exigir(boolean permitido) {
        if (!permitido) {
            throw new IllegalArgumentException("Você não tem permissão para esta operação.");
        }
    }

    private static void cadastrarCliente(Scanner sc) {
        System.out.print("Nome: ");
        String nome = sc.nextLine().trim();
        System.out.print("CPF ou CNPJ: ");
        String documento = sc.nextLine().trim();
        System.out.print("Telefone: ");
        String telefone = sc.nextLine().trim();
        System.out.print("E-mail: ");
        String email = sc.nextLine().trim();

        Cliente cliente = Cliente.criarCliente(nome, documento, telefone, email);
        salvarUsuarios();
        cliente.verCliente();
        exibirCredenciais(cliente);
    }

    private static void editarCliente(Scanner sc) {
        Cliente cliente = buscarCliente(sc);
        System.out.print("Novo nome: ");
        String nome = sc.nextLine().trim();
        System.out.print("Novo CPF ou CNPJ: ");
        String documento = sc.nextLine().trim();
        System.out.print("Novo telefone: ");
        String telefone = sc.nextLine().trim();
        System.out.print("Novo e-mail: ");
        String email = sc.nextLine().trim();

        Cliente.editarCliente(cliente, nome, documento, telefone, email);
        salvarUsuarios();
        cliente.verCliente();
    }

    private static void cadastrarFornecedor(Scanner sc) {
        System.out.print("Razão social: ");
        String razaoSocial = sc.nextLine().trim();
        System.out.print("CNPJ: ");
        String cnpj = sc.nextLine().trim();
        System.out.print("Telefone: ");
        String telefone = sc.nextLine().trim();
        System.out.print("E-mail: ");
        String email = sc.nextLine().trim();

        Fornecedor fornecedor = Fornecedor.criarFornecedor(razaoSocial, cnpj, telefone, email);
        salvarUsuarios();
        fornecedor.verFornecedor();
        exibirCredenciais(fornecedor);
    }

    private static void editarFornecedor(Scanner sc) {
        Fornecedor fornecedor = buscarFornecedor(sc);
        System.out.print("Nova razão social: ");
        String razaoSocial = sc.nextLine().trim();
        System.out.print("Novo CNPJ: ");
        String cnpj = sc.nextLine().trim();
        System.out.print("Novo telefone: ");
        String telefone = sc.nextLine().trim();
        System.out.print("Novo e-mail: ");
        String email = sc.nextLine().trim();

        Fornecedor.editarFornecedor(fornecedor, razaoSocial, cnpj, telefone, email);
        salvarUsuarios();
        fornecedor.verFornecedor();
    }

    private static Cliente buscarCliente(Scanner sc) {
        System.out.print("CPF ou CNPJ do cliente: ");
        Cliente cliente = Cliente.buscarClientePorDocumento(sc.nextLine().trim());
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado.");
        }
        return cliente;
    }

    private static Fornecedor buscarFornecedor(Scanner sc) {
        System.out.print("CNPJ do fornecedor: ");
        Fornecedor fornecedor = Fornecedor.buscarFornecedoresPorCNPJ(sc.nextLine().trim());
        if (fornecedor == null) {
            throw new IllegalArgumentException("Fornecedor não encontrado.");
        }
        return fornecedor;
    }

    private static void mostrarClientes(List<Cliente> clientes) {
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente encontrado.");
            return;
        }
        for (Cliente cliente : clientes) {
            cliente.verCliente();
        }
    }

    private static void mostrarFornecedores(List<Fornecedor> fornecedores) {
        if (fornecedores.isEmpty()) {
            System.out.println("Nenhum fornecedor encontrado.");
            return;
        }
        for (Fornecedor fornecedor : fornecedores) {
            fornecedor.verFornecedor();
        }
    }

    private static void exibirCredenciais(Usuario usuario) {
        System.out.println("Credenciais iniciais - anote e entregue ao titular da conta:");
        System.out.println("Login: " + usuario.getLogin());
        System.out.println("Senha: " + usuario.getSenha());
    }

    private static void salvarUsuarios() {
        try {
            Persistencia.salvarUsuarios();
        } catch (IOException e) {
            System.out.println("Erro ao salvar os usuários: " + e.getMessage());
        }
    }
}