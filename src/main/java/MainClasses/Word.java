package MainClasses;

import java.util.ArrayList;

public class Word implements Comparable{
    private String word;
    private String word_en;
    private String type;
    private ArrayList<String> singular;
    private ArrayList<String> plural;
    private ArrayList<Definition> definitions;

    public Word(String word) {
        this.word = word;
    }

    public Word(String word, String word_en, String type, ArrayList<String> singular, ArrayList<String> plural, ArrayList<Definition> definitions) {
        this.word = word;
        this.word_en = word_en;
        this.type = type;
        this.singular = singular;
        this.plural = plural;
        this.definitions = definitions;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWord_en() {
        return word_en;
    }

    public void setWord_en(String word_en) {
        this.word_en = word_en;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getSingular() {
        return singular;
    }

    public ArrayList<String> getPlural() {
        return plural;
    }

    public ArrayList<Definition> getDefinitions() {
        return definitions;
    }

    /**
     * Get synonyms of word
     * @return list of word's synonyms
     */
    public ArrayList<String> getSynonyms(){
        for(Definition d : this.getDefinitions()){
            if(d.getDictType().equals("synonyms")){
                return d.getText();
            }
        }
        return new ArrayList<>();
    }

    /**
     * Check if two words are identical.
     * @param o word to compare with
     * @return true - words are identical, false - otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Word word1)) return false;
        if(word1.singular.contains(this.getWord().toLowerCase())
                || word1.plural.contains(this.getWord().toLowerCase())) return true;
        return this.getWord().equalsIgnoreCase(word1.getWord().toLowerCase())
                || this.getWord().equalsIgnoreCase(word1.getWord_en().toLowerCase());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{word='").append(word).append('\'');
        sb.append(", word_en='").append(word_en).append("\'}");
        return sb.toString();
    }

    @Override
    public int compareTo(Object o) {
        Word thatWord = (Word)o;
        return this.getWord().compareTo(thatWord.getWord());
    }
}
