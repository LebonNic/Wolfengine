package com.arvernistudio.wolfengine.scene;

import com.arvernistudio.wolfengine.gameobject.GameObject;
import com.badlogic.gdx.utils.Pool;

public class PendingOperation implements Pool.Poolable {

    public enum OperationType{
        AddOperation,
        RemoveOperation
    }

    public GameObject gameObject;
    public OperationType type;

    @Override
    public void reset() {
        gameObject = null;
    }
}
