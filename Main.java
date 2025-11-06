import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Argumentos faltantes: " + (3 - args.length));
            System.err.println("Uso: java Main <diretorio_documentos> <limiar> <modo> [argumentos_opcionais]");
            return;
        }
        if (args[2] == "busca" && args.length < 5) {
            System.err.println("Documentos de busca faltantes!");
            System.err.println("Uso: java Main <diretorio_documentos> <limiar> <busca> [documento_1] [documento_2]");
            return;
        }

        String stringCaminho = args[0];
        Path caminhoPasta = Paths.get(stringCaminho);

        if (!Files.exists(caminhoPasta)) {
            System.err.println("Erro: O caminho '" + caminhoPasta + "' não existe.");
            return;
        }
        if (!Files.isDirectory(caminhoPasta)) {
            System.err.println("Erro: O caminho '" + caminhoPasta + "' não é um diretório.");
            return;
        }

        ProcessaArquivos processaArquivos= new ProcessaArquivos();

        System.out.println("Analisando documentos em: " + caminhoPasta.toAbsolutePath());
        System.out.println("----------------------------------------\n");

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(caminhoPasta)) {
            for (Path arquivo : stream) {
                //(ignorando subpastas)
                if (Files.isRegularFile(arquivo)) {
                    if (!processaArquivos.analisaArquivo(arquivo)) return;
                }
            }
        } catch (IOException e) {
            // Lidar com possíveis erros de I/O (Ex: problemas de permissão)
            System.err.println("Erro ao tentar acessar o diretório: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
