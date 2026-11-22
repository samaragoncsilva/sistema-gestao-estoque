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
            System.out.println("\nUsuários carregados: " + usuarios.size());

        } catch (NoSuchFileException e) {
            System.out.println("\nNenhum arquivo de usuários encontrado.");

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("\nErro ao carregar usuários: " + e.getMessage());
            return;
        }

        Scanner input = new Scanner(System.in);
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
                    }
                    break;
                }
            }
        } else {
            System.out.println("\nLogin ou senha incorretos, ou conta foi desativada. Tente novamente.");
        }
    }
}