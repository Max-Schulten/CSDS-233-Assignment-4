import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;

/**
 * Class that reads a text file or array of strings and gives the user access to various statistics
 */
public class WordStat {

    // Field that holds a hashtable of words
    private HashTable<String> table;

    // Field that holds sorted nodes in order of occurrences
    private ArrayList<HashTable.Node<String>> sortedNodes = new ArrayList<>();

    // Field that has the ranks of each word as a hashtable where the element is the key's rank
    private HashTable<Integer> rankedTable;

    // Field that holds the tokenizer used to parse through the input
    private Tokenizer tokenizer;

    /**
     * Constructor that initializes appropriate statistics
     * @param file File path of a txt file
     * @throws IOException Thrown in the event that tokenizer cannot read the file
     */
    public WordStat(String file) throws IOException {
        // Calling private helper to initialize
        initialize(new Tokenizer(file));
    }

    /**
     * Constructor that initializes appropriate statistics
     * @param text Array of string to parse
     * @throws IOException Thrown in the event that tokenizer cannot read the array
     */
    public WordStat(String[] text) throws IOException {
        // Calling private helper to initialize
        initialize(new Tokenizer(text));
    }

    /**
     * Method that returns the number of times the word is used
     * @param word the word to be searched for
     * @return int # of times the word occurs
     */
    public int wordCount(String word){
        // Used to catch a non-existent word
        try {
            // Returning the number of occurrences
            return table.getNode(word).getOccurrences();
        } catch (NoSuchElementException e){
            // Return 0 if the word doesn't appear
            return 0;
        }
    }

    /**
     * Method that returns the "rank" of the word in terms of how often it occurs
     * @param word the word to find the "rank" of
     * @return the word's "rank"
     */
    public int wordRank(String word){
        // Returns the rank of the word
        return rankedTable.get(word);
    }

    /**
     * Method that returns the k most common words
     * @param k number of words to return
     * @return Array in descending order of the k most common words
     */
    public String[] mostCommonWords(int k){
        // Checking to ensure parameter is valid
        if(k < 0)
            // If not throw exception
            throw new IllegalArgumentException();
        // If k is too large change to the max
        if(k > sortedNodes.size())
            k = sortedNodes.size();
        // Local variable to index the return array
        int index = 0;
        // Return array
        String[] temp = new String[k];
        // Parses through the sorted last k sorted nodes
        for(int i = sortedNodes.size()-1; i > sortedNodes.size()-1-k; i--){
            // Adds the nodes to the array
            temp[index] = sortedNodes.get(i).getKey();
            // Increments the index of the return array
            index++;
        }
        // Return the array
        return temp;
    }

    /**
     * Method that returns the k least common words
     * @param k number of words to return
     * @return Array in descending order of the k least common words
     */
    public String[] leastCommonWords(int k){
        // Checks for valid k value
        if(k < 0)
            // If not valid throw exception
            throw new IllegalArgumentException();
        // If k is too large make it the number of distinct words
        if(k > sortedNodes.size())
            k = sortedNodes.size();
        // Return array
        String[] temp = new String[k];
        // Index of the return array
        int index = 0;
        // Parses the sorted nodes in order
        for(int i = 0; i < k; i++){
            // Adds every distinct word in order
            temp[index] = sortedNodes.get(i).getKey();
            // Increments return array index
            index++;
        }
        // Return the array
        return temp;
    }

    /**
     * Returns the most common words following or preceding the first instance of the base word in the input
     * @param k number of words to return
     * @param baseWord word to parse for
     * @param precede boolean representing if to precede or follow the base word
     * @return Array of the k most common collocations
     */
    public String[] mostCommonCollocations(int k, String baseWord, boolean precede){
        // Creating new hash-table, array list, and node list
        HashTable<String> table = new HashTable<>();
        ArrayList<String> list = new ArrayList<>();
        ArrayList<HashTable.Node<String>> nodeList = new ArrayList<>();
        // Boolean to check if we have seen the base word
        boolean add = false;
        // Parses the words in the input
        for(String word : tokenizer.wordList()){
            // If precede is true add until we reach the base word
            if(precede){
                if(!word.equals(baseWord)){
                    // Add the word to the hashtable and list
                    table.put(word, word);
                    list.add(word);
                }
                else
                    break;
            // If precede is false add after we have seen the base word
            } else {
                if(add){
                    // Add the word to the hashtable and list
                    table.put(word, word);
                    list.add(word);
                }
                // Baseword found
                if(word.equals(baseWord)){
                    add = true;
                }

            }
        }
        // Parse the words after the base
        for(String word : list) {
            // Check if the word has already appeared
            boolean dupe = false;
            // Parses the hashtable to make sure it isn't duplicated
            for(HashTable.Node<String> n : nodeList) {
                if(n.getKey().equals(word)) {
                    dupe = true;
                    break;
                }
            }
            if(!dupe)
                // Add the word to the node list
                nodeList.add(table.getNode(word));
        }
        // Sorting the node list in ascending order
        Collections.sort(nodeList);
        // Indexing return array
        int index = 0;
        // Make sure k is not too large
        if(k > nodeList.size())
            k = nodeList.size();
        // Instantiate return array
        String[] returnArray = new String[k];
        // Parses the node list
        for(int i = nodeList.size()-1; i >= nodeList.size()-k; i--){
            // Add the word to the array
            returnArray[index] = nodeList.get(i).getKey();
            // increment index
            index++;
        }

        // Return the array
        return returnArray;
    }


    /**
     * Private helper that initializes the appropriate fields
     * @param tokenizer tokenizer based on what constructor is used
     */
    private void initialize(Tokenizer tokenizer){
        // Initializing tokenizer
        this.tokenizer = tokenizer;
        // Initializing the hashtable
        this.table = new HashTable<>();
        // Puts all words in the hashtable
        for(String word : this.tokenizer.wordList()){
            table.put(word, word);
        }
        // Adding all unique words to the sorted nodes
        for(String word : this.tokenizer.wordList()) {
            // Checking duplicate
            boolean dupe = false;
            // Parses all nodes
            for(HashTable.Node<String> n : sortedNodes) {
                // If keys are equivalent dupe and do not add
                if(n.getKey().equals(word)) {
                    dupe = true;
                    break;
                }
            }
            if(!dupe)
                // Adding the word if unique
                sortedNodes.add(table.getNode(word));
        }
        // Sort the node list
        Collections.sort(sortedNodes);
        // Initializing our ranked table
        rankedTable = new HashTable<>();
        // Rank variable
        int rank = 0;
        // Holds last occurrence
        int lastOccurrences = 0;
        // Amount of ranks to add
        int toAdd = 1;
        // Parses the sorted nodes in reverse
        for (int i = sortedNodes.size()-1; i >= 0; i--) {
            // If the occurrences are the same increment
            if (sortedNodes.get(i).getOccurrences() == lastOccurrences) {
                toAdd++;
            // If we reach a unique # of occurrences rank it
            } else if (sortedNodes.get(i).getOccurrences() != lastOccurrences) {
                rank = toAdd + rank;
                toAdd = 1;
            }
            // Add them to the ranked hashtable
            rankedTable.put(sortedNodes.get(i).getKey(), rank);
            // Save last occurrences
            lastOccurrences = sortedNodes.get(i).getOccurrences();
        }
    }
}
