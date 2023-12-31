import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
//    public static int ECHEC = 0;
//    public static int OK = 1;

    public static int compteurGetCombi = 0;
    public static int nbSolution = 0;
    public static int nbEchec = 0;
    public static int nbCopy = 0;

    public static String SEP = File.separator;
    public static String DOSSIER_RESOURCES = "src" + SEP + "main" + SEP +  "resources";


    public static void main(String[] args) throws Exception {
//        SimplePicross picross = new SimplePicross(1,1,null,null);
//        picross.ordreRemplissage.add(new Point(1,1));
//        System.out.println("ordreRemplissage "+picross.ordreRemplissage.contains(new Point(1,1)));

        runPicross("Picross1.txt");
//        runTestMegaPicross();
    }

    public static void runPicross(String filename) throws Exception {
        //Lire fichier
        //todo jordan
        //Créer la matrice
        SimplePicross picross = fichierToPicross(filename);
        System.out.println("runPicross valeursColonnes: "+picross.valeursColonnes);
        System.out.println("runPicross valeursLignes: "+picross.valeursLignes);





        //afficher resultat
        System.out.println();
        long startTime = System.currentTimeMillis();
        Parcours parcoursNormal = new Parcours(false);
//        Parcours parcoursNormal = new Parcours(Parcours.Ordre.COLONNE, true, true, true);
        if(picross.trouverSolution(parcoursNormal)) {
            System.out.println("Solution Trouvée en " + (System.currentTimeMillis() - startTime) + "ms");
        }else{
            System.out.println("Solution Impossible (" + (System.currentTimeMillis() - startTime) + "ms)");
        }
        for(String ligne : picross.forPrintMatrice()){
            System.out.println(ligne);
        }
        // Ordre de remplissage
        System.out.println("Ordre: "+picross.ordreRemplissage.size() + " - " +picross.ordreRemplissage);

        // Statistiques
        System.out.println("Statistiques: "+picross.statistiques);
    }



    public static SimplePicross fichierToPicross(String filename){
        Integer nbColonnes = 0;
        Integer nbLignes = 0;
        List<List<Integer>> valeursColonnes = new ArrayList<>();
        List<List<Integer>> valeursLignes = new ArrayList<>();
        try{
            // Le fichier d'entrée
            File file = new File(DOSSIER_RESOURCES + SEP + filename);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String line;
            //Taille matrice
            line = br.readLine();
            nbColonnes = Integer.parseInt(line.split("x")[0]);
            nbLignes = Integer.parseInt(line.split("x")[1]);

            //Colonnes:
            while(valeursColonnes.size() < nbColonnes){
                line = br.readLine();
                if(line.startsWith("#")){
                    continue;
                }
                String[] split = line.split("-");
                List<Integer> valeursColonne = new ArrayList<>();
                for (String s : split) {
                    if(Integer.parseInt(s) > 0) {
                        valeursColonne.add(Integer.parseInt(s));
                    }
                }
                valeursColonnes.add(valeursColonne);
            }

            //Lignes:
            while(valeursLignes.size() < nbLignes){
                line = br.readLine();
                if(line.startsWith("#")){
                    continue;
                }
                String[] split = line.split("-");
                List<Integer> valeursLigne = new ArrayList<>();
                for (String s : split) {
                    if(Integer.parseInt(s) > 0) {
                        valeursLigne.add(Integer.parseInt(s));
                    }
                }
                valeursLignes.add(valeursLigne);
            }
            fr.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return new SimplePicross(nbColonnes,nbLignes,valeursColonnes,valeursLignes,true);
    }

    public static void runTestMegaPicross() throws Exception {
        Boolean[][] matrice;
        int tailleColonne = 6;
        List<Boolean[][]> resultats = new ArrayList<>();
        List<Boolean[][]> resultatsTemp;
        List<MegaValeur> megaValeurs = new ArrayList<>();
        megaValeurs.add(new MegaValeur(3, MegaValeur.Position.LES_DEUX));
        megaValeurs.add(new MegaValeur(1, MegaValeur.Position.SUPERIEUR));
        megaValeurs.add(new MegaValeur(3, MegaValeur.Position.LES_DEUX));
//        megaValeurs.add(new MegaValeur(1, MegaValeur.Position.SUPERIEUR));
//        megaValeurs.add(new MegaValeur(4, MegaValeur.Position.INFERIEUR));
//        megaValeurs.add(new MegaValeur(1, MegaValeur.Position.INFERIEUR));
//        megaValeurs.add(new MegaValeur(1, MegaValeur.Position.INFERIEUR));
//        megaValeurs.add(new MegaValeur(2, MegaValeur.Position.INFERIEUR));
//        megaValeurs.add(new MegaValeur(3, MegaValeur.Position.LES_DEUX));



        for(int i = 0; i< megaValeurs.size(); i++){
            MegaValeur megaValeur = megaValeurs.get(i);
            resultatsTemp = dupliquer(resultats);
            resultats = new ArrayList<>();

            if(!resultatsTemp.isEmpty()){
                for(Boolean[][] res : resultatsTemp){
                    matrice = res;
                    ParametresMega param = setPositionDepart(megaValeur, matrice);
                    int taillemin = calculTailleMinimum(megaValeurs, i);
                    if(taillemin <= (matrice[0].length - param.colonne)) {
                        getCombi(megaValeur, param, matrice, resultats);
                    }
                }
            }else if(i == 0){
                matrice = new Boolean[2][tailleColonne];
                ParametresMega param = setPositionDepart(megaValeur, matrice);
                int taillemin = calculTailleMinimum(megaValeurs, i);
                if(taillemin <= (matrice[0].length - param.colonne)){
                    getCombi(megaValeur, param, matrice, resultats);
                }else{ //Il n'y aura aucun resultat possible si le premier ne passe pas
                    break;
                }
            }else{
                break;
            }
            System.out.println("nbSolution " + nbSolution);
            nbSolution = 0;
        }

        for(Boolean[][] mat : resultats){
            System.out.println("Solution:");
            for (Boolean[] booleans : mat) {
                System.out.println(Arrays.toString(booleans));
            }
            System.out.println("-------------");
        }
        System.out.println("compteurGetCombi " + compteurGetCombi);
        System.out.println("nbEchec " + nbEchec);
        System.out.println("nbCopy " + nbCopy);
        System.out.println("resultat " + resultats.size());

    }

    //A peu pres
    public static int calculTailleMinimum(List<MegaValeur> megaValeurs, int index){
        //todo verif
        int taille = 0;
        int compteurSup = 0;
        int compteurInf = 0;
        MegaValeur.Position lastPosition = MegaValeur.Position.LES_DEUX;
        for(int i = index; i<megaValeurs.size(); i++){
            if(i > index){
                taille++;
            }
            if(megaValeurs.get(i).getPosition() == MegaValeur.Position.LES_DEUX){
                taille = taille + megaValeurs.get(i).getValeur()/2;
                if(lastPosition != MegaValeur.Position.LES_DEUX){
                    taille = taille - Math.min(compteurSup,compteurInf)*2;
                    if(compteurSup == compteurInf){
                        taille++;
                    }
                    compteurSup = 0;
                    compteurInf = 0;
                }
                lastPosition = MegaValeur.Position.LES_DEUX;
            }else if(megaValeurs.get(i).getPosition() == MegaValeur.Position.SUPERIEUR){
                taille = taille + megaValeurs.get(i).getValeur();
                compteurSup++;
                lastPosition = MegaValeur.Position.SUPERIEUR;
            }else{
                taille = taille + megaValeurs.get(i).getValeur();
                compteurInf++;
                lastPosition = MegaValeur.Position.INFERIEUR;
            }
        }
        if(lastPosition != MegaValeur.Position.LES_DEUX){
            taille = taille - Math.min(compteurSup,compteurInf)*2;
            if(compteurSup == compteurInf){
                taille++;
            }
        }
        if(index > 0 && megaValeurs.get(index-1).getPosition() == MegaValeur.Position.LES_DEUX
                && megaValeurs.get(index-1).getValeur()%2 == 1){
            taille--;
        }
        return taille;
    }

    public static Boolean[][] copyMatrice(Boolean[][] src) {
        if (src == null) {
            return null;
        }

        Boolean[][] copy = new Boolean[src.length][];
        for (int i = 0; i < src.length; i++) {
            copy[i] = src[i].clone();
        }
        nbCopy++;
        return copy;
    }

    public static List<Boolean[][]> dupliquer(List<Boolean[][]> list){
        List<Boolean[][]> dupliquer = new ArrayList<>();
        for(Boolean[][] booleans : list){
            dupliquer.add(copyMatrice(booleans));
        }

        return dupliquer;
    }

    public static void transferer(List<Boolean[][]> list, List<Boolean[][]> resultat){
        for(Boolean[][] booleans : list){
            resultat.add(copyMatrice(booleans));
        }
    }

    public static ParametresMega setPositionDepart(MegaValeur megaValeur, Boolean[][] mat) throws Exception {
        ParametresMega param = new ParametresMega();
        if(megaValeur.getPosition() == MegaValeur.Position.SUPERIEUR
                || megaValeur.getPosition() == MegaValeur.Position.INFERIEUR){
            param.ligne = megaValeur.getPosition() == MegaValeur.Position.SUPERIEUR ? 0 : 1;
            param.colonne = mat[0].length;
            for(int c = 0; c < mat[0].length; c++ ){
                if(mat[param.ligne][c] == null){
                    param.colonne = c;
                    return param;
                }
            }
        }else{ //LES_DEUX
            param.ligne = 0;
            param.colonne = 0;
            for(int c = mat[0].length -1; c >= 0; c-- ){
                for(int l = mat.length -1; l >= 0; l-- ){
                    if(mat[l][c] != null){
                        if(mat[inverserLigne(l)][c] == null){
                            param.ligne = inverserLigne(l);
                            param.colonne = c;
                        }else{
                            param.ligne = l;
                            param.colonne = c+1;
                        }
                        return param;
                    }
                }
            }
        }
        return param;
    }

    public static boolean isValid(MegaValeur megaValeur, ParametresMega param){
        return (megaValeur.getPosition() == MegaValeur.Position.LES_DEUX && param.ok2Lignes)
                || megaValeur.getPosition() == MegaValeur.Position.INFERIEUR
                || megaValeur.getPosition() == MegaValeur.Position.SUPERIEUR;

    }

    public static void getCombi(MegaValeur megaValeur, ParametresMega param, Boolean[][] mat, List<Boolean[][]> resultats) throws Exception {
        compteurGetCombi++;
        if (param.statut != ParametresMega.Statut.ECHEC) {
            if (param.compteur == megaValeur.getValeur() && isValid(megaValeur,param)) {
                nbSolution++;
                if (param.colonne < mat[param.ligne].length) {
                    //rajouter les false pour clore
                    cloreValeur(mat, param.ligne, param.colonne, megaValeur.getPosition());
                }
                //ajout resultat
                resultats.add(mat);
            } else {
                if (param.ligne < mat.length && param.colonne < mat[param.ligne].length && mat[param.ligne][param.colonne] == null) {
                    if (param.compteur < megaValeur.getValeur() && resteDeLaPlace(megaValeur,param,mat)) {
                        caseTrue(megaValeur, param, copyMatrice(mat), resultats);
                        caseFalse(megaValeur, param, copyMatrice(mat), resultats);
                    }else{
                        enEchec(megaValeur, param, mat, resultats);
                    }
                } else {
                    enEchec(megaValeur, param, mat, resultats);
                }
            }
        }else{
            nbEchec++;
        }

    }

    //A Peut pres
    public static boolean resteDeLaPlace(MegaValeur megaValeur, ParametresMega param, Boolean[][] mat) throws Exception {
        if(megaValeur.getPosition() == MegaValeur.Position.LES_DEUX) {
            int reste = megaValeur.getValeur() - param.compteur;
            int tailleRestante = (mat[0].length - param.colonne) * 2;
            if(mat[inverserLigne(param.ligne)][param.colonne] != null){
                tailleRestante--;
            }
            return tailleRestante - reste >= 0;
        }else{
            int reste = megaValeur.getValeur() - param.compteur;
            int tailleRestante = mat[0].length - param.colonne;
            return tailleRestante - reste >= 0;
        }
    }

    public static void enEchec(MegaValeur megaValeur, ParametresMega param, Boolean[][] mat, List<Boolean[][]> resultats) throws Exception {
        ParametresMega newParam = param.cloner();
        newParam.statut = ParametresMega.Statut.ECHEC;
        getCombi(megaValeur, newParam, copyMatrice(mat), resultats);
    }

    public static void cloreValeur(Boolean[][] mat, int ligne, int colonne, MegaValeur.Position position) throws Exception {
        if(position == MegaValeur.Position.LES_DEUX) {
            // TODO: 06/07/2023 mettre tous les nulles d'avant a faux
            if (mat[ligne][colonne] == null) {
                mat[ligne][colonne] = false;
            } else {
                throw new Exception("Devrait etre null");
            }

            if (mat[inverserLigne(ligne)][colonne] == null) {
                if(colonne > 0 && mat[inverserLigne(ligne)][colonne -1] != null && mat[inverserLigne(ligne)][colonne -1]) {
                    mat[inverserLigne(ligne)][colonne] = false;
                }
            } else if (mat[inverserLigne(ligne)][colonne] && colonne + 1 < mat[ligne].length) {
                mat[inverserLigne(ligne)][colonne + 1] = false;
            }
        }else if(position == MegaValeur.Position.SUPERIEUR){
            if (mat[ligne][colonne] == null) {
                mat[ligne][colonne] = false;
            }else{
                throw new Exception("Devrait etre null");
            }
        }else{ // INFERIEUR
            if (mat[ligne][colonne] == null) {
                mat[ligne][colonne] = false;
            }
        }
    }

    public static void caseTrue(MegaValeur megaValeur, ParametresMega param, Boolean[][] mat, List<Boolean[][]> resultats) throws Exception {
        ParametresMega newParam = param.cloner();

        mat[param.ligne][param.colonne] = true;
        if(megaValeur.getPosition() == MegaValeur.Position.LES_DEUX) {
            if (!param.ok2Lignes && Boolean.TRUE.equals(mat[0][param.colonne]) && Boolean.TRUE.equals(mat[1][param.colonne])) {
                newParam.ok2Lignes = true;
            }
            if (mat[inverserLigne(param.ligne)][param.colonne] == null) {
                newParam.ligne = inverserLigne(param.ligne);
            } else {
                newParam.colonne = param.colonne +1;
            }
        }else{
            mat[inverserLigne(param.ligne)][param.colonne] = false;
            newParam.colonne = param.colonne +1;
        }

        newParam.compteur = param.compteur +1;
        getCombi(megaValeur, newParam, copyMatrice(mat), resultats);
    }

    public static void caseFalse(MegaValeur megaValeur, ParametresMega param, Boolean[][] mat, List<Boolean[][]> resultats) throws Exception {
        ParametresMega newParam = param.cloner();

        mat[param.ligne][param.colonne] = false;
        if(megaValeur.getPosition() == MegaValeur.Position.LES_DEUX) {
            if (param.compteur > 0) {
                if (mat[inverserLigne(param.ligne)][param.colonne] == null) {
                    if (Boolean.TRUE.equals(mat[inverserLigne(param.ligne)][param.colonne - 1])) {
                        newParam.ligne = inverserLigne(param.ligne);
                    } else {
                        newParam.statut = ParametresMega.Statut.ECHEC;
                    }
                } else if (mat[inverserLigne(param.ligne)][param.colonne]) {
                    newParam.ligne = inverserLigne(param.ligne);
                    newParam.colonne = param.colonne + 1;
                } else {
                    newParam.statut = ParametresMega.Statut.ECHEC;
                }
            } else {//compteur == 0
                if (mat[inverserLigne(param.ligne)][param.colonne] == null) {
                    newParam.ligne = inverserLigne(param.ligne);
                } else {
                    newParam.colonne = param.colonne + 1;
                }
            }
        }else{
            newParam.colonne = param.colonne + 1;
        }
        getCombi(megaValeur, newParam, copyMatrice(mat), resultats);
    }

    public static int inverserLigne(int ligne) throws Exception {
        if(ligne == 0){
            return 1;
        }else if(ligne == 1){
            return 0;
        }else{
            throw new Exception("Ligne pas entre 0 et 1");
        }

    }
}