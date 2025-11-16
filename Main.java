/* 
Análise de tempo: 
https://docs.oracle.com/javase/1.5.0/docs/api/java/lang/System.html#currentTimeMillis() 
 */

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        /* VARIAVEL QUE ATIVA A ANALISE EXPERIMENTAL:
            -> True para ativar
            -> False para desativar
        */ 
        boolean ANALISE_EXPERIMENTAL = true;

        long inicioPrograma = 0;

        if (ANALISE_EXPERIMENTAL) {
            inicioPrograma = System.currentTimeMillis();
        }





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

            // VARIÁVEIS DE ANÁLISE
            long tempoTotalSimilaridade = 0;
            long tempoTotalInsercaoAVL = 0;
            int totalColisoesHash = 0;
            List<Double> fatoresCarga = new ArrayList<>();


            // carrega documentos
            List<Documento> documentos = new ArrayList<>();
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath)) {
                for (Path arquivo : stream) {
                    if (Files.isRegularFile(arquivo)) {

                        Documento doc = new Documento(arquivo);
                        documentos.add(doc);

                        // coleta dados de hash
                        TabelaHash tabela = doc.getVocabulario();
                        totalColisoesHash += tabela.getColisoes();
                        fatoresCarga.add((double)tabela.getColisoes() / tabela.getTabela().length);

                    }
                }System.out.println("");
            }


            // processamento de todos os pares
            for (int i = 0; i < documentos.size(); i++) {
                for (int j = i + 1; j < documentos.size(); j++) {
                    Documento d1 = documentos.get(i);
                    Documento d2 = documentos.get(j);

                    if (modo.equals("busca")) {
                        if (!((d1.getNomeArquivo().equals(buscaArquivos.get(0)) && d2.getNomeArquivo().equals(buscaArquivos.get(1))) ||
                              (d1.getNomeArquivo().equals(buscaArquivos.get(1)) && d2.getNomeArquivo().equals(buscaArquivos.get(0))))) {
                            continue;
                        }
                    }

                    // tempo da similaridade
                    long t0 = System.nanoTime();
                    double sim = ComparadorDeDocumentos.calcularSimilaridadeCosseno(d1, d2);
                    long t1 = System.nanoTime();
                    tempoTotalSimilaridade += (t1 - t0);

                    if (sim >= limiar) {

                        // medir inserção na AVL
                        long tA1 = System.nanoTime();
                        avl.insert(new Resultado(d1.getNomeArquivo(), d2.getNomeArquivo(), sim));
                        long tA2 = System.nanoTime();
                        tempoTotalInsercaoAVL += (tA2 - tA1);

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


            // ============================
            // RELATÓRIO DE ANÁLISE EXPERIMENTAL
            // ============================
            if (ANALISE_EXPERIMENTAL) {

                long fimPrograma = System.currentTimeMillis();
                double tempoTotal = fimPrograma - inicioPrograma;

                System.out.println("\n=== ANÁLISE EXPERIMENTAL ===");

                System.out.println("Tempo total do programa: " + tempoTotal + " ms");
                System.out.println("Tempo médio por comparação: " +
                    (tempoTotalSimilaridade / 1_000_000.0) / totalComparados + " ms");

                System.out.println("Tempo total em similaridade: " +
                    (tempoTotalSimilaridade / 1_000_000.0) + " ms");

                System.out.println("Tempo médio de inserção na AVL: " +
                    (tempoTotalInsercaoAVL / 1_000_000.0) + " ms");

                System.out.println("Total de colisões (todos os documentos): " + totalColisoesHash);
                System.out.println("Colisões médias por documento: " +
                    (double) totalColisoesHash / documentos.size());

                System.out.println("Fatores de carga médios das hash tables: " +
                    fatoresCarga.stream().mapToDouble(Double::doubleValue).average().orElse(0));

                System.out.println("Árvore AVL:\n");
                avl.imprimir();

                System.out.println("\n=== ANÁLISE DE COLISÕES POR DOCUMENTO ===");
                for (Documento doc : documentos) {
                    TabelaHash tabela = doc.getVocabulario();
                    System.out.println("Documento: " + doc.getNomeArquivo() +
                                    " | Colisões: " + tabela.getColisoes() +
                                    " | Tamanho da tabela: " + tabela.getTabela().length);
                }
                System.out.println("=====================================");

                
            }

            
            
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
}