import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class MenuAdministracao {

    public static void exibir(Scanner sc, Funcionario logado) {
        NivelAcesso nivel = logado.getNivelAcesso();
        if (!podeConsultar(nivel)) {
            System.out.println("Acesso negado: seu nível não permite administrar funcionários.");
            return;
        }

        String opcao;
        do {
            System.out.println("\n====== ADMINISTRAÇÃO DE FUNCIONÁRIOS ======\n");
            System.out.println("1 - Cadastrar funcionário");
            System.out.println("2 - Listar funcionários");
            System.out.println("3 - Listar por nível de acesso");
            System.out.println("4 - Buscar por ID");
            System.out.println("5 - Editar funcionário");
            System.out.println("6 - Desativar funcionário");
            System.out.println("0 - Voltar");
            System.out.print("\nEscolha uma opção: ");
            opcao = sc.nextLine().trim();

            try {
                switch (opcao) {
                    case "1":
                        exigir(podeCadastrarEditar(nivel));
                        cadastrar(sc, nivel);
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
                        exigir(podeCadastrarEditar(nivel));
                        editar(sc, nivel);
                        Persistencia.salvarUsuarios();
                        break;
                    case "6":
                        exigir(podeDesativar(nivel));
                        Usuario.desativarUsuario(encontrar(sc));
                        Persistencia.salvarUsuarios();
                        System.out.println("Funcionário desativado.");
                        break;
                    case "0":
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Erro: Digite um número válido.");
            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Erro ao salvar os dados: " + e.getMessage());
            }
        } while (!opcao.equals("0"));
    }

    private static boolean podeConsultar(NivelAcesso nivel) {
        return nivel == NivelAcesso.ADM_PADRAO
                || nivel == NivelAcesso.ADM_AVANCADO
                || nivel == NivelAcesso.ADM_TOTAL;
    }

    private static boolean podeCadastrarEditar(NivelAcesso nivel) {
        return nivel == NivelAcesso.ADM_AVANCADO || nivel == NivelAcesso.ADM_TOTAL;
    }

    private static boolean podeDesativar(NivelAcesso nivel) {
        return nivel == NivelAcesso.ADM_TOTAL;
    }

    private static void exigir(boolean permitido) {
        if (!permitido) {
            throw new IllegalArgumentException("Você não tem permissão para esta operação.");
        }
    }

    private static void validarNivelAtribuido(NivelAcesso nivelLogado, NivelAcesso nivelEscolhido) {
        if (nivelEscolhido == NivelAcesso.ADM_TOTAL && nivelLogado != NivelAcesso.ADM_TOTAL) {
            throw new IllegalArgumentException("Apenas ADM_TOTAL pode atribuir o nível ADM_TOTAL.");
        }
    }

    private static void validarLoginUnico(String login, Funcionario atual) {
        if (login == null || login.trim().isEmpty()) {
            throw new IllegalArgumentException("O login não pode ser vazio.");
        }
        String loginLimpo = login.trim();
        for (Usuario u : Usuario.listarUsuarios()) {
            boolean mesmoUsuario = atual != null && u.getId().equals(atual.getId());
            if (!mesmoUsuario && u.getLogin() != null && u.getLogin().trim().equalsIgnoreCase(loginLimpo)) {
                throw new IllegalArgumentException("Já existe um usuário com esse login.");
            }
        }
    }

    private static void cadastrar(Scanner sc, NivelAcesso nivelLogado) {
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
        validarLoginUnico(login, null);
        System.out.print("Senha: ");
        String senha = sc.nextLine();
        NivelAcesso nivelNovo = lerNivel(sc, "Nível de acesso");
        validarNivelAtribuido(nivelLogado, nivelNovo);
        Funcionario.criarFuncionario(nome, cpf, email, telefone, login.trim(), senha, nivelNovo).verFuncionario();
    }

    private static void editar(Scanner sc, NivelAcesso nivelLogado) {
        Funcionario funcionario = encontrar(sc);
        if (funcionario.getNivelAcesso() == NivelAcesso.ADM_TOTAL && nivelLogado != NivelAcesso.ADM_TOTAL) {
            throw new IllegalArgumentException("Apenas ADM_TOTAL pode editar um usuário ADM_TOTAL.");
        }

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
        validarLoginUnico(login, funcionario);
        System.out.print("Nova senha: ");
        String senha = sc.nextLine();
        boolean ativo = funcionario.isAtivo();
        NivelAcesso nivelNovo = lerNivel(sc, "Novo nível de acesso");
        validarNivelAtribuido(nivelLogado, nivelNovo);
        Funcionario.editarFuncionario(funcionario, nome, cpf, email, telefone, login.trim(), senha, ativo, nivelNovo);
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
}