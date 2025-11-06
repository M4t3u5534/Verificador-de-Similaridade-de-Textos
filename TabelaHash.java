// Classe Entry para armazenar chave e valor
class Entry {
    String chave;
    int valor;

    public Entry(String chave) {
        this.chave = chave;
        this.valor = 0;
    }
}

// Tabela Hash com Enderecamento Aberto - Dispersao Dupla
public class TabelaHash {
    private Entry[] tabela;
    private int m; // tamanho da tabela
    private int n; // numero de elementos

    // Construtor
    public TabelaHash(int tamanho) {
        this.m = tamanho;
        this.tabela = new Entry[m];
        this.n = 0;
    }

    // Primeira funcao hash - metodo da divisao
    private int h1(int chave) {
        if (chave < 78)
            return chave % 26 - 13;
        return chave % 26 + 13;
    }

    // Segunda funcao hash
    private int h2(int chave) {
        return 26 + chave % 7; // usando 7 como constante
    }

    // Funcao de dispersao dupla
    private int hash(int chave, int i) {
        return (h1(chave) + i * h2(chave)) % m;
    }

    // Inserir elemento
    public void inserir(String chave, int i) {
        if (n >= m) {
            System.out.println("Tabela cheia!");
            return;
        }

        int posicao = hash(chave.charAt(0), i);

        // Se posicao vazia, insere
        if (tabela[posicao] == null) {
            tabela[posicao] = new Entry(chave);
            n++;
            System.out.println("Chave " + chave + " inserida na posicao " + posicao);
            return;
        }

        // Se chave ja existe, atualiza
        if (tabela[posicao].chave.equals(chave)) {
            tabela[posicao].valor += 1;
            System.out.println("Chave " + chave + " atualizada");
            return;
        }

        inserir(chave, i+1);
    }

    // Buscar elemento
    public int buscar(String chave) {
        int i = 0;
        int posicao;

        while (i < m) {
            posicao = hash(chave.charAt(0), i);

            // Se posicao vazia, elemento nao existe
            if (tabela[posicao] == null) {
                System.out.println("Chave " + chave + " nao encontrada");
                return -1;
            }

            // Se encontrou a chave e nao esta deletada
            if (tabela[posicao].chave.equals(chave)) {
                System.out.println("Chave " + chave + " encontrada na posicao " + posicao);
                return tabela[posicao].valor;
            }

            i++;
        }

        System.out.println("Chave " + chave + " nao encontrada");
        return -1;
    }

    // Remover elemento (remocao logica)
    public void remover(String chave) {
        int i = 0;
        int posicao;

        while (i < m) {
            posicao = hash(chave.charAt(0), i);

            if (tabela[posicao] == null) {
                System.out.println("Chave " + chave + " nao encontrada");
                return;
            }

            if (tabela[posicao].chave == chave) {
                n--;
                tabela[posicao] = null;
                System.out.println("Chave " + chave + " removida da posicao " + posicao);
                return;
            }

            i++;
        }

        System.out.println("Chave " + chave + " nao encontrada");
    }

    // Imprimir tabela
    public void imprimir() {
        System.out.println("\n--- Tabela Hash ---");
        System.out.println("Tamanho: " + m);
        System.out.println("Elementos: " + n);
        System.out.println("Fator de carga: " + (double) n / m);
        System.out.println("-------------------");

        for (int i = 0; i < m; i++) {
            System.out.print("[" + i + "] ");
            if (tabela[i] == null) {
                System.out.println("vazio");
            } else {
                System.out.println(tabela[i].chave + " -> " + tabela[i].valor);
            }
        }
        System.out.println();
    }
}