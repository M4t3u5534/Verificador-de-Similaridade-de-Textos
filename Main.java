import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Argumentos faltantes: " + (3 - args.length));
            System.err.println("Uso: java Main <diretorio_documentos> <limiar> <modo> [argumentos_opcionais]");
            return;
        }
        // principais inicializações
        String diretorio = args[0];
        double limiar = Double.parseDouble(args[1]);
        String modo = args[2].toLowerCase();
        int topK = -1;
        List<String> buscaArquivos = new ArrayList<>();

        // SETUP
        // topk
        if (modo.equals("topk")) {
            if (args.length < 4) {
                System.err.println("Modo topK requer argumento K: java Main <diretorio> <limiar> topK <K>");
                return;
            }
            topK = Integer.parseInt(args[3]);

        // busca
        } else if (modo.equals("busca")) {
            if (args.length < 5) {
                System.err.println("Modo busca requer dois arquivos: java Main <diretorio> <limiar> busca <arquivo1> <arquivo2>");
                return;
            }
            
            buscaArquivos.add(args[3]); 
            buscaArquivos.add(args[4]);
        }


        // verificações de diretório
        Path dirPath = Paths.get(diretorio);
        if (!Files.exists(dirPath) || !Files.isDirectory(dirPath)) {
            System.err.println("Diretório " + diretorio + " não encontrado.");
            return;
        }

        // OPERAÇÕES
        try {

            // inicializações
            AVL avl = new AVL();
            int totalComparados = 0;


            // carrega documentos
            List<Documento> documentos = new ArrayList<>();
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath)) {
                for (Path arquivo : stream) {
                    if (Files.isRegularFile(arquivo)) {
                        documentos.add(new Documento(arquivo));
                    }
                }
            }

            // processamento de todos os pares
            for (int i = 0; i < documentos.size(); i++) {
                for (int j = i + 1; j < documentos.size(); j++) {
                    Documento d1 = documentos.get(i);
                    Documento d2 = documentos.get(j);

                    // filtro do modo busca para apenas os selecionados
                    if (modo.equals("busca")) {
                        if (!((d1.getNomeArquivo().equals(buscaArquivos.get(0)) && d2.getNomeArquivo().equals(buscaArquivos.get(1))) ||
                              (d1.getNomeArquivo().equals(buscaArquivos.get(1)) && d2.getNomeArquivo().equals(buscaArquivos.get(0))))) {
                            continue;
                        }
                    }

                    double sim = ComparadorDeDocumentos.calcularSimilaridadeCosseno(d1, d2);
                    if (sim >= limiar) {
                        avl.insert(new Resultado(d1.getNomeArquivo(), d2.getNomeArquivo(), sim));
                        totalComparados++;
                    }
                }
            }

            // Coletar resultados em ordem decrescente
            List<Resultado> resultados = new ArrayList<>();
            coletarResultadosEmOrdem(avl.raiz, resultados);
            resultados.sort((r1, r2) -> Double.compare(r2.getSimilaridade(), r1.getSimilaridade()));

            if (modo.equals("topk") && topK > 0 && resultados.size() > topK) {
                resultados = resultados.subList(0, topK);
            }

            // Gerar arquivo resultado.txt em UTF-8 e imprimir no console
            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream("resultado.txt"), "UTF-8"))) {
                writer.write("=== VERIFICADOR DE SIMILARIDADE DE TEXTOS ===\n");
                writer.write("Total de documentos processados: " + documentos.size() + "\n");
                writer.write("Total de pares comparados: " + totalComparados + "\n");
                writer.write("Função hash utilizada: hashMultiplicativo\n");
                writer.write("Métrica de similaridade: Cosseno\n");
                writer.write("Pares com similaridade >= " + limiar + ":\n");
                writer.write("---------------------------------\n");

                for (Resultado r : resultados) {
                    writer.write(r.toString() + "\n");
                    System.out.println(r);
                }
            }

            System.out.println("\nResultados salvos em resultado.txt");
            System.out.println("Rotações simples: " + avl.rotacoes_simples);
            System.out.println("Rotações duplas: " + avl.rotacoes_duplas);
            
            avl.imprimir();
            
            analisaDesempenho(documentos);
            
        } catch (IOException e) {
            System.err.println("Erro ao processar arquivos: " + e.getMessage());
        }
    }    
    // Método auxiliar para coletar resultados da AVL em lista
    private static void coletarResultadosEmOrdem(Node node, List<Resultado> lista) {
        if (node != null) {
            coletarResultadosEmOrdem(node.left, lista);
            lista.addAll(node.resultados);
            coletarResultadosEmOrdem(node.right, lista);
        }
    }

    private static void analisaDesempenho(List<Documento> documentos){
        int[] dispersao = new int[93];
        int quant = 0;
        for (Documento d : documentos) {
            quant++;
            int[] aux = d.getVocabulario().getBucketSizes();
            for (int i : aux) {
                dispersao[i] += i;
            }
        }
        for (int i = 0; i < dispersao.length; i++) {
            dispersao[i] /= quant;
        }

        System.out.println("-----------------------------------------------------------------------------------------------");
        System.out.println("------------ Grafico da distribuicao das colisoes medias das tabelas de dispersao -------------\n");
        
        int maxValue = 0;
        for (int size : dispersao) {
            if (size > maxValue) {
                maxValue = size;
            }
        }

        for (int h = maxValue; h >= 1; h--) {
            System.out.printf("%4s |", (h * maxValue / maxValue));
            
            for (int size : dispersao) {
                if (size >= h) {
                    System.out.print(" # ");
                } else {
                    System.out.print("   ");
                }
            }
            System.out.println();
        }
        System.out.println("-----------------------------------------------------------------------------------------------");
        System.out.print(" Slot|");
        for (int i = 0; i < dispersao.length; i++) {
             System.out.printf(" %d ", i % 10);
        }
        System.out.println("\n");
    }
}
