package Scoring;

import Tools.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class Scorer {
    final static int TAB = 8;

    private static ArrayList<String> extractVarDeclarationsInList(Node ast){
        ArrayList<String> res = new ArrayList<>();

        if (ast instanceof DeclarationNode){
            DeclarationNode currentNode = (DeclarationNode) ast;
            res.add(currentNode.getName());
        }

        for (Node n : ast.getChildren()){
            res.addAll(extractVarDeclarationsInList(n));
        }

        return res;
    }

/*    private static ArrayList<String> extractVarAffectationInList(Node ast){
        ArrayList<String> res = new ArrayList<String>();

        if (ast instanceof AffectationNode){
            DeclarationNode currentNode = (DeclarationNode) ast;
            res.add(currentNode.getName());
        }

        for (Node n : ast.getChildren()){
            res.addAll(extractVarAffectationInList(n));
        }

        return res;
    }*/

/*    ArrayList<String> extractFunDeclarationsInList(Node ast){

    }*/

    public static ArrayList<String> varNameConventions(Node ast){
        ArrayList<String> retErrors = new ArrayList<>();

        ArrayList<String> varDeclList = extractVarDeclarationsInList(ast);

        String caseError = "Your variable %s is not in lower case and it's wrong !";
        String caseLenght = "This var %s is not long enough and can't be clear !";
        for (String decl: varDeclList){
            if (!decl.equals(decl.toLowerCase())){
                // Cas où la variable n'est pas en minuscule
                retErrors.add(String.format(caseError, decl));
            }

            if (decl.length() < 3){
                retErrors.add(String.format(caseLenght, decl));
            }
        }

        return retErrors;
    }

/*    ArrayList<String> checkUsedDecl(Node ast){
        ArrayList<String> varDeclList = extractVarDeclarationsInList(ast);
        ArrayList<String> varAffectList = extractVarAffectationInList(ast);
        ArrayList<String> retErrors = new ArrayList<String>();

        for (String word: varDeclList){
            if (!varAffectList.contains(word)){
                retErrors.add(word);
            }
        }
        return retErrors;
    }*/

    public static ArrayList<String> checkIndentation(HashMap<Integer, Integer> indentationIndex, ArrayList<LexicalToken> lexicalsTokens){
        ArrayList<String> retErrors = new ArrayList<>();

        String wrongIndentationLenght = "Your indendation line %d is not a multiple of %d it seem you used mix indentation style or weird tab length";
        for (int line = 0; line < indentationIndex.size(); line++){
            if (indentationIndex.get(line) % TAB != 0){
                retErrors.add(String.format(wrongIndentationLenght, line, TAB));
            }
        }

        // Generation du guide d'indentation
        HashMap<Integer, Integer> indentGuide = new HashMap<Integer, Integer>();
        int indentChar = 0;
        for(LexicalToken token : lexicalsTokens){
            if(token.getType().equals(lexicalType.Lb)){
                indentGuide.put(token.getLine(), indentChar);
                indentChar += TAB;
            }
            else if (token.getType().equals(lexicalType.Rb)){
                indentChar -= TAB;
                indentGuide.put(token.getLine(), indentChar);
            }
            else{
                indentGuide.put(token.getLine(), indentChar);
            }
        }

        String wrongIdent = "On the line %d you're indendation is bad, too much or not enough tab";
        for(Map.Entry<Integer, Integer> item : indentGuide.entrySet()){
            if(!indentationIndex.get(item.getKey()).equals(item.getValue())){
                retErrors.add(String.format(wrongIdent, item.getKey()));
            }
        }

        return retErrors;
    }

    public static boolean notMore200Lines(HashMap<Integer, Integer> indentationIndex){
        return indentationIndex.size() <= 200;
    }
}