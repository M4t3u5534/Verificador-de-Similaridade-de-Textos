public class TabelaHash {
    private Entry[] tabela;
    private int m; // tamanho da tabela
    private int n; // numero de elementos
    private int colisoes = 0; // numero de colisoes

    // Contrutor
    public TabelaHash(int tamanho) {
        this.m = tamanho;
        this.tabela = new Entry[m];
        this.n = 0;
    }

    // Primeira fucao hash
    private int h1(String chave) {
        int hash = 0;
        for (char c : chave.toCharArray()) hash = (31 * hash + c) % m; // hash clássico
        return hash;
    }

    // Segunda funcao hash
    private int h2(String chave) {
        int hash = 0;
        for (char c : chave.toCharArray()) hash = (37 * hash + c) % (m - 1); // menor que m
        return hash + 1; // garante que nunca seja zero
    }

    // Função de hash dupla
    private int hash(String chave, int i) {
        return (h1(chave) + i * h2(chave)) % m;
    }

    // Inserir elemento
    public void inserir(String chave, int i) {
        if (n >= m) { 
            System.out.println("Tabela cheia!"); 
            return; 
        }

        int posicao = hash(chave, i);

        if (tabela[posicao] == null) {
            tabela[posicao] = new Entry(chave);
            n++;
            return;
        }

        if (tabela[posicao].chave.equals(chave)) {
            tabela[posicao].valor++;
            return;
        }

        colisoes++;
        inserir(chave, i + 1);
    }

    // Busca usando double hashing
    public int buscar(String chave) {
        for (int i = 0; i < m; i++) {
            int pos = hash(chave, i);

            if (tabela[pos] == null) {
                return -1;
            }

            if (tabela[pos].chave.equals(chave)) {
                return tabela[pos].valor;
            }
        }
        return -1;
    }
    public int getColisoes() { return colisoes; }

    public Entry[] getTabela() { return tabela; }

    public static class Entry {
        String chave;
        int valor;

        Entry(String chave) { 
            this.chave = chave; 
            this.valor = 1; 
        }
    }
}