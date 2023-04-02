import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Class representing a hash table
 * @param <T> Type of values stored
 */
public class HashTable<T> {
    // Field that holds the hash table array
    private LinkedList<Node<T>>[] table;

    // Field containing the size of the hash table
    private int size = 0;

    /**
     * Constructor that creates a hash table with a default capacity of 500 to keep load factor relatively low
     * while maintaining a low space complexity
     */
    public HashTable(){
        table = new LinkedList[500];
    }

    /**
     * Constructor that creates a hash table with a given capacity
     * @param capacity of hash table
     */
    public HashTable(int capacity){
        // Throws exception if input is not valid
        if(capacity < 0)
            throw new IllegalArgumentException();
        table = new LinkedList[capacity];
    }

    /**
     * Method that returns the size of the hash table
     * @return size
     */
    public int size(){
        return size;
    }

    /**
     * Method that returns the value associated with the given key, essentially a wrapper for getNode
     * @param key given key
     * @return the value of the key if present
     */
    public T get(String key){
        return getNode(key).element;
    }

    /**
     * Protected helper method that returns the node with the given key, this provides enough acces for wordstat to be
     * maximally efficient
     * @param key given key
     * @return Node associated with key
     */
    protected Node<T> getNode(String key){
        // Retrieving hashed index
        int index = Math.abs(key.hashCode()%table.length);
        // Checks that the index is present in the table
        if(table[index] != null){
            // Iterates through the chain at the given index until the key is found
            for(Node<T> n : table[index]) {
                // If keys match return the element
                if (n.key.equals(key))
                    return n;
            }
        }
        // Only reachable if no such element is present in table
        throw new NoSuchElementException();
    }

    /**
     * Method that adds a new element to our hash table given a key and value associated
     * @param key given key to add
     * @param value given value of associated key
     */
    public void put(String key, T value){
        // Retrieving hashed index
        int index = Math.abs(key.hashCode()%table.length);
        // Checking if hashed index is already in use
        if(table[index] != null) {
            boolean duplicate = false;

            // Checks for duplicates and increments the nodes occurrences if they match
            for (Node<T> n : table[index]) {
                if (n.key.equals(key)) {
                    // Increment occurrences and change duplicate to prevent adding a second occurrence
                    duplicate = true;
                    n.occurrences++;
                }
            }
            // Checking if duplicate key
            if(!duplicate)
                // O(1) insertion if not a duplicate
                table[index].addFirst(new Node<>(key, value));
        }
        else {
            // Reachable if hashed index has not been used
            // Creates a new chain and adds the given value
            table[index] = new LinkedList<>();
            table[index].add(new Node<>(key, value));
        }
        // Increments size
        size++;
        updateLoadFactor();
    }

    /**
     * Method that removes an element of a given key from the table
     * @param key given key to remove
     * @return the element associated with removed key
     */
    public T remove(String key){
        // Retrieving hashed index
        int index = Math.abs(key.hashCode()%table.length);
        // Checking if hashed index is in use at all
        if(table[index] != null){
            // Parses through the chain at the hashed index
            for(Node<T> n : table[index]) {
                // If keys match remove from chain, decrement size, return the element associated with that key
                if (n.key.equals(key)) {
                    table[index].remove(n);
                    size--;
                    updateLoadFactor();
                    return n.element;
                }
            }
        }
        // Only accessible if hashed index is not in use or not in chain
        throw new NoSuchElementException();
    }

    /**
     * Private helper that updates the load factor of the hashtable and rehashes if needed
     */
    private void updateLoadFactor(){
        // Update load factor
        // Field containing the current load factor
        double load = (double) size / table.length;
        // If load factor at threshold or greater rehash
        if(load >= 1.0){
            // New temporary hash table with double the capacity of the first
            HashTable<T> reHash = new HashTable<>(table.length*2);
            // Parses the slots in the table
            for(LinkedList<Node<T>> l : table){
                // Ensures the node is non-null
                if(l != null) {
                    // Parses every chain
                    for (Node<T> n : l) {
                        // Rehashes every node
                        reHash.put(n.key, n.element);
                    }
                }
            }
            // Completes rehash
            table = reHash.table;
        }
    }

    /**
     * Private nested class representing a node, used to store key and element together in a chain in the table
     * @param <F> Type of element
     */
    protected static class Node<F> implements Comparable<Node<F>> {

        // Field that holds key
        private final String key;

        // Field that holds the element
        private final F element;

        // Field that holds how many times this key has been inputted, how duplicates are dealt with in this implementation
        private int occurrences = 1;

        // Constructor that creates a new node with given key and element
        public Node(String key, F element) {
            this.key = key;
            this.element = element;
        }

        /**
         * Occurrences getter
         * @return # of occurrences of Nodes key
         */
        public int getOccurrences() {
            return occurrences;
        }

        /**
         * Getter method for a node's key
         * @return Node's key
         */
        public String getKey() {
            return key;
        }

        @Override
        public int compareTo(Node<F> o1) {
            return Integer.compare(this.getOccurrences(), o1.getOccurrences());
        }
    }
}
