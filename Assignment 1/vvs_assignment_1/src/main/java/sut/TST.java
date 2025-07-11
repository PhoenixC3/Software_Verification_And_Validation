package sut;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

/******************************************************************************
 *  Symbol table with string keys using a ternary search trie (TST).
 *
 *  Remarks: can't use a key that is the empty string ""
 *
 ******************************************************************************/

/**
 *  The {@code TST} class represents an symbol table of key-value
 *  pairs, with string keys and generic values.
 *  It supports the usual <em>put</em>, <em>get</em>, <em>contains</em>,
 *  <em>delete</em>, <em>size</em>, and <em>is-empty</em> methods.
 *  It also provides character-based methods for finding the string
 *  in the symbol table that is the <em>longest prefix</em> of a given prefix,
 *  finding all strings in the symbol table that <em>start with</em> a given prefix,
 *  and finding all strings in the symbol table that <em>match</em> a given pattern.
 *  A symbol table implements the <em>associative array</em> abstraction:
 *  when associating a value with a key that is already in the symbol table,
 *  the convention is to replace the old value with the new value.
 *  Unlike {@link java.util.Map}, this class uses the convention that
 *  values cannot be {@code null}—setting the
 *  value associated with a key to {@code null} is equivalent to deleting the key
 *  from the symbol table.
 *  <p>
 *  This implementation uses a ternary search trie.
 *  <p>
 *  For additional documentation, see <a href="https://algs4.cs.princeton.edu/52trie">Section 5.2</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *  Ternary search tries are described at page 746.
 */
public class TST<T> {
	
    private Node<T> root;   // root of TST
    private int n;          // size

    private static class Node<T> {
        private char c;                    // character
        private Node<T> left, mid, right;  // left, middle, and right subtries
        private T val;                     // value associated with string
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return n;
    }

    /**
     * Does this symbol table contain the given key?
     * @param key the key
     * @return {@code true} if this symbol table contains {@code key} and
     *     {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean contains(String key) {
        if (key == null) 
            throw new IllegalArgumentException("argument to contains() is null");
        return get(key) != null;
    }

    /**
     * Returns the value associated with the given key.
     * @param key the key
     * @return the value associated with the given key if the key is in the symbol table
     *     and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public T get(String key) {
        if (key == null)
            throw new IllegalArgumentException("calls get() with null argument");
        if (key.length() == 0) 
        	throw new IllegalArgumentException("key must have length >= 1");
        Node<T> x = get(root, key, 0);
        if (x == null) 
        	return null;
        return x.val;
    }

    // return subtrie corresponding to given key
    private Node<T> get(Node<T> x, String key, int d) {
        if (x == null) 
        	return null;
        if (key.length() == 0) 
        	throw new IllegalArgumentException("key must have length >= 1");
        char c = key.charAt(d);
        if      (c < x.c)              return get(x.left,  key, d);
        else if (c > x.c)              return get(x.right, key, d);
        else if (d < key.length() - 1) return get(x.mid,   key, d+1);
        else                           return x;
    }

    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is {@code null}, this effectively deletes the key from the symbol table.
     * @param key the key
     * @param val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(String key, T val) {
        if (key == null)
            throw new IllegalArgumentException("calls put() with null key");
        if (!contains(key)) 
        	n++;
        root = put(root, key, val, 0);
    }

    private Node<T> put(Node<T> x, String key, T val, int d) {
        char c = key.charAt(d); // 1
        if (x == null) { // 2
            x = new Node<>(); // 3
            x.c = c; // 4
        }
        if (c < x.c) { // 5
            x.left = put(x.left,  key, val, d); // 6
        }
        else if (c > x.c) { // 7
            x.right = put(x.right, key, val, d); // 8
        }
        else if (d < key.length() - 1) { // 9
            x.mid = put(x.mid,   key, val, d+1); // 10
        }
        else {
            x.val = val; // 11
        }
        return x; // 12
    }

    /**
     * Returns the string in the symbol table that is the longest prefix of {@code query},
     * or {@code null}, if no such string.
     * @param query the query string
     * @return the string in the symbol table that is the longest prefix of {@code query},
     *     or {@code null} if no such string
     * @throws IllegalArgumentException if {@code query} is {@code null}
     */
    public String longestPrefixOf(String query) {
        if (query == null) // 1
            throw new IllegalArgumentException("calls longestPrefixOf() with null argument"); // 2
        if (query.length() == 0) // 3
        	return null; // 4
        int length = 0; // 5
        Node<T> x = root;  // 5
        int i = 0; // 5
        while (x != null && i < query.length()) { // 6
            char c = query.charAt(i); // 7
            if  (c < x.c)  { // 8
                x = x.left; // 9
            }
            else if (c > x.c) { // 10
                x = x.right; // 11
            }
            else { // 12
                i++; // 12
                if (x.val != null) // 13
                	length = i; // 14
                x = x.mid; // 15
            }
        }
        return query.substring(0, length); // 16
    }

