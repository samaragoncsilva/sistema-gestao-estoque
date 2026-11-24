import java.util.Scanner;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            Object dados = Persistencia.carregar("dados/produtos.dat");

            if (!(dados instanceof List<?>)) {
                System.out.println("\nErro: Arquivo de dados não contém uma lista válida de produtos.");
                return;
            }

            List<Produto> produtos = new ArrayList<>();

            for (Object objeto : (List<?>) dados) {
                if (!(objeto instanceof Produto)) {
                    System.out.println("\nErro: Arquivo de dados contém dados inválidos.");
                    return;
                }
                produtos.add((Produto) objeto);
            }

            Produto.setProdutos(produtos);
            System.out.println("\nProdutos carregados: " + produtos.size());
        
        } catch (NoSuchFileException e) {
            System.out.println("\nNenhum arquivo de produtos encontrado.");

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("\nErro ao carregar produtos: " + e.getMessage());
            return;
        }

        try {
            Object dados = Persistencia.carregar("dados/usuarios.dat");

            if (!(dados instanceof List<?>)) {
                System.out.println("\nErro: Arquivo de dados não contém uma lista válida de usuários.");
                return;
            }

            List<Usuario> usuarios = new ArrayList<>();

            for (Object objeto : (List<?>) dados) {
                if (!(objeto instanceof Usuario)) {
                    System.out.println("\nErro: Arquivo de dados contém dados inválidos.");
                    return;
                }
                usuarios.add((Usuario) objeto);
            }

            Usuario.setListaDeUsuarios(usuarios);
            System.out.println("Usuários carregados: " + usuarios.size());

        } catch (NoSuchFileException e) {
            System.out.println("Nenhum arquivo de usuários encontrado.");

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao carregar usuários: " + e.getMessage());
            return;
        }

        Scanner input = new Scanner(System.in);
        
        if (Usuario.listarUsuarios().isEmpty()) {
            if (!cadastrarAdministradorInicial(input)) {
                input.close();
                return;
            }
        }
        
        String option = "";

        do {
            System.out.println("\n====== DISTRIBUIDORA - SISTEMA DE GESTÃO DE ESTOQUE ======\n");
            System.out.println("1 - Fazer login");
            System.out.println("0 - Sair");
            System.out.print("\nEscolha uma opção: ");

            if (!input.hasNextLine()) {
                System.out.println("\nEntrada encerrada.");
                break;
            }
            option = input.nextLine().trim();

            switch (option) {
                case "1":
                    fazerLogin(input);
                    break;
                case "0":
                    System.out.println("\nEncerrando o sistema.");
                    break;
                default:
                    System.out.println("\nOpção inválida. Tente novamente.");
            }
        } while (!option.equals("0"));

        input.close();
    }

    private static void fazerLogin(Scanner input) {
        System.out.print("\nLogin: ");
        if (!input.hasNextLine()) {
            System.out.println("\nEntrada encerrada.");
            return;
        }

        String login = input.nextLine().trim();
        
        System.out.print("Senha: ");
        if (!input.hasNextLine()) {
            System.out.println("\nEntrada encerrada.");
            return;
        }

        String senha = input.nextLine().trim();

        if (Usuario.autenticar(login, senha)) {
            System.out.println("\nLogin realizado com sucesso.");

            for (Usuario usuario : Usuario.listarUsuarios()) {
                if (login.equals(usuario.getLogin()) && senha.equals(usuario.getSenha()) && usuario.isAtivo()) {
                    System.out.println("Usuário: " + usuario.getNome());

                    if (usuario instanceof Funcionario) {
                        Funcionario funcionario = (Funcionario) usuario;
                        System.out.println("Nível de acesso: " + funcionario.getNivelAcesso());
                        exibirMenuFuncionario(input, funcionario);
                    }
                    break;
                }
            }
        } else {
            System.out.println("\nLogin ou senha incorretos, ou conta foi desativada. Tente novamente.");
        }
    }

    private static void exibirMenuFuncionario(Scanner input, Funcionario funcionario) {
        String option;

        do {
            System.out.println("\n====== MENU DO FUNCIONÁRIO ======\n");
            System.out.println("1 - Administração de funcionários");
            System.out.println("2 - Produtos e estoque");
            System.out.println("3 - Clientes e fornecedores");
            System.out.println("0 - Encerrar sessão");
            System.out.print("\nEscolha uma opção: ");

            if (!input.hasNextLine()) {
                System.out.println("\nEntrada encerrada.");
                return;
            }

            option = input.nextLine().trim();

            switch (option) {
                case "1":
                    MenuAdministracao.exibir(input, funcionario);
                    break;
                case "2":
                    MenuProdutos.exibir(input, funcionario);
                    break;
                case "3":
                    MenuCadastros.exibir(input, funcionario);
                    break;
                case "0":
                    System.out.println("\nSessão encerrada.");
                    break;
                default:
                    System.out.println("\nOpção inválida. Tente novamente.");
            }
        } while (!option.equals("0"));
    }

    private static boolean cadastrarAdministradorInicial(Scanner input) {
        System.out.println("\n====== CONFIGURAÇÃO INICIAL ======\n");
        System.out.println("Cadastre o primeiro administrador do sistema.\n");

        String nome = lerCampo(input, "Nome");
        String cpf = lerCampo(input, "CPF");
        String email = lerCampo(input, "E-mail");
        String telefone = lerCampo(input, "Telefone");
        String login = lerCampo(input, "Login");
        String senha = lerCampo(input, "Senha");

        if (nome == null || cpf == null || email == null || telefone == null || login == null || senha == null) {
            System.out.println("\nCadastro interrompido.");
            return false;
        }

        try {
            Funcionario.criarFuncionario(nome, cpf, email, telefone, login, senha, NivelAcesso.ADM_TOTAL);
            Persistencia.salvarUsuarios();
            System.out.println("\nAdministrador inicial cadastrado com sucesso.");
            return true;
        } catch (IOException e) {
            System.out.println("\nErro ao salvar o administrador inicial: " + e.getMessage());
            return false;
        }
    }

    private static String lerCampo(Scanner input, String mensagem) {
        while (true) {
            System.out.print(mensagem + ": ");
            if (!input.hasNextLine()) {
                return null;
            }
            String valor = input.nextLine().trim();
            if (!valor.isEmpty()) {
                return valor;
            }
            System.out.println("\nCampo obrigatório.");
        }
    }
}