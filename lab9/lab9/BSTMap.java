package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Your name here
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
        return;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (p == null) {
            return null;
        }
        int gapOfKey = p.key.compareTo(key);
        if (gapOfKey < 0) {
            return getHelper(key, p.right);
        } else if (gapOfKey > 0) {
            return getHelper(key, p.left);
        } else {
            return p.value;
        }
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key, root);
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (p == null) {
            return new Node(key, value);
        }
        int gapOfKey = p.key.compareTo(key);
        if (gapOfKey < 0) {
            p.right = putHelper(key, value, p.right);
        } else if (gapOfKey > 0) {
            p.left = putHelper(key, value, p.left);
        }
        return p;
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        size++;
        root = putHelper(key, value, root);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    Set<K> keySet = new TreeSet<>();

    private Set<K> keySetHelper(Node p) {
        if (p == null) {
            return null;
        } else {
            keySet.add(p.key);
            keySetHelper(p.left);
            keySetHelper(p.right);
        }
        return keySet;
    }

    @Override
    public Set<K> keySet() {
        try {
            return keySetHelper(root);
        } catch (UnsupportedOperationException e) {
            System.out.println("Unsupported operation!");
        }
        return keySetHelper(root);
    }

    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */

    private Node findKeyNodeParent(K key, Node p) {
        Node i = p;
        while (ifLeftNode(i) || ifRightNode(i)) {
            int gapOfKey = i.key.compareTo(key);
            if (gapOfKey < 0 && ifLeftNode(i)) {
                if (i.left.key != key) {
                    i = i.left;
                }
            }
            if (gapOfKey > 0 && ifRightNode(i)) {
                if (i.right.key != key) {
                    i = i.right;
                }
            }
        }
        return i;
    }

    private Node findKeyNode(K key, Node p) {
        /** 如果findKeyNodeParent != null, 那么p是root && p.key == key 或者 p的child.key == key.
         *  如果findKeyNodeParent == null, 那么p == null 或者 p.key != key && p没有child.
         */
        if (p == null || p.key == key) {
            return p;
        }
        int gapOfKey = p.key.compareTo(key);
        Node keyNode = null;
        if (gapOfKey < 0 && ifLeftNode(p)) {
            keyNode = findKeyNodeParent(key, p.left);
        } else if (gapOfKey > 0 && ifLeftNode(p)) {
            keyNode = findKeyNodeParent(key, p.right);
        }
        return keyNode;
    }

    private boolean ifLeftNode(Node p) {
        if (p == null || p.left == null) {
            return false;
        }
        return true;
    }

    private boolean ifRightNode(Node p) {
        if (p == null || p.right == null) {
            return false;
        }
        return true;
    }

    /** 找到最接近p的child, 替换掉p, 并返回p.value */
    private V searchToBottom(Node p) {
        V removedV = p.value;
        if (ifLeftNode(p)) {
            Node leftSubtreeBottom = p.left;
            Node leftSubtreeParent = p;
            while (ifRightNode(leftSubtreeBottom)) {
                leftSubtreeParent = leftSubtreeBottom;
                leftSubtreeBottom = leftSubtreeBottom.right;
            }
            p.value = leftSubtreeBottom.value;
            p.key = leftSubtreeBottom.key;
            leftSubtreeParent.right = null;
            return removedV;
        } else if (ifRightNode(p)) {
            Node rightSubtreeBottom = p.right;
            Node rightSubtreeParent = p;
            while (ifLeftNode(rightSubtreeBottom)) {
                rightSubtreeParent = rightSubtreeBottom;
                rightSubtreeBottom = rightSubtreeParent.left;
            }
            p.value = rightSubtreeBottom.value;
            p.key = rightSubtreeBottom.key;
            rightSubtreeParent.left = null;
        }
        return removedV;
    }

    private V removeHelper(K key, Node p) {
        Node keyNodeParent = findKeyNodeParent(key, p);
        Node keyNode = findKeyNode(key, p);
        if (keyNode == null) {
            return null;
        } else {
            V removedV;
            if (keyNodeParent != keyNode) { //keyNode不是root
                if (!ifLeftNode(keyNode) && !ifRightNode(keyNode)) { //keyNode没有child
                    removedV = keyNode.value;
                    if (keyNodeParent.left == keyNode) {
                        keyNodeParent.left = null;
                    } else if (keyNodeParent.right == keyNode) {
                        keyNodeParent.right =null;
                    }
                } else { //keyNode有1个或2个child
                    removedV = searchToBottom(keyNode);
                }
            } else { //keyNode是root
                if (!ifLeftNode(keyNode) && !ifRightNode(keyNode)) { //keyNode没有child
                    removedV = keyNode.value;
                    keyNode = null;
                } else {
                        removedV = searchToBottom(keyNode);
                    }
            }
            return removedV;
        }
    }

    @Override
    public V remove(K key) {
        try {
            return removeHelper(key, root);
        } catch (UnsupportedOperationException e) {
            System.out.println("Unsupported operation!");
        }
        return removeHelper(key, root);
    }

    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }

    public static void main(String[] args) {
        BSTMap<String, Integer> test = new BSTMap<>();
        test.put("a", 5);
        test.put("b", 3);
        test.put("c", 7);
        test.put("d", 4);
        test.put("e", 6);
        test.findKeyNodeParent("e", test.root);
    }
}
