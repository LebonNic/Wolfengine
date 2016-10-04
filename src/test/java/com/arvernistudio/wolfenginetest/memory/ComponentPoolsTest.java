package com.arvernistudio.wolfenginetest.memory;

import com.arvernistudio.wolfengine.mapper.ComponentMapper;
import com.arvernistudio.wolfengine.mapper.ObjectMapComponentMapper;
import com.arvernistudio.wolfengine.memory.ComponentPools;
import com.arvernistudio.wolfengine.services.ServiceLocator;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;


public class ComponentPoolsTest {

    @Before
    public void setUp(){
        //Avoid NullPointerException when Gdx.app.log is called in the tested methods
        Gdx.app = Mockito.mock(Application.class);

        //Reset the component mapper for the tests set
        ComponentMapper mapper = new ObjectMapComponentMapper();
        mapper.getComponentIndex(FooComponent.class);
        ServiceLocator.injectComponentMapper(mapper);
    }

    @Test
    public void instantiationTest(){
        ComponentPools componentPools = new ComponentPools(12, 150);

        assertEquals(12, componentPools.getInitialSize());
        assertEquals(150, componentPools.getMaxSize());
    }

    @Test
    public void poolCreationTest(){
        ComponentPools componentPools = new ComponentPools();

        boolean isPoolCreatedBeforeOperation =
                componentPools.isComponentPoolCreatedFor(FooComponent.class);

        FooComponent component = componentPools.obtain(FooComponent.class);

        boolean isPoolCreatedAfterOperation =
                componentPools.isComponentPoolCreatedFor(FooComponent.class);

        assertNotNull(component);
        assertTrue(component.isUsed());
        assertFalse(isPoolCreatedBeforeOperation);
        assertTrue(isPoolCreatedAfterOperation);
    }

    @Test
    public void freeTest(){
        ComponentPools componentPools = new ComponentPools();
        FooComponent component = componentPools.obtain(FooComponent.class);
        componentPools.free(component);

        assertFalse(component.isUsed());
    }

    @Test(expected=IllegalArgumentException.class)
    public void freeNullTest(){
        ComponentPools componentPools = new ComponentPools();
        componentPools.free(null);
    }
    
    @Test
    public void freeUnretainedComponentTest(){
        ComponentPools componentPools = new ComponentPools();
        FooComponent component = new FooComponent();

        componentPools.free(component);
        assertTrue(component.isUsed());
    }
}
