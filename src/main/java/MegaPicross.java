import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MegaPicross {

    public List<List<Integer>> valeursColonnes;
    public List<List<Integer>> valeursLignes;
    public Boolean[][] matrice;

    public static final String OUI = "o";
    public static final String NON = "x";
    public static final String ATTENTE = " ";

    public MegaPicross(int nbCol, int nbLigne, List<List<Integer>> valeursColonnes, List<List<Integer>> valeursLignes){
        this.valeursColonnes = valeursColonnes;
        this.valeursLignes = valeursLignes;
        matrice = new Boolean[nbLigne][nbCol];
        System.out.println("size "+nbLigne + " " + nbCol);
        System.out.println("size matrice "+matrice.length);
        System.out.println("size matrice[0] "+matrice[0].length);
    }

    public Boolean[] getLigne(int ligne){
        Boolean[] ligneArray = new Boolean[matrice[0].length];
        for(int i = 0; i < matrice[0].length; i++) {
            ligneArray[i] = matrice[ligne][i];
        }
        System.out.println("Ligne "+ ligne + ": "+Arrays.toString(ligneArray));
        return ligneArray;
    }

    public Boolean[] getColonne(int colonne){
        Boolean[] colArray = new Boolean[matrice.length];
        for(int i = 0; i < matrice.length; i++) {
            colArray[i] = matrice[i][colonne];
        }
        System.out.println("colonne "+ colonne + ": "+Arrays.toString(colArray));
        return colArray;
    }

    public void remplir(int ligne, int colonne, boolean valeur){
        matrice[ligne][colonne] = valeur;
    }

    public boolean construirePossibiliteColonne(int colonne){
        final Boolean[] base = getColonne(colonne);
        if(estRempli(base)){
            return false;
        }
        Boolean[] resultatFinal = new Boolean[base.length];
        List<Integer> valeursColonne = valeursColonnes.get(colonne);
        System.out.println("valeursColonne "+valeursColonne);
        //recup matrice de toutes les solutions
        Boolean[][] possibilites = getPossibilite(valeursColonne, base.length);

        //faire le tri en fonction de si c'est ok avec base
        Boolean[][] trie = triEnFonctionDeBase(base, possibilites);
        //recuperer dans resultatFinal les endroits ou tout le monde est daccord
        Boolean[] masquage = masquage(trie, base);
        //remplir matrice
        return remplirColonne(colonne, masquage);
    }

    public boolean construirePossibiliteLigne(int ligne){
        final Boolean[] base = getLigne(ligne);
        if(estRempli(base)){
            return false;
        }
        Boolean[] resultatFinal = new Boolean[base.length];
        List<Integer> valeursLigne = valeursLignes.get(ligne);
        System.out.println("valeursLignes "+valeursLignes);
        //recup matrice de toutes les solutions
        Boolean[][] possibilites = getPossibilite(valeursLigne, base.length);

        //faire le tri en fonction de si c'est ok avec base
        Boolean[][] trie = triEnFonctionDeBase(base, possibilites);
        //recuperer dans resultatFinal les endroits ou tout le monde est daccord
        Boolean[] masquage = masquage(trie, base);
        //remplir matrice
        return remplirLigne(ligne, masquage);
    }

    public Boolean[][] getPossibilite(List<Integer> valeursColonne, int taille){
        List<Boolean> combinations = new ArrayList<>();
        generateCombinationsHelper(valeursColonne, 0, new boolean[taille], 0, combinations);

        Boolean[][] conversion = conversion(combinations,taille);
        System.out.println("possibilite: " + Arrays.deepToString(conversion));
        return conversion;
    }

    public Boolean[][] conversion(List<Boolean> booleans,int taille){
        Boolean[][] conv = new Boolean[booleans.size()/taille][taille];
        for(int l = 0; l< conv.length; l++){
            for(int c = 0; c< conv[0].length; c++){
                conv[l][c] = booleans.get((taille*l)+c);
            }
        }
        return conv;
    }

    public Boolean[][] triEnFonctionDeBase(Boolean[] base, Boolean[][] possibilites){
        Boolean[][] resultat = null;
        boolean ok = true;
        for (Boolean[] possibilite : possibilites) {
            ok = true;
            for (int c = 0; c < possibilites[0].length; c++) {
                if (base[c] == null) {
                    continue;
                } else if (base[c] != possibilite[c]) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                resultat = ajouterResultat(resultat, possibilite);
            }
        }
        System.out.println("trie "+ Arrays.deepToString(resultat));
        return resultat;
    }

    public Boolean[][] ajouterResultat(Boolean[][] resultat, Boolean[] valeur){
        Boolean[][] newResultat;
        if(resultat == null){
            newResultat = new Boolean[1][valeur.length];
            newResultat[0] = valeur;
        }else {
            newResultat = new Boolean[resultat.length + 1][resultat[0].length];

            for (int i = 0; i < resultat.length; i++) {
                System.arraycopy(resultat[i], 0, newResultat[i], 0, resultat[i].length);
            }
            newResultat[resultat.length] = valeur;
        }

        System.out.println("ajouter "+ Arrays.deepToString(newResultat));
        return newResultat;
    }

    public Boolean[] masquage(Boolean[][] trie, Boolean[] base){
        Boolean[] resultat = new Boolean[trie[0].length];
        Boolean b = null;
        for(int c = 0; c < trie[0].length; c++){
            b = null;
            for (Boolean[] booleans : trie) {
                if (b == null) {
                    b = booleans[c];
                } else if (b != booleans[c]) {
                    b = null;
                    break;
                }
            }
            if(base[c] == b){
                resultat[c] = null;
            }else {
                resultat[c] = b;
            }
        }
        System.out.println("masquage "+ Arrays.deepToString(resultat));
        return resultat;
    }

    public boolean remplirColonne(int colonne, Boolean[] masquage){
        boolean aRempli = false;
        for(int i=0; i<masquage.length; i++){
            if(masquage[i] != null){
                remplir(i,colonne,masquage[i]);
                aRempli = true;
            }
        }
        return aRempli;
    }

    public boolean remplirLigne(int ligne, Boolean[] masquage){
        boolean aRempli = false;
        for(int i=0; i<masquage.length; i++){
            if(masquage[i] != null){
                remplir(ligne,i,masquage[i]);
                aRempli = true;
            }
        }
        return aRempli;
    }

    public List<String> forPrintMatrice(){
        List<String> toPrint = new ArrayList<>();
        StringBuilder ligne = new StringBuilder();
//        ligne.append("_".repeat(Math.max(0, matrice.length + 2)));
        ligne.append(String.join("", Collections.nCopies(Math.max(0, matrice.length + 2), "_")));
        toPrint.add(ligne.toString());

        for (Boolean[] booleans : matrice) {
            ligne = new StringBuilder();
            ligne.append("|");
            for (int c = 0; c < matrice[0].length; c++) {
                if (booleans[c] == null) {
                    ligne.append(ATTENTE);
                } else if (booleans[c]) {
                    ligne.append(OUI);
                } else {
                    ligne.append(NON);
                }
            }
            ligne.append("|");
            toPrint.add(ligne.toString());
        }

        ligne = new StringBuilder();
//        ligne.append("_".repeat(Math.max(0, matrice.length + 2)));
        ligne.append(String.join("", Collections.nCopies(Math.max(0, matrice.length + 2), "_")));
        toPrint.add(ligne.toString());

        return toPrint;
    }

    private void generateCombinationsHelper(List<Integer> counts, int indexCount, boolean[] array, int indexArray, List<Boolean> combinations) {
        if (indexCount == counts.size()) {
            for (boolean value : array) {
                combinations.add(value);
            }
            return;
        }

        int count = counts.get(indexCount);

        for (int i = indexArray; i <= array.length - count; i++) {
            int index = 0;
            for (int j = i; j < i + count; j++) {
                array[j] = true;
                index = j;
            }
            generateCombinationsHelper(counts, indexCount + 1, array, index+2, combinations);
            for (int j = i; j < i + count; j++) {
                array[j] = false;
            }
        }
    }

    public boolean estRempli() throws Exception {
        for(Boolean[] b : matrice){
            if(!estRempli(b)){
                return false;
            }
        }
        verification();
        return true;
    }

    public boolean estRempli(Boolean[] list){
        for(Boolean b : list){
            if(b == null){
                return false;
            }
        }
        return true;
    }

    private void verification() throws Exception {
        for(int l=0; l<matrice.length; l++){
            List<Integer> list = valeursLignes.get(l);
            int compteur = 0;
            int nbValeur = 0;
            for(Boolean b : getLigne(l) ){
                if(b == null){
                    throw new Exception("La matrice est censée etre remplise");
                }else if(b){
                    compteur++;
                }else{
                    if(compteur!=0){
                        if(compteur != list.get(nbValeur)){
                            throw new Exception("La ligne " + l +" n'est pas bonne.");
                        }
                        nbValeur++;
                        compteur=0;
                    }
                }
            }
            if(compteur!=0){
                if(compteur != list.get(nbValeur)){
                    throw new Exception("La ligne " + l +" n'est pas bonne.");
                }
                nbValeur++;
            }
            if(nbValeur != list.size()){
                throw new Exception("La ligne " + l +" n'est pas bonne.");
            }
        }

        for(int c=0; c<matrice.length; c++){
            List<Integer> list = valeursColonnes.get(c);
            int compteur = 0;
            int nbValeur = 0;
            for(Boolean b : getColonne(c) ){
                if(b == null){
                    throw new Exception("La matrice est censée etre remplise");
                }else if(b){
                    compteur++;
                }else{
                    if(compteur!=0){
                        if(compteur != list.get(nbValeur)){
                            throw new Exception("La colonne " + c +" n'est pas bonne.");
                        }
                        nbValeur++;
                        compteur=0;
                    }
                }
            }
            if(compteur!=0){
                if(compteur != list.get(nbValeur)){
                    throw new Exception("La colonne " + c +" n'est pas bonne.");
                }
                nbValeur++;
            }
            if(nbValeur != list.size()){
                throw new Exception("La colonne " + c +" n'est pas bonne.");
            }
        }
    }

}
