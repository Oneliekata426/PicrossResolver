import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombinationGenerator2 {
    public static int ECHEC = 0;
    public static int OK = 1;

    public static SimplePicross fichierToPicross(String nameFile){
        Integer colonnes = 0;
        Integer lignes = 0;
        List<List<Integer>> valeursColonnes = new ArrayList<>();
        List<List<Integer>> valeursLignes = new ArrayList<>();
        try{
            // Le fichier d'entrée
            File file = new File("src/resources/"+nameFile);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String line;
            //Taille matrice
            line = br.readLine();
            colonnes = Integer.parseInt(line.split("x")[0]);
            lignes = Integer.parseInt(line.split("x")[1]);

            //Colonnes:
            br.readLine();

            for(int i = 0; i<colonnes; i++){
                line = br.readLine();
                String[] split = line.split("-");
                List<Integer> valeursColonne = new ArrayList<>();
                for (String s : split) {
                    valeursColonne.add(Integer.parseInt(s));
                }
                valeursColonnes.add(valeursColonne);
            }

            //Lignes:
            br.readLine();

            for(int i = 0; i<lignes; i++){
                line = br.readLine();
                String[] split = line.split("-");
                List<Integer> valeursLigne = new ArrayList<>();
                for (String s : split) {
                    valeursLigne.add(Integer.parseInt(s));
                }
                valeursLignes.add(valeursLigne);
            }
            fr.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return new SimplePicross(colonnes,lignes,valeursColonnes,valeursLignes,true);
    }

    public static void runAllPicross() throws Exception {
//        //Lire fichier
//
//        //creer la matrice
//        SimplePicross picross = fichierToPicross("Picross3.txt");
//        System.out.println("test "+picross.valeursColonnes);
//        System.out.println("test "+picross.valeursLignes);
//
//        boolean changement = true;
//        long startTime = System.currentTimeMillis();
//        while(changement) {
//            changement = false;
//            for (int c = 0; c < picross.matriceNbColonnes(); c++) {
//                changement = changement || picross.construirePossibiliteColonne(c);
//            }
//            for (int l = 0; l < picross.matriceNbLignes(); l++) {
//                changement = changement || picross.construirePossibiliteLigne(l);
//            }
//        }
//
//        //afficher resultat
//        System.out.println();
//        if(picross.estRempli()) {
//            System.out.println("Solution Trouvée en " + (System.currentTimeMillis() - startTime) + "ms");
//        }else{
//            System.out.println("Solution Impossible (" + (System.currentTimeMillis() - startTime) + "ms)");
//        }
//        for(String ligne : picross.forPrintMatrice()){
//            System.out.println(ligne);
//        }
    }

    public static Boolean[][] copy(Boolean[][] src) {
        if (src == null) {
            return null;
        }

        Boolean[][] copy = new Boolean[src.length][];
        for (int i = 0; i < src.length; i++) {
            copy[i] = src[i].clone();
        }

        return copy;
    }

    public static void main(String[] args) throws Exception {
//        Boolean[][] b = new Boolean[2][3];
//        System.out.println("matrice1 " + Arrays.deepToString(b));
//        Boolean[][] b2 = copy(b);
//        System.out.println("matrice2 " + Arrays.deepToString(b2));
//        b[1][1] = true;
//        System.out.println("matrice3 " + Arrays.deepToString(b2));
        Boolean[][] b = new Boolean[2][5];
//        b[0][2] = false;
        getCombi(OK,4,0,false, 0,0, b);
//        for (Boolean[] booleans : matrice) {
//            System.out.println(Arrays.toString(booleans));
//        }
    }

    public static void getCombi(int statut, int valeur,int compteur, boolean ok2Lignes, int ligne, int colonne, Boolean[][] mat){
        int newStatut = statut;
        int newValeur = valeur;
        int newCompteur = compteur;
        boolean newOk2Lignes = ok2Lignes;
        int newLigne = ligne;
        int newColonne = colonne;
//        System.out.println(String.format("Test %d %b %d %d", compteur, ok2Lignes, ligne, colonne));//todo jordan
//        for (Boolean[] booleans : mat) {
//            System.out.println(Arrays.toString(booleans));
//        }
        if(statut == ECHEC){
//            System.out.println("ECHEC:");
//            for (Boolean[] booleans : mat) {
//                System.out.println(Arrays.toString(booleans));
//            }
//            System.out.println("-------------");
        }else if(compteur == valeur && ok2Lignes){
            System.out.println("Solution:");
            for (Boolean[] booleans : mat) {
                System.out.println(Arrays.toString(booleans));
            }
            System.out.println("-------------");
        }else{
            if(ligne < mat.length && colonne<mat[ligne].length) {
                if(compteur < valeur) {
                    //cas true
                    mat[ligne][colonne] = true;
                    newCompteur++;
                    if (!ok2Lignes && Boolean.TRUE.equals(mat[0][colonne]) && Boolean.TRUE.equals(mat[1][colonne])) {
                        newOk2Lignes = true;
                    }
                    if (ligne == 0 && mat[ligne + 1][colonne] == null) {
                        newLigne++;
                    } else if (ligne == 1 && mat[ligne - 1][colonne] == null) {
                        newLigne--;
                    } else {
                        newColonne++;
                    }
                    getCombi(newStatut, valeur, newCompteur, newOk2Lignes, newLigne, newColonne, copy(mat));

                    //cas false
                    mat[ligne][colonne] = false;
                    if(compteur > 0) {
                        if(ligne == 0) {
                            if (mat[ligne + 1][colonne] == null) {
                                if (Boolean.TRUE.equals(mat[ligne + 1][colonne - 1])) {
                                    newLigne++;
                                } else {
                                    newStatut = ECHEC;
                                }
                            } else if (mat[ligne + 1][colonne]) {
                                newLigne++;
                                newColonne++;
                            } else {
                                newStatut = ECHEC;
                            }
                        }else{//ligne == 1
                            if (mat[ligne - 1][colonne] == null) {
                                if (Boolean.TRUE.equals(mat[ligne - 1][colonne - 1])) {
                                    newLigne--;
                                } else {
                                    newStatut = ECHEC;
                                }
                            } else if (mat[ligne - 1][colonne]) {
                                newLigne--;
                                newColonne++;
                            } else {
                                newStatut = ECHEC;
                            }
                        }
                    }else{//compteur == 0
                        if (ligne == 0 && mat[ligne + 1][colonne] == null) {
                            newLigne++;
                        } else if (ligne == 1 && mat[ligne - 1][colonne] == null) {
                            newLigne--;
                        } else {
                            newColonne++;
                        }
                    }
                    getCombi(newStatut, valeur, newCompteur, newOk2Lignes, newLigne, newColonne, copy(mat));
                }
            }else{
                newStatut = ECHEC;
                getCombi(newStatut, valeur, newCompteur, newOk2Lignes, newLigne, newColonne, copy(mat));
            }
        }

    }
}