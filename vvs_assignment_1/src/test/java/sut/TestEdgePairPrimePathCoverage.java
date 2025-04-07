package sut;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

// Edge-Pair Coverage and Prime Path Coverage for method longestPrefixOf

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

//Graph Edges:
// 1 2
// 1 3
// 3 4
// 3 5
// 5 6
// 6 16
// 6 7
// 7 8
// 8 9
// 8 10
// 10 11
// 10 12
// 12 13
// 13 14
// 13 15
// 14 15
// 9 6
// 11 6
// 15 6

public class TestEdgePairPrimePathCoverage {

    private TST<String> tst;

    @Before
    public void setUp() {
        tst = new TST<>();
    }

    //----------------EDGE PAIR COVERAGE------------------

    // Path: [1, 2]
    @Test(expected = IllegalArgumentException.class)
    public void ep1() {
        tst.longestPrefixOf(null);
    }

    // Path: [1, 3, 4]
    @Test
    public void ep2() {
        assertNull(tst.longestPrefixOf(""));
    }

    // Path: [1, 3, 5, 6, 16]
    @Test
    public void ep3() {
        assertEquals("", tst.longestPrefixOf("anything"));
    }

    // Path: [1,3,5,6,7,8,10,12,13,15,6,16]
    @Test
    public void ep4() {
        tst.put("a", "1");
        assertEquals("a", tst.longestPrefixOf("abc"));
    }

    // Path: [1,3,5,6,7,8,9,6,7,8,10,11,6,16]
    @Test
    public void ep5() {
        tst.put("f", "1");
        tst.put("b", "2");
        assertEquals("", tst.longestPrefixOf("d"));
    }

    // Path: [1,3,5,6,7,8,10,11,6,7,8,9,6,16]
    @Test
    public void ep6() {
        tst.put("b", "1");
        tst.put("d", "2");
        assertEquals("", tst.longestPrefixOf("c"));
    }

    // Path: [1,3,5,6,7,8,10,12,13,14,15,6,7,8,9,6,16]
    @Test
    public void ep7() {
        tst.put("c", "1");
        tst.put("cb", "2");
        assertEquals("c", tst.longestPrefixOf("ca"));
    }

    //----------------PRIME PATH COVERAGE------------------

    // Path: [1, 2]
    @Test(expected = IllegalArgumentException.class)
    public void pp1() {
        tst.longestPrefixOf(null);
    }

    // Path: [1, 3, 4]
    @Test
    public void pp2() {
        assertNull(tst.longestPrefixOf(""));
    }

    // Path: [1,3,5,6,16]
    @Test
    public void pp3() {
        assertEquals("", tst.longestPrefixOf("anything"));
    }

    // Path: [1,3,5,6,7,8,10,12,13,14,15,6,7,8,9,6,16]
    @Test
    public void pp4() {
        tst.put("c", "1");
        tst.put("cb", "2");
        assertEquals("c", tst.longestPrefixOf("ca"));
    }

    // Path: [1,3,5,6,7,8,9,6,7,8,10,12,13,14,15,6,16]
    @Test
    public void pp5() {
        tst.put("bc", "1");
        tst.put("a", "2");
        assertEquals("a", tst.longestPrefixOf("ack"));
    }

    // Path: [1,3,5,6,7,8,10,12,13,14,15,6,7,8,10,12,13,14,15,6,16]
    @Test
    public void pp6() {
        tst.put("b", "1");
        tst.put("bc", "2");
        assertEquals("bc", tst.longestPrefixOf("bcd"));
    }

    // Path: [1,3,5,6,7,8,10,11,6,7,8,10,12,13,14,15,6,16]
    @Test
    public void pp7() {
        tst.put("apple", "1");
        tst.put("b", "2");
        assertEquals("b", tst.longestPrefixOf("baker"));
    }

    // Path: [1,3,5,6,7,8,10,12,13,15,6,7,8,10,12,13,14,15,6,16]
    @Test
    public void pp8() {
        tst.put("an", "1");
        assertEquals("an", tst.longestPrefixOf("and"));
    }

    // Path: [1,3,5,6,7,8,10,12,13,14,15,6,7,8,10,12,13,15,6,16]
    @Test
    public void pp9() {
        tst.put("b", "1");
        tst.put("bcc", "2");
        assertEquals("b", tst.longestPrefixOf("bc"));
    }

    // Path: [1,3,5,6,7,8,10,12,13,14,15,6,7,8,10,11,6,16]
    @Test
    public void pp10() {
        tst.put("b", "1");
        tst.put("bc", "2");
        assertEquals("b", tst.longestPrefixOf("bd"));
    }

    // Path: [1,3,5,6,7,8,10,12,13,15,6,7,8,9,6,16]
    @Test
    public void pp11() {
        tst.put("bb", "1");
        tst.put("bc", "2");
        assertEquals("", tst.longestPrefixOf("ba"));
    }


    // Path: [1,3,5,6,7,8,10,12,13,15,6,7,8,10,11,6,16]
    @Test
    public void pp12() {
        tst.put("bd", "1");
        assertEquals("", tst.longestPrefixOf("be"));
    }

    // Path: [1,3,5,6,7,8,9,6,7,8,10,12,13,15,6,16]
    @Test
    public void pp13() {
        tst.put("cd", "1");
        tst.put("aa", "2");
        assertEquals("", tst.longestPrefixOf("a"));
    }

    // Path: [1,3,5,6,7,8,10,12,13,15,6,7,8,10,12,13,15,6,16]
    @Test
    public void pp14() {
        tst.put("aaa", "1");
        assertEquals("", tst.longestPrefixOf("aa"));
    }
    
    // Path: [1,3,5,6,7,8,10,11,6,7,8,10,12,13,15,6,16]
    @Test
    public void pp15() {
        tst.put("aaab", "1");
        tst.put("ba", "2");
        assertEquals("", tst.longestPrefixOf("b"));
    }

    // Path: [1,3,5,6,7,8,10,11,6,7,8,9,6,16]
    @Test
    public void pp16() {
        tst.put("a", "1");
        tst.put("c", "2");
        assertEquals("", tst.longestPrefixOf("b"));
    }

    // Path: [1,3,5,6,7,8,10,11,6,7,8,10,11,6,16]
    @Test
    public void pp17() {
        tst.put("a", "1");
        tst.put("c", "2");
        assertEquals("", tst.longestPrefixOf("d"));
    }

    // Path: [1,3,5,6,7,8,9,6,7,8,10,11,6,16]
    @Test
    public void pp18() {
        tst.put("c", "1");
        tst.put("a", "2");
        assertEquals("", tst.longestPrefixOf("b"));
    }

    // Path: [1,3,5,6,7,8,9,6,7,8,9,6,16]
    @Test
    public void pp19() {
        tst.put("c", "1");
        tst.put("b", "2");
        assertEquals("", tst.longestPrefixOf("a"));
    }
}
