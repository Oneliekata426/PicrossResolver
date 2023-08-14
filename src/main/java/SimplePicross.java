import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class SimplePicross {

    public List<List<Integer>> valeursColonnes;
    public List<List<Integer>> valeursLignes;
    private Boolean[][] matrice;
    public List<Point> ordreRemplissage = new ArrayList<>();
    public List<Statistique> statistiques = new ArrayList<>();
    public boolean statActif;

    public static final String OUI = "o";
    public static final String NON = "x";
    public static final String ATTENTE = " ";

    public SimplePicross(int nbCol, int nbLigne, List<List<Integer>> valeursColonnes, List<List<Integer>> valeursLignes, boolean statActif){
        this.valeursColonnes = valeursColonnes;
        this.valeursLignes = valeursLignes;
        matrice = new Boolean[nbLigne][nbCol];
        this.statActif = statActif;
        System.out.println(String.format("SimplePicross - ligne(%d/%d); colonne(%d/%d)",nbLigne,matriceNbLignes(),nbCol,matriceNbColonnes()));
    }

    public int matriceNbLignes(){
        return matrice.length;
    }

    public int matriceNbColonnes(){
        return matrice[0].length;
    }

    public boolean trouverSolution(Parcours parcours) throws Exception {
        boolean changement = true;
        int nbBoucle = 0;
        while(changement) {
            nbBoucle++;
            changement = false;
            boolean ligneEnPremiere = Parcours.Ordre.LIGNE.equals(parcours.lireEnPremier);
            int end = ligneEnPremiere ? matriceNbLignes() : matriceNbColonnes();
            for (int start = 0; start < end; start++) {
                System.out.println("trouverSolution-----------------start "+start);// TODO: 13/08/2023
                //Lecture normal ou inversé (de bas en haut/droite à gauche)
                int position = (ligneEnPremiere && !parcours.lectureLigneInverse)
                        || (!ligneEnPremiere && !parcours.lectureColonneInverse) ? start : (end-1-start);
                if(ligneEnPremiere) {
                    changement = construirePossibiliteLigne(position,nbBoucle) || changement;
                }else{
                    changement = construirePossibiliteColonne(position,nbBoucle) || changement;
                }
                if(parcours.reset && changement){
                    System.out.println("trouverSolution-----------------RESET ");// TODO: 13/08/2023
                    break;
                }
            }
            if(!(parcours.reset && changement)) {
                boolean colonneEnDerniere = ligneEnPremiere;
                end = colonneEnDerniere ? matriceNbColonnes() : matriceNbLignes();
                for (int start = 0; start < end; start++) {
                    System.out.println("trouverSolution-----------------start2 "+start);// TODO: 13/08/2023
                    //Lecture normal ou inversé (de bas en haut/droite à gauche)
                    int position = (colonneEnDerniere && !parcours.lectureColonneInverse)
                            || (!colonneEnDerniere && !parcours.lectureLigneInverse) ? start : (end-1-start);
                    if(colonneEnDerniere) {
                        changement = construirePossibiliteColonne(position,nbBoucle) || changement;
                    }else{
                        changement = construirePossibiliteLigne(position,nbBoucle) || changement;
                    }
                    if(parcours.reset && changement){
                        System.out.println("trouverSolution-----------------RESET2 ");// TODO: 13/08/2023
                        break;
                    }
                }
            }
        }

        System.out.println("Nombre de boucles parcourus: " + nbBoucle);
        return estRempli();
    }

    public Boolean[] getLigne(int ligne){
        Boolean[] ligneArray = new Boolean[matriceNbColonnes()];
        for(int i = 0; i < matriceNbColonnes(); i++) {
            ligneArray[i] = matrice[ligne][i];
        }
        System.out.println("getLigne "+ ligne + ": "+Arrays.toString(ligneArray));
        return ligneArray;
    }

    public Boolean[] getColonne(int colonne){
        Boolean[] colArray = new Boolean[matriceNbLignes()];
        for(int i = 0; i < matriceNbLignes(); i++) {
            colArray[i] = matrice[i][colonne];
        }
        System.out.println("getColonne "+ colonne + ": "+Arrays.toString(colArray));
        return colArray;
    }

    public boolean construirePossibiliteColonne(int colonne, int nbBoucle){
        final Boolean[] base = getColonne(colonne);
        if(estRempli(base)){
            return false;
        }
//        Boolean[] resultatFinal = new Boolean[base.length];//todo
        List<Integer> valeursColonne = valeursColonnes.get(colonne);
        System.out.println("valeursColonne "+valeursColonne);
        //recup matrice de toutes les solutions
        Boolean[][] possibilites = getPossibilite(valeursColonne, base.length);

        //faire le tri en fonction de si c'est ok avec base
        Boolean[][] trie = triEnFonctionDeBase(base, possibilites);
        //recuperer dans resultatFinal les endroits ou tout le monde est daccord
        Boolean[] masquage = masquage(trie, base);
        //remplir matrice
        boolean aRempli = remplir(colonne, masquage,false);
        if(aRempli){
            ajouterStatistique(nbBoucle,colonne, masquage,false);
        }
        return aRempli;
    }

    public boolean remplir(int indice, Boolean[] masquage, boolean pourLigne){
        boolean aRempli = false;
        for(int i=0; i<masquage.length; i++){
            if(masquage[i] != null){
                if(pourLigne){
                    remplirMatrice(new Point(indice, i), masquage[i]);
                }else {
                    remplirMatrice(new Point(i, indice), masquage[i]);
                }
                aRempli = true;
            }
        }
        return aRempli;
    }

    public void remplirMatrice(Point position, boolean valeur){
        matrice[position.x][position.y] = valeur;
        ajouterOrdreRemplissage(position);
    }

    public boolean construirePossibiliteLigne(int ligne, int nbBoucle){
        final Boolean[] base = getLigne(ligne);
        if(estRempli(base)){
            return false;
        }
//        Boolean[] resultatFinal = new Boolean[base.length];todo
        List<Integer> valeursLigne = valeursLignes.get(ligne);
        System.out.println("valeursLignes "+valeursLignes);
        //recup matrice de toutes les solutions
        Boolean[][] possibilites = getPossibilite(valeursLigne, base.length);

        //faire le tri en fonction de si c'est ok avec base
        Boolean[][] trie = triEnFonctionDeBase(base, possibilites);
        //recuperer dans resultatFinal les endroits ou tout le monde est daccord
        Boolean[] masquage = masquage(trie, base);
        //remplir matrice
        boolean aRempli = remplir(ligne, masquage,true);
        if(aRempli){
            ajouterStatistique(nbBoucle,ligne, masquage,true);
        }
        return aRempli;
    }

    public Boolean[][] getPossibilite(List<Integer> valeursColonne, int taille){
        List<Boolean> combinations = new ArrayList<>();
        generateCombinationsHelper(valeursColonne, 0, new boolean[taille], 0, combinations);

        Boolean[][] conversion = conversion(combinations,taille);
        System.out.println("possibilite: " + Arrays.deepToString(conversion));
        return conversion;
    }

    // Transforme une liste en tableau: exemple pour [o;o;o;x] de taille 2
    // |oo|
    // |ox|
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
        System.out.println("triEnFonctionDeBase "+ Arrays.deepToString(resultat));
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

    public void ajouterStatistique(int nbBoucle, int indice, Boolean[] masquage,boolean pourLigne){
        if(statActif) {
            Statistique stat = new Statistique(nbBoucle);
            List<Point> points = new ArrayList<>();
            for (int i = 0; i < masquage.length; i++) {
                if (masquage[i] != null) {
                    if (pourLigne) {
                        points.add(new Point(indice, i));
                    } else {
                        points.add(new Point(i, indice));
                    }
                }
            }
            if (points.size() > 0) {
                stat.positionPossible.add(points);
                stat.valeurRestante = 0;//todo
                statistiques.add(stat);
            }
        }
    }

    public List<String> forPrintMatrice(){
        List<String> toPrint = new ArrayList<>();
        StringBuilder ligne = new StringBuilder();
        ligne.append(String.join("", Collections.nCopies(Math.max(0, matriceNbLignes() + 2), "_")));
        toPrint.add(ligne.toString());

        for (Boolean[] booleans : matrice) {
            ligne = new StringBuilder();
            ligne.append("|");
            for (int c = 0; c < matriceNbColonnes(); c++) {
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
        ligne.append(String.join("", Collections.nCopies(Math.max(0, matriceNbLignes() + 2), "_")));
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
        for(int l=0; l<matriceNbLignes(); l++){
            List<Integer> list = valeursLignes.get(l);
            int compteur = 0;
            int nbValeur = 0;
            for(Boolean b : getLigne(l) ){
                if(b == null){
                    throw new Exception("La matrice est censée etre remplise");
                }else if(b){
                    compteur++;
                }else{
                    if(compteur != 0){
                        if(compteur != list.get(nbValeur)){
                            throw new Exception("La ligne " + l +" n'est pas bonne.");
                        }
                        nbValeur++;
                        compteur=0;
                    }
                }
            }
            if(compteur != 0){
                if(compteur != list.get(nbValeur)){
                    throw new Exception("La ligne " + l +" n'est pas bonne.");
                }
                nbValeur++;
            }
            if(nbValeur != list.size()){
                System.out.println("nbValeur "+nbValeur +" - "+list.size());//todo jordan
                throw new Exception("La ligne " + l +" n'est pas bonne.");
            }
        }

        for(int c=0; c<matriceNbColonnes(); c++){
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

    public boolean ajouterOrdreRemplissage(Point position){
        if(ordreRemplissage.contains(position)){
            return false;
        }else{
            ordreRemplissage.add(position);
            return true;
        }
    }

}
