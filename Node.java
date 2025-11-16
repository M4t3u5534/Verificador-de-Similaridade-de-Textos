/*
Referencia utilizada para implementação das rotações:
- https://www.guj.com.br/t/arvore-avl-resolvido/58272/3
 */

import java.util.ArrayList;
import java.util.List;

public class Node {
    double similaridade;
    List<Resultado> resultados;
    Node left, right;
    int h;

    // contrutor
    public Node(Resultado r) {
        this.similaridade = r.getSimilaridade();
        this.resultados = new ArrayList<>();
        this.resultados.add(r);
        this.h = 1;
    }

    // objeto no formato resultado
    public void addResultado(Resultado r) { 
        resultados.add(r); 
    }

    public void atualiza_h() { 
        h = 1 + Math.max(getAltura(left), getAltura(right)); 
    }

    public int fator_balanceamento() { 
        return getAltura(left) - getAltura(right); 
    }

    private int getAltura(Node n) {
        if (n == null) {
            return 0;
        } else {
            return n.h;
        }
    }

    public Node rotacao_direita() {
        Node x = left;
        Node T2 = x.right;
        x.right = this;
        left = T2;
        atualiza_h();
        x.atualiza_h();
        return x;
    }

    public Node rotacao_esquerda() {
        Node y = right;
        Node T2 = y.left;
        y.left = this;
        right = T2;
        atualiza_h();
        y.atualiza_h();
        return y;
    }
}