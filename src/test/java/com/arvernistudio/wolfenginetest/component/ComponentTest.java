package com.arvernistudio.wolfenginetest.component;

import com.arvernistudio.wolfengine.component.Component;
import com.arvernistudio.wolfengine.gameobject.GameObject;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class ComponentTest {

    @Before
    public void setUp(){
        //Avoid NullPointerException when Gdx.app.log is called in the tested methods
        Gdx.app = Mockito.mock(Application.class);
    }

    @Test
    public void setContainerTest(){
        Component component = Mockito.mock(Component.class, Mockito.CALLS_REAL_METHODS);
        GameObject gameObject = Mockito.mock(GameObject.class);
        GameObject gameObjectBeforeSetContainer = component.getContainer();
        component.setContainer(gameObject);
        GameObject gameObjectAfterSetContainer = component.getContainer();

        assertNull("The container of the Component should be null " +
                "before the setContainer method call.",
                gameObjectBeforeSetContainer);
        assertEquals("The container of the Component should be the GameObject " +
                "passed to the setContainer method upon the call.",
                gameObject, gameObjectAfterSetContainer);
    }
}
