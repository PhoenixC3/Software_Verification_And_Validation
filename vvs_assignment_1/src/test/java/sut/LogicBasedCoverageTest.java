package sut;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

// Select and apply one Logic-based test coverage for method longestPrefixOf,
// justify your option

// Selected Coverage Criteria: PREDICATE COVERAGE (PC)
// Well-suited due to the logic-heavy control flow in longestPrefixOf
// Sufficient to uncover common faults (null/empty handling, branching errors, prefix logic)

// List of Predicates:
// (query == null)
// (query.length() == 0)
// (x != null && i < query.length())
// (c < x.c)
// (c > x.c)
// (x.val != null)

public class LogicBasedCoverageTest {
    private TST<String> tst;
    
    @BeforeEach
    void setUp() {
        tst = new TST<>();
    }

    // Covers: (query == null)
    @Test
    public void testNullQuery_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> tst.longestPrefixOf(null));
    }

    // Covers: (query.length() == 0)
    @Test
    public void testEmptyQuery_returnsNull() {
        assertNull(tst.longestPrefixOf(""));
    }

    // Covers: (x != null && i < query.length())
    // Covers: (c < x.c)
    // Covers: (c > x.c)
    @Test
    public void testNoMatchingPrefix_returnsEmptyString() {
        tst.put("dog", "1");
        assertEquals("", tst.longestPrefixOf("cat"));
    }

    // Covers: (x != null && i < query.length())
    // Covers: (c < x.c)
    // Covers: (c > x.c)
    // Covers: (x.val != null)
    @Test
    public void testPartialMatch_returnsCorrectPrefix() {
        tst.put("car", "1");
        tst.put("cart", "2");
        tst.put("carry", "3");
        assertEquals("cart", tst.longestPrefixOf("cartoon"));
    }

    // Covers: (x != null && i < query.length())
    // Covers: (x.val != null)
    @Test
    public void testFullMatch_returnsQuery() {
        tst.put("hello", "1");
        assertEquals("hello", tst.longestPrefixOf("hello"));
    }

    // Covers: (x != null && i < query.length())
    @Test
    public void testPrefixWithNoValue_returnsShorterMatch() {
        tst.put("a", "1");
        tst.put("ab", null);
        assertEquals("a", tst.longestPrefixOf("abc"));
    }
}
