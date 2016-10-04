package com.arvernistudio.wolfenginetest.memory;

import com.arvernistudio.wolfengine.gameobject.GameObject;
import com.arvernistudio.wolfengine.memory.GameObjectPool;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameObjectPoolTest {

    @Test
    public void obtainTest(){
        GameObjectPool gameObjectPool = new GameObjectPool(10, 150);
        GameObject gameObject = gameObjectPool.obtain();

        assertNotNull(gameObject);
    }
}
