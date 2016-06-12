package com.arvernistudio.wolfenginetest.utils;

import com.arvernistudio.wolfengine.utils.Bag;
import org.junit.Test;

import static org.junit.Assert.*;

public class BagTest {
    @Test
    public void instantiationTest(){
        Bag<Integer> bag = new Bag<>();
        Bag<Integer> bagWithCustomCapacity = new Bag<>(10);

        assertEquals(0, bag.getSize());
        assertEquals(Bag.BAG_DEFAULT_CAPACITY,  bag.getCapacity());
        assertTrue(bag.isEmpty());
        assertEquals(0, bagWithCustomCapacity.getSize());
        assertEquals(10, bagWithCustomCapacity.getCapacity());
        assertTrue(bagWithCustomCapacity.isEmpty());
    }

    @Test
    public void addTest(){
        Bag<Integer> bag = new Bag<>();
        Integer expectedInt = 0;
        bag.add(expectedInt);
        int bagSize = bag.getSize();
        Integer actualInt = bag.get(0);

        assertEquals(1, bagSize);
        assertEquals(expectedInt, actualInt);
    }

    @Test
    public void removeTest(){
        Bag<Integer> bag = new Bag<>();
        Integer expectedInt = 18;
        bag.add(expectedInt);
        Integer actualInt = bag.remove(0);

        assertEquals(0, bag.getSize());
        assertEquals(expectedInt, actualInt);
    }

    @Test
    public void removeLast(){
        Bag<Integer> bag = new Bag<>();
        Integer nullPointer = bag.removeLast();
        Integer expectedInt = 22;
        bag.add(expectedInt);
        Integer actualInt = bag.removeLast();

        assertNull(nullPointer);
        assertEquals(0, bag.getSize());
        assertEquals(expectedInt, actualInt);
    }

    @Test
    public void removeByElementTest(){
        Bag<Integer> bag = new Bag<>();
        Integer fooInt = 10;
        boolean hasBeenRemovedBeforeAddOperation = bag.remove(fooInt);
        bag.add(fooInt);
        boolean hasBeenRemovedAfterAddOperation = bag.remove(fooInt);

        assertFalse(hasBeenRemovedBeforeAddOperation);
        assertTrue(hasBeenRemovedAfterAddOperation);
        assertEquals(0, bag.getSize());
    }

     @Test
    public void containsTest(){
         Bag<Integer> bag = new Bag<>();
         Integer barInt = 9;
         boolean isContainedBeforeAddOperation = bag.contains(barInt);
         bag.add(barInt);
         boolean isContainedAfterAddOperation = bag.contains(barInt);

         assertFalse(isContainedBeforeAddOperation);
         assertTrue(isContainedAfterAddOperation);
     }

    @Test
    public void indexBoundsTest(){
        Bag<Integer> bag = new Bag<>();
        boolean isNegativeIndexWithinBounds = bag.isIndexWithinBounds(-10);
        boolean isBigIndexWithinBounds = bag.isIndexWithinBounds(Bag.BAG_DEFAULT_CAPACITY);
        bag.add(1);
        bag.add(9);
        bag.add(10);
        boolean isTwoWithinBounds = bag.isIndexWithinBounds(2);
        boolean isThreeWithinBounds = bag.isIndexWithinBounds(3);

        assertFalse(isNegativeIndexWithinBounds);
        assertFalse(isBigIndexWithinBounds);
        assertTrue(isTwoWithinBounds);
        assertTrue(isThreeWithinBounds);
    }

    @Test
    public void clearTest(){
        Bag<Integer> bag = new Bag<>();
        Integer fooInt = 12;
        bag.add(fooInt);
        bag.add(38);
        bag.add(90);
        int sizeBeforeClear = bag.getSize();
        boolean isFooIntContainedBeforeClear = bag.contains(fooInt);
        bag.clear();
        int sizeAfterClear = bag.getSize();
        boolean isFooIntContainedAfterClear = bag.contains(fooInt);

        assertEquals(3, sizeBeforeClear);
        assertTrue(isFooIntContainedBeforeClear);
        assertEquals(0, sizeAfterClear);
        assertFalse(isFooIntContainedAfterClear);
    }

    @Test
    public void setTestWithoutGrow(){
        Bag<Integer> bag = new Bag<>();
        Integer expectedInt = 11;
        bag.set(28, expectedInt);
        Integer actualInt = bag.get(28);

        assertEquals(expectedInt, actualInt);
        assertEquals(Bag.BAG_DEFAULT_CAPACITY, bag.getCapacity());
    }

    @Test
    public void setTestWithGrow(){
        Bag<Integer> bag = new Bag<>();
        Integer expectedInt = 20;
        bag.set(Bag.BAG_DEFAULT_CAPACITY, expectedInt);
        Integer actualInt = bag.get(Bag.BAG_DEFAULT_CAPACITY);

        assertEquals(expectedInt, actualInt);
        assertTrue(Bag.BAG_DEFAULT_CAPACITY < bag.getCapacity());
    }

    @Test
    public void growTest(){
        Bag<Integer> bag = new Bag<>();
        int capacityBeforeGrow = bag.getCapacity();
        for(int i = 0; i < Bag.BAG_DEFAULT_CAPACITY + 1; ++i){
            bag.add(i);
        }
        int capacityAfterGrow = bag.getCapacity();

        assertTrue(capacityBeforeGrow < capacityAfterGrow);
        assertEquals(Bag.BAG_DEFAULT_CAPACITY + 1, bag.getSize());
    }
}
