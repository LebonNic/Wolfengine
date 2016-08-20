package com.arvernistudio.wolfenginetest.finder;

import com.arvernistudio.wolfengine.component.Component;
import com.arvernistudio.wolfengine.finder.Family;
import com.arvernistudio.wolfengine.finder.FamilyBuilder;
import com.arvernistudio.wolfengine.finder.GameObjectFinder;
import com.arvernistudio.wolfengine.gameobject.GameObject;
import com.arvernistudio.wolfengine.mapper.ComponentMapper;
import com.arvernistudio.wolfengine.mapper.ObjectMapComponentMapper;
import com.arvernistudio.wolfengine.services.ServiceLocator;
import com.arvernistudio.wolfengine.utils.ImmutableArray;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectSet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class GameObjectFinderTest {

    private class FooComponent extends Component {}

    private class BarComponent extends Component{}

    private class BazComponent extends Component{}

    private ObjectSet<GameObject> _gameObjects;

    private static final int FOO_BAR_FAMILY_COUNT = 7;
    private static final int BAR_BAZ_FAMILY_COUNT = 5;
    private static final int FOO_BAR_BAZ_FAMILY_COUNT = 2;
    private static final int BAR_FAMILY_COUNT = 3;

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

        _gameObjects = setUpObjectSet();
    }

    private ObjectSet<GameObject> setUpObjectSet(){
        ObjectSet<GameObject> objectSet = new ObjectSet<>();

        // Initialize a family of game objects composed of the two components
        // FooComponent and BarComponent
        for(int i = 0; i < GameObjectFinderTest.FOO_BAR_FAMILY_COUNT; i++){
            GameObject gameObject = new GameObject();
            gameObject.addComponent(new FooComponent())
                    .addComponent(new BarComponent());
            objectSet.add(gameObject);
        }

        for(int i = 0; i < GameObjectFinderTest.BAR_BAZ_FAMILY_COUNT; i++){
            GameObject gameObject = new GameObject()
                    .addComponent(new BarComponent())
                    .addComponent(new BazComponent());
            objectSet.add(gameObject);
        }

        for(int i = 0; i < GameObjectFinderTest.FOO_BAR_BAZ_FAMILY_COUNT; i++){
            GameObject gameObject = new GameObject()
                    .addComponent(new FooComponent())
                    .addComponent(new BarComponent())
                    .addComponent(new BazComponent());
            objectSet.add(gameObject);
        }

        for(int i = 0; i < GameObjectFinderTest.BAR_FAMILY_COUNT; i++){
            GameObject gameObject = new GameObject()
                    .addComponent(new BarComponent());

            objectSet.add(gameObject);
        }

        return objectSet;
    }

    @Test
    public void getGameObjectsForTest(){
        GameObjectFinder finder = new GameObjectFinder(_gameObjects);
        FamilyBuilder builder = ServiceLocator.getFamilyBuilder();
        Family allFooBarFamily = builder.all(FooComponent.class, BarComponent.class).get();

        ImmutableArray<GameObject> fooBarFamilyGameObjects =
                finder.getGameObjectsFor(allFooBarFamily);

        assertEquals(GameObjectFinderTest.FOO_BAR_FAMILY_COUNT
                + GameObjectFinderTest.FOO_BAR_BAZ_FAMILY_COUNT, fooBarFamilyGameObjects.size());

        int loopCount = GameObjectFinderTest.FOO_BAR_FAMILY_COUNT
                + GameObjectFinderTest.FOO_BAR_BAZ_FAMILY_COUNT;

        int FooComponentIndex =
                ServiceLocator.getComponentTypeMapper().getComponentIndex(FooComponent.class);
        int BarComponentIndex =
                ServiceLocator.getComponentTypeMapper().getComponentIndex(BarComponent.class);

        for(int i = 0; i < loopCount; i++){
            assertNotNull(
                    fooBarFamilyGameObjects.get(i).getComponent(FooComponentIndex)
            );
            assertNotNull(
                    fooBarFamilyGameObjects.get(i).getComponent(BarComponentIndex)
            );
        }
    }

    @Test
    public void addFamilyMembershipForTest(){
        GameObjectFinder finder = new GameObjectFinder(_gameObjects);
        FamilyBuilder builder = ServiceLocator.getFamilyBuilder();
        Family oneBarFamily = builder.one(BarComponent.class).get();
        Family allFooBarBazFamily =
                builder.all(FooComponent.class, BarComponent.class, BazComponent.class)
                        .get();

        ImmutableArray<GameObject> oneBarGameObjectsFamily =
                finder.getGameObjectsFor(oneBarFamily);
        ImmutableArray<GameObject> allFooBarBazGameObjectsFamily =
                finder.getGameObjectsFor(allFooBarBazFamily);

        assertEquals(GameObjectFinderTest.FOO_BAR_FAMILY_COUNT
                + GameObjectFinderTest.BAR_FAMILY_COUNT
                + GameObjectFinderTest.BAR_BAZ_FAMILY_COUNT
                + GameObjectFinderTest.FOO_BAR_BAZ_FAMILY_COUNT,
                oneBarGameObjectsFamily.size()
        );

        assertEquals(GameObjectFinderTest.FOO_BAR_BAZ_FAMILY_COUNT,
                allFooBarBazGameObjectsFamily.size());

        GameObject gameObject = new GameObject()
                .addComponent(new BarComponent());

        finder.addFamilyMembershipFor(gameObject);

        oneBarGameObjectsFamily =
                finder.getGameObjectsFor(oneBarFamily);
        allFooBarBazGameObjectsFamily =
                finder.getGameObjectsFor(allFooBarBazFamily);

        assertEquals(GameObjectFinderTest.FOO_BAR_FAMILY_COUNT
                        + GameObjectFinderTest.BAR_FAMILY_COUNT
                        + GameObjectFinderTest.BAR_BAZ_FAMILY_COUNT
                        + GameObjectFinderTest.FOO_BAR_BAZ_FAMILY_COUNT
                        + 1,
                oneBarGameObjectsFamily.size()
        );

        assertEquals(GameObjectFinderTest.FOO_BAR_BAZ_FAMILY_COUNT,
                allFooBarBazGameObjectsFamily.size());
    }

    @Test
    public void eraseFamilyMembershipForTest(){
        GameObjectFinder finder = new GameObjectFinder(_gameObjects);
        FamilyBuilder builder = ServiceLocator.getFamilyBuilder();

        Family allBarBazFamily = builder.all(BarComponent.class, BazComponent.class).get();

        ImmutableArray<GameObject> allBarBazFamilyGameObjects =
                finder.getGameObjectsFor(allBarBazFamily);

        assertEquals(GameObjectFinderTest.FOO_BAR_BAZ_FAMILY_COUNT
        + GameObjectFinderTest.BAR_BAZ_FAMILY_COUNT,
                allBarBazFamilyGameObjects.size());

        GameObject firstGameObject = allBarBazFamilyGameObjects.get(0);
        finder.eraseFamilyMembershipFor(firstGameObject);
        allBarBazFamilyGameObjects = finder.getGameObjectsFor(allBarBazFamily);

        assertEquals(GameObjectFinderTest.FOO_BAR_BAZ_FAMILY_COUNT
                + GameObjectFinderTest.BAR_BAZ_FAMILY_COUNT
                - 1,
                allBarBazFamilyGameObjects.size());

        finder.addFamilyMembershipFor(firstGameObject);
        allBarBazFamilyGameObjects = finder.getGameObjectsFor(allBarBazFamily);

        assertEquals(GameObjectFinderTest.FOO_BAR_BAZ_FAMILY_COUNT
                        + GameObjectFinderTest.BAR_BAZ_FAMILY_COUNT,
                allBarBazFamilyGameObjects.size());
    }
}
