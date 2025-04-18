package sut;

import org.junit.runner.RunWith;
import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(JUnitQuickcheck.class)
public class TestQuickCheck {

    public static class RandomTSTStringGenerator extends Generator<TST<String>> {
        public static final int MAX_SIZE = 10;
        public static final int MAX_STRING_LENGTH = 8;
    
        public RandomTSTStringGenerator(Class<TST<String>> type) {
            super(type);
        }
    
        @Override
        public TST<String> generate(SourceOfRandomness random, GenerationStatus status) {
            TST<String> tst = new TST<>();
            int size = 1 + random.nextInt(MAX_SIZE);
            while (size-- > 0) {
                String key = generateRandomString(random);
                tst.put(key, key);
            }
            return tst;
        }
    
        private String generateRandomString(SourceOfRandomness random) {
            int length = 1 + random.nextInt(MAX_STRING_LENGTH);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                sb.append((char) random.nextChar('a', 'z'));
            }
            return sb.toString();
        }
    }

    // 1. The order of insertion of different keys does not change the final tree value
    @Property
    public void insertionOrderIndependence(@From(RandomTSTStringGenerator.class) TST<String> tree) {
        assertNotEquals(tree, null);

        List<String> words = new ArrayList<>();
        List<String> wordsToShuffle = new ArrayList<>();
        for (String key : tree.keys()) {
            words.add(key);
            wordsToShuffle.add(key);
        }

        TST<String> trie1 = new TST<>();
        for (String word : words) {
            trie1.put(word, word);
        }

        TST<String> trie2 = new TST<>();
        Collections.shuffle(wordsToShuffle);
        for (String word : wordsToShuffle) {
            trie2.put(word, word);
        }

        assertEquals(trie1, trie2);
    }

    // 2. If you remove all keys from a tree, the tree must be empty
    @Property
    public void removeAllKeys(@From(RandomTSTStringGenerator.class) TST<String> tree) {
        assertNotEquals(tree, null);

        for (String key : tree.keys()) {
            tree.delete(key);
        }

        assertTrue(tree.size() == 0);
    }

    // 3. Given a tree, inserting and then removing the same key value will not change its initial value
    @Property
    public void insertRemoveDoesNotChangeInitialValue(@From(RandomTSTStringGenerator.class) TST<String> tree) {
        assertNotEquals(tree, null);

        TST<String> initialState = new TST<>();
        for (String key : tree.keys()) {
            initialState.put(key, tree.get(key));
        }

        String randomKey = tree.keys().iterator().next();
        tree.delete(randomKey);

        tree.put(randomKey, randomKey);

        assertEquals(initialState, tree);
    }

    // 4. Selecting a stricter prefix keysWithPrefix returns a strict subset result. Eg, keysWithPrefix("sub") ⊆ keysWithPrefix("su")
    @Property
    public void prefixSubset(@From(RandomTSTStringGenerator.class) TST<String> tree) {
        assertNotNull(tree);

        Set<String> keysWithSu = new HashSet<>();
        for (String key : tree.keysWithPrefix("su")) {
            keysWithSu.add(key);
        }

        Set<String> keysWithSub = new HashSet<>();
        for (String key : tree.keysWithPrefix("sub")) {
            keysWithSub.add(key);
        }

        assertTrue(keysWithSu.containsAll(keysWithSub));

        if (!keysWithSub.isEmpty()) {
            assertTrue(keysWithSu.size() > keysWithSub.size());
        }
    }
}
