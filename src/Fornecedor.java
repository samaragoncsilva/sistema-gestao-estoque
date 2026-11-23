import java.util.ArrayList;
import java.util.List;

public class Fornecedor extends Usuario {
    private static List<Fornecedor> fornecedores = new ArrayList<>();
    private static int contador = 0;

    private String razaoSocial;
    private String cnpj;

    private Fornecedor(String id, String razaoSocial, String cnpj, String telefone, String email) {
        super(id, razaoSocial, cnpj, email, telefone, cnpj, "123456", true);
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
        fornecedor.setNome(razaoSocial);
        fornecedor.cnpj = cnpj;
        fornecedor.setTelefone(telefone);
        fornecedor.setEmail(email);
    }

    public static void excluirFornecedor(Fornecedor fornecedor) {
        if (fornecedor!=null){
            fornecedores.remove(fornecedor);
        }
    }

    public static List<Fornecedor> listarFornecedores() {
        return fornecedores;
    }

    public static Fornecedor buscarFornecedoresPorCNPJ(String cnpj) {
        if (cnpj == null|| cnpj.isBlank()) {
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
