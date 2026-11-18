import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Usuario implements Serializable {
    private String id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String login;
    private String senha;
    private boolean ativo;

    public Usuario(String id, String nome, String cpf, String email, String telefone, String login, String senha, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.login = login;
        this.senha = senha;
        this.ativo = ativo;
    }

    public void verUsuario() {
        System.out.println("Id: " + this.id);
        System.out.println("Nome: " + this.nome);
        System.out.println("Cpf: " + this.cpf);
        System.out.println("Email: " + this.email);
        System.out.println("Telefone: " + this.telefone);
        System.out.println("Login: " + this.login);
        System.out.println("Senha: " + this.senha);
        System.out.println("Ativo: " + this.ativo);
    }

    public void alterarSenha(String novaSenha) {
        this.senha = novaSenha;
    }

    public static void editarUsuario(Usuario usuario, String nome, String cpf, String login, String senha, boolean ativo) {
        usuario.nome = nome;
        usuario.cpf = cpf;
        usuario.login = login;
        usuario.senha = senha;
        usuario.ativo = ativo;
    }

    public static void desativarUsuario(Usuario usuario) {
        usuario.ativo = false;
    }

    public static boolean autenticar(String login, String senha) {
        List<Usuario> listaDeUsuarios = listarUsuarios();

        for (Usuario u : listaDeUsuarios) {
            if (u.getLogin().equals(login) && u.getSenha().equals(senha) && u.isAtivo()) {
                return true;
            }
        }
        return false;
    }

    public static List<Usuario> listarUsuarios() {
        List<Usuario> listaDeUsuarios = new ArrayList<>();
        File arquivo = new File("usuario.dat");

        if (!arquivo.exists()) {
            return listaDeUsuarios;
        }

        try (ObjectInputStream leitor = new ObjectInputStream(new FileInputStream(arquivo))) {
            listaDeUsuarios = (List<Usuario>) leitor.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao ler o ficheiro: " + e.getMessage());
        }
        return listaDeUsuarios;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}