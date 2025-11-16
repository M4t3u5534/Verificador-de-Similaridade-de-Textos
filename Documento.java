import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Documento {
    private String nomeArquivo;
    private Path caminhoArquivo;
    private TabelaHash vocabulario;
    
    // palavras de parada (lista)
    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
        "a", "o", "e", "é", "de", "da", "do", "em", "um", "uma", "os", "as",
        "dos", "das", "para", "com", "por", "não", "que", "se", "na", "no",
        "ao", "aos", "à", "às", "pelo", "pela", "pelos", "pelas", "mais",
        "menos", "muito", "muita", "pouco", "pouca", "todo", "toda", "tudo",
        "ou", "mas", "como", "quando", "onde", "quem", "qual", "sobre",
        "até", "sem", "sob", "após", "antes", "durante", "entre", "contra",
        "foi", "ser", "estar", "ter", "há", "isso", "isto", "esse", "este",
        "aquele", "aquela", "sua", "seu", "meu", "minha", "nosso", "nossa"
    ));


    public Documento(Path caminho) throws IOException {
        this.caminhoArquivo = caminho;
        this.nomeArquivo = caminho.getFileName().toString();
        
        // le as linhas do arquivo
        List<String> linhas = Files.readAllLines(caminho);
        
        // junta as linhas em um texto unico
        String textoCompleto = String.join(" ", linhas);
        
        // normaliza o texto
        String textoNormalizado = normalizarTexto(textoCompleto);
        
        // tokeniza divide em palavras
        String[] palavras = textoNormalizado.split("\\s+");
        
        // cria a tabela hash com tamanho apropriado
        // multiplicamos por 2 para manter fator de carga baixo 
        int tamanhoTabela = Math.max(palavras.length * 2, 10); // minimo de 10 
        this.vocabulario = new TabelaHash(tamanhoTabela);
        
        // processa e insere as palavras na tabela hash
        processarPalavras(palavras);
    }

    // normaliza o texto converte para minusculas, remove pontuacao e espacos extras
    // retorna texto normalizado
    private String normalizarTexto(String texto) {
        // converte para minusculas
        texto = texto.toLowerCase();// texto a ser normalizado
        
        // retira tudo que nao estiver entre a-z e 0-9
        texto = texto.replaceAll("[^a-záàâãéèêíóôõúç0-9\\s]", ""); 
        
        // remove espacos extras (multiplos espacos viram um)
        texto = texto.trim().replaceAll("\\s+", " ");
        
        return texto;
    }

    // contabiliza a frequencia de cada palavra
    // palavras representa o array de palavras a serem processadas
    private void processarPalavras(String[] palavras) {
        for (String palavra : palavras) {
            // pula palavras vazias
            if (palavra.isEmpty()) {
                continue;
            }
            
            // pula palavras de parada
            if (STOP_WORDS.contains(palavra)) {
                continue;
            }
            
            // insere na tabela hash cria se nao existir
            vocabulario.inserir(palavra, 0); 
        }
    }

    // retorna nome do arquivo
    public String getNomeArquivo() {
        return nomeArquivo;
    }

    // retorna caminho do arquivo
    public Path getCaminhoArquivo() {
        return caminhoArquivo;
    }

    // retorna tabelahash mostrando palavra e frequencia
    public TabelaHash getVocabulario() {
        return vocabulario;
    }

    // retorna string com nome do arquivo
    @Override
    public String toString() {
        return "Documento: " + nomeArquivo;
    }
}