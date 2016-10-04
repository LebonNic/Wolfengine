package com.arvernistudio.wolfengine.memory;

import com.arvernistudio.wolfengine.gameobject.GameObject;
import com.badlogic.gdx.utils.Pool;

public class GameObjectPool extends Pool<GameObject>{

    public GameObjectPool(int initialSize, int maxSize){
        super(initialSize, maxSize);
    }

    public GameObjectPool(){
        this(16, 96);
    }

    @Override
    protected GameObject newObject() {
        return new GameObject();
    }
}
