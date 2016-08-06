package com.arvernistudio.wolfenginetest.utils;

import com.arvernistudio.wolfengine.gameobject.GameObject;
import com.arvernistudio.wolfengine.utils.ImmutableArray;
import com.badlogic.gdx.utils.Array;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

public class ImmutableArrayTest {

    @Test
    public void sizeTest(){
        Array<GameObject> array = new Array<>();
        ImmutableArray<GameObject> immutableArray =
                new ImmutableArray<>(array);

        int gameObjectsCount = 10;
        for(int i = 0; i < gameObjectsCount; ++i){
            array.add(new GameObject());
        }

        assertEquals(gameObjectsCount, immutableArray.size());
    }

    @Test
    public void getTest(){
        Array<GameObject> array = new Array<>();
        ImmutableArray<GameObject> immutableArray = new ImmutableArray<>(array);

        GameObject gameObject = new GameObject();
        int gameObjectId = gameObject.getId();
        array.add(gameObject);

        int gameObjectsCount = 10;
        for(int i = 0; i < gameObjectsCount; ++i){
            array.add(new GameObject());
        }

        GameObject retrievedGameObject = immutableArray.get(0);

        assertEquals(gameObjectId, retrievedGameObject.getId());
        assertEquals(gameObject, retrievedGameObject);
    }

    @Test
    public void containsTest(){
        Array<GameObject> array = new Array<>();
        ImmutableArray<GameObject> immutableArray = new ImmutableArray<>(array);

        GameObject gameObject = new GameObject();
        array.add(gameObject);

        assertTrue(immutableArray.contains(gameObject, false));
    }

    @Test
    public void indexOfTest(){
        Array<GameObject> array = new Array<>();
        ImmutableArray<GameObject> immutableArray = new ImmutableArray<>(array);

        GameObject firstGameObject = new GameObject();
        GameObject secondGameObject = new GameObject();
        GameObject thirdGameObject = new GameObject();

        array.add(firstGameObject);     // Index 0
        array.add(secondGameObject);    // Index 1
        array.add(thirdGameObject);     // Index 2

        int secondGameObjectIndex = immutableArray.indexOf(secondGameObject, false);

        assertEquals(1, secondGameObjectIndex);
    }

    @Test
    public void lastIndexOfTest(){
        Array<GameObject> array = new Array<>();
        ImmutableArray<GameObject> immutableArray = new ImmutableArray<>(array);

        GameObject firstGameObject = new GameObject();
        GameObject secondGameObject = new GameObject();
        GameObject thirdGameObject = new GameObject();
        GameObject notAddedGameObject = new GameObject();

        array.add(firstGameObject);     // Index 0
        array.add(secondGameObject);    // Index 1
        array.add(thirdGameObject);     // Index 2
        array.add(firstGameObject);     //Index 3

        int lastIndexOfFirstGameObject = immutableArray.lastIndexOf(firstGameObject, false);
        int notAddedGameObjectLastIndex = immutableArray.lastIndexOf(notAddedGameObject, false);

        assertEquals(3, lastIndexOfFirstGameObject);
        assertEquals(-1, notAddedGameObjectLastIndex);
    }

    @Test
    public void peekTest(){
        Array<GameObject> array = new Array<>();
        ImmutableArray<GameObject> immutableArray = new ImmutableArray<>(array);

        GameObject firstGameObject = new GameObject();
        GameObject secondGameObject = new GameObject();
        GameObject thirdGameObject = new GameObject();

        array.add(firstGameObject);     // Index 0
        array.add(secondGameObject);    // Index 1
        array.add(thirdGameObject);     // Index 2

        GameObject lastGameObject = immutableArray.peek();

        assertEquals(thirdGameObject.getId(), lastGameObject.getId());
        assertEquals(thirdGameObject, lastGameObject);
    }

    @Test
    public void firstTest(){
        Array<GameObject> array = new Array<>();
        ImmutableArray<GameObject> immutableArray = new ImmutableArray<>(array);

        GameObject firstGameObject = new GameObject();
        GameObject secondGameObject = new GameObject();
        GameObject thirdGameObject = new GameObject();

        array.add(firstGameObject);     // Index 0
        array.add(secondGameObject);    // Index 1
        array.add(thirdGameObject);     // Index 2

        GameObject first = immutableArray.first();

        assertEquals(firstGameObject.getId(), first.getId());
        assertEquals(firstGameObject, first);
    }

    @Test
    public void randomTest(){
        Array<GameObject> array = new Array<>();
        ImmutableArray<GameObject> immutableArray = new ImmutableArray<>(array);

        GameObject randomGameObjectBeforeAdd = immutableArray.random();
        GameObject gameObject = new GameObject();
        array.add(gameObject);
        GameObject randomGameObjectAfterAdd = immutableArray.random();

        assertNull(randomGameObjectBeforeAdd);
        assertEquals(gameObject.getId(), randomGameObjectAfterAdd.getId());
        assertEquals(gameObject, randomGameObjectAfterAdd);
    }

    @Test
    public void iteratorTest(){
        Array<GameObject> array = new Array<>();
        ImmutableArray<GameObject> immutableArray = new ImmutableArray<>(array);

        GameObject firstGameObject = new GameObject();
        GameObject secondGameObject = new GameObject();
        GameObject thirdGameObject = new GameObject();

        array.add(firstGameObject);     // Index 0
        array.add(secondGameObject);    // Index 1
        array.add(thirdGameObject);     // Index 2

        int i = 0;
        for(GameObject gameObject : immutableArray){
            assertEquals(gameObject.getId(), array.get(i).getId());
            assertEquals(gameObject, array.get(i));
            i++;
        }
    }
}
