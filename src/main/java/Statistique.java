import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class Statistique {

    public int numeroBoucle;
    public List<List<Point>> positionPossible = new ArrayList<>();
    public int valeurRestante;

    public Statistique(int numeroBoucle) {
        this.numeroBoucle = numeroBoucle;
    }

    public int nombreValeuresPossibles(){
        int nbValeur = 0;
        for(int i = 0; i < positionPossible.size(); i++){
            nbValeur += positionPossible.get(i).size();
        }
        return nbValeur;
    }

    public int nombreTest(){
        return positionPossible.size();
    }

    @Override
    public String toString() {
        return String.format("Statistiques[Boucle(%d) - NbValeures(%d) - NbTests(%d) - nbRestantes(%d)]",
                numeroBoucle, nombreValeuresPossibles(), nombreTest(), valeurRestante);
    }
}
