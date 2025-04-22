package sut;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Line and Branch Coverage for all public methods.
// Public Methods: size(), put(), get(), contains(), longestPrefixOf(), keys(), keysWithPrefix(), keysThatMatch()

public class TestLineBranchCoverage {

    private TST<String> tst;

    @Before
    public void setUp() {
        tst = new TST<>();
    }

    // ----------------------------
    // LINE COVERAGE TESTS
    // ----------------------------

    // --- size() ---
    // Gets the size of the trie
    @Test
    public void testSizeInitiallyZero() {
        assertEquals(0, tst.size());
    }

    // --- put() ---
    // Insertion of keys increases size
    @Test
    public void testPutAndSizeIncrements() {
        tst.put("cat", "1");
        tst.put("dog", "2");
        assertEquals(2, tst.size());
    }

    // Overwrites existing value but does not change size
    @Test
    public void testPutOverwriteValue() {
        tst.put("cat", "1");
        tst.put("cat", "2");
        assertEquals(1, tst.size());
        assertEquals("2", tst.get("cat"));
    }

    // --- get() ---
    // Retrieves correct value if exists
    @Test
    public void testGetReturnsCorrectValue() {
        tst.put("bat", "1");
        assertEquals("1", tst.get("bat"));
        assertNull(tst.get("bad"));
    }

    // --- contains() ---
    // Checks if key exists in the trie
    @Test
    public void testContainsKey() {
        tst.put("cow", "1");
        assertTrue(tst.contains("cow"));
        assertFalse(tst.contains("sheep"));
    }

    // --- longestPrefixOf() ---
    // Finds the longest prefix that matches the query
    @Test
    public void testLongestPrefixOf() {
        tst.put("car", "1");
        tst.put("cart", "2");
        tst.put("cat", "3");
        assertEquals("car", tst.longestPrefixOf("cargo"));
        assertEquals("cat", tst.longestPrefixOf("cat"));
    }

    // No prefix matches
    @Test
    public void testLongestPrefixOfNoMatch() {
        tst.put("dog", "1");
        tst.put("deer", "2");
        tst.put("deal", "3");
        assertEquals("", tst.longestPrefixOf("cat"));
    }

    // --- keys() ---
    // Collects all keys from the trie
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

    // --- keysWithPrefix() ---
    // Returns keys with the specified prefix
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

    // --- keysThatMatch() ---
    // Matches keys with the given wildcard pattern
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

    // No matches with pattern
    @Test
    public void testKeysThatMatchNoMatch() {
        tst.put("apple", "1");
        List<String> result = new ArrayList<>();
        for (String k : tst.keysThatMatch("z...")) {
            result.add(k);
        }
        assertTrue(result.isEmpty());
    }

    // Matches pattern at mid-branch of trie
    @Test
    public void testKeysThatMatchPatternMidBranch() {
        tst.put("rate", "1");
        List<String> result = new ArrayList<>();
        for (String s : tst.keysThatMatch("ra.e")) {
            result.add(s);
        }
        assertTrue(result.contains("rate"));
    }

    // ----------------------------
    // BRANCH COVERAGE TESTS
    // ----------------------------

    // --- put() ---
    // Null key in put() method
    @Test(expected = IllegalArgumentException.class)
    public void testPutNullKeyThrows() {
        tst.put(null, "1");
    }

    // --- get() ---
    // Null key in get() method
    @Test(expected = IllegalArgumentException.class)
    public void testGetNullKeyThrows() {
        tst.get(null);
    }

    // Empty key in get()
    @Test(expected = IllegalArgumentException.class)
    public void testGetEmptyKeyThrows() {
        tst.get("");
    }

    // --- contains() ---
    // Null key in contains()
    @Test(expected = IllegalArgumentException.class)
    public void testContainsNullKeyThrows() {
        tst.contains(null);
    }

    // --- longestPrefixOf() ---
    // Null query in longestPrefixOf()
    @Test(expected = IllegalArgumentException.class)
    public void testLongestPrefixOfNull() {
        tst.longestPrefixOf(null);
    }

    // Empty query in longestPrefixOf()
    @Test
    public void testLongestPrefixOfEmpty() {
        assertNull(tst.longestPrefixOf(""));
    }

    // Prefix exists, but no match
    @Test
    public void testLongestPrefixOfExistsButNoMatch() {
        tst.put("abc", "1");
        assertEquals("", tst.longestPrefixOf("ab"));
    }

    // Empty trie condition in longestPrefixOf()
    @Test
    public void testLongestPrefixOfWithEmptyTrie() {
        assertEquals("", tst.longestPrefixOf("anything"));
    }

    // --- keysWithPrefix() ---
    // Null prefix in keysWithPrefix()
    @Test(expected = IllegalArgumentException.class)
    public void testKeysWithPrefixNullThrows() {
        tst.keysWithPrefix(null);
    }

    // Empty prefix in keysWithPrefix()
    @Test
    public void testKeysWithPrefixEmptyThrows() {
        assertEquals(Collections.emptyList(), tst.keysWithPrefix(""));
    }
}