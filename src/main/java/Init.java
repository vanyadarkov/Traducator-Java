import Administration.Administration;
import Administration.Test;
import MainClasses.Word;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Init {
    public static void main(String[] args) throws IOException {
        File folder;
        FileReader fileReader = null;
        JsonReader jsonReader = null;
        Administration admin = new Administration();
        Gson gson = new Gson();

        /*
         * Reading from directory where dictionaries are
         */
        try{
            folder = new File("dirs");
            File[] listOfFiles = folder.listFiles();
            assert listOfFiles != null;
            for (File file : listOfFiles) {
                String langName = file.getName().split("_")[0];
                fileReader = new FileReader(file);
                jsonReader = new JsonReader(fileReader);
                ArrayList<Word> words = gson.fromJson(jsonReader, new TypeToken<ArrayList<Word>>(){}.getType());
                ArrayList<Word> foundWords = admin.langs.get(langName);
                if(foundWords == null){
                    admin.langs.put(langName, words);
                }
                else{
                    admin.refreshLanguageDictionary(foundWords, words, langName);
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            if(fileReader != null) fileReader.close();
            if(jsonReader != null) jsonReader.close();
        }
        /*
        *   Testing all implemented methods
        */
        Test test = new Test(admin);
        test.testAllMethods();

        /*
        * Test exportDictionary
        */
        try{
            admin.exportDictionary("fr");
            admin.exportDictionary("ro");
            admin.exportDictionary("ru");
            admin.exportDictionary("test");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
