import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ProcessaArquivos {

    public boolean analisaArquivo(Path arquivo) {
        try {
            List<String> linhas = Files.readAllLines(arquivo);
            int cont = 0;
            for (String linha : linhas) {
                String[] palavras = linha.split("\\s+");
                cont += palavras.length;
            }
            TabelaHash hash = new TabelaHash(cont);
            for (String linha : linhas) {
                String[] palavras = linha.split("\\s+");
                for (String palavra : palavras) {
                    hash.inserir(palavra, 0);
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo " + arquivo.getFileName() + ": " + e.getMessage());
            return false;
        }
    }
}
