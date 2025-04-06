package sut;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

// Select and apply one Logic-based test coverage for method longestPrefixOf,
// justify your option

public class LogicBasedCoverageTest {
    private TST<String> tst;
    
    @BeforeEach
    void setUp() {
        tst = new TST<>();
        tst.put("she", "1");
        tst.put("shell", "2");
        tst.put("shore", "3");
    }

    @Test
    void mcdc_nullQuery() {
        assertNull(tst.longestPrefixOf(null)); // query == null (true), query.length == 0 (N/A)
    }

    @Test
    void mcdc_emptyQuery() {
        assertNull(tst.longestPrefixOf("")); // query == null (false), query.length == 0 (true)
    }

    @Test
    void mcdc_noMatchingPrefix() {
        assertNull(tst.longestPrefixOf("xylophone")); // no matching prefix, should return null
    }

    @Test
    void mcdc_prefixIsExactKey() {
        assertEquals("shell", tst.longestPrefixOf("shell")); // exact match
    }

    @Test
    void mcdc_partialPrefixMatch() {
        assertEquals("she", tst.longestPrefixOf("shelter")); // "she" matches, but "shelter" is not a key
    }

    @Test
    void mcdc_fullPrefixThenNonMatch() {
        assertEquals("shell", tst.longestPrefixOf("shellfish")); // "shell" is the longest valid prefix
    }

    @Test
    void mcdc_earlyBreakPath_xIsNull() {
        TST<String> customTst = new TST<>();
        customTst.put("z", "val");
        assertNull(customTst.longestPrefixOf("a")); // breaks early due to null path
    }
}
