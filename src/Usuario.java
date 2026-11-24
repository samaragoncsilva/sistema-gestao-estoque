import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Usuario implements Serializable {

    private static List<Usuario> listaDeUsuarios = new ArrayList<>();

    private String id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String login;
    private String senha;
    private boolean ativo;

    public Usuario(String id, String nome, String cpf, String email, String telefone, String login, String senha, boolean ativo) {
        validarLoginUnico(login, null);

        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.login = login.trim();
        this.senha = senha;
        this.ativo = ativo;

        listaDeUsuarios.add(this);
    }

    protected static void validarLoginUnico(String login, String idIgnorar) {
        if (login == null || login.trim().isEmpty()) {
            throw new IllegalArgumentException("O login não pode ser vazio.");
        }
        String loginLimpo = login.trim();
        for (Usuario u : listaDeUsuarios) {
            boolean mesmoUsuario = idIgnorar != null && u.getId().equals(idIgnorar);
            if (!mesmoUsuario && u.getLogin() != null && u.getLogin().trim().equalsIgnoreCase(loginLimpo)) {
                throw new IllegalArgumentException("Já existe um usuário com esse login.");
            }
        }
    }

    public void verUsuario() {
        System.out.println("Id: " + this.id);
        System.out.println("Nome: " + this.nome);
        System.out.println("Cpf: " + this.cpf);
        System.out.println("Email: " + this.email);
        System.out.println("Telefone: " + this.telefone);
        System.out.println("Login: " + this.login);
        System.out.println("Ativo: " + this.ativo);
    }

    public void alterarSenha(String novaSenha) {
        this.senha = novaSenha;
    }

    public static void editarUsuario(Usuario usuario, String nome, String cpf, String login, String senha, boolean ativo) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário inválido.");
        }
        validarLoginUnico(login, usuario.getId());

        usuario.nome = nome;
        usuario.cpf = cpf;
        usuario.login = login.trim();
        usuario.senha = senha;
        usuario.ativo = ativo;
    }

    public static void desativarUsuario(Usuario usuario) {
        usuario.ativo = false;
    }

    public static boolean autenticar(String login, String senha) {
        for (Usuario u : listaDeUsuarios) {
            if (login.equals(u.getLogin())
                    && senha.equals(u.getSenha())
                    && u.isAtivo()) {
                return true;
            }
        }
        return false;
    }

    public static List<Usuario> listarUsuarios() {
        return listaDeUsuarios;
    }

    public static void setListaDeUsuarios(List<Usuario> novaLista) {
        listaDeUsuarios = novaLista;
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
        validarLoginUnico(login, this.id);
        this.login = login.trim();
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