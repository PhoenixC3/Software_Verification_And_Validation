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

    // Line Coverage: size() : n == 0
    @Test
    public void testSizeInitiallyZero() {
        assertEquals(0, tst.size()); 
    }

    // Line Coverage: size() : n != 0
    @Test
    public void testPutAndSizeIncrements() {
        tst.put("cat", "1");
        tst.put("dog", "2");

        assertEquals(2, tst.size()); 
    }

    // Line + Branch Coverage: put() and get() : overwrite key and get() value
    @Test
    public void testPutOverwriteValue() {
        tst.put("cat", "1");
        tst.put("cat", "2");

        assertEquals(1, tst.size());
        assertEquals("2", tst.get("cat"));
    }

    // Line + Branch Coverage: put() and get() : put() new key and get() value, and null value
    @Test
    public void testGetReturnsCorrectValue() {
        tst.put("bat", "1");

        assertEquals("1", tst.get("bat"));
        assertNull(tst.get("bad"));
    }

    // get() : null key
    @Test
    public void testGetNullKeyThrows() {
        assertThrows(IllegalArgumentException.class, () -> tst.get(null)); 
    }

    // get() : empty key
    @Test
    public void testGetEmptyKeyThrows() {
        assertThrows(IllegalArgumentException.class, () -> tst.get("")); 
    }

    // Branch Coverage: contains() : true and false paths
    @Test
    public void testContainsKey() {
        tst.put("cow", "1");

        assertTrue(tst.contains("cow"));
        assertFalse(tst.contains("sheep"));
    }

    // contains() : null key
    @Test
    public void testContainsNullKeyThrows() {
        assertThrows(IllegalArgumentException.class, () -> tst.contains(null)); 
    }

    // Line + Branch Coverage: longestPrefixOf() : partial match and full match
    @Test
    public void testLongestPrefixOf() {
        tst.put("car", "1");
        tst.put("cart", "2");
        tst.put("cat", "3");

        assertEquals("car", tst.longestPrefixOf("cargo"));
        assertEquals("cat", tst.longestPrefixOf("cat"));
    }

    // null input and empty string
    @Test
    public void testLongestPrefixOfEmptyOrNull() {
        assertNull(tst.longestPrefixOf(""));
        assertThrows(IllegalArgumentException.class, () -> tst.longestPrefixOf(null)); 
    }

    // Line Coverage: keys() : full trie traversal and appending to queue
    @Test
    public void testKeys() {
        tst.put("apple", "1");
        tst.put("banana", "2");
        tst.put("avocado", "3");

        List<String> keys = new ArrayList<>();
        tst.keys().forEach(keys::add);

        assertTrue(keys.contains("apple"));
        assertTrue(keys.contains("banana"));
        assertTrue(keys.contains("avocado"));
    }

    // Branch Coverage: keysWithPrefix() : prefix match and subtree traversal, add prefix
    @Test
    public void testKeysWithPrefix() {
        tst.put("app", "1");
        tst.put("apple", "2");
        tst.put("application", "3");
        tst.put("bat", "4");

        Iterable<String> results = tst.keysWithPrefix("app");
        List<String> list = new ArrayList<>();
        results.forEach(list::add);

        assertTrue(list.contains("app"));
        assertTrue(list.contains("apple"));
        assertTrue(list.contains("application"));
        assertFalse(list.contains("bat"));
    }

    // keysWithPrefix() : null input
    @Test
    public void testKeysWithPrefixNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> tst.keysWithPrefix(null)); 
    }

    // Line + Branch Coverage: keysThatMatch() : exact match, wildcard match, and no match
    @Test
    public void testKeysThatMatchExact() {
        tst.put("dog", "1");

        List<String> result = new ArrayList<>();
        for (String k : tst.keysThatMatch("dog")) {
            result.add(k);
        }

        assertEquals(List.of("dog"), result);
    }

    @Test
    public void testKeysThatMatchWithWildcard() {
        tst.put("dog", "1");
        tst.put("dig", "2");
        tst.put("dug", "3");

        List<String> matches = new ArrayList<>();
        for (String k : tst.keysThatMatch("d.g")) {
            matches.add(k);
        }

        assertTrue(matches.contains("dog"));
        assertTrue(matches.contains("dig"));
        assertTrue(matches.contains("dug"));
        assertEquals(3, matches.size());
    }

    @Test
    public void testKeysThatMatchNoMatch() {
        tst.put("apple", "1");

        List<String> result = new ArrayList<>();
        for (String k : tst.keysThatMatch("z...")) {
            result.add(k);
        }
        
        assertTrue(result.isEmpty());
    }
}
