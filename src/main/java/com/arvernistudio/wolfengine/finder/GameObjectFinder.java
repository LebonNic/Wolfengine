package com.arvernistudio.wolfengine.finder;

import com.arvernistudio.wolfengine.gameobject.GameObject;
import com.arvernistudio.wolfengine.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;

public class GameObjectFinder {
    private static final String TAG = GameObjectFinder.class.getSimpleName();
    private ObjectMap<Family, Array<GameObject>> _families;
    private ObjectMap<Family, ImmutableArray<GameObject>> _exposedFamilies;
    private ObjectSet<GameObject> _gameObjects;

    public GameObjectFinder(ObjectSet<GameObject> gameObjects){
        assert gameObjects != null;

        _gameObjects = gameObjects;
        _families = new ObjectMap<>();
        _exposedFamilies = new ObjectMap<>();
    }

    public ImmutableArray<GameObject> getGameObjectsFor(Family family){
        ImmutableArray<GameObject> exposedGameObjectsInFamily =
                _exposedFamilies.get(family);

        if(exposedGameObjectsInFamily == null){
            Array<GameObject> gameObjectsInFamily = new Array<>();
            exposedGameObjectsInFamily = new ImmutableArray<>(gameObjectsInFamily);

            for(GameObject gameObject : _gameObjects){
                if(family.matchWith(gameObject)){
                    gameObjectsInFamily.add(gameObject);
                    gameObject.toggleFamilyMembership(family);
                }
            }

            Gdx.app.log(GameObjectFinder.TAG, "Put the family with the following key in cache: "
            + family.getFamilyKey());
            _families.put(family, gameObjectsInFamily);
            _exposedFamilies.put(family, exposedGameObjectsInFamily);
        }

        return exposedGameObjectsInFamily;
    }

    public void addFamilyMembershipFor(GameObject gameObject){
        for (ObjectMap.Entry<Family, Array<GameObject>> familyToGameObjects : _families) {
            Family family = familyToGameObjects.key;

            if(family.matchWith(gameObject) && !gameObject.isFamilyMembershipToggledFor(family)){
                Array<GameObject> gameObjects = familyToGameObjects.value;
                gameObjects.add(gameObject);
                gameObject.toggleFamilyMembership(family);
            }
        }
    }

    public void eraseFamilyMembershipFor(GameObject gameObject){
        for (ObjectMap.Entry<Family, Array<GameObject>> familyToGameObjects : _families) {
            Family family = familyToGameObjects.key;

            if (family.matchWith(gameObject) && gameObject.isFamilyMembershipToggledFor(family)) {
                Array<GameObject> gameObjects = familyToGameObjects.value;
                gameObjects.removeValue(gameObject, true);
                gameObject.toggleFamilyMembership(family);
            }
        }
    }
}
