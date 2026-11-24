import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario {
    private String cpfCnpj;
    private boolean pessoaJuridica;

    private Cliente(String id, String nome, String cpfCnpj, String telefone, String email) {
        super(id, nome, cpfCnpj, email, telefone, normalizarDocumento(cpfCnpj), "123456", true);
        this.cpfCnpj = cpfCnpj;
        this.pessoaJuridica = contarDigitos(cpfCnpj) > 11;
    }

    @Override
    public void verUsuario() {
        verCliente();
    }

    public void verCliente() {
        System.out.println("Id: " + getId());
        System.out.println("Nome: " + getNome());
        System.out.println("Cpf/Cnpj: " + cpfCnpj);
        System.out.println("Telefone: " + getTelefone());
        System.out.println("Email: " + getEmail());
        System.out.println("Pessoa jurídica: " + pessoaJuridica);
    }

    public static Cliente criarCliente(String nome, String cpfCnpj, String telefone, String email) {
        validar(nome, cpfCnpj);

        if (buscarClientePorDocumento(cpfCnpj) != null) {
            throw new IllegalArgumentException("Já existe um cliente com o mesmo documento.");
        }
        String novoId = String.valueOf(Usuario.listarUsuarios().size() + 1);
        return new Cliente(novoId, nome, cpfCnpj, telefone, email);
    }

    public static void editarCliente(Cliente cliente, String nome, String cpfCnpj, String telefone, String email) {
        if (cliente == null) {
            throw new IllegalArgumentException("O cliente é obrigatório.");
        }
        validar(nome, cpfCnpj);

        Cliente existente = buscarClientePorDocumento(cpfCnpj);
        if (existente != null && existente != cliente) {
            throw new IllegalArgumentException("Já existe um cliente com o mesmo documento.");
        }

        cliente.setCpfCnpj(cpfCnpj);
        cliente.setNome(nome);
        cliente.setTelefone(telefone);
        cliente.setEmail(email);
    }

    public static void excluirCliente(Cliente cliente) {
        if(cliente!=null){
            Usuario.desativarUsuario(cliente);;
        }
    }

    public static List<Cliente> listarClientes() {
        List<Cliente> clientes = new ArrayList<>();
        for(Usuario usuario : Usuario.listarUsuarios()){
            if(usuario instanceof Cliente){
                clientes.add((Cliente) usuario);
            }
        }
        return clientes;
    }

    public static Cliente buscarClientePorDocumento(String cpfCnpj) {
        if (cpfCnpj == null) {
            return null;
        }

        String documentoBusca = normalizarDocumento(cpfCnpj);
        for (Cliente cliente : listarClientes()) {
            if (normalizarDocumento(cliente.cpfCnpj).equals(documentoBusca)) {
                return cliente;
            }
        }
        return null;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        validarDocumento(cpfCnpj);
        setLogin(normalizarDocumento(cpfCnpj));
        this.cpfCnpj = cpfCnpj;
        setCpf(cpfCnpj);
        this.pessoaJuridica = contarDigitos(cpfCnpj) > 11;
    }

    public boolean isPessoaJuridica() {
        return pessoaJuridica;
    }

    private static void validar(String nome, String cpfCnpj) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome é obrigatório.");
        }
        validarDocumento(cpfCnpj);
    }

    private static void validarDocumento(String cpfCnpj) {
        int quantidadeDigitos = contarDigitos(cpfCnpj);
        if (cpfCnpj == null || (quantidadeDigitos != 11 && quantidadeDigitos != 14)) {
            throw new IllegalArgumentException("O documento deve conter 11 dígitos (CPF) ou 14 dígitos (CNPJ).");
        }
    }

    private static int contarDigitos(String documento) {
        if (documento == null) {
            return 0;
        }
        return documento.replaceAll("\\D", "").length();
    }

    private static String normalizarDocumento(String documento) {
        return documento.replaceAll("\\D", "");
    }
}