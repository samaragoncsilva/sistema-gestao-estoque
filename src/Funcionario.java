import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Funcionario extends Usuario {
    private NivelAcesso nivelAcesso;

    public Funcionario(String id, String nome, String cpf, String email, String telefone, String login, String senha, boolean ativo, NivelAcesso nivelAcesso) {
        super(id, nome, cpf, email, telefone, login, senha, ativo);
        this.nivelAcesso = nivelAcesso;
    }

    public void verFuncionario() {
        super.verUsuario();
        System.out.println("Nível de Acesso: " + this.nivelAcesso);
    }

    public static Funcionario criarFuncionario(String nome, String cpf, String email, String telefone, String login, String senha, NivelAcesso nivelAcesso) {
        List<Usuario> listaUsuarios = listarUsuarios();
        String novoId = String.valueOf(listaUsuarios.size() + 1);

        Funcionario novoFuncionario = new Funcionario(novoId, nome, cpf, email, telefone, login, senha, true, nivelAcesso);
        listaUsuarios.add(novoFuncionario);

        try (ObjectOutputStream empacotador = new ObjectOutputStream(new FileOutputStream("usuario.dat"))) {
            empacotador.writeObject(listaUsuarios);
        } catch (IOException e) {
            System.out.println("Erro ao salvar funcionário: " + e.getMessage());
        }

        return novoFuncionario;
    }

    public static void editarFuncionario(Funcionario funcionario, String nome, String cpf, String email, String telefone, String login, String senha, boolean ativo, NivelAcesso nivelAcesso) {
        List<Usuario> listaUsuarios = listarUsuarios();

        for (Usuario u : listaUsuarios) {
            if (u.getId().equals(funcionario.getId()) && u instanceof Funcionario) {
                Funcionario f = (Funcionario) u;

                Usuario.editarUsuario(f, nome, cpf, login, senha, ativo);
                f.setEmail(email);
                f.setTelefone(telefone);
                f.setNivelAcesso(nivelAcesso);
                break;
            }
        }

        Usuario.editarUsuario(funcionario, nome, cpf, login, senha, ativo);
        funcionario.setEmail(email);
        funcionario.setTelefone(telefone);
        funcionario.setNivelAcesso(nivelAcesso);

        try (ObjectOutputStream empacotador = new ObjectOutputStream(new FileOutputStream("usuario.dat"))) {
            empacotador.writeObject(listaUsuarios);
        } catch (IOException e) {
            System.out.println("Erro ao salvar edição do funcionário: " + e.getMessage());
        }
    }

    public static List<Funcionario> listarFuncionarios() {
        List<Usuario> listaUsuarios = listarUsuarios();
        List<Funcionario> listaFuncionarios = new ArrayList<>();

        for (Usuario u : listaUsuarios) {
            if (u instanceof Funcionario) {
                listaFuncionarios.add((Funcionario) u);
            }
        }

        return listaFuncionarios;
    }

    public static List<Funcionario> listarFuncionariosPorNivel(NivelAcesso nivelAcesso) {
        List<Funcionario> todosFuncionarios = listarFuncionarios();
        List<Funcionario> filtrados = new ArrayList<>();

        for (Funcionario f : todosFuncionarios) {
            if (f.getNivelAcesso() == nivelAcesso) {
                filtrados.add(f);
            }
        }

        return filtrados;
    }

    public NivelAcesso getNivelAcesso() {
        return nivelAcesso;
    }

    public void setNivelAcesso(NivelAcesso nivelAcesso) {
        this.nivelAcesso = nivelAcesso;
    }
}