/*
Esse método calcula a similaridade entre os documentos com base na frequência das palavras.
Cada documento é representado como um vetor no espaço das palavras (vocabulario) e 
medir o ângulo entre esses vetores. Quanto menor o ângulo, mais similares são os documentos.

Etapas:
    1. Criar vetores de frequência de palavras para cada documento.
    2. Calcular o produto escalar entre os vetores.
    3. Calcular a magnitude (norma) de cada vetor.
    4. Dividir o produto escalar pelo produto das magnitudes para obter a similaridade.
    5. Adiciona um valor quase insignificante (1e-10) para evitar divisão por zero.

Referência:
https://en.wikipedia.org/wiki/Cosine_similarity
 */

public class ComparadorDeDocumentos {

    public static double calcularSimilaridadeCosseno(Documento d1, Documento d2) {
        int somaProduto = 0; // acumula o produto escalar entre os vetores dos documentos
        int soma1 = 0; // acumula soma dos quadrados das frequências do doc1
        int soma2 = 0; // acumula soma dos quadrados das frequências do doc2

        // percorre todas as palavras do doc1
        for (TabelaHash.Entry e1 : d1.getVocabulario().getTabela()) {
            if (e1 == null) continue; // ignora posições vazias da tabela hash

            // busca a frequência da mesma palavra no doc2
            int freq2 = d2.getVocabulario().buscar(e1.chave);
            if (freq2 < 0) freq2 = 0; // se a palavra não existir, considera frequência 0

            // atualiza o produto escalar (quant * freq)
            somaProduto += e1.valor * freq2;

            // atualiza a soma dos quadrados do doc1
            soma1 += e1.valor * e1.valor;
        }

        // percorre todas as entradas do doc2 para calcular a soma dos quadrados
        for (TabelaHash.Entry e2 : d2.getVocabulario().getTabela()) {
            if (e2 == null) continue; // posições vazias
            soma2 += e2.valor * e2.valor; // soma os quadrados das frequências
        }

        // Divide o produto escalar pelo produto das magnitudes (raiz quadrada das somas dos quadrados)
        // 1e-10 é adicionado para evitar divisão por zero caso algum vetor seja nulo
        return somaProduto / (Math.sqrt(soma1) * Math.sqrt(soma2) + 1e-10);
    }
}