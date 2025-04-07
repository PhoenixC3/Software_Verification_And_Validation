package sut;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

// All-Du-Paths Coverage for method longestPrefixOf
// Variables: x, i, c, length, query

// Graph Nodes:
// N1: if (query == null)
// N2: throw new IllegalArgumentException("calls longestPrefixOf() with null argument");
// N3: if (query.length() == 0)
// N4: return null;
// N5: int length = 0; Node<T> x = root; int i = 0;
// N6: while (x != null && i < query.length()) {
// N7: char c = query.charAt(i);
// N8: if (c < x.c)
// N9: x = x.left;
// N10: else if (c > x.c)
// N11: x = x.right;
// N12: else { i++;
// N13: if (x.val != null)
// N14: length = i;
// N15: x = x.mid;
// N16: return query.substring(0, length);

// x defs : 5, 9, 11, 15
// i defs : 5, 12
// c defs : 7
// length defs : 5, 14
// query defs : -

// x uses: 5, 6, 8, 10, 12, 13, 15
// i uses: 5, 6, 7, 12, 14
// c uses: 7, 8, 10
// length uses: 5, 14, 16
// query uses: 1, 3, 6, 7, 16

public class AllDuPathsCoverageTest {

    private TST<String> tst;
    
    @BeforeEach
    void setUp() {
        tst = new TST<>();
    }

    // Path: [1,3,5,6,16]
    @Test
    public void ad1() {
        assertNull(tst.longestPrefixOf("anything"));
    }

    // Path: [1,3,5,6,7,8,9,6,16]
    @Test
    public void ad2() {
        tst.put("f", "1");
        assertEquals("", tst.longestPrefixOf("d"));
    }

    // Path: [1,3,5,6,7,8,10,11,6,16]
    @Test
    public void ad3() {
        tst.put("f", "1");
        assertEquals("", tst.longestPrefixOf("g"));
    }

    // Path: [1,3,5,6,7,8,10,12,13,15,6,16]
    @Test
    public void ad4() {
        tst.put("fg", "1");
        assertEquals("", tst.longestPrefixOf("f"));
    }

    // Path: [1,3,5,6,7,8,10,12,13,14,15,6,16]
    @Test
    public void ad5() {
        tst.put("f", "1");
        assertEquals("f", tst.longestPrefixOf("f"));
    }

    // Path: [1,3,5,6,7,8,9,6,7,8,9,6,16]
    @Test
    public void ad6() {
        tst.put("c", "1");
        tst.put("b", "2");
        assertEquals("", tst.longestPrefixOf("a"));
    }

    // Path: [1,3,5,6,7,8,9,6,7,8,10,11,6,16]
    @Test
    public void ad7() {
        tst.put("d", "1");
        tst.put("b", "2");
        assertEquals("", tst.longestPrefixOf("cc"));
    }

    // Path: [1,3,5,6,7,8,9,6,7,8,10,12,13,15,6,16]
    @Test
    public void ad8() {
        tst.put("caab", "1");
        tst.put("ba", "2");
        assertEquals("", tst.longestPrefixOf("b"));
    }

    // Path: [1,3,5,6,7,8,9,6,7,8,10,12,13,14,15,6,16]
    @Test
    public void ad9() {
        tst.put("caab", "1");
        tst.put("b", "2");
        assertEquals("b", tst.longestPrefixOf("b"));
    }

    // Path: [1,3,5,6,7,8,10,11,6,7,8,9,6,16]
    @Test
    public void ad10() {
        tst.put("b", "1");
        tst.put("d", "2");
        assertEquals("", tst.longestPrefixOf("c"));
    }

    // Path: [1,3,5,6,7,8,10,11,6,7,8,10,11,6,16]
    @Test
    public void ad11() {
        tst.put("bd", "1");
        tst.put("d", "2");
        assertEquals("", tst.longestPrefixOf("e"));
    }

    // Path: [1,3,5,6,7,8,10,11,6,7,8,10,12,13,15,6,16]
    @Test
    public void ad12() {
        tst.put("aaab", "1");
        tst.put("ba", "2");
        assertEquals("", tst.longestPrefixOf("b"));
    }

    // Path: [1,3,5,6,7,8,10,11,6,7,8,10,12,13,14,15,6,16]
    @Test
    public void ad13() {
        tst.put("apple", "1");
        tst.put("b", "2");
        assertEquals("b", tst.longestPrefixOf("baker"));
    }

    // Path: [1,3,5,6,7,8,10,12,13,15,6,7,8,9,6,16]
    @Test
    public void ad14() {
        tst.put("bb", "1");
        tst.put("bc", "2");
        assertEquals("", tst.longestPrefixOf("ba"));
    }

    // Path: [1,3,5,6,7,8,10,12,13,15,6,7,8,10,11,6,16]
    @Test
    public void ad15() {
        tst.put("bd", "1");
        assertEquals("", tst.longestPrefixOf("be"));
    }

    // Path: [1,3,5,6,7,8,10,12,13,15,6,7,8,10,12,13,15,6,16]
    @Test
    public void ad16() {
        tst.put("aaa", "1");
        assertEquals("", tst.longestPrefixOf("aa"));
    }

    // Path: [1,3,5,6,7,8,10,12,13,15,6,7,8,10,12,13,14,15,6,16]
    @Test
    public void ad17() {
        tst.put("an", "1");
        assertEquals("an", tst.longestPrefixOf("and"));
    }

    // Path: [1,3,5,6,7,8,10,12,13,14,15,6,7,8,9,6,16]
    @Test
    public void ad18() {
        tst.put("a", "1");
        tst.put("an", "2");
        assertEquals("a", tst.longestPrefixOf("am"));
    }

    // Path: [1,3,5,6,7,8,10,12,13,14,15,6,7,8,10,12,13,15,6,16]
    @Test
    public void ad19() {
        tst.put("b", "1");
        tst.put("bcc", "2");
        assertEquals("b", tst.longestPrefixOf("bc"));
    }

    // Path: [1,3,5,6,7,8,10,12,13,14,15,6,7,8,10,12,13,14,15,6,16]
    @Test
    public void ad20() {
        tst.put("b", "1");
        tst.put("bc", "2");
        assertEquals("bc", tst.longestPrefixOf("bcd"));
    }
}
