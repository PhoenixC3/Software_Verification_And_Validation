package sut;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

// All-Du-Paths Coverage for method longestPrefixOf

public class AllDuPathsCoverageTest {

    private TST<String> tst;
    
    @BeforeEach
    void setUp() {
        tst = new TST<>();
    }

    // Covers no DU-paths inside function (early exit)
    @Test
    void du_nullQuery_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> tst.longestPrefixOf(null));
    }

    // Covers use of query.length() in second if
    @Test
    void du_emptyQuery_returnsNull() {
        assertNull(tst.longestPrefixOf(""));
    }

    // Covers:
    // - DEF(x) → USE(x in while)
    // - DEF(i) → USE(i in while)
    // - DEF(c) → USE(c < x.c)
    // - DEF(x) → USE(x in if)
    @Test
    void du_noMatch_entersWhileAndExitsImmediately() {
        tst.put("dog", "value");
        assertEquals("", tst.longestPrefixOf("cat"));
    }

    // Covers DEF(x) → x.left
    @Test
    void du_leftBranchTraversal() {
        tst.put("dog", "value");
        assertEquals("", tst.longestPrefixOf("cat"));
    }

    // Covers DEF(x) → x.right
    @Test
    void du_rightBranchTraversal() {
        tst.put("bat", "value");
        assertEquals("", tst.longestPrefixOf("cat"));
    }

    // Covers:
    // - DEF(i) → USE(i in query.charAt(i))
    // - DEF(i++) → USE(i in length = i)
    // - DEF(length) → USE(length in return)
    // - DEF(x) → USE(x.val, x.mid)
    @Test
    void du_midBranchSetsLength() {
        tst.put("car", "auto");
        assertEquals("car", tst.longestPrefixOf("carbon"));
    }

    // Covers full path including repeated mid-branch traversals and setting length multiple times
    @Test
    void du_fullMatchSetsFinalLength() {
        tst.put("shell", "val");
        assertEquals("shell", tst.longestPrefixOf("shell"));
    }

    // Traverses mid without hitting x.val ≠ null → length stays at shorter value
    @Test
    void du_midBranchWithNoVal_skipsLengthUpdate() {
        tst.put("abc", "val");
        assertEquals("ab", tst.longestPrefixOf("abd"));
    }
}
