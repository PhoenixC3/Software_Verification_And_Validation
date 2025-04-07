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
    public void lb1() {
        assertThrows(IllegalArgumentException.class, () -> tst.longestPrefixOf(null));
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
        assertNull(tst.longestPrefixOf("dog"));
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
