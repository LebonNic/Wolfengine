package com.arvernistudio.wolfenginetest.memory;

import com.arvernistudio.wolfengine.component.Component;
import com.badlogic.gdx.utils.Pool;

public class FooComponent extends Component implements Pool.Poolable {

    private boolean isUsed;

    public FooComponent(){
        isUsed = true;
    }

    @Override
    public void reset() {
        isUsed = false;
    }

    public boolean isUsed(){
        return isUsed;
    }
}
