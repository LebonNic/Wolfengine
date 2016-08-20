package com.arvernistudio.wolfengine.scene;

import com.arvernistudio.wolfengine.finder.Family;
import com.arvernistudio.wolfengine.finder.GameObjectFinder;
import com.arvernistudio.wolfengine.gameobject.GameObject;
import com.arvernistudio.wolfengine.utils.ImmutableArray;
import com.badlogic.gdx.utils.ObjectSet;

public class Scene {
    private ObjectSet<GameObject> _gameObjects;
    private GameObjectFinder _finder;
    private boolean _isLocked;

    public Scene(){
        _gameObjects = new ObjectSet<>();
        _finder = new GameObjectFinder(_gameObjects);
        _isLocked = false;
    }

    public ImmutableArray<GameObject> getGameObjectsFor(Family family){
        return _finder.getGameObjectsFor(family);
    }

    public void addGameObject(GameObject gameObject){
        assert gameObject != null;

        if(!_isLocked){
            _gameObjects.add(gameObject);
            _finder.addFamilyMembershipFor(gameObject);
        }
        else{
            // TODO Prepare post lock processing
        }
    }

    public void removeGameObject(GameObject gameObject){
        if(!_isLocked){
            _gameObjects.remove(gameObject);
            _finder.eraseFamilyMembershipFor(gameObject);
        }
        else {
            // TODO Prepare post lock processing
        }
    }

    public void lock(){
        _isLocked = true;
    }

    public void unlock(){
        _isLocked = false;
        // TODO Add post lock processing here
    }
}
