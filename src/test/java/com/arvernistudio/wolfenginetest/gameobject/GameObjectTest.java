package com.arvernistudio.wolfenginetest.gameobject;

import com.arvernistudio.wolfengine.component.Component;
import com.arvernistudio.wolfengine.gameobject.GameObject;
import com.arvernistudio.wolfengine.services.ServiceLocator;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class GameObjectTest {

    private class FooComponent extends Component{}

    @Before
    public void setUp(){
        //Avoid NullPointerException when Gdx.app.log is called in the tested methods
        Gdx.app = Mockito.mock(Application.class);
    }

    @Test
    public void instantiationTest(){
        int instanceCounterBeforeInstantiation = GameObject.getInstanceCounterValue();
        GameObject gameObject = new GameObject();
        int instanceCounterAfterInstantiation = GameObject.getInstanceCounterValue();

        assertEquals("The instance counter should be incremented after a GameObject instantiation.",
                instanceCounterBeforeInstantiation + 1, instanceCounterAfterInstantiation);
        assertEquals("The ID of the new GameObject should be the new value of the instance counter.",
                instanceCounterAfterInstantiation, gameObject.getId());
    }

    @Test
    public void getComponentByIndexTest(){
        GameObject gameObject = new GameObject();
        FooComponent fooComponent = new FooComponent();
        int componentIndex = ServiceLocator.getComponentTypeMapper().getComponentIndex(FooComponent.class);
        Component componentBeforeAddOperation = gameObject.getComponent(componentIndex);
        gameObject.addComponent(fooComponent);
        Component componentAfterAddOperation = gameObject.getComponent(componentIndex);

        assertNull("The Component retrieved from the GameObject before the " +
                "add operation should be null.",
                componentBeforeAddOperation);
        assertNotNull("The Component retrieved from the GameObject after the " +
                "add operation should not be null.",
                componentAfterAddOperation);
        assertEquals("The Component retrieved from the GameObject should be the " +
                "same as the one which has been added previously to the GameObject.",
                fooComponent, componentAfterAddOperation);
    }

    @Test
    public void getComponentByObjectTypeTest(){
        GameObject gameObject = new GameObject();
        FooComponent fooComponent = new FooComponent();
        Component componentBeforeAddOperation = gameObject.getComponent(FooComponent.class);
        gameObject.addComponent(fooComponent);
        Component componentAfterAddOperation = gameObject.getComponent(FooComponent.class);

        assertNull("The Component retrieved from the GameObject before the " +
                        "add operation should be null.",
                componentBeforeAddOperation);
        assertNotNull("The Component retrieved from the GameObject after the " +
                        "add operation should not be null.",
                componentAfterAddOperation);
        assertEquals("The Component retrieved from the GameObject should be the " +
                        "same as the one which has been added previously to the GameObject.",
                fooComponent, componentAfterAddOperation);
    }
}