    /**
     * Returns all keys in the symbol table as an {@code Iterable}.
     * To iterate over all of the keys in the symbol table named {@code st},
     * use the foreach notation: {@code for (Key key : st.keys())}.
     * @return all keys in the symbol table as an {@code Iterable}
     */
    public Iterable<String> keys() {
        Queue<String> queue = new LinkedList<>();
        collect(root, new StringBuilder(), queue);
        return queue;
    }

    /**
     * Returns all of the keys in the set that start with {@code prefix}.
     * @param prefix the prefix
     * @return all of the keys in the set that start with {@code prefix},
     *     as an iterable
     * @throws IllegalArgumentException if {@code prefix} is {@code null}
     */
    public Iterable<String> keysWithPrefix(String prefix) {
        if (prefix == null)
            throw new IllegalArgumentException("calls keysWithPrefix() with null argument");
        Queue<String> queue = new LinkedList<>();
        Node<T> x = get(root, prefix, 0);
        if (x == null) 
        	return queue;
        if (x.val != null) 
        	queue.add(prefix);
        collect(x.mid, new StringBuilder(prefix), queue);
        return queue;
    }

    // all keys in subtrie rooted at x with given prefix
    private void collect(Node<T> x, StringBuilder prefix, Queue<String> queue) {
        if (x == null) 
        	return;
        collect(x.left,  prefix, queue);
        if (x.val != null) 
        	queue.add(prefix.toString() + x.c);
        collect(x.mid,   prefix.append(x.c), queue);
        prefix.deleteCharAt(prefix.length() - 1);
        collect(x.right, prefix, queue);
    }


    /**
     * Returns all of the keys in the symbol table that match {@code pattern},
     * where . symbol is treated as a wildcard character.
     * @param pattern the pattern
     * @return all of the keys in the symbol table that match {@code pattern},
     *     as an iterable, where . is treated as a wildcard character.
     */
    public Iterable<String> keysThatMatch(String pattern) {
        Queue<String> queue = new LinkedList<>();
        collect(root, new StringBuilder(), 0, pattern, queue);
        return queue;
    }
 
    private void collect(Node<T> x, StringBuilder prefix, int i, String pattern, Queue<String> queue) {
        if (x == null) 
        	return;
        char c = pattern.charAt(i);
        if (c == '.' || c < x.c) 
        	collect(x.left, prefix, i, pattern, queue);
        if (c == '.' || c == x.c) {
            if (i == pattern.length() - 1 && x.val != null) 
            	queue.add(prefix.toString() + x.c);
            if (i < pattern.length() - 1) {
                collect(x.mid, prefix.append(x.c), i+1, pattern, queue);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
        if (c == '.' || c > x.c) 
        	collect(x.right, prefix, i, pattern, queue);
    }

    /**
     * Deletes the key (and associated value) from the symbol table.
     * If the key is not present, the symbol table is unchanged.
     * @param key the key to delete
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void delete(String key) {
        if (key == null)
            throw new IllegalArgumentException("calls delete() with null key");
        
        if (contains(key)) {
            root = delete(root, key, 0);
            n--;
        }
    }

    private Node<T> delete(Node<T> x, String key, int d) {
        if (x == null) return null;
    
        char c = key.charAt(d);
        if      (c < x.c) x.left  = delete(x.left,  key, d);
        else if (c > x.c) x.right = delete(x.right, key, d);
        else if (d < key.length() - 1) x.mid = delete(x.mid, key, d+1);
        else x.val = null;
    
        if (x.val != null || x.left != null || x.mid != null || x.right != null) {
            return x;
        }
        return null;
    }

    /**
     * Compares this TST with another for equality.
     * @param obj the other TST
     * @return {@code true} if the two TSTs are equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TST<?> other = (TST<?>) obj;

        if (this.size() != other.size()) return false;

        for (String key : this.keys()) {
            if (!Objects.equals(this.get(key), other.get(key))) {
                return false;
            }
        }

        return true;
    }
}
