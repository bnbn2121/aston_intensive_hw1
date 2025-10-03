package test;

import model.MyHashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MyHashMapTest {
    private MyHashMap<String, Integer> map;

    @BeforeEach
    void createMap() {
        map = new MyHashMap<>();
    }

    @Test
    void testPutAndGet() {
        assertEquals(0, map.size());

        map.put("str1", 100);
        assertEquals(100, map.get("str1"));
        assertEquals(1, map.size());

        map.put("str2", 200);
        assertEquals(100, map.get("str1"));
        assertEquals(200, map.get("str2"));
        assertEquals(2, map.size());
    }

    @Test
    void testGetByNonexistentKey() {
        assertNull(map.get("str"));
        assertEquals(0, map.size());

        map.put("str1", 100);
        map.put("str2", 200);
        assertEquals(2, map.size());
        assertNull(map.get("str3"));
    }

    @Test
    void testOverrideNode() {
        assertEquals(0, map.size());

        map.put("str1", 100);
        assertEquals(100, map.get("str1"));
        assertEquals(1, map.size());

        map.put("str1", 200);
        assertEquals(200, map.get("str1"));
        assertEquals(1, map.size());
    }

    @Test
    void testRemove() {
        assertEquals(0, map.size());
        assertFalse(map.remove("str"));

        map.put("str1", 100);
        map.put("str2", 200);
        assertEquals(2, map.size());
        assertEquals(100, map.get("str1"));
        assertTrue(map.remove("str1"));
        assertEquals(1, map.size());
        assertNull(map.get("str1"));
        assertEquals(200, map.get("str2"));
        assertFalse(map.remove("str1"));
    }

    @Test
    void testNullKey() {
        assertThrows(IllegalArgumentException.class, () -> map.put(null, 100));
        assertThrows(IllegalArgumentException.class, () -> map.get(null));
        assertThrows(IllegalArgumentException.class, () -> map.remove(null));
    }

    @Test
    void testResize() {
        //размер по умолчанию - 16. ресайз при 0.75 * 16 элементов
        int firstNum = 1;
        int lastNum = 25;

        for (int i = firstNum; i <= lastNum; i++) {
            map.put("str" + i, i);
            assertEquals(i, map.size());
        }

        for (int i = firstNum; i <= lastNum; i++) {
            assertEquals(i, map.get("str" + i));
        }
    }

}
