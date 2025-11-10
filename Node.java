import java.util.ArrayList;

class Par {
    public String elemento1;
    public String elemento2;

    public Par(String um, String dois){
        elemento1 = um;
        elemento2 = dois;
    }
}

public class Node {
    int similaridade, h;
    ArrayList<Par> pares = new ArrayList<>();
    Node left, right;

    Node(int similaridade, String arquivo1, String arquivo2) {
        this.similaridade = similaridade;
        pares.add(new Par(arquivo1, arquivo2));
        h = 1;
    }

    public void add_par(int similaridade, String arquivo1, String arquivo2){
        if (this.similaridade == similaridade)
            pares.add(new Par(arquivo1, arquivo2));
    }

    public void remove_par(int similaridade, String arquivo1, String arquivo2){
        int i = 0;
        while (!(pares.get(i).elemento1 == arquivo1 || pares.get(i).elemento1 == arquivo2 && pares.get(i).elemento2 == arquivo1 || pares.get(i).elemento2 == arquivo2) && i <= pares.size()) {
            i++;
        }
        if (i <= pares.size()) pares.remove(pares.get(i));
    }

    public int quant_pares(){
        return pares.size();
    }

    public void print_pares(){
        for (Par par : pares) {
            System.out.println(par.elemento1 + " " + par.elemento2);
        }
    }
}