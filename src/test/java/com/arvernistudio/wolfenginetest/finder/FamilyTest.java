package com.arvernistudio.wolfenginetest.finder;

import com.arvernistudio.wolfengine.component.Component;
import com.arvernistudio.wolfengine.finder.Family;
import com.arvernistudio.wolfengine.gameobject.GameObject;
import com.arvernistudio.wolfengine.mapper.ComponentMapper;
import com.arvernistudio.wolfengine.mapper.ObjectMapComponentMapper;
import com.arvernistudio.wolfengine.services.ServiceLocator;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Bits;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class FamilyTest {

    private class FooComponent extends Component{}

    private class BarComponent extends Component{}

    private class BazComponent extends Component{}

    @Before
    public void setUp(){
        //Avoid NullPointerException when Gdx.app.log is called in the tested methods
        Gdx.app = Mockito.mock(Application.class);

        //Reset the component mapper for the tests set
        ComponentMapper mapper = new ObjectMapComponentMapper();
        mapper.getComponentIndex(FooComponent.class);
        mapper.getComponentIndex(BarComponent.class);
        mapper.getComponentIndex(BazComponent.class);
        ServiceLocator.injectComponentMapper(mapper);
    }

    @Test
    public void allMatchTest(){
        ComponentMapper mapper = ServiceLocator.getComponentTypeMapper();
        Bits all = new Bits();
        Bits one = new Bits();
        GameObject gameObjectWithAllComponents = new GameObject();
        GameObject gameObjectWithoutAllComponents = new GameObject();

        all.set(mapper.getComponentIndex(FooComponent.class));
        all.set(mapper.getComponentIndex(BarComponent.class));
        all.set(mapper.getComponentIndex(BazComponent.class));

        Family family = new Family(all, one);
        gameObjectWithAllComponents.addComponent(new FooComponent())
                .addComponent(new BarComponent())
                .addComponent(new BazComponent())
                .addComponent(new BarComponent());

        assertTrue(family.matchWith(gameObjectWithAllComponents));
        assertFalse(family.matchWith(gameObjectWithoutAllComponents));
    }

    @Test
    public void oneMatchTest(){
        ComponentMapper mapper = ServiceLocator.getComponentTypeMapper();
        Bits all = new Bits();
        Bits one = new Bits();
        GameObject gameObjectWithOneComponent = new GameObject();
        GameObject gameObjectWithoutComponent = new GameObject();

        one.set(mapper.getComponentIndex(FooComponent.class));

        Family family = new Family(all, one);

        gameObjectWithOneComponent.addComponent(new FooComponent());

        assertTrue(family.matchWith(gameObjectWithOneComponent));
        assertFalse(family.matchWith(gameObjectWithoutComponent));
    }

    @Test
    public void familyKeyTest(){
        ComponentMapper mapper = ServiceLocator.getComponentTypeMapper();
        Bits all = new Bits();
        Bits one = new Bits();

        all.set(mapper.getComponentIndex(FooComponent.class));
        all.set(mapper.getComponentIndex(BarComponent.class));
        one.set(mapper.getComponentIndex(BazComponent.class));

        Family family = new Family(all, one);
        String familyKey = family.getFamilyKey();

        assertEquals("{all:11}{one:001}", familyKey);
    }

}
