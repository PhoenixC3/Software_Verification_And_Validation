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

// Trie State:
// Base Choice: Trie is empty.
// Other: Trie contains some keys.

// Key Presence:
// Base Choice: Key does not exist in the trie.
// Other: Key already exists in the trie.

// Key Lexicographic Order:
// Base Choice: Key is a typical key.
// Others: Key is the smallest or largest in lexicographic order.

public class BaseChoiceCoverageTest {

    private TST<String> tst;

    @BeforeEach
    void setUp() {
        tst = new TST<>();
    }

    // Trie is empty, key is new (Base Case)
    @Test
    public void testInsertIntoEmptyTrie() {
        tst.put("cat", "1");
        assertEquals("1", tst.get("cat"));
    }

    // Trie contains the key already
    public void testInsertExistingKey() {
        tst.put("cat", "1");
        tst.put("cat", "2");
        assertEquals("2", tst.get("cat"));
    }

    // Trie contains a prefix of the key
    @Test
    public void testInsertWithExistingPrefix() {
        tst.put("car", "1");
        tst.put("cart", "2");
        assertEquals("1", tst.get("car"));
        assertEquals("2", tst.get("cart"));
    }

    // Insert a typical key
    @Test
    public void testInsertTypicalKey() {
        tst.put("mango", "7");
        assertEquals("7", tst.get("mango"));
    }

    // Insert key with smallest lexicographic value
    @Test
    public void testInsertSmallestLexKey() {
        tst.put("ant", "1");
        tst.put("zebra", "2");
        assertEquals("1", tst.get("ant"));
    }

    // Insert key with largest lexicographic value
    @Test
    public void testInsertLargestLexKey() {
        tst.put("apple", "1");
        tst.put("zulu", "9");
        assertEquals("9", tst.get("zulu"));
    }
}
