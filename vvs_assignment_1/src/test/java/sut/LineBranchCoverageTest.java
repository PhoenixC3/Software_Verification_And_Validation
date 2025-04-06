package sut;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

// Line and Branch Coverage for all public methods.
// Public methods: size, contains, get, put, longestPrefixOf, keys, keysWithPrefix, keysThatMatch

public class LineBranchCoverageTest {

    private TST<String> tst;

    @BeforeEach
    public void setUp() {
        tst = new TST<>();
    }

    // ======================================
    // Line Coverage: Covers size()
    // ======================================

    @Test
    public void testSizeInitiallyZero() {
        // Covers: return n; when n == 0
        assertEquals(0, tst.size()); 
    }

    @Test
    public void testPutAndSizeIncrements() {
        // Covers: n++ in put() when key is new
        // Covers: return n; when n > 0
        tst.put("cat", "meow");
        tst.put("dog", "bark");

        assertEquals(2, tst.size()); 
    }

    // ======================================
    // Line + Branch Coverage: TST.put() and TST.get()
    // ======================================

    @Test
    public void testPutOverwriteValue() {
        // Covers: path where key already exists, skips n++
        // Covers: (overwrite x.val)
        // Covers: get() traversal branches
        tst.put("cat", "meow");
        tst.put("cat", "purrr");

        assertEquals(1, tst.size()); // put avoids increment
        assertEquals("purrr", tst.get("cat")); // updated value fetched
    }

    @Test
    public void testGetReturnsCorrectValue() {
        // Covers: new insertion via put()
        // Covers: all traversal cases in get(Node, key, d): <, >, ==, leaf
        tst.put("bat", "fly");

        assertEquals("fly", tst.get("bat"));
        assertNull(tst.get("bad")); // x == null path in get()
    }

    @Test
    public void testGetNullKeyThrows() {
        // Covers: get(String) throws IllegalArgumentException on null
        assertThrows(IllegalArgumentException.class, () -> tst.get(null)); 
    }

    @Test
    public void testGetEmptyKeyThrows() {
        // Covers: get(String) throws on empty string
        assertThrows(IllegalArgumentException.class, () -> tst.get("")); 
    }

    // ======================================
    // Branch Coverage: TST.contains()
    // ======================================

    @Test
    public void testContainsKey() {
        // Covers: contains() true path
        // Covers: contains() false path
        tst.put("cow", "moo");

        assertTrue(tst.contains("cow"));
        assertFalse(tst.contains("sheep"));
    }

    @Test
    public void testContainsNullKeyThrows() {
        // Covers: null key check in contains()
        assertThrows(IllegalArgumentException.class, () -> tst.contains(null)); 
    }

    // ======================================
    // Line + Branch Coverage: longestPrefixOf()
    // ======================================

    @Test
    public void testLongestPrefixOf() {
        // Covers: valid match via middle branches
        // Covers: full match ending in x.val != null
        tst.put("car", "vehicle");
        tst.put("cart", "push");

        assertEquals("car", tst.longestPrefixOf("carbon")); // partial match
        assertEquals("cart", tst.longestPrefixOf("cartwheel")); // full match
    }

    @Test
    public void testLongestPrefixOfEmptyOrNull() {
        // Covers: null input throws
        // Covers: length == 0 returns null
        assertNull(tst.longestPrefixOf(""));
        assertThrows(IllegalArgumentException.class, () -> tst.longestPrefixOf(null)); 
    }

    // ======================================
    // Line Coverage: keys()
    // ======================================

    @Test
    public void testKeys() {
        // Covers: keys() traversal
        // Covers: full trie traversal and appending to queue
        tst.put("apple", "fruit");
        tst.put("banana", "fruit");
        tst.put("avocado", "fruit");

        List<String> keys = new ArrayList<>();
        tst.keys().forEach(keys::add);

        assertTrue(keys.contains("apple"));
        assertTrue(keys.contains("banana"));
        assertTrue(keys.contains("avocado"));
    }

    // ======================================
    // Branch Coverage: keysWithPrefix()
    // ======================================

    @Test
    public void testKeysWithPrefix() {
        // Covers: prefix match and subtree traversal
        // Covers: x.val != null adds prefix
        tst.put("app", "a");
        tst.put("apple", "b");
        tst.put("application", "c");
        tst.put("bat", "d");

        Iterable<String> results = tst.keysWithPrefix("app");
        List<String> list = new ArrayList<>();
        results.forEach(list::add);

        assertTrue(list.contains("app"));
        assertTrue(list.contains("apple"));
        assertTrue(list.contains("application"));
        assertFalse(list.contains("bat")); // not matching prefix
    }

    @Test
    public void testKeysWithPrefixNullThrows() {
        // Covers: null input check
        assertThrows(IllegalArgumentException.class, () -> tst.keysWithPrefix(null)); 
    }

    // ======================================
    // Line + Branch Coverage: keysThatMatch()
    // ======================================

    @Test
    public void testKeysThatMatch() {
        // Covers: all branching in wildcard logic
        // Covers: . as wildcard, mid match, left and right recursion
        tst.put("bad", "x");
        tst.put("bed", "y");
        tst.put("bid", "z");

        Iterable<String> matches = tst.keysThatMatch("b.d");
        List<String> matchList = new ArrayList<>();
        matches.forEach(matchList::add);
        
        assertEquals(3, matchList.size());
        assertTrue(matchList.contains("bad"));
        assertTrue(matchList.contains("bed"));
        assertTrue(matchList.contains("bid"));
    }
}
