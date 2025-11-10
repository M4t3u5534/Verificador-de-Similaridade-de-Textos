public class AVL {
    Node raiz;
    int rotacoes_simples = 0, rotacoes_duplas = 0;

    private int h(Node N) {
        if (N == null)
            return 0;
        return N.h;
    }

    private int fator_balanceamento(Node N) {
        if (N == null)
            return 0;
        return h(N.left) - h(N.right);
    }

    private void atualiza_h(Node N) {
        N.h = 1 + Math.max(h(N.left), h(N.right));
    }

    private Node rotacao_direita(Node y) {
        Node x = y.left;
        Node T2 = x.right;
        
        x.right = y;
        y.left = T2;

        atualiza_h(y);
        atualiza_h(x);

        return x;
    }

    private Node rotacao_esquerda(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        atualiza_h(x);
        atualiza_h(y);

        return y;
    }

    public void insert(int key, String arquivo1, String arquivo2) {
        raiz = insert_recursivo(raiz, key, arquivo1, arquivo2);
    }

    Node insert_recursivo(Node node, int key, String arquivo1, String arquivo2) {
        if (node == null)
            return (new Node(key, arquivo1, arquivo2));
        if (key < node.similaridade)
            node.left = insert_recursivo(node.left, key, arquivo1, arquivo2);
        else if (key > node.similaridade)
            node.right = insert_recursivo(node.right, key, arquivo1, arquivo2);
        else {
            node.add_par(key, arquivo1, arquivo2);
            return node;
        }

        atualiza_h(node);

        int balanco = fator_balanceamento(node);

        if (balanco > 1 && key < node.left.similaridade){
            rotacoes_simples++;
            return rotacao_direita(node);
        }
        if (balanco < -1 && key > node.right.similaridade){
            rotacoes_simples++;
            return rotacao_esquerda(node);
        }
        if (balanco > 1 && key > node.left.similaridade) {
            rotacoes_duplas++;
            node.left = rotacao_esquerda(node.left);
            return rotacao_direita(node);
        }
        if (balanco < -1 && key < node.right.similaridade) {
            rotacoes_duplas++;
            node.right = rotacao_direita(node.right);
            return rotacao_esquerda(node);
        }
        return node;
    }

    public boolean search(int key) {
        return searchRecursive(raiz, key);
    }

    private boolean searchRecursive(Node node, int key) {
        if (node == null) {
            return false;
        }

        // Caso Base 2: A chave foi encontrada
        if (key == node.similaridade) {
            return true;
        }

        // Se a chave for menor, procure na subárvore esquerda
        if (key < node.similaridade) {
            return searchRecursive(node.left, key);
        }
        
        // Se a chave for maior, procure na subárvore direita
        // (key > node.key)
        else {
            return searchRecursive(node.right, key);
        }
    }

    Node minValueNode(Node node) {
        Node current = node;
        while (current.left != null)
            current = current.left;
        return current;
    }

    public void remove(int key) {
        raiz = remove_recursivo(raiz, key);
    }

    Node remove_recursivo(Node raiz, int key) {
        if (raiz == null)
            return raiz;
        if (key < raiz.similaridade)
            raiz.left = remove_recursivo(raiz.left, key);
        else if (key > raiz.similaridade)
            raiz.right = remove_recursivo(raiz.right, key);
        else {
            //if (raiz.quant_pares() == 1) {
                if ((raiz.left == null) || (raiz.right == null)) {
                    Node temp = (raiz.left != null) ? raiz.left : raiz.right;
                    if (temp == null) {
                        temp = raiz;
                        raiz = null;
                    } else
                        raiz = temp;
                } else {
                    Node temp = minValueNode(raiz.right);
                    raiz.similaridade = temp.similaridade;
                    raiz.right = remove_recursivo(raiz.right, temp.similaridade);
                }
            //}
            //else {
            //    raiz.remove_par(key, null, null);
            //}
        }
        if (raiz == null)
            return raiz;
        atualiza_h(raiz);

        int balanco = fator_balanceamento(raiz);

        if (balanco > 1 && fator_balanceamento(raiz.left) >= 0)
            return rotacao_direita(raiz);
        if (balanco > 1 && fator_balanceamento(raiz.left) < 0) {
            raiz.left = rotacao_esquerda(raiz.left);
            return rotacao_direita(raiz);
        }
        if (balanco < -1 && fator_balanceamento(raiz.right) <= 0)
            return rotacao_esquerda(raiz);
        if (balanco < -1 && fator_balanceamento(raiz.right) > 0) {
            raiz.right = rotacao_direita(raiz.right);
            return rotacao_esquerda(raiz);
        }

        return raiz;
    }

    public void inorderTraversal() {
        inorder(raiz);
        System.out.println();
    }

    private void inorder(Node node) {
        if (node != null) {
            inorder(node.left);
            System.out.print(node.similaridade + ": ");
            node.print_pares();
            inorder(node.right);
        }
    }

    public int getRotacoes_simples() {
        return rotacoes_simples;
    }

    public int getRotacoes_duplas() {
        return rotacoes_duplas;
    }
}