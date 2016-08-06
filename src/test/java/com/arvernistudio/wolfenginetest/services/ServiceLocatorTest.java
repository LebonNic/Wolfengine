package com.arvernistudio.wolfenginetest.services;

import com.arvernistudio.wolfengine.mapper.ComponentMapper;
import com.arvernistudio.wolfengine.mapper.ObjectMapComponentMapper;
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
    public void locateComponentMapper(){
        ComponentMapper mapper = ServiceLocator.getComponentTypeMapper();
        assertNotNull(mapper);
    }
}
