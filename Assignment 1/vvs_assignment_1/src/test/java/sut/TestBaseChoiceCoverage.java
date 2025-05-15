package sut;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

// For method put, include a test set based of Input State Partitioning, namely
// Base Choice Coverage, using the following characteristics:

// 1. Trie already includes the new key - No
// 2. Trie already includes some new key prefix - No
// 3. Trie is empty - No
// 4. The new key is the smallest/largest/typical key (in lexicographic terms) - Typical

// Base Choice - No, No, No, Typical

public class TestBaseChoiceCoverage {

    private TST<String> tst;

    @Before
    public void setUp() {
        tst = new TST<>();
    }

    // No, No, No, Typical
    @Test
    public void testBaseChoice() {
        tst.put("apple", "1");
        tst.put("mango", "2");
        assertEquals("2", tst.get("mango"));
    }

    // Yes, No, No, Typical
    @Test
    public void testIncludesNewKey() {
        tst.put("mango", "1");
        tst.put("mango", "2");
        assertEquals("2", tst.get("mango"));
    }

    // No, Yes, No, Typical
    @Test
    public void testIncludesNewPrefix() {
        tst.put("apple", "1");
        tst.put("apricot", "2");
        assertEquals("1", tst.get("apple"));
        assertEquals("2", tst.get("apricot"));
    }

    // No, No, Yes, Typical
    @Test
    public void testEmpty() {
        tst.put("banana", "1");
        assertEquals("1", tst.get("banana"));
    }

    // No, No, No, Smallest
    @Test
    public void testSmallest() {
        tst.put("b", "1");
        tst.put("a", "2");
        assertEquals("2", tst.get("a"));
    }

    // No, No, No, Largest
    @Test
    public void testLargest() {
        tst.put("c", "3");
        tst.put("zzzzzzz", "1");
        assertEquals("1", tst.get("zzzzzzz"));
    }
}
