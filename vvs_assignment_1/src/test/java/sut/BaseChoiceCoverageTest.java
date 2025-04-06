package sut;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

// For method put, include a test set based of Input State Partitioning, namely
// Base Choice Coverage, using the following characteristics:
// 1. Trie already includes the new key
// 2. Trie already includes some new key prefix
// 3. Trie is empty
// 4. The new key is the smallest/largest/a typical key (in lexicographic terms)

public class BaseChoiceCoverageTest {

    private TST<String> tst;

    @BeforeEach
    void setUp() {
        tst = new TST<>();
        tst.put("apple", "fruit1");
        tst.put("banana", "fruit2");
    }

    // Base case: insert typical key into non-empty trie, not already present
    @Test
    void baseCase_typicalKeyNotPresent() {
        tst.put("mango", "fruit3");
        assertEquals("fruit3", tst.get("mango"));
    }

    // Variation 1: Key already exists
    @Test
    void trieAlreadyIncludesNewKey() {
        tst.put("apple", "fruitX"); // override
        assertEquals("fruitX", tst.get("apple"));
    }

    // Variation 2: Trie already includes key prefix
    @Test
    void trieIncludesSomePrefixOfNewKey() {
        tst.put("ban", "prefix1");
        tst.put("banana", "updatedBanana");
        assertEquals("updatedBanana", tst.get("banana"));
    }

    // Variation 3: Trie is empty
    @Test
    void trieIsInitiallyEmpty() {
        TST<String> emptyTrie = new TST<>();
        emptyTrie.put("grape", "fruit4");
        assertEquals("fruit4", emptyTrie.get("grape"));
    }

    // Variation 4a: New key is smallest in lexicographic order
    @Test
    void newKeyIsSmallest() {
        tst.put("aardvark", "smallest");
        assertEquals("smallest", tst.get("aardvark"));
    }

    // Variation 4b: New key is largest in lexicographic order
    @Test
    void newKeyIsLargest() {
        tst.put("zucchini", "largest");
        assertEquals("largest", tst.get("zucchini"));
    }

    // Variation 4c: New key is another typical key
    @Test
    void newKeyIsTypicalAgain() {
        tst.put("melon", "typical2");
        assertEquals("typical2", tst.get("melon"));
    }
}
