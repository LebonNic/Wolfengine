package com.arvernistudio.wolfenginetest.scene;

import com.arvernistudio.wolfengine.component.Component;
import com.arvernistudio.wolfengine.finder.Family;
import com.arvernistudio.wolfengine.finder.FamilyBuilder;
import com.arvernistudio.wolfengine.gameobject.GameObject;
import com.arvernistudio.wolfengine.mapper.ComponentMapper;
import com.arvernistudio.wolfengine.mapper.ObjectMapComponentMapper;
import com.arvernistudio.wolfengine.scene.Scene;
import com.arvernistudio.wolfengine.services.ServiceLocator;
import com.arvernistudio.wolfengine.utils.ImmutableArray;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class SceneTest {

    private class FooComponent extends Component {}

    private class BarComponent extends Component{}

    private class BazComponent extends Component{}

    private static final int FOO_BAR_FAMILY_COUNT = 7;
    private static final int BAR_BAZ_FAMILY_COUNT = 5;
    private static final int FOO_BAR_BAZ_FAMILY_COUNT = 2;
    private static final int BAR_FAMILY_COUNT = 3;

    private static final int REMOVED_GAME_OBJECTS_COUNT = 3;
    private static final int ADDED_GAME_OBJECTS_COUNT = 5;

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

    private void injectGameObjectsIn(Scene scene){
        for(int i = 0; i < SceneTest.FOO_BAR_FAMILY_COUNT; i++){
            scene.addGameObject(
                    new GameObject()
                            .addComponent(new FooComponent())
                            .addComponent(new BarComponent())
            );
        }

        for(int i = 0; i < SceneTest.FOO_BAR_BAZ_FAMILY_COUNT; i++){
            scene.addGameObject(
                    new GameObject()
                            .addComponent(new FooComponent())
                            .addComponent(new BarComponent())
                            .addComponent(new BazComponent())
            );
        }

        for(int i = 0; i < SceneTest.BAR_BAZ_FAMILY_COUNT; i++){
            scene.addGameObject(
                    new GameObject()
                            .addComponent(new BarComponent())
                            .addComponent(new BazComponent())
            );
        }

        for (int i = 0; i < SceneTest.BAR_FAMILY_COUNT; ++i){
            scene.addGameObject(
                    new GameObject()
                        .addComponent(new BarComponent())
            );
        }
    }

    @Test
    public void addTest(){
        Scene scene = new Scene();
        injectGameObjectsIn(scene);
        FamilyBuilder builder = ServiceLocator.getFamilyBuilder();
        Family allFooBarBazFamily = builder.all(
                                        FooComponent.class,
                                        BarComponent.class,
                                        BazComponent.class)
                                        .get();

        Family oneBarFamily = builder.one(BarComponent.class).get();

        ImmutableArray<GameObject> allFooBazBazGameObjects =
                scene.getGameObjectsFor(allFooBarBazFamily);

        ImmutableArray<GameObject> oneBarGameObjects =
                scene.getGameObjectsFor(oneBarFamily);

        assertEquals(SceneTest.FOO_BAR_BAZ_FAMILY_COUNT,
                allFooBazBazGameObjects.size());

        assertEquals(
                SceneTest.FOO_BAR_BAZ_FAMILY_COUNT
                    + SceneTest.BAR_FAMILY_COUNT
                    + SceneTest.BAR_BAZ_FAMILY_COUNT
                    + SceneTest.FOO_BAR_FAMILY_COUNT,
                oneBarGameObjects.size());
    }

    @Test
    public void removeTest(){
        Scene scene = new Scene();
        injectGameObjectsIn(scene);
        FamilyBuilder builder = ServiceLocator.getFamilyBuilder();

        Family oneFooBarBazFamily =
                builder.one(
                FooComponent.class,
                BarComponent.class,
                BazComponent.class)
                .get();

        ImmutableArray<GameObject> oneFooBarBazGameObjects =
                scene.getGameObjectsFor(oneFooBarBazFamily);

        assertEquals(
                SceneTest.FOO_BAR_BAZ_FAMILY_COUNT
                    + SceneTest.BAR_FAMILY_COUNT
                    + SceneTest.BAR_BAZ_FAMILY_COUNT
                    + SceneTest.FOO_BAR_FAMILY_COUNT,
                oneFooBarBazGameObjects.size());


        //Remove some game objects from the scene
        for(int i = 0; i < SceneTest.REMOVED_GAME_OBJECTS_COUNT; i++){
            GameObject gameObject = oneFooBarBazGameObjects.get(i);
            scene.removeGameObject(gameObject);
        }

        oneFooBarBazGameObjects = scene.getGameObjectsFor(oneFooBarBazFamily);

        assertEquals(
                SceneTest.FOO_BAR_BAZ_FAMILY_COUNT
                    + SceneTest.BAR_FAMILY_COUNT
                    + SceneTest.BAR_BAZ_FAMILY_COUNT
                    + SceneTest.FOO_BAR_FAMILY_COUNT
                    - SceneTest.REMOVED_GAME_OBJECTS_COUNT,
                oneFooBarBazGameObjects.size());
    }

    @Test
    public void addWhileSceneIsLockedTest(){
        Scene scene = new Scene();
        injectGameObjectsIn(scene);
        FamilyBuilder builder = ServiceLocator.getFamilyBuilder();
        Family allBarBazAndOneFooFamily =
                builder.all(
                        BarComponent.class,
                        BazComponent.class)
                        .one(FooComponent.class)
                        .get();

        ImmutableArray<GameObject> allBarBazAndOneFooGameObjects =
                scene.getGameObjectsFor(allBarBazAndOneFooFamily);

        assertEquals(
                SceneTest.FOO_BAR_BAZ_FAMILY_COUNT,
                allBarBazAndOneFooGameObjects.size());

        scene.lock();

        for(int i = 0; i < SceneTest.ADDED_GAME_OBJECTS_COUNT; i++){
            GameObject gameObject =
                    new GameObject()
                    .addComponent(new FooComponent())
                    .addComponent(new BarComponent())
                    .addComponent(new BazComponent());

            scene.addGameObject(gameObject);
        }

        for(int i = 0; i < SceneTest.ADDED_GAME_OBJECTS_COUNT; i++){
            GameObject gameObject =
                    new GameObject()
                    .addComponent(new BarComponent())
                    .addComponent(new FooComponent());

            scene.addGameObject(gameObject);
        }

        allBarBazAndOneFooGameObjects =
                scene.getGameObjectsFor(allBarBazAndOneFooFamily);

        assertEquals(
                SceneTest.FOO_BAR_BAZ_FAMILY_COUNT,
                allBarBazAndOneFooGameObjects.size());

        scene.unlock();

        allBarBazAndOneFooGameObjects =
                scene.getGameObjectsFor(allBarBazAndOneFooFamily);

        assertEquals(
                SceneTest.FOO_BAR_BAZ_FAMILY_COUNT
                + SceneTest.ADDED_GAME_OBJECTS_COUNT,
                allBarBazAndOneFooGameObjects.size());

    }

    @Test
    public void removeWhileSceneIsLockedTest(){
        Scene scene = new Scene();
        injectGameObjectsIn(scene);
        FamilyBuilder builder = ServiceLocator.getFamilyBuilder();

        Family allFooBarFamily =
                builder.all(FooComponent.class, BarComponent.class).get();

        ImmutableArray<GameObject> allFooBarGameObjects =
                scene.getGameObjectsFor(allFooBarFamily);

        assertEquals(
                SceneTest.FOO_BAR_BAZ_FAMILY_COUNT
                        + SceneTest.FOO_BAR_FAMILY_COUNT,
                allFooBarGameObjects.size());

        scene.lock();

        for(int i = 0; i < SceneTest.REMOVED_GAME_OBJECTS_COUNT; i++){
            scene.removeGameObject(
                    allFooBarGameObjects.get(i)
            );
        }

        allFooBarGameObjects =
                scene.getGameObjectsFor(allFooBarFamily);

        assertEquals(
                SceneTest.FOO_BAR_BAZ_FAMILY_COUNT
                        + SceneTest.FOO_BAR_FAMILY_COUNT,
                allFooBarGameObjects.size());

        scene.unlock();

        allFooBarGameObjects =
                scene.getGameObjectsFor(allFooBarFamily);

        assertEquals(
                SceneTest.FOO_BAR_BAZ_FAMILY_COUNT
                        + SceneTest.FOO_BAR_FAMILY_COUNT
                        - SceneTest.REMOVED_GAME_OBJECTS_COUNT,
                allFooBarGameObjects.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addNullGameObjectTest(){
        Scene scene = new Scene();
        scene.addGameObject(null);
    }
}
