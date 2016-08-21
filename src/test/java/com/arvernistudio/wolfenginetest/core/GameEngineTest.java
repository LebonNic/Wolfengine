package com.arvernistudio.wolfenginetest.core;

import com.arvernistudio.wolfengine.core.GameEngine;
import com.arvernistudio.wolfengine.core.GameLogicProcessor;
import com.arvernistudio.wolfengine.core.RenderingEngine;
import com.arvernistudio.wolfengine.scene.Scene;
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

    //In milliseconds
    private static final long consumedTimeToUpdateGameState = 23;
    private static final long fixedUpdateConsumedTime = 8;
    private static final long consumedTimeToRender = 22;

    //In seconds
    private static final float delta = 0.016f;

    @Before
    public void setUp(){
        //Avoid NullPointerException when Gdx.app.log is called in the tested methods
        Gdx.app = Mockito.mock(Application.class);
    }

    @BeforeClass
    public static void initTests(){
        renderingEngine = Mockito.mock(RenderingEngine.class);
        gameLogicProcessor = Mockito.mock(GameLogicProcessor.class);

        when(renderingEngine.render(any(Scene.class))).thenReturn(
                TimeUnit.MILLISECONDS.toNanos(consumedTimeToRender));

        when(gameLogicProcessor.fixedUpdate(any(Scene.class))).thenReturn(
                TimeUnit.MILLISECONDS.toNanos(fixedUpdateConsumedTime));

        when(gameLogicProcessor.update(any(Scene.class))).thenReturn(
                TimeUnit.MILLISECONDS.toNanos(consumedTimeToUpdateGameState));
    }

    @Test
    public void instantiationTest(){
        GameEngine gameEngine = new GameEngine(renderingEngine, gameLogicProcessor);

        assertEquals(0, gameEngine.getFixedUpdatesCountPerFame());
        assertEquals(0, gameEngine.getFixedUpdateTime());
        assertEquals(0, gameEngine.getUpdateGameStateTime());
        assertEquals(0, gameEngine.getTimeToRender());
        assertEquals(0, gameEngine.getTotalUpdateTime());
    }

    @Test
    public void updateMethodTest(){
        GameEngine gameEngine = new GameEngine(renderingEngine, gameLogicProcessor);
        gameEngine.update(GameEngineTest.delta);

        assertEquals(TimeUnit.MILLISECONDS.toNanos(fixedUpdateConsumedTime),
                gameEngine.getFixedUpdateTime());

        assertEquals(TimeUnit.MILLISECONDS.toNanos(consumedTimeToUpdateGameState),
                gameEngine.getUpdateGameStateTime());

        assertEquals(TimeUnit.MILLISECONDS.toNanos(consumedTimeToRender),
                gameEngine.getTimeToRender());

        assertEquals(TimeUnit.MILLISECONDS.toNanos(fixedUpdateConsumedTime
                + consumedTimeToRender + consumedTimeToUpdateGameState),
                gameEngine.getTotalUpdateTime());

        assertEquals(1, gameEngine.getFixedUpdatesCountPerFame());
    }

    @Test
    public void adaptationToLowFrameRateTest(){
        GameEngine gameEngine = new GameEngine(renderingEngine, gameLogicProcessor);
        gameEngine.update(GameEngineTest.delta * 2);

        assertEquals(2, gameEngine.getFixedUpdatesCountPerFame());

        assertEquals(TimeUnit.MILLISECONDS.toNanos(2 * fixedUpdateConsumedTime),
                gameEngine.getFixedUpdateTime());

        assertEquals(TimeUnit.MILLISECONDS.toNanos(consumedTimeToUpdateGameState),
                gameEngine.getUpdateGameStateTime());

        assertEquals(TimeUnit.MILLISECONDS.toNanos(consumedTimeToRender),
                gameEngine.getTimeToRender());
    }

    @Test
    public void adaptationToHighFrameRateTest(){
        GameEngine gameEngine = new GameEngine(renderingEngine, gameLogicProcessor);
        gameEngine.update(GameEngineTest.delta / 2f);

        assertEquals(0, gameEngine.getFixedUpdatesCountPerFame());
        assertEquals(0, gameEngine.getFixedUpdateTime());
        assertEquals(0, gameEngine.getUpdateGameStateTime());
    }

    @Test
    public void avoidInfiniteLoopTest(){
        GameEngine gameEngine = new GameEngine(renderingEngine, gameLogicProcessor);
        gameEngine.update(GameEngineTest.delta * (GameEngine.MAX_UPDATE_ITERATION + 1));

        assertEquals(GameEngine.MAX_UPDATE_ITERATION, gameEngine.getFixedUpdatesCountPerFame());
    }

    @Test(expected=IllegalArgumentException.class)
    public void badArgumentsOnInstantiationTest(){
        GameEngine engine = new GameEngine(null, null);
    }
}
