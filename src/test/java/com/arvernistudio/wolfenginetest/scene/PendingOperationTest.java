package com.arvernistudio.wolfenginetest.scene;

import com.arvernistudio.wolfengine.gameobject.GameObject;
import com.arvernistudio.wolfengine.scene.PendingOperation;
import org.junit.Test;

import static org.junit.Assert.*;

public class PendingOperationTest {

    @Test
    public void resetTest(){
        PendingOperation operation = new PendingOperation();
        operation.type = PendingOperation.OperationType.RemoveOperation;
        operation.gameObject = new GameObject();

        assertEquals(PendingOperation.OperationType.RemoveOperation,
                operation.type);
        assertNotNull(operation.gameObject);

        operation.reset();

        assertEquals(PendingOperation.OperationType.RemoveOperation,
                operation.type);
        assertNull(operation.gameObject);
    }
}
