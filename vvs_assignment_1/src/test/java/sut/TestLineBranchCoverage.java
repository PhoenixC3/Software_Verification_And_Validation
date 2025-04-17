package sut;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Line and Branch Coverage for all public methods
// Public Methods: size(), put(), get(), contains(), longestPrefixOf(), keys(), keysWithPrefix(), keysThatMatch()

public class TestLineBranchCoverage {

    private TST<String> tst;

    @Before
    public void setUp() {
        tst = new TST<>();
    }

    @Test
    public void testSizeInitiallyZero() {
        assertEquals(0, tst.size()); 
    }

    @Test
    public void testPutAndSizeIncrements() {
        tst.put("cat", "1");
        tst.put("dog", "2");
        assertEquals(2, tst.size()); 
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullKeyThrows() {
        tst.put(null, "1");
    }

    @Test
    public void testPutOverwriteValue() {
        tst.put("cat", "1");
        tst.put("cat", "2");
        assertEquals(1, tst.size());
        assertEquals("2", tst.get("cat"));
    }

    @Test
    public void testGetReturnsCorrectValue() {
        tst.put("bat", "1");
        assertEquals("1", tst.get("bat"));
        assertNull(tst.get("bad"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNullKeyThrows() {
        tst.get(null); 
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEmptyKeyThrows() {
        tst.get("");
    }

    @Test
    public void testContainsKey() {
        tst.put("cow", "1");
        assertTrue(tst.contains("cow"));
        assertFalse(tst.contains("sheep"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsNullKeyThrows() {
        tst.contains(null);
    }

    @Test
    public void testLongestPrefixOf() {
        tst.put("car", "1");
        tst.put("cart", "2");
        tst.put("cat", "3");
        assertEquals("car", tst.longestPrefixOf("cargo"));
        assertEquals("cat", tst.longestPrefixOf("cat"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLongestPrefixOfEmptyOrNull() {
        assertNull(tst.longestPrefixOf(""));
        tst.longestPrefixOf(null);
    }

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

    @Test(expected = IllegalArgumentException.class)
    public void testKeysWithPrefixNullThrows() {
        tst.keysWithPrefix(null);
    }

    @Test
    public void testKeysWithPrefixEmptyThrows() {
        assertEquals(Collections.emptyList(), tst.keysWithPrefix(""));
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

    @Test
    public void testKeysThatMatchPatternMidBranch() {
        tst.put("rate", "1");
        List<String> result = new ArrayList<>();
        for (String s : tst.keysThatMatch("ra.e")) {
            result.add(s);
        }
        assertTrue(result.contains("rate"));
    }
}