package model;

public class MyHashMap<K, V> {
    private int capacity;
    private int realSize;
    private Node<K, V>[] bucket;

    private final int DEFAULT_CAPACITY = 16;
    private final double DEFAULT_LOAD_FACTOR = 0.75;

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> nextNode;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public MyHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.realSize = 0;
        this.bucket = (Node<K, V>[]) new Node[capacity];
    }

    public MyHashMap(int userCapacity) {
        if (userCapacity <= 0) {
            throw new IllegalArgumentException();
        }
        this.capacity = userCapacity;
        this.realSize = 0;
        this.bucket = (Node<K, V>[]) new Node[capacity];
    }

    public int size() {
        return realSize;
    }

    public void put(K key, V value) {
        validateKey(key);
        if (isNeedResize()) {
            resize();
        }
        putNodeInBucket(key, value);
    }

    private void validateKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isNeedResize() {
        return realSize >= capacity * DEFAULT_LOAD_FACTOR;
    }

    private void resize() {
        int newCapacity = (int) (capacity * 1.5);
        MyHashMap<K, V> resizedMyHashMap = new MyHashMap<>(newCapacity);
        for (Node<K, V> firstNodePerBucket : bucket) {
            Node<K, V> currentNode = firstNodePerBucket;
            while (currentNode != null) {
                Node<K, V> nextNode = currentNode.nextNode;
                resizedMyHashMap.putNodeInBucketForResize(currentNode);
                currentNode = nextNode;
            }
        }
        this.capacity = newCapacity;
        this.bucket = resizedMyHashMap.bucket;
    }

    private void putNodeInBucketForResize(Node<K, V> addedNode) {
        int indexBucketToPut = calcIndexBucket(addedNode.key);
        addedNode.nextNode = bucket[indexBucketToPut];
        bucket[indexBucketToPut] = addedNode;
    }

    private void putNodeInBucket(K key, V value) {
        int indexBucketToPut = calcIndexBucket(key);
        Node<K, V> currentNode = bucket[indexBucketToPut];

        while (currentNode != null) {
            if (isEqualKeys(key, currentNode.key)) {
                currentNode.value = value;
                return;
            } else {
                currentNode = currentNode.nextNode;
            }
        }

        Node<K, V> newNode = new Node<>(key, value);
        newNode.nextNode = bucket[indexBucketToPut];
        bucket[indexBucketToPut] = newNode;
        realSize++;
    }

    private int calcIndexBucket(K key) {
        int indexBucket = Math.abs(key.hashCode() % capacity);
        return indexBucket;
    }

    private boolean isEqualKeys(K key1, K key2) {
        if (key1.hashCode() != key2.hashCode()) {
            return false;
        }
        if (!key1.equals(key2)) {
            return false;
        }
        return true;
    }

    public V get(K key) {
        validateKey(key);
        Node<K, V> foundedNode = findNodeByKey(key);
        if (foundedNode == null) {
            return null;
        }
        return foundedNode.value;
    }

    private Node<K, V> findNodeByKey(K key) {
        int indexBucketToGet = calcIndexBucket(key);
        Node<K, V> currentNode = bucket[indexBucketToGet];
        while (currentNode != null) {
            if (isEqualKeys(key, currentNode.key)) {
                return currentNode;
            }
            currentNode = currentNode.nextNode;
        }
        return null;
    }

    public boolean remove(K key) {
        validateKey(key);
        return removeNodeByKey(key);
    }

    private boolean removeNodeByKey(K key) {
        int indexBucketToRemove = calcIndexBucket(key);
        Node<K, V> currentNode = bucket[indexBucketToRemove];
        Node<K, V> firstNode = new Node<>(null, null);
        firstNode.nextNode = currentNode;
        Node<K, V> previousNode = firstNode;

        while (currentNode != null) {
            if (isEqualKeys(key, currentNode.key)) {
                previousNode.nextNode = currentNode.nextNode;
                currentNode = null;
                bucket[indexBucketToRemove] = firstNode.nextNode;
                realSize--;
                return true;
            } else {
                previousNode = currentNode;
                currentNode = currentNode.nextNode;
            }
        }
        return false;
    }
}
