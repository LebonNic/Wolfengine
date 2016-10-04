package com.arvernistudio.wolfengine.scene;

import com.arvernistudio.wolfengine.finder.Family;
import com.arvernistudio.wolfengine.finder.GameObjectFinder;
import com.arvernistudio.wolfengine.gameobject.GameObject;
import com.arvernistudio.wolfengine.utils.ImmutableArray;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;

public class Scene {
    private ObjectSet<GameObject> _gameObjects;
    private GameObjectFinder _finder;
    private boolean _isLocked;
    private OperationPool _operationPool;
    private Array<PendingOperation> _pendingOperations;

    public Scene(){
        _gameObjects = new ObjectSet<>();
        _finder = new GameObjectFinder(_gameObjects);
        _isLocked = false;
        _operationPool = new OperationPool();
        _pendingOperations = new Array<>();
    }

    public ImmutableArray<GameObject> getGameObjectsFor(Family family){
        return _finder.getGameObjectsFor(family);
    }

    public void addGameObject(GameObject gameObject){
        if(gameObject == null){
            throw new IllegalArgumentException("The parameter gameObject cannot be null.");
        }

        if(!_isLocked){
            addInternal(gameObject);
        }
        else{
            PendingOperation addOperation = _operationPool.obtain();
            addOperation.type = PendingOperation.OperationType.AddOperation;
            addOperation.gameObject = gameObject;
            _pendingOperations.add(addOperation);
        }
    }

    private void addInternal(GameObject gameObject){
        _gameObjects.add(gameObject);
        _finder.addFamilyMembershipFor(gameObject);
    }

    public void removeGameObject(GameObject gameObject){
        if(!_isLocked){
            removeInternal(gameObject);
        }
        else {
            PendingOperation removeOperation = _operationPool.obtain();
            removeOperation.type = PendingOperation.OperationType.RemoveOperation;
            removeOperation.gameObject = gameObject;
            _pendingOperations.add(removeOperation);
        }
    }

    private void removeInternal(GameObject gameObject){
        _gameObjects.remove(gameObject);
        _finder.eraseFamilyMembershipFor(gameObject);
    }

    public void lock(){
        _isLocked = true;
    }

    public void unlock(){
        for (PendingOperation operation : _pendingOperations) {
            switch (operation.type){
                case AddOperation:
                    addInternal(operation.gameObject);
                    break;
                case RemoveOperation:
                    removeInternal(operation.gameObject);
                    break;
            }
            _operationPool.free(operation);
        }

        _pendingOperations.clear();
        _isLocked = false;
    }
}
