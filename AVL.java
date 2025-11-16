public class AVL {
    Node raiz;
    int rotacoes_simples = 0, rotacoes_duplas = 0;


    // inserção
    public void insert(Resultado res) {
        raiz = insereRec(raiz, res);
    }

    private Node insereRec(Node node, Resultado res) {
        if (node == null) return new Node(res);

        if (res.getSimilaridade() < node.similaridade) {
            node.left = insereRec(node.left, res);
        
        } else if (res.getSimilaridade() > node.similaridade) {
            node.right = insereRec(node.right, res);
        
        } else {node.addResultado(res);}


        node.atualiza_h();

        // balanceamento
        int balanco = node.fator_balanceamento(); // dif entre alturas

        if (balanco > 1 && res.getSimilaridade() < node.left.similaridade) { 
            rotacoes_simples++; 
            return node.rotacao_direita(); 
        
        } if (balanco < -1 && res.getSimilaridade() > node.right.similaridade) { 
            rotacoes_simples++; 
            return node.rotacao_esquerda(); 
        
        } if (balanco > 1 && res.getSimilaridade() > node.left.similaridade) { 
            rotacoes_duplas++; 
            node.left = node.left.rotacao_esquerda(); 
            return node.rotacao_direita(); 
        
        } if (balanco < -1 && res.getSimilaridade() < node.right.similaridade) { 
            rotacoes_duplas++; 
            node.right = node.right.rotacao_direita(); 
            return node.rotacao_esquerda(); 
        
        } return node;
    }

    public void imprimir() {
        imprimirRec(raiz, 0);
    }

    private void imprimirRec(Node node, int nivel) {
        if (node == null) return;

        // nó direito
        imprimirRec(node.right, nivel + 1);

        // Indentação proporcional ao nível
        for (int i = 0; i < nivel; i++) System.out.print("        ");

        // similaridade e pares de documentos do nó
        System.out.print("[");
        for (int i = 0; i < node.resultados.size(); i++) {
            // já formatado pelo toString em override da estrutura de resultado
            System.out.print(node.resultados.get(i)); 
            if (i < node.resultados.size() - 1) System.out.print(", ");
        }
        System.out.println("]");

        // nó esquerdo
        imprimirRec(node.left, nivel + 1);
    }
}