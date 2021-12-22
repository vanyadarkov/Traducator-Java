package MainClasses;

import java.util.ArrayList;

public class Definition implements Comparable{
    private String dict;
    private String dictType;
    private int year;
    private ArrayList<String> text;

    public String getDict() {
        return dict;
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public ArrayList<String> getText() {
        return text;
    }

    public Definition(String dict, String dictType, int year, ArrayList<String> text) {
        this.dict = dict;
        this.dictType = dictType;
        this.year = year;
        this.text = text;
    }

    public Definition(String dict) {
        this.dict = dict;
    }

    /**
     * Check if two dictionaries are identical. If they have same name - check their type, if same type - check year.
     * @param o dictionary to compare with
     * @return true - dictionaries are identical, false - otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Definition)) return false;
        Definition that = (Definition) o;
        if(this.getDict().equals(that.getDict())){
            if(this.getDictType() == that.getDictType()){
                return this.getYear() == that.getYear();
            }
        }
        return false;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Definitie = {");
        sb.append("dict='").append(dict).append('\'');
        sb.append(", dictType='").append(dictType).append('\'');
        sb.append(", year=").append(year);
        sb.append(", text=").append(text);
        sb.append("}");
        return sb.toString();
    }

    @Override
    public int compareTo(Object o) {
        Definition thatDef = (Definition)o;
        return this.getYear() - thatDef.getYear();
    }
}
