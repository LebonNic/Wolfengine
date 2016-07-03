package com.arvernistudio.wolfenginetest.mapper;

import com.arvernistudio.wolfengine.component.Component;
import com.arvernistudio.wolfengine.mapper.ObjectMapComponentMapper;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class ObjectMapComponentMapperTest {

    private class FooComponent extends Component{}
    private class BarComponent extends Component{}
    private class BazComponent extends Component{}

    @Before
    public void setUp(){
        //Avoid NullPointerException when Gdx.app.log is called in the tested methods
        Gdx.app = Mockito.mock(Application.class);
    }

    @Test
    public void indexGenerationTest(){
        ObjectMapComponentMapper mapper = new ObjectMapComponentMapper();
        int indexForFooComponent  =  mapper.getComponentIndex(FooComponent.class);
        int indexForBarComponent = mapper.getComponentIndex(BarComponent.class);
        int indexForBazComponent = mapper.getComponentIndex(BazComponent.class);

        assertEquals(indexForFooComponent, 1);
        assertEquals(indexForBarComponent, 2);
        assertEquals(indexForBazComponent, 3);
    }

    @Test
    public void indexConservationTest(){
        ObjectMapComponentMapper mapper = new ObjectMapComponentMapper();
        int indexOnGeneration  = mapper.getComponentIndex(FooComponent.class);
        int indexOnSecondCall  = mapper.getComponentIndex(FooComponent.class);

        assertEquals(indexOnGeneration, 1);
        assertEquals(indexOnSecondCall, 1);
    }
}
