package sut;

import org.junit.runner.RunWith;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.*;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import com.pholser.junit.quickcheck.From;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

@RunWith(JUnitQuickcheck.class)
public class TestQuickCheck {

    public static class RandomStringListGenerator extends Generator<List<String>> {
        private final Random rng = new Random();

        public RandomStringListGenerator() {
            super((Class<List<String>>) (Class<?>) List.class);
        }

        @Override
        public List<String> generate(SourceOfRandomness random, GenerationStatus status) {
            int size = random.nextInt(1, 10);  // List of 1–10 strings
            Set<String> result = new HashSet<>();
            while (result.size() < size) {
                String s = generateRandomString(random);
                if (!s.isEmpty()) {
                    result.add(s);
                }
            }
            return new ArrayList<>(result);
        }

        private String generateRandomString(SourceOfRandomness random) {
            int length = random.nextInt(1, 8); // Strings of 1–8 characters
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                sb.append((char) random.nextChar('a', 'z'));
            }
            return sb.toString();
        }
    }

    @Property
    public void insertionOrderDoesNotAffectTreeEquality(
        @From(RandomStringListGenerator.class) List<String> keys) {

        // Make sure keys are unique to avoid ambiguity
        Set<String> uniqueKeys = new HashSet<>(keys);
        assumeTrue(uniqueKeys.size() == keys.size());

        TST<Integer> trie1 = new TST<>();
        TST<Integer> trie2 = new TST<>();

        Map<String, Integer> keyValues = new LinkedHashMap<>();
        int value = 1;
        for (String key : keys) {
            keyValues.put(key, value++);
        }

        for (Map.Entry<String, Integer> entry : keyValues.entrySet()) {
            trie1.put(entry.getKey(), entry.getValue());
        }

        List<String> shuffledKeys = new ArrayList<>(keys);
        Collections.shuffle(shuffledKeys);

        for (String key : shuffledKeys) {
            trie2.put(key, keyValues.get(key));
        }

        assertTrue(trie1.equals(trie2));
    }

    @Property
    public void deletingAllKeysLeavesEmptyTrie(@From(RandomStringListGenerator.class) List<String> keys) {
        TST<Integer> trie = new TST<>();
        int value = 42;

        for (String key : keys) {
            trie.put(key, value);
        }

        for (String key : keys) {
            trie.delete(key);
        }

        assertEquals(0, trie.size());
        for (String key : keys) {
            assertNull(trie.get(key));
        }
    }

    @Property
    public void insertThenDeleteYieldsSameTrie(@From(RandomStringListGenerator.class) List<String> keys) {
        TST<Integer> original = new TST<>();
        for (int i = 0; i < keys.size(); i++) {
            original.put(keys.get(i), i);
        }

        for (String key : keys) {
            TST<Integer> copy = cloneTrie(original);

            // Generate a key that does not already exist
            String newKey = key + "_extra";
            assumeTrue(!original.contains(newKey));

            copy.put(newKey, 999);
            copy.delete(newKey);
            assertTrue(original.equals(copy));
        }
    }

    @Property(trials = 50)
    public void stricterPrefixGivesSubset(
        @From(RandomStringListGenerator.class) List<String> keys
    ) {
        // Ensure we always have at least one "sub..." prefixed key
        keys = new ArrayList<>(keys.stream()
            .filter(s -> s.length() >= 3)
            .limit(20)
            .collect(Collectors.toList()));

        // Inject a known prefix string if necessary
        if (keys.stream().noneMatch(s -> s.startsWith("su"))) {
            keys.add("submarine"); // or "sunlight", "subzero" — guaranteed match
        }

        TST<Integer> trie = new TST<>();
        for (int i = 0; i < keys.size(); i++) {
            trie.put(keys.get(i), i);
        }

        Set<String> suMatches = new HashSet<>();
        for (String s : trie.keysWithPrefix("su")) suMatches.add(s);

        Set<String> subMatches = new HashSet<>();
        for (String s : trie.keysWithPrefix("sub")) subMatches.add(s);

        // Actual property check
        assertTrue("Subprefix result not subset of prefix result", suMatches.containsAll(subMatches));
    }

    private <T> TST<T> cloneTrie(TST<T> original) {
        TST<T> clone = new TST<>();
        for (String key : original.keys()) {
            clone.put(key, original.get(key));
        }
        return clone;
    }
}
