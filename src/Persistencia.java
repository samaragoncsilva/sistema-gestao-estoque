import java.io.*;
import java.nio.file.*;

public class Persistencia {
    
    public static void salvar(Serializable dados, String caminho) throws IOException {
        Path arquivo = Path.of(caminho);
        Path pasta = arquivo.getParent();

        if (pasta != null) {
            Files.createDirectories(pasta);
        }

        try (ObjectOutputStream saida = new ObjectOutputStream(Files.newOutputStream(arquivo))) {
            saida.writeObject(dados);
        }
    }

    public static Object carregar(String caminho) throws IOException, ClassNotFoundException {
        try (ObjectInputStream entrada = new ObjectInputStream(Files.newInputStream(Path.of(caminho)))) {
            return entrada.readObject();
        }
    }
}