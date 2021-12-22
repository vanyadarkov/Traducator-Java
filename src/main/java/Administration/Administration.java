package Administration;

import MainClasses.Definition;
import MainClasses.Word;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Administration {
    public Map<String, ArrayList<Word>> langs;
    static Random r = new Random();

    public Administration() {
        if(langs == null) langs = new HashMap<>();
    }

    /**
     * Method which search for the required word in the required language in langs map
     * @param word required word
     * @param language required language
     * @return null - word not found, Word object of found word
     */
    public Word searchWord(String word, String language){
        if(!langs.containsKey(language)) return null;
        ArrayList<Word> words = langs.get(language);
        if(words == null) return null;
        Word auxWord = new Word(word);
        if(!words.contains(auxWord)) return null;
        return words.get(words.indexOf(auxWord));
    }

    /**
     * Refresh the words in language dictionary. If old dictionary don't have a word from new, add it. If word already exists, check for its definitions.
     * It means if the old word didnt have a definition from readed new one, add missed definition to old word.
     * @param existentWords existent words in our database
     * @param newWords new words read from file
     * @param language language of dictionary
     */
    public void refreshLanguageDictionary(ArrayList<Word> existentWords, ArrayList<Word> newWords, String language){
        for(Word w : newWords){
            if(existentWords.contains(w)){
                Word existentWord = searchWord(w.getWord(), language);
                ArrayList<Definition> definitionList = w.getDefinitions();
                checkWordForDefinitionRefresh(existentWord, definitionList);
            }
            else{
                existentWords.add(w);
            }
        }
    }

    /**
     * Sort all words from database and their definition
     */
    public void sortWordsAndDefinitions(){
        for(ArrayList<Word> words : langs.values()){
            for(Word word : words){
                Collections.sort(word.getDefinitions());
            }
            Collections.sort(words);
        }
    }

    /**
     * Merge all words from String[] to a sentence
     * @param array words from sentence to be merged
     * @return sentence with all words
     */
    public String getSentenceFromArray(String[] array){
        StringBuilder sb = new StringBuilder();

        for(String w : array){
            sb.append(w).append(" ");
        }
        return sb.toString().replaceAll(".$", "");
    }

    /**
     * Check if word have or not all definitions from defs. If one or more missing, add to the word
     * @param word word to check its definitions
     * @param defs definitions to add or no to the word
     * @return true - word was updated, false - otherwise
     */
    public boolean checkWordForDefinitionRefresh(Word word,ArrayList<Definition> defs){
        int updated = 0;
        for(Definition d : defs){
            if(!word.getDefinitions().contains(d)){
                word.getDefinitions().add(d);
                return true;
            }
            else{
                Definition aux = word.getDefinitions().get(word.getDefinitions().indexOf(d));
                for(String text : d.getText()){
                    if(!aux.getText().contains(text)){
                        aux.getText().add(text);
                        updated = 1;
                    }
                }
            }
        }
        return updated == 1;
    }

    /**
     * Add a word from language to our database(which is a HashMap with Key String(language) and Value ArrayList(words of a language))
     * @param word Object Word to add
     * @param language Language at which we add a word
     * @return true - word was added/the existent word was updated(its definitions), false - word already exists
     */
    public boolean addWord(Word word, String language){
        if(!langs.containsKey(language)){
            langs.put(language, new ArrayList<Word>());
            langs.get(language).add(word);
            return true;
        }
        Word foundWord = searchWord(word.getWord(), language);
        if(foundWord != null){
            return checkWordForDefinitionRefresh(foundWord, word.getDefinitions());
        }
        langs.get(language).add(word);
        return true;
    }

    /**
     * Remove a word from specified language
     * @param word word to remove
     * @param language language in which we search requested word
     * @return false - language/word doesn't exists, true - removed with success
     */
    public boolean removeWord(String word, String language){
        if(!langs.containsKey(language)) return false;
        Word foundWord = searchWord(word, language);
        if( foundWord != null ) {
            langs.get(language).remove(foundWord);
            return true;
        }
        return false;
    }

    /**
     * Add a definition to word. If definition didnt exists(name/dicttype/year is different) add this definition to word.
     * Otherwise(definition exists/word has no definition), check word for definition refresh, which means that it will
     * update current definitions
     * @param word requested word
     * @param language from which language
     * @param definition definition object to add to word
     * @return true - definition added/word updated, false - word/language not found, word not updated
     */
    public boolean addDefinitionForWord(String word, String language, Definition definition){
        if(!langs.containsKey(language)) return false;
        Word foundWord = searchWord(word, language);
        if(foundWord != null){
            if(!foundWord.getDefinitions().contains(definition)){
                foundWord.getDefinitions().add(definition);
                return true;
            }
            ArrayList<Definition> aux = new ArrayList<>();
            aux.add(definition);
            return checkWordForDefinitionRefresh(foundWord, aux);
        }
        return false;
    }

    /**
     * remove a definition from given dictionary from a given word
     * @param word requested word
     * @param language from which language
     * @param dictionary which dictionary
     * @return true - removed with success, false - word/language not found
     */
    public boolean removeDefinition(String word, String language, String dictionary){
        if(!langs.containsKey(language)) return false;
        Word foundWord = searchWord(word, language);
        Definition auxDefinition = new Definition(dictionary);
        if(foundWord != null){
            for(Definition d : foundWord.getDefinitions()){
                if(d.getDict().equals(dictionary)){
                    foundWord.getDefinitions().remove(d);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Translate a word fromLanguage toLanguage.
     * @param word word to translate
     * @param fromLanguage language from which is given word
     * @param toLanguage language to which we want to translate
     * @return translated word to given language
     */
    public String translateWord(String word, String fromLanguage, String toLanguage){
        Word foundWordFromLang = null;
        Word foundWordToLang = null;
        if(langs.containsKey(fromLanguage)) {
            foundWordFromLang = searchWord(word, fromLanguage);
        }
        if(langs.containsKey(toLanguage)){
            foundWordToLang = searchWord(word, toLanguage);
        }

        if(fromLanguage.equals("en") && !toLanguage.equals("en")){
            if(!langs.containsKey(toLanguage)) return word;
            if(foundWordToLang != null) {
                return foundWordToLang.getWord();
            } else return word;
        }
        else if(!fromLanguage.equals("en") && toLanguage.equals("en")){
            if(!langs.containsKey(fromLanguage)) return word;
            if(foundWordFromLang != null) {
                return foundWordFromLang.getWord_en();
            } else return word;
        }
        else if (!fromLanguage.equals("en")){
            if(!langs.containsKey(fromLanguage)) return word;
            if(!langs.containsKey(toLanguage)) return word;
            if(foundWordFromLang == null) return word;
            foundWordToLang = searchWord(foundWordFromLang.getWord_en(), toLanguage);

            if(foundWordToLang == null) {
                return word;
            }

            int index = foundWordFromLang.getSingular().indexOf(word);
            if(index < 0) {
                index = foundWordFromLang.getPlural().indexOf(word);
                if( index >= 0 ) {
                    if(!foundWordToLang.getPlural().isEmpty()) return foundWordToLang.getPlural().get(index);
                }
                return word;
            }
            else {
                return foundWordToLang.getSingular().get(index);
            }
        }
        return word;
    }

    /**
     * Translate a sentence fromLanguage toLanguage. Uses translateWord from every word in sentence. The given sentence
     * will be processed, all non-letter characters will be deleted, lowercase will be done, whitespaces will be deleted.
     * @param sentence sentence to translate
     * @param fromLanguage from language
     * @param toLanguage to language
     * @return translated sentence to language
     */
    public String translateSentence(String sentence, String fromLanguage, String toLanguage){
        String toTranslate = sentence.replaceAll("[^A-Za-z0-9 ÂâĂăÎîşŞţŢșȘțȚ]", "").trim().toLowerCase().replaceAll(" +", " ");
        String[] words = toTranslate.split(" ");
        String[] translated = new String[words.length];
        int i = 0;
        for(String word : words){
            translated[i] = translateWord(word, fromLanguage, toLanguage);
            i++;
        }

        return getSentenceFromArray(translated);
    }

    /**
     * Translate a sentence fromLanguage toLanguage and gives 3 different forms of translation. Uses translateSentence(String s, String fromL, String toL).
     * If there are no 3 translation variants of the sentence, only the possible variants will be provided.
     * @param sentence sentence to translate
     * @param fromLanguage from which language the translation takes place
     * @param toLanguage to which language the translation takes place
     * @return The list containing the translation variants of the sentence
     */
    public ArrayList<String> translateSentences(String sentence, String fromLanguage, String toLanguage){
        String firstVer = translateSentence(sentence, fromLanguage, toLanguage);
        String[] splitVer = firstVer.split(" ");
        ArrayList<Word> words = new ArrayList<>();
        for(String w : splitVer) {
            Word foundWord = searchWord(w, toLanguage);
            if (foundWord == null) words.add(new Word(w));
            else words.add(foundWord);
        }
        ArrayList<ArrayList<String>> wordVersions = new ArrayList<>(splitVer.length);
        int i = 0;
        for(Word w : words){
            wordVersions.add(new ArrayList<>());
            if(w.getType() == null || w.getType().equals("verb")){
                wordVersions.get(i).add(splitVer[i]);
            }
            else{
                if(w.getPlural().contains(splitVer[i])){
                    wordVersions.get(i).add(splitVer[i]);
                }
                else {
                    wordVersions.get(i).addAll(w.getSingular());
                    wordVersions.get(i).addAll(w.getSynonyms());
                }
            }
            i++;
        }
        ArrayList<String> res = new ArrayList<>();
        ArrayList<String> storage = new ArrayList<>();
        i = 0;
        while (i < 3){
            for(ArrayList<String> versions : wordVersions){
                int index = r.nextInt(versions.size());
                storage.add(versions.get(index));
                if(versions.size() != 1){
                    versions.remove(index);
                }
            }
            String[] arrayStorage = new String[storage.size()];
            storage.toArray(arrayStorage);
            String toAdd = getSentenceFromArray(arrayStorage);
            if(res.contains(toAdd)) {
                break;
            }
            else res.add(toAdd);
            storage.clear();
            i++;
        }

        return res;
    }

    /**
     * Get the list of definition from requested word
     * @param word requested word
     * @param language the language of the required word
     * @return List of definitions required word contain
     */
    public ArrayList<Definition> getDefinitionsForWord(String word, String language){
        this.sortWordsAndDefinitions();
        if(!langs.containsKey(language)) return new ArrayList<>();
        Word foundWord = searchWord(word, language);
        if(foundWord != null) {
            return foundWord.getDefinitions();
        }
        return new ArrayList<>();
    }

    /**
     * Export dictionary of a given language to a .json file
     * @param language requested language to export
     * @throws IOException if an I/O error occurs
     */
    public void exportDictionary(String language) throws IOException {
        this.sortWordsAndDefinitions();
        if(!langs.containsKey(language)) return;
        File outputDir = new File("dirs_out");
        if(!outputDir.exists()) outputDir.mkdir();
        File outputFile = new File("dirs_out/" + language + "_dict.json");
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        writer.write(gson.toJson(langs.get(language)));
        writer.close();
    }
}
