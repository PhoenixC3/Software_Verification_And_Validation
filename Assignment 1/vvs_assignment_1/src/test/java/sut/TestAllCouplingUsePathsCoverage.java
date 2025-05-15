package sut;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

// All-Coupling-Use-Paths Coverage for private method put()
// For each variable x, cover at least one path from its last definition (def)
// to its first use (use) in another statement without redefining it.

//Last Defs:
// key: {1}
// val: {1}
// x: {1,4}
// d: {1,10}
// x’: {vi,viii,x,xi}

// First Uses:
// key: {i}
// val: {vi,viii,x,xi}
// x: {ii}
// d: {i}
// x’: {12}

public class TestAllCouplingUsePathsCoverage {

    private TST<String> tst;

    @Before
    public void setUp() {
        tst = new TST<>();
    }

    // key: {1} -> key: {i}
    @Test
    public void keyRecursion() {
        tst.put("ab", "1");
        assertEquals("1", tst.get("ab"));
    }

    // val: {1} -> val: {vi}
    @Test
    public void valRecursionLeftBranch() {
        tst.put("cb", "1");
        tst.put("ca", "2");
        assertEquals("2", tst.get("ca"));
    }

    // val: {1} -> val: {viii}
    @Test
    public void valRecursionRightBranch() {
        tst.put("cb", "1");
        tst.put("cd", "2");
        assertEquals("2", tst.get("cd"));
    }

    // val: {1} -> val: {x}
    @Test
    public void valRecursionMiddleBranch() {
        tst.put("bb", "1");
        tst.put("bbb", "2");
        assertEquals("2", tst.get("bbb"));
    }

    // val: {1} -> val: {xi}
    @Test
    public void valRecursionAssign() {
        tst.put("bb", "1");
        tst.put("bb", "2");
        assertEquals("2", tst.get("bb"));
    }

    // x: {1} -> x: {ii}
    @Test
    public void xRecursionToLeft() {
        tst.put("ba", "1");
        assertEquals("1", tst.get("ba"));
    }

    // x: {4} -> x: {ii}
    @Test
    public void xDefinedToLeft() {
        tst.put("ac", "1");
        assertEquals("1", tst.get("ac"));
    }

    // d: {1} -> d: {i}
    @Test
    public void dRecursion() {
        tst.put("xy", "1");
        assertEquals("1", tst.get("xy"));
    }

    // d: {10} -> d: {i}
    @Test
    public void dRecursionNext() {
        tst.put("yzzz", "1");
        assertEquals("1", tst.get("yzzz"));
    }

    // x’: {vi} -> x’: {12}
    @Test
    public void xLeftRecursiveFinish() {
        tst.put("cb", "1");
        tst.put("ca", "2");
        assertEquals("2", tst.get("ca"));
    }

    // x’: {viii} -> x’: {12}
    @Test
    public void xRightRecursiveFinish() {
        tst.put("cb", "1");
        tst.put("cd", "2");
        assertEquals("2", tst.get("cd"));
    }

    // x’: {x} -> x’: {12}
    @Test
    public void xMidRecursiveFinish() {
        tst.put("bb", "1");
        tst.put("bbb", "2");
        assertEquals("2", tst.get("bbb"));
    }

    // x’: {xi} -> x’: {12}
    @Test
    public void xAssignRecursiveFinish() {
        tst.put("bb", "1");
        tst.put("bb", "2");
        assertEquals("2", tst.get("bb"));
    }
}
