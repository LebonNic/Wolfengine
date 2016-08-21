package com.arvernistudio.wolfengine.scene;

import com.badlogic.gdx.utils.Pool;

public class OperationPool extends Pool<PendingOperation>{

    @Override
    protected PendingOperation newObject() {
        return new PendingOperation();
    }
}
