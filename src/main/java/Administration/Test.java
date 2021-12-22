package Administration;

import MainClasses.Definition;
import MainClasses.Word;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class Test {
    Administration admin;

    public Test(Administration admin) {
        this.admin = admin;
    }

    public void testAllMethods(){
        System.out.println("==================================================");
        testAddWord();
        System.out.println("==================================================");
        testRemoveWord();
        System.out.println("==================================================");
        testAddDefinitionForWord();
        System.out.println("==================================================");
        testRemoveDefinition();
        System.out.println("==================================================");
        testTranslateWord();
        System.out.println("==================================================");
        testTranslateSentence();
        System.out.println("==================================================");
        testTranslateSentences();
        System.out.println("==================================================");
        //testGetDefinitionForWord();      //Uncomment this if u want to test getDefinitionForWord()
    }

    public void testAddWord(){
        ArrayList<String> sing = new ArrayList<>();
        sing.add("иду");
        sing.add("идёшь");
        sing.add("идёт");
        ArrayList<String> plu = new ArrayList<>();
        plu.add("идём");
        plu.add("идёте");
        plu.add("идут");
        ArrayList<String> text = new ArrayList<>();
        text.add("Двигаться, переступая ногами");
        text.add("Двигаться, перемещаться");
        Definition def = new Definition("Wiktionary", "definitions", 2009, text);
        ArrayList<Definition> defs = new ArrayList<>();
        defs.add(def);
        Word newWord = new Word("идти", "walk", "verb", sing, plu, defs);
        System.out.println("Adaugare cuvant inexistest = " + admin.addWord(newWord, "ru"));

        ArrayList<String> text1 = new ArrayList<>();
        text1.add("Двигаться, переступая ногами");
        Definition def1 = new Definition("Wiktionary", "definitions", 2009, text1);
        ArrayList<Definition> defs1 = new ArrayList<>();
        defs1.add(def1);
        newWord = new Word("идти", "walk", "verb", sing, plu, defs1);
        System.out.println("Adaugare cuvant existent cu definitie existenta = " + admin.addWord(newWord, "ru"));

        text1 = new ArrayList<>();
        text1.add("Cовершать поступательное движение, обычно управляемое, контролируемое");
        def1 = new Definition("Wiktionary", "definitions", 2009, text1);
        defs1 = new ArrayList<>();
        defs1.add(def1);
        newWord = new Word("идти", "walk", "verb", sing, plu, defs1);
        System.out.println("Adaugare cuvant existent cu definitie noua = " + admin.addWord(newWord, "ru"));

    }

    public void testRemoveWord(){
        Word aux = admin.searchWord("eu", "ro");
        System.out.println("Stergere cuvant existent = " + admin.removeWord("eu", "ro"));
        System.out.println("Cautare cuvant sters = " + admin.searchWord("eu", "ro"));
        System.out.println("Stergere cuvant inexistent = " + admin.removeWord("blablabla", "ro"));
        admin.addWord(aux, "ro");
    }

    public void testAddDefinitionForWord(){
        ArrayList text = new ArrayList<String>();
        text.add("помещать");
        text.add("выпускать");
        Definition d = new Definition("sinonim.org", "synonyms", 2018, text);
        System.out.println("Adaugare definitie noua = " + admin.addDefinitionForWord("публиковать", "ru", d));
        System.out.println("Adaugare definitie care exista la cuvant = " + admin.addDefinitionForWord("публиковать", "ru", d));
        text = new ArrayList<String>();
        text.add("test");
        text.add("помещать");
        d = new Definition("sinonim.org", "synonyms", 2018, text);
        System.out.println("Adaugare definitie noua la cuvant existent = " +
                                    admin.addDefinitionForWord("публиковать", "ru", d));
        System.out.println("Adaugare definitie la cuvant inexistent = " +
                                    admin.addDefinitionForWord("test", "ru", d));
        System.out.println("Adaugare definitie la cuvant inexistent si limba inexistenta = " +
                                    admin.addDefinitionForWord("test", "test", d));
    }

    public void testRemoveDefinition(){
        System.out.println("Stergere definitie cuvant existent = " + admin.removeDefinition("публиковать", "ru", "sinonim.org"));
        System.out.println("Stergere definitie cuvant inexistent = " + admin.removeDefinition("test", "ro", "MDA2 (2010)"));
        System.out.println("Stergere definitie inexistenta cuvant existent = " + admin.removeDefinition("публиковать", "ru", "test"));
    }

    public void testTranslateWord(){
        String s = "pisici";
        String fromL = "ro";
        String toL = "fr";
        System.out.println("translate word |" + s + "| " + fromL + " -> " + toL + " = " + admin.translateWord(s, fromL, toL));
        s = "vais";
        fromL = "fr";
        toL = "ro";
        System.out.println("translate word |" + s + "| " + fromL + " -> " + toL + " = " + admin.translateWord(s, fromL, toL));
        s = "cat";
        fromL = "en";
        toL = "ro";
        System.out.println("translate word |" + s + "| " + fromL + " -> " + toL + " = " + admin.translateWord(s, fromL, toL));
        s = "noi";
        fromL = "ro";
        toL = "fr";
        System.out.println("translate word |" + s + "| " + fromL + " -> " + toL + " = " + admin.translateWord(s, fromL, toL));
        s = "test";
        fromL = "en";
        toL = "en";
        System.out.println("translate word |" + s + "| " + fromL + " -> " + toL + " = " + admin.translateWord(s, fromL, toL));
    }

    public void testTranslateSentence(){
        String res;
        String sentence = "Eu merg";
        String fromL = "ro";
        String toL = "fr";
        res = admin.translateSentence(sentence, fromL, toL);
        System.out.println("Translate sentence |" + sentence + "| " + fromL + " -> " + toL + " = " + res);
        sentence = "pisici merg noi publicăm";
        res = admin.translateSentence(sentence, fromL, toL);
        System.out.println("Translate sentence |" + sentence + "| " + fromL + " -> " + toL + " = " + res);
        sentence = "eu mănânc clătite";
        res = admin.translateSentence(sentence, fromL, toL);
        System.out.println("Translate sentence |" + sentence + "| " + fromL + " -> " + toL + " = " + res);
    }

    public void testTranslateSentences(){
        int i = 1;
        String sentence = "Eu merg";
        String fromL = "ro";
        String toL = "fr";
        ArrayList<String> res = admin.translateSentences(sentence, fromL, toL);
        System.out.println("Variante pentru propozitia |" + sentence + "| " + fromL + " -> " + toL);
        for(String s : res){
            System.out.println(i + ") " + s);
            i++;
        }
        i = 1;
        sentence = "eu mănânc clătită";
        res = admin.translateSentences(sentence, fromL, toL);
        System.out.println("Variante pentru propozitia |" + sentence + "| " + fromL + " -> " + toL);
        for(String s : res){
            System.out.println(i + ") " + s);
            i++;
        }
        i = 1;
        sentence = "noi publicăm";
        toL = "ru";
        res = admin.translateSentences(sentence, fromL, toL);
        System.out.println("Variante pentru propozitia |" + sentence + "| " + fromL + " -> " + toL);
        for(String s : res){
            System.out.println(i + ") " + s);
            i++;
        }
        i = 1;
        sentence = "eu public eu mănânc clătită";
        toL = "en";
        res = admin.translateSentences(sentence, fromL, toL);
        System.out.println("Variante pentru propozitia |" + sentence + "| " + fromL + " -> " + toL);
        for(String s : res){
            System.out.println(i + ") " + s);
            i++;
        }
        i = 1;
        sentence = "I eat pancake";
        toL = "ro";
        fromL = "en";
        res = admin.translateSentences(sentence, fromL, toL);
        System.out.println("Variante pentru propozitia |" + sentence + "| " + fromL + " -> " + toL);
        for(String s : res){
            System.out.println(i + ") " + s);
            i++;
        }
    }

    public void testGetDefinitionForWord(){
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        String word = "eu";
        String lang = "ro";
        ArrayList<Definition> res = admin.getDefinitionsForWord(word, lang);
        System.out.println("Definitii pentru " + word + " din " + lang + "\n" + gson.toJson(res));
        word = "clătite";
        res = admin.getDefinitionsForWord(word, lang);
        System.out.println("Definitii pentru " + word + " din " + lang + "\n" + gson.toJson(res));
        word = "jeu";
        lang = "fr";
        res = admin.getDefinitionsForWord(word, lang);
        System.out.println("Definitii pentru " + word + " din " + lang + "\n" + gson.toJson(res));
        word = "публикуете";
        lang = "ru";
        res = admin.getDefinitionsForWord(word, lang);
        System.out.println("Definitii pentru " + word + " din " + lang + "\n" + gson.toJson(res));
        word = "test";
        lang = "ro";
        res = admin.getDefinitionsForWord(word, lang);
        System.out.println("Definitii pentru " + word + " din " + lang + "\n" + gson.toJson(res));
        word = "публикуете";
        lang = "test";
        res = admin.getDefinitionsForWord(word, lang);
        System.out.println("Definitii pentru " + word + " din " + lang + "\n" + gson.toJson(res));
    }
}
