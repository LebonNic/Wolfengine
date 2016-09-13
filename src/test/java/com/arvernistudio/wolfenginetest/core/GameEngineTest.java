package com.arvernistudio.wolfenginetest.core;

import com.arvernistudio.wolfengine.component.Component;
import com.arvernistudio.wolfengine.core.GameEngine;
import com.arvernistudio.wolfengine.core.GameLogicProcessor;
import com.arvernistudio.wolfengine.core.InputsProcessor;
import com.arvernistudio.wolfengine.core.RenderingEngine;
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
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class GameEngineTest {
    private static RenderingEngine renderingEngine;
    private static GameLogicProcessor gameLogicProcessor;
    private static InputsProcessor inputsProcessor;

    private class FooComponent extends Component {}

    private class BarComponent extends Component{}

    private class BazComponent extends Component{}


    //In milliseconds
    private static final long consumedTimeToProcessUserInputs = 10;
    private static final long consumedTimeToUpdateGameState = 23;
    private static final long fixedUpdateConsumedTime = 8;
    private static final long consumedTimeToRender = 22;

    //In seconds
    private static final float delta = 0.016f;

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

    @BeforeClass
    public static void initTests(){
        renderingEngine = Mockito.mock(RenderingEngine.class);
        gameLogicProcessor = Mockito.mock(GameLogicProcessor.class);
        inputsProcessor = Mockito.mock(InputsProcessor.class);

        when(renderingEngine.render(any(Scene.class))).thenReturn(
                TimeUnit.MILLISECONDS.toNanos(consumedTimeToRender));

        when(gameLogicProcessor.fixedUpdate(any(Scene.class))).thenReturn(
                TimeUnit.MILLISECONDS.toNanos(fixedUpdateConsumedTime));

        when(gameLogicProcessor.update(any(Scene.class))).thenReturn(
                TimeUnit.MILLISECONDS.toNanos(consumedTimeToUpdateGameState));

        when(inputsProcessor.processUserInputs(any(Scene.class))).thenReturn(
                TimeUnit.MILLISECONDS.toNanos(consumedTimeToProcessUserInputs));
    }

    @Test
    public void instantiationTest(){
        GameEngine gameEngine = new GameEngine(
                renderingEngine, gameLogicProcessor, inputsProcessor);

        assertEquals(0, gameEngine.getFixedUpdatesCountPerFame());
        assertEquals(0, gameEngine.getInputsProcessingTime());
        assertEquals(0, gameEngine.getFixedUpdateTime());
        assertEquals(0, gameEngine.getUpdateGameStateTime());
        assertEquals(0, gameEngine.getTimeToRender());
        assertEquals(0, gameEngine.getTotalUpdateTime());
        assertEquals(GameEngine.EngineState.Stopped, gameEngine.getEngineState());
    }

    @Test
    public void updateMethodTest(){
        GameEngine gameEngine = new GameEngine(
                renderingEngine, gameLogicProcessor, inputsProcessor);
        gameEngine.start();
        gameEngine.update(GameEngineTest.delta);

        assertEquals(TimeUnit.MILLISECONDS.toNanos(consumedTimeToProcessUserInputs),
                gameEngine.getInputsProcessingTime());

        assertEquals(TimeUnit.MILLISECONDS.toNanos(fixedUpdateConsumedTime),
                gameEngine.getFixedUpdateTime());

        assertEquals(TimeUnit.MILLISECONDS.toNanos(consumedTimeToUpdateGameState),
                gameEngine.getUpdateGameStateTime());

        assertEquals(TimeUnit.MILLISECONDS.toNanos(consumedTimeToRender),
                gameEngine.getTimeToRender());

        assertEquals(TimeUnit.MILLISECONDS.toNanos(
                consumedTimeToProcessUserInputs + fixedUpdateConsumedTime
                + consumedTimeToRender + consumedTimeToUpdateGameState),
                gameEngine.getTotalUpdateTime());

        assertEquals(1, gameEngine.getFixedUpdatesCountPerFame());
    }

    @Test
    public void adaptationToLowFrameRateTest(){
        GameEngine gameEngine = new GameEngine(
                renderingEngine, gameLogicProcessor, inputsProcessor);
        gameEngine.start();
        gameEngine.update(GameEngineTest.delta * 2);

        assertEquals(2, gameEngine.getFixedUpdatesCountPerFame());

        assertEquals(TimeUnit.MILLISECONDS.toNanos(consumedTimeToProcessUserInputs),
                gameEngine.getInputsProcessingTime());

        assertEquals(TimeUnit.MILLISECONDS.toNanos(2 * fixedUpdateConsumedTime),
                gameEngine.getFixedUpdateTime());

        assertEquals(TimeUnit.MILLISECONDS.toNanos(consumedTimeToUpdateGameState),
                gameEngine.getUpdateGameStateTime());

        assertEquals(TimeUnit.MILLISECONDS.toNanos(consumedTimeToRender),
                gameEngine.getTimeToRender());
    }

    @Test
    public void adaptationToHighFrameRateTest(){
        GameEngine gameEngine = new GameEngine(
                renderingEngine, gameLogicProcessor, inputsProcessor);
        gameEngine.start();
        gameEngine.update(GameEngineTest.delta / 2.0f);

        assertEquals(0, gameEngine.getFixedUpdatesCountPerFame());
        assertEquals(0, gameEngine.getInputsProcessingTime());
        assertEquals(0, gameEngine.getFixedUpdateTime());
        assertEquals(0, gameEngine.getUpdateGameStateTime());
        assertEquals(TimeUnit.MILLISECONDS.toNanos(consumedTimeToRender),
                gameEngine.getTimeToRender());
    }

    @Test
    public void avoidInfiniteLoopTest(){
        GameEngine gameEngine = new GameEngine(
                renderingEngine, gameLogicProcessor, inputsProcessor);
        gameEngine.start();
        gameEngine.update(GameEngineTest.delta * (GameEngine.MAX_UPDATE_ITERATION + 1));

        assertEquals(GameEngine.MAX_UPDATE_ITERATION, gameEngine.getFixedUpdatesCountPerFame());
    }

    @Test(expected=IllegalArgumentException.class)
    public void badArgumentsOnInstantiationTest(){
        GameEngine engine = new GameEngine(null, null, null);
    }

    @Test
    public void startStopTest(){
        GameEngine gameEngine = new GameEngine(
                renderingEngine, gameLogicProcessor, inputsProcessor);

        assertEquals(GameEngine.EngineState.Stopped, gameEngine.getEngineState());
        gameEngine.start();
        assertEquals(GameEngine.EngineState.Running, gameEngine.getEngineState());
        gameEngine.stop();
        assertEquals(GameEngine.EngineState.Stopped, gameEngine.getEngineState());
    }

    @Test
    public void updateWhileStoppedTest(){
        GameEngine gameEngine = new GameEngine(
                renderingEngine, gameLogicProcessor, inputsProcessor);

        gameEngine.start();
        gameEngine.stop();
        gameEngine.update(GameEngineTest.delta);

        assertEquals(0, gameEngine.getInputsProcessingTime());
        assertEquals(0, gameEngine.getFixedUpdatesCountPerFame());
        assertEquals(0, gameEngine.getUpdateGameStateTime());
        assertEquals(TimeUnit.MILLISECONDS.toNanos(consumedTimeToRender),
                gameEngine.getTimeToRender());
    }

    @Test
    public void addAndRemoveGameObjectsTest(){
        GameEngine gameEngine = new GameEngine(
                renderingEngine, gameLogicProcessor, inputsProcessor);

        gameEngine.addGameObjectToCurrentScene(
                new GameObject()
                        .addComponent(new FooComponent())
                        .addComponent(new BarComponent())
        );

        FamilyBuilder builder = ServiceLocator.getFamilyBuilder();
        Family fooBarFamily = builder.all(FooComponent.class, BarComponent.class).get();
        ImmutableArray<GameObject> fooBarObjects = gameEngine.getGameObjectsFor(fooBarFamily);
        assertEquals(1, fooBarObjects.size());

        GameObject gameObject = fooBarObjects.first();
        gameEngine.removeGameObjectFromCurrentScene(gameObject);
        fooBarObjects = gameEngine.getGameObjectsFor(fooBarFamily);
        assertEquals(0, fooBarObjects.size());
    }
}
