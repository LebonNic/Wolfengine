package com.arvernistudio.wolfenginetest.gameobject;

import com.arvernistudio.wolfengine.component.Component;
import com.arvernistudio.wolfengine.finder.Family;
import com.arvernistudio.wolfengine.finder.FamilyBuilder;
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

public class GameObjectTest {

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

        // Reset the family builder for the tests set
        FamilyBuilder builder = new FamilyBuilder();
        ServiceLocator.injectFamilyBuilder(builder);
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

        assertEquals("The number of components added to the GameObject should be" +
                "one.", 1, gameObject.getComponents().size());
    }

    @Test
    public void getComponentsMaskTest(){
        ComponentMapper mapper = ServiceLocator.getComponentTypeMapper();
        GameObject gameObject = new GameObject();
        gameObject.addComponent(new FooComponent())
                .addComponent(new BarComponent())
                .addComponent(new BazComponent());

        Bits expectedComponentMask = new Bits();
        expectedComponentMask.set(mapper.getComponentIndex(FooComponent.class));
        expectedComponentMask.set(mapper.getComponentIndex(BarComponent.class));
        expectedComponentMask.set(mapper.getComponentIndex(BazComponent.class));

        assertEquals(expectedComponentMask, gameObject.getComponentsMask());
    }

    @Test
    public void toggleFamilyMembershipTest(){
        FamilyBuilder builder = ServiceLocator.getFamilyBuilder();
        Family family = builder.all(FooComponent.class, BazComponent.class).get();

        GameObject gameObject = new GameObject()
                .addComponent(new FooComponent())
                .addComponent(new BazComponent());

        boolean  isGameObjectInFamilyAtFirst = gameObject.isFamilyMembershipToggledFor(family);
        gameObject.toggleFamilyMembership(family);
        boolean familyMembershipToggled =  gameObject.isFamilyMembershipToggledFor(family);
        gameObject.toggleFamilyMembership(family);
        boolean familyMembershipNotToggled = gameObject.isFamilyMembershipToggledFor(family);

        assertFalse(isGameObjectInFamilyAtFirst);
        assertTrue(familyMembershipToggled);
        assertFalse(familyMembershipNotToggled);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullComponentAddTest(){
        GameObject gameObject = new GameObject();
        gameObject.addComponent(null);
    }

    @Test(expected = InternalError.class)
    public void wrongFamilyMembershipTest() {
        GameObject gameObject = new GameObject();
        FamilyBuilder builder = ServiceLocator.getFamilyBuilder();
        Family family = builder.all(FooComponent.class, BazComponent.class).get();

        gameObject.toggleFamilyMembership(family);
    }
}
