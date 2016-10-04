package com.arvernistudio.wolfenginetest.services;

import com.arvernistudio.wolfengine.finder.FamilyBuilder;
import com.arvernistudio.wolfengine.mapper.ComponentMapper;
import com.arvernistudio.wolfengine.mapper.ObjectMapComponentMapper;
import com.arvernistudio.wolfengine.memory.ComponentPools;
import com.arvernistudio.wolfengine.memory.GameObjectPool;
import com.arvernistudio.wolfengine.services.ServiceLocator;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class ServiceLocatorTest {
    @Before
    public void setUp(){
        //Avoid NullPointerException when Gdx.app.log is called in the tested methods
        Gdx.app = Mockito.mock(Application.class);
    }

    @Test
    public void locateServicesTest(){
        ComponentMapper mapper = ServiceLocator.getComponentTypeMapper();
        FamilyBuilder builder = ServiceLocator.getFamilyBuilder();
        GameObjectPool gameObjectPool = ServiceLocator.getGameObjectPool();
        ComponentPools componentPools = ServiceLocator.getComponentPools();

        assertNotNull(mapper);
        assertNotNull(builder);
        assertNotNull(gameObjectPool);
        assertNotNull(componentPools);
    }

    @Test(expected = IllegalArgumentException.class)
    public void injectNullMapperTest(){
        ServiceLocator.injectComponentMapper(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void injectNullFamilyBuilderTest(){
        ServiceLocator.injectComponentMapper(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void injectNullGameObjectPoolTest(){
        ServiceLocator.injectComponentMapper(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void injectNullComponentPoolsTest(){
        ServiceLocator.injectComponentPools(null);
    }
}
