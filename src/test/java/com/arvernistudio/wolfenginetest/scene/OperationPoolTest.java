package com.arvernistudio.wolfenginetest.scene;

import com.arvernistudio.wolfengine.scene.OperationPool;
import com.arvernistudio.wolfengine.scene.PendingOperation;
import org.junit.Test;

import static org.junit.Assert.*;

public class OperationPoolTest {

    @Test
    public void resetTest(){
        OperationPool operationPool = new OperationPool();
        PendingOperation operation  = operationPool.obtain();

        assertNotNull(operation);
    }
}
