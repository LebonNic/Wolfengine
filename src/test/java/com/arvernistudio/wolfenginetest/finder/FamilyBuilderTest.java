package com.arvernistudio.wolfenginetest.finder;

import com.arvernistudio.wolfengine.component.Component;
import com.arvernistudio.wolfengine.finder.Family;
import com.arvernistudio.wolfengine.finder.FamilyBuilder;
import com.arvernistudio.wolfengine.gameobject.GameObject;
import com.arvernistudio.wolfengine.mapper.ComponentMapper;
import com.arvernistudio.wolfengine.mapper.ObjectMapComponentMapper;
import com.arvernistudio.wolfengine.services.ServiceLocator;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class FamilyBuilderTest {

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
    public void allBuildTest(){
        FamilyBuilder builder = new FamilyBuilder();
        Family family = builder.all(FooComponent.class, BazComponent.class).get();
        GameObject gameObject = new GameObject();
        gameObject.addComponent(new FooComponent())
                .addComponent(new BazComponent());

        assertTrue(family.matchWith(gameObject));
        assertEquals("{all:101}", family.getFamilyKey());
    }

    @Test
    public void oneBuildTest(){
        FamilyBuilder builder = new FamilyBuilder();
        Family family = builder.all().one(FooComponent.class, BarComponent.class).get();

        GameObject gameObject = new GameObject();
        gameObject.addComponent(new FooComponent())
                .addComponent(new BazComponent());

        assertTrue(family.matchWith(gameObject));
        assertEquals("{one:11}", family.getFamilyKey());
    }

    @Test
    public void buildEmptyFamily(){
        FamilyBuilder builder = new FamilyBuilder();
        Family family = builder.all().one().get();
        GameObject gameObject = new GameObject();

        assertTrue(family.matchWith(gameObject));
        assertEquals("", family.getFamilyKey());
    }
}
