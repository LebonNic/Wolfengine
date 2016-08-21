package com.arvernistudio.wolfengine.core;

import com.arvernistudio.wolfengine.gameobject.GameObject;
import com.arvernistudio.wolfengine.scene.Scene;

public class GameEngine {
    public static final long NS_PER_FIXED_UPDATE = 16000000;
    public static final int MAX_UPDATE_ITERATION = 10;

    private int _fixedUpdatesCountPerFrame;
    private long _amountOfRealTimeToCatchUp;
    private long _fixedUpdateTime;
    private long _updateGameStateTime;
    private long _timeToRender;
    private long _totalUpdateTime;

    private RenderingEngine _renderingEngine;
    private GameLogicProcessor _gameLogicProcessor;
    private Scene _currentScene;

    public GameEngine(RenderingEngine renderingEngine, GameLogicProcessor gameLogicProcessor){

        if(renderingEngine != null && gameLogicProcessor != null){
            _fixedUpdatesCountPerFrame = 0;
            _amountOfRealTimeToCatchUp = 0;
            _fixedUpdateTime = 0;
            _updateGameStateTime = 0;
            _timeToRender = 0;
            _totalUpdateTime = 0;

            _renderingEngine = renderingEngine;
            _gameLogicProcessor = gameLogicProcessor;
            _currentScene = new Scene();
        }
        else{
            throw new IllegalArgumentException();
        }
    }

    public void start(){
        _gameLogicProcessor.start(_currentScene);
    }

    public void update(float delta){
        int i = 0;
        _amountOfRealTimeToCatchUp += delta * 1e9;
        _fixedUpdatesCountPerFrame = 0;
        _fixedUpdateTime = 0;
        _currentScene.lock();

        if(_amountOfRealTimeToCatchUp >= GameEngine.NS_PER_FIXED_UPDATE){
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

        _totalUpdateTime = + _fixedUpdateTime + _updateGameStateTime +
                _timeToRender;
    }

    public void addGameObjectToCurrentScene(GameObject gameObject){
        _currentScene.addGameObject(gameObject);
    }

    public void removeGameObjectFromCurrentScene(GameObject gameObject){
        _currentScene.removeGameObject(gameObject);
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

    public int getFixedUpdatesCountPerFame(){ return _fixedUpdatesCountPerFrame; }
    public long getTotalUpdateTime(){ return _totalUpdateTime; }
    public long getFixedUpdateTime(){ return _fixedUpdateTime;}
    public long getUpdateGameStateTime() { return _updateGameStateTime; }
    public long getTimeToRender(){ return _timeToRender; }
}
