package sut;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

// All-Coupling-Use-Paths Coverage for private method put

public class AllCouplingUsePathsCoverageTest {

    private TST<String> tst;
    
    @BeforeEach
    void setUp() {
        tst = new TST<>();
    }

    // path: public.put → private.put (x == null)
    // covers: key passed and char used, val stored
    @Test
    void acu_insertNewRootNode() {
        tst.put("a", "val");
        assertEquals("val", tst.get("a"));
    }

    // path: public.put → private.put → private.put → private.put
    // each call uses next char from key, passes val
    // covers: coupling paths from key/val to recursive uses
    @Test
    void acu_insertDeepMidRecursion() {
        tst.put("abc", "val");
        assertEquals("val", tst.get("abc"));
    }
    
    // forces c < x.c ⇒ x.left
    @Test
    void acu_insertLeftBranch() {
        tst.put("c", "val1");
        tst.put("a", "val2");

        assertEquals("val1", tst.get("c"));
        assertEquals("val2", tst.get("a"));
    }

    // forces c > x.c ⇒ x.right
    @Test
    void acu_insertRightBranch() {
        tst.put("a", "val1");
        tst.put("c", "val2");

        assertEquals("val1", tst.get("a"));
        assertEquals("val2", tst.get("c"));
    }

    // reuses entire key path ⇒ key passed again, val updated
    @Test
    void acu_overwriteValue() {
        tst.put("cat", "first");
        tst.put("cat", "second");

        assertEquals("second", tst.get("cat"));
    }
}
