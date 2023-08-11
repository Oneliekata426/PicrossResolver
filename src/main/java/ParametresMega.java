import java.util.List;

public class ParametresMega {
    public enum Statut{
        ECHEC,
        OK;
    }

    public Statut statut;
    public int compteur;
    public boolean ok2Lignes;
    public int ligne;
    public int colonne;

    public ParametresMega(){
        this.statut = Statut.OK;
        this.compteur = 0;
        this.ok2Lignes = false;
        this.ligne = 0;
        this.colonne = 0;
    }

    public ParametresMega cloner() {
        ParametresMega newParam = new ParametresMega();
        newParam.statut = this.statut;
        newParam.compteur = this.compteur;
        newParam.ok2Lignes = this.ok2Lignes;
        newParam.ligne = this.ligne;
        newParam.colonne = this.colonne;

        return newParam;
    }

    @Override
    public String toString() {
        return "ParametresMega{" +
                "statut=" + statut +
                ", compteur=" + compteur +
                ", ok2Lignes=" + ok2Lignes +
                ", ligne=" + ligne +
                ", colonne=" + colonne +
                '}';
    }
}
