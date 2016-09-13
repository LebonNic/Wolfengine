package com.arvernistudio.wolfengine.core;

import com.arvernistudio.wolfengine.finder.Family;
import com.arvernistudio.wolfengine.gameobject.GameObject;
import com.arvernistudio.wolfengine.scene.Scene;
import com.arvernistudio.wolfengine.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;

public class GameEngine {
    public enum EngineState{
        Running,
        Stopped
    }

    private static final long NS_PER_FIXED_UPDATE = 16000000;
    public static final int MAX_UPDATE_ITERATION = 10;
    private static final String TAG = GameEngine.class.getSimpleName();

    private int _fixedUpdatesCountPerFrame;
    private long _amountOfRealTimeToCatchUp;
    private long _fixedUpdateTime;
    private long _updateGameStateTime;
    private long _timeToRender;
    private long _inputsProcessingTime;
    private long _totalUpdateTime;

    private RenderingEngine _renderingEngine;
    private GameLogicProcessor _gameLogicProcessor;
    private InputsProcessor _inputsProcessor;
    private Scene _currentScene;
    private EngineState _engineState;

    public GameEngine(RenderingEngine renderingEngine, GameLogicProcessor gameLogicProcessor,
                      InputsProcessor inputsProcessor){

        if(renderingEngine != null && gameLogicProcessor != null
                && inputsProcessor != null){
            _fixedUpdatesCountPerFrame = 0;
            _amountOfRealTimeToCatchUp = 0;
            _fixedUpdateTime = 0;
            _updateGameStateTime = 0;
            _timeToRender = 0;
            _inputsProcessingTime = 0;
            _totalUpdateTime = 0;

            _renderingEngine = renderingEngine;
            _gameLogicProcessor = gameLogicProcessor;
            _inputsProcessor = inputsProcessor;
            _currentScene = new Scene();
            _engineState = EngineState.Stopped;
        }
        else{
            throw new IllegalArgumentException();
        }
    }

    public void start(){
        Gdx.app.log(GameEngine.TAG, "Game engine starts...");
        _gameLogicProcessor.start(_currentScene);
        _engineState = EngineState.Running;
    }

    public void stop(){
        Gdx.app.log(GameEngine.TAG, "Game engine stops...");
        _engineState = EngineState.Stopped;
    }

    public EngineState getEngineState(){
        return _engineState;
    }

    public EngineState update(float delta){
        int i = 0;
        _amountOfRealTimeToCatchUp += delta * 1e9;
        _fixedUpdatesCountPerFrame = 0;
        _fixedUpdateTime = 0;
        _currentScene.lock();

        if(_engineState == EngineState.Running &&
                _amountOfRealTimeToCatchUp >= GameEngine.NS_PER_FIXED_UPDATE){

            _inputsProcessingTime = processUserInputs();
            while(_amountOfRealTimeToCatchUp >= GameEngine.NS_PER_FIXED_UPDATE &&
                    i < GameEngine.MAX_UPDATE_ITERATION) {
                _fixedUpdateTime += fixedUpdate();
                _amountOfRealTimeToCatchUp -= GameEngine.NS_PER_FIXED_UPDATE;
                ++i;
            }
            _updateGameStateTime = updateGameState();
        }

        _timeToRender = render();
        _currentScene.unlock();

        _totalUpdateTime = _inputsProcessingTime + _fixedUpdateTime
                + _updateGameStateTime + _timeToRender;

        return _engineState;
    }

    public void addGameObjectToCurrentScene(GameObject gameObject){
        _currentScene.addGameObject(gameObject);
    }

    public void removeGameObjectFromCurrentScene(GameObject gameObject){
        _currentScene.removeGameObject(gameObject);
    }

    public ImmutableArray<GameObject> getGameObjectsFor(Family family){
        return _currentScene.getGameObjectsFor(family);
    }

    private long fixedUpdate(){
        _fixedUpdatesCountPerFrame += 1;

        return _gameLogicProcessor.fixedUpdate(_currentScene);
    }

    private long updateGameState(){
        return _gameLogicProcessor.update(_currentScene);
    }

    private long render(){
        return _renderingEngine.render(_currentScene);
    }

    private long processUserInputs(){
        return _inputsProcessor.processUserInputs(_currentScene);
    }

    public int getFixedUpdatesCountPerFame(){ return _fixedUpdatesCountPerFrame; }
    public long getTotalUpdateTime(){ return _totalUpdateTime; }
    public long getFixedUpdateTime(){ return _fixedUpdateTime;}
    public long getInputsProcessingTime(){ return _inputsProcessingTime; }
    public long getUpdateGameStateTime() { return _updateGameStateTime; }
    public long getTimeToRender(){ return _timeToRender; }
}
