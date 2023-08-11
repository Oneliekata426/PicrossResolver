public class MegaValeur {
    public enum Position{
        SUPERIEUR,
        INFERIEUR,
        LES_DEUX;
    }

    private int valeur;
    private Position position;


    public MegaValeur(int valeur, Position position){
        this.valeur = valeur;
        this.position = position;
    }

    public int getValeur() {
        return valeur;
    }

    public void setValeur(int valeur) {
        this.valeur = valeur;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
