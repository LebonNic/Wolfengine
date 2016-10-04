package com.arvernistudio.wolfengine.memory;

import com.arvernistudio.wolfengine.gameobject.GameObject;
import com.badlogic.gdx.utils.Pool;

public class GameObjectPool extends Pool<GameObject>{

    private static final int DEFAULT_INITIAL_POOL_SIZE = 16;
    private static final int DEFAULT_MAXIMUM_POOL_SIZE = 96;

    public GameObjectPool(int initialSize, int maxSize){
        super(initialSize, maxSize);
    }

    public GameObjectPool(){
        this(GameObjectPool.DEFAULT_INITIAL_POOL_SIZE,
                GameObjectPool.DEFAULT_MAXIMUM_POOL_SIZE);
    }

    @Override
    protected GameObject newObject() {
        return new GameObject();
    }
}
