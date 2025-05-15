package sut;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

// Select and apply one Logic-based test coverage for method longestPrefixOf,
// justify your option

// Selected Coverage Criteria: PREDICATE COVERAGE (PC)
// It guarantees that each decision point is tested for both possible outcomes 
// (e.g., a null input vs. a valid string, an empty string vs. a non-empty string, traversing the trie vs. terminating early). 
// It exposes edge cases, like null queries, empty strings, and scenarios where no prefix matches. It helps validate correct 
// traversal logic in the while loop and that the substring computation is only reached with valid conditions.

// List of Predicates:
// (query == null)
// (query.length() == 0)
// (x != null && i < query.length())
// (c < x.c)
// (c > x.c)
// (x.val != null)

public class TestLogicBasedCoverage {
    private TST<String> tst;
    
    @Before
    public void setUp() {
        tst = new TST<>();
    }

    // Covers: (query == null)
    @Test(expected = IllegalArgumentException.class)
    public void lb1() {
        tst.longestPrefixOf(null);
    }

    // Covers: (query.length() == 0)
    @Test
    public void lb2() {
        assertNull(tst.longestPrefixOf(""));
    }

    // Covers: (query != null)
    // Covers: (query.length() != 0)
    // Covers: (x == null && i < query.length())
    @Test
    public void lb3() {
        assertEquals("", tst.longestPrefixOf("dog"));
    }

    // Covers: (query != null)
    // Covers: (query.length() != 0)
    // Covers: (x != null && i < query.length())
    // Covers: (c < x.c)
    @Test
    public void lb4() {
        tst.put("dog", "1");
        assertEquals("", tst.longestPrefixOf("cat"));
    }

    // Covers: (query != null)
    // Covers: (query.length() != 0)
    // Covers: (x != null && i < query.length())
    // Covers: (c > x.c)
    @Test
    public void lb5() {
        tst.put("dog", "1");
        assertEquals("", tst.longestPrefixOf("eel"));
    }

    // Covers: (query != null)
    // Covers: (query.length() != 0)
    // Covers: (x != null && i < query.length())
    // Covers: (x.val == null)
    @Test
    public void lb6() {
        tst.put("car", "1");
        assertEquals("", tst.longestPrefixOf("c"));
    }

    // Covers: (query != null)
    // Covers: (query.length() != 0)
    // Covers: (x != null && i < query.length())
    // Covers: (x.val != null)
    @Test
    public void lb7() {
        tst.put("h", "1");
        assertEquals("h", tst.longestPrefixOf("h"));
    }
}
