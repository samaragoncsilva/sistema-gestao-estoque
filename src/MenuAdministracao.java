import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class MenuAdministracao {

    public static void exibir(Scanner sc) {
        String opcao;
        do {
            System.out.println("\n=== ADMINISTRAÇÃO DE FUNCIONÁRIOS ===");
            System.out.println("1 - Cadastrar funcionário");
            System.out.println("2 - Listar funcionários");
            System.out.println("3 - Listar por nível de acesso");
            System.out.println("4 - Buscar por ID");
            System.out.println("5 - Editar funcionário");
            System.out.println("6 - Desativar funcionário");
            System.out.println("0 - Voltar");
            System.out.print("Opção: ");
            opcao = sc.nextLine().trim();

            try {
                switch (opcao) {
                    case "1":
                        cadastrar(sc);
                        Persistencia.salvarUsuarios();
                        break;
                    case "2":
                        mostrar(Funcionario.listarFuncionarios());
                        break;
                    case "3":
                        mostrar(Funcionario.listarFuncionariosPorNivel(lerNivel(sc, "Nível de acesso")));
                        break;
                    case "4":
                        encontrar(sc).verFuncionario();
                        break;
                    case "5":
                        editar(sc);
                        Persistencia.salvarUsuarios();
                        break;
                    case "6":
                        Usuario.desativarUsuario(encontrar(sc));
                        Persistencia.salvarUsuarios();
                        System.out.println("Funcionário desativado.");
                        break;
                    case "0":
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Erro ao salvar os dados: " + e.getMessage());
            }
        } while (!opcao.equals("0"));
    }

    private static void cadastrar(Scanner sc) {
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("CPF: ");
        String cpf = sc.nextLine();
        System.out.print("E-mail: ");
        String email = sc.nextLine();
        System.out.print("Telefone: ");
        String telefone = sc.nextLine();
        System.out.print("Login: ");
        String login = sc.nextLine();
        System.out.print("Senha: ");
        String senha = sc.nextLine();
        NivelAcesso nivel = lerNivel(sc, "Nível de acesso");
        Funcionario.criarFuncionario(nome, cpf, email, telefone, login, senha, nivel).verFuncionario();
    }

    private static void editar(Scanner sc) {
        Funcionario funcionario = encontrar(sc);
        System.out.print("Novo nome: ");
        String nome = sc.nextLine();
        System.out.print("Novo CPF: ");
        String cpf = sc.nextLine();
        System.out.print("Novo e-mail: ");
        String email = sc.nextLine();
        System.out.print("Novo telefone: ");
        String telefone = sc.nextLine();
        System.out.print("Novo login: ");
        String login = sc.nextLine();
        System.out.print("Nova senha: ");
        String senha = sc.nextLine();
        boolean ativo = lerBoolean(sc, "Ativo (s/n): ");
        NivelAcesso nivel = lerNivel(sc, "Novo nível de acesso");
        Funcionario.editarFuncionario(funcionario, nome, cpf, email, telefone, login, senha, ativo, nivel);
        funcionario.verFuncionario();
    }

    private static Funcionario encontrar(Scanner sc) {
        System.out.print("ID do funcionário: ");
        String id = sc.nextLine().trim();
        for (Funcionario funcionario : Funcionario.listarFuncionarios()) {
            if (funcionario.getId().equals(id)) {
                return funcionario;
            }
        }
        throw new IllegalArgumentException("Funcionário não encontrado.");
    }

    private static void mostrar(List<Funcionario> lista) {
        if (lista.isEmpty()) {
            System.out.println("Nenhum funcionário encontrado.");
        }
        for (Funcionario funcionario : lista) {
            funcionario.verFuncionario();
        }
    }

    private static NivelAcesso lerNivel(Scanner sc, String mensagem) {
        NivelAcesso[] niveis = NivelAcesso.values();
        System.out.println(mensagem + ":");
        for (int i = 0; i < niveis.length; i++) {
            System.out.println("  " + (i + 1) + " - " + niveis[i]);
        }
        System.out.print("Escolha: ");
        int indice = Integer.parseInt(sc.nextLine().trim());
        if (indice < 1 || indice > niveis.length) {
            throw new IllegalArgumentException("Nível inválido.");
        }
        return niveis[indice - 1];
    }

    private static boolean lerBoolean(Scanner sc, String mensagem) {
        System.out.print(mensagem);
        return sc.nextLine().trim().equalsIgnoreCase("s");
    }
}