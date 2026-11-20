import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario {
    private static List<Cliente> clientes = new ArrayList<>();
    private static int contador = 0;

    private String cpfCnpj;
    private boolean pessoaJuridica;

    private Cliente(String id, String nome, String cpfCnpj, String telefone, String email) {
        super(id, nome, cpfCnpj, email, telefone, null, null, true);
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

        contador++;
        Cliente cliente = new Cliente("C" + contador, nome, cpfCnpj, telefone, email);
        clientes.add(cliente);
        return cliente;
    }

    public static void editarCliente(Cliente cliente, String nome, String cpfCnpj, String telefone, String email) {
        if (cliente == null) {
            throw new IllegalArgumentException("O cliente é obrigatório.");
        }
        validar(nome, cpfCnpj);

        cliente.setNome(nome);
        cliente.setCpf(cpfCnpj);
        cliente.setTelefone(telefone);
        cliente.setEmail(email);
        cliente.cpfCnpj = cpfCnpj;
        cliente.pessoaJuridica = contarDigitos(cpfCnpj) > 11;
    }

    public static void excluirCliente(Cliente cliente) {
        clientes.remove(cliente);
    }

    public static List<Cliente> listarClientes() {
        return clientes;
    }

    public static Cliente buscarClientePorDocumento(String cpfCnpj) {
        if (cpfCnpj == null) {
            return null;
        }

        String documentoBusca = normalizarDocumento(cpfCnpj);
        for (Cliente cliente : clientes) {
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
