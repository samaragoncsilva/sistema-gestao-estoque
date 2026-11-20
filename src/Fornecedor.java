import java.util.ArrayList;
import java.util.List;

public class Fornecedor {
    private static List<Fornecedor> fornecedores = new ArrayList<>();
    private static int contador = 0;

    private String id;
    private String razaoSocial;
    private String cnpj;
    private String telefone;
    private String email;

    private Fornecedor(String id, String razaoSocial, String cnpj, String telefone, String email) {
        this.id = id;
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.telefone = telefone;
        this.email = email;
    }

    public void verFornecedor() {
        System.out.println("Id: " + id);
        System.out.println("Razão social: " + razaoSocial);
        System.out.println("CNPJ: " + cnpj);
        System.out.println("Telefone: " + telefone);
        System.out.println("Email: " + email);
    }

    public static Fornecedor criarFornecedor(String razaoSocial, String cnpj, String telefone, String email) {
        validar(razaoSocial, cnpj);

        contador++;
        Fornecedor fornecedor = new Fornecedor("F" + contador, razaoSocial, cnpj, telefone, email);
        fornecedores.add(fornecedor);
        return fornecedor;
    }

    public static void editarFornecedor(Fornecedor fornecedor, String razaoSocial, String cnpj, String telefone, String email) {
        if (fornecedor == null) {
            throw new IllegalArgumentException("O fornecedor é obrigatório.");
        }
        validar(razaoSocial, cnpj);

        fornecedor.razaoSocial = razaoSocial;
        fornecedor.cnpj = cnpj;
        fornecedor.telefone = telefone;
        fornecedor.email = email;
    }

    public static void excluirFornecedor(Fornecedor fornecedor) {
        fornecedores.remove(fornecedor);
    }

    public static List<Fornecedor> listarFornecedores() {
        return fornecedores;
    }

    public static Fornecedor buscarFornecedoresPorCNPJ(String cnpj) {
        if (cnpj == null) {
            return null;
        }

        String cnpjBusca = normalizarCnpj(cnpj);
        for (Fornecedor fornecedor : fornecedores) {
            if (normalizarCnpj(fornecedor.cnpj).equals(cnpjBusca)) {
                return fornecedor;
            }
        }
        return null;
    }

    public String getId() {
        return id;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
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
