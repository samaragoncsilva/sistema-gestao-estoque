import java.util.ArrayList;
import java.util.List;

public class Fornecedor extends Usuario {
    private String razaoSocial;
    private String cnpj;

    private Fornecedor(String id, String razaoSocial, String cnpj, String telefone, String email) {
        super(id, razaoSocial, cnpj, email, telefone, normalizarCnpj(cnpj), "123456", true);
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
    }

    public void verUsuario(){
        verFornecedor();
    }

    public void verFornecedor() {
        System.out.println("Id: " + getId());
        System.out.println("Razão social: " + razaoSocial);
        System.out.println("CNPJ: " + cnpj);
        System.out.println("Telefone: " + getTelefone());
        System.out.println("Email: " + getEmail());
        System.out.println("Ativo: " + (isAtivo() ? "Sim" : "Não"));
    }

    public static Fornecedor criarFornecedor(String razaoSocial, String cnpj, String telefone, String email) {
        validar(razaoSocial, cnpj);

        if(buscarFornecedoresPorCNPJ(cnpj) != null) {
            throw new IllegalArgumentException("Já existe um fornecedor com o mesmo CNPJ.");
        }
        String novoId = String.valueOf(Usuario.listarUsuarios().size() + 1);
        return new Fornecedor(novoId, razaoSocial, cnpj, telefone, email);
    }

    public static void editarFornecedor(Fornecedor fornecedor, String razaoSocial, String cnpj, String telefone, String email) {
        if (fornecedor == null) {
            throw new IllegalArgumentException("O fornecedor é obrigatório.");
        }
        validar(razaoSocial, cnpj);

        Fornecedor existente = buscarFornecedoresPorCNPJ(cnpj);
        if (existente != null && existente != fornecedor) {
            throw new IllegalArgumentException("Já existe um fornecedor com o mesmo CNPJ.");
        }

        fornecedor.razaoSocial = razaoSocial;
        fornecedor.setNome(razaoSocial);
        fornecedor.cnpj = cnpj;
        fornecedor.setCpf(cnpj);
        fornecedor.setLogin(normalizarCnpj(cnpj));
        fornecedor.setTelefone(telefone);
        fornecedor.setEmail(email);
    }

    public static void excluirFornecedor(Fornecedor fornecedor) {
        if (fornecedor!=null){
            Usuario.desativarUsuario(fornecedor);
        }
    }

    public static List<Fornecedor> listarFornecedores() {
        List<Fornecedor> fornecedores = new ArrayList<>();
        for (Usuario usuario : Usuario.listarUsuarios()) {
            if (usuario instanceof Fornecedor) {
                fornecedores.add((Fornecedor) usuario);
            }
        }
        return fornecedores;
    }

    public static Fornecedor buscarFornecedoresPorCNPJ(String cnpj) {
        if (cnpj == null|| cnpj.isBlank()) {
            return null;
        }

        String cnpjBusca = normalizarCnpj(cnpj);
        for (Fornecedor fornecedor : listarFornecedores()) {
            if (normalizarCnpj(fornecedor.cnpj).equals(cnpjBusca)) {
                return fornecedor;
            }
        }
        return null;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }


    private static void validar(String razaoSocial, String cnpj) {
        if (razaoSocial == null || razaoSocial.isBlank()) {
            throw new IllegalArgumentException("A razão social é obrigatória.");
        }
        if (cnpj == null || normalizarCnpj(cnpj).length() != 14) {
            throw new IllegalArgumentException("O CNPJ deve conter 14 dígitos.");
        }
    }

    private static String normalizarCnpj(String cnpj) {
        return cnpj.replaceAll("\\D", "");
    }
}
