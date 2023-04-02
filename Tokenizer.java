import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Class that parses through an input stream of characters and normalizes them
 * @author Maximilian L. Schulten
 */
public class Tokenizer {

    // Holds our array list of words
    private ArrayList<String> words = new ArrayList<>();

    /**
     * Constructor that parses all normalized words in a .txt file and
     * adds them to an array list
     * @param file directory
     */
    public Tokenizer(String file) throws IOException {
        // Initializing a new FileReader to parse the .txt file
        FileReader reader = new FileReader(file);
        // Initializing a new StringBuilder used to add the normalized words to the array list
        StringBuilder sb = new StringBuilder();
        // Parses through the file character by character while we have not reached the end of the file
        while(reader.ready()){
            // Saves the current character
            char save = (char)reader.read();
            // If letter add its lowercase version
            if(Character.isLetter(save))
                sb.append(Character.toLowerCase(save));
            // If not an abridging character and the string builder is not empty add the word and and create a new string builder
            else if (save != '\'' && save != '-' && !sb.toString().equals("")) {
                words.add(sb.toString());
                sb = new StringBuilder();
            }
        }
        // Adding any final words
        words.add(sb.toString());
    }

    /**
     * Constructor that parses all normalized words in an array and
     * adds them to an array list
     * @param text String array
     */
    public Tokenizer(String[] text) throws IOException {
        // Parses through all strings in passed in array
        for(String string : text){
            // Creates a new StringBuilder to hold normalized words
            StringBuilder sb = new StringBuilder();
            // Creates a new BufferedReader by passing in the current string through a StringReader
            BufferedReader reader = new BufferedReader(new StringReader(string));
            // Parses through all letters in a string
            for(int i = 0; i < string.length()+1; i++){
                // Saves the current character
                char save = (char)reader.read();
                // If letter add its lowercase version
                if(Character.isLetter(save))
                    sb.append(Character.toLowerCase(save));
                    // If not an abridging character and the string builder is not empty add the word and and create a new string builder
                else if (save != '\'' && save != '-' && !sb.toString().equals("")) {
                    words.add(sb.toString());
                    sb = new StringBuilder();
                }
            }
        }
    }

    /**
     * Method that returns the normalized word list
     * @return words
     */
    public ArrayList<String> wordList() {
        return words;
    }
}
