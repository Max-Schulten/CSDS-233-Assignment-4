import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class Assignment4Tester {

    @Test
    public void testTokenizer() throws IOException {
        /*
        Passing in text file test.txt that reads:

        hello thIs is A tEst.
         I hope! this is. working correctly?
        My gramMar isn't ver\y good-
         */
        Tokenizer t = new Tokenizer("/Users/maximilianschulten/IdeaProjects/Assignment 4/src/test.txt");

        // Creating a string of all words in our new word list thereby also texting the wordList() method
        StringBuilder s = new StringBuilder();
        for(String word:t.wordList()){
            s.append(word);
        }


        assertEquals("hellothisisatestihopethisisworkingcorrectlymygrammarisntverygood",s.toString());

        /*
        Passing in an array of strings that read:

        hello thIs is A tEst.
         I hope! this is. working correctly?
        My gramMar isn't ver\y good-
         */
        Tokenizer t1 = new Tokenizer(new String[]{"hello thIs is A tEst.", " I hope! this is. working correctly?", "My gramMar isn't ver\\y good-"});

        // Creating a string of all words in our new word list thereby also texting the wordList() method
        StringBuilder s1 = new StringBuilder();
        for(String word:t.wordList()){
            s1.append(word);
        }

        assertEquals("hellothisisatestihopethisisworkingcorrectlymygrammarisntverygood",s1.toString());
    }

    @Test
    public void testHashTable() {
        HashTable<Integer> table = new HashTable<>();

        // Add some key-value pairs to the table
        table.put("Alice", 25);
        table.put("Bob", 30);
        table.put("Charlie", 35);

        // Retrieve the values and check that they're correct
        assertEquals(25, table.get("Alice"));
        assertEquals(30, table.get("Bob"));
        assertEquals(35, table.get("Charlie"));

        // Check the size is correct after adding
        assertEquals(3, table.size());

        // Check removal and size is correct
        table.remove("Bob");
        assertThrows(NoSuchElementException.class, () -> table.get("Bob"));
        assertEquals(2, table.size());

        // Checking that exception is appropriately thrown
        assertThrows(NoSuchElementException.class, () -> table.remove("John"));

        // Checking that exception is appropriately thrown
        assertThrows(NoSuchElementException.class, () -> table.get("John"));

        // Checking the rehash
        HashTable<Integer> overload = new HashTable<>(2);

        overload.put("Jeff", 3);
        // Here the load factor reaches 1, meaning we must rehash
        overload.put("John", 1);

        // It is difficult to test that the rehash has worked without accessing the internals of HashTable
        // However when tested before submission the rehashing worked as intended as size was doubled eachtime
        assertEquals(1, overload.get("John"));
        assertEquals(2, overload.size());

    }

    @Test
    public void testWordStat() throws IOException {
        WordStat ws = new WordStat("/Users/maximilianschulten/IdeaProjects/Assignment 4/src/wordstat.txt");

        /* WordStat.txt:
        hello test this is a test and I am testing the wordstat method this is a test hello
        hello hi test
        yes this, is a test
        there. hi it's me
        test test test test tester?


        Most common word: test (9 occurences)
         */

        assertEquals(1, ws.wordRank("test"));
        assertEquals(9, ws.wordCount("test"));
        assertEquals("test", ws.mostCommonWords(1)[0]);
        assertEquals(2, ws.wordCount("hi"));
        assertEquals("and", ws.leastCommonWords(3)[0]);
        assertEquals("i", ws.leastCommonWords(3)[1]);
        assertEquals(7, ws.wordRank("and"));
        assertEquals(7, ws.wordRank("i"));
        assertEquals("test", ws.mostCommonCollocations(1, "me", false)[0]);
        assertEquals("test", ws.mostCommonCollocations(1, "and", true)[0]);


        WordStat ws1 = new WordStat(new String[]{"hello test this is a test and I am testing the wordstat method this is a test hello", "hello hi test\n" +
                "        yes this, is a test\n" +
                "        there. hi it's me\n" +
                "        test test test test tester?"});

        /* WordStat.txt:
        hello test this is a test and I am testing the wordstat method this is a test hello
        hello hi test
        yes this, is a test
        there. hi it's me
        test test test test tester?


        Most common word: test (9 occurences)
         */

        assertEquals(1, ws1.wordRank("test"));
        assertEquals(9, ws1.wordCount("test"));
        assertEquals("test", ws1.mostCommonWords(1)[0]);
        assertEquals(2, ws1.wordCount("hi"));
        assertEquals("and", ws1.leastCommonWords(3)[0]);
        assertEquals("i", ws1.leastCommonWords(3)[1]);
        assertEquals(7, ws1.wordRank("and"));
        assertEquals(7, ws1.wordRank("i"));
        assertEquals("test", ws1.mostCommonCollocations(1, "me", false)[0]);
        assertEquals("test", ws1.mostCommonCollocations(1, "and", true)[0]);
    }
}
