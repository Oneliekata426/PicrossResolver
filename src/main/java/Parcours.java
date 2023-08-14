public class Parcours {

    public enum Ordre {
        LIGNE,COLONNE;
    }

    public Ordre lireEnPremier;
    public boolean lectureLigneInverse;
    public boolean lectureColonneInverse;
    public boolean reset;

    public Parcours(boolean reset) {
        this.lireEnPremier = Ordre.LIGNE;
        this.lectureLigneInverse = false;
        this.lectureColonneInverse = false;
        this.reset = reset;
    }

    public Parcours(Ordre lireEnPremier, boolean lectureLigneInverse, boolean lectureColonneInverse, boolean reset) {
        this.lireEnPremier = lireEnPremier;
        this.lectureLigneInverse = lectureLigneInverse;
        this.lectureColonneInverse = lectureColonneInverse;
        this.reset = reset;
    }
}
