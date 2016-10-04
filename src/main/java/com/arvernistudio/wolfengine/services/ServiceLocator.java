package com.arvernistudio.wolfengine.services;

import com.arvernistudio.wolfengine.finder.FamilyBuilder;
import com.arvernistudio.wolfengine.gameobject.GameObject;
import com.arvernistudio.wolfengine.mapper.ComponentMapper;
import com.arvernistudio.wolfengine.mapper.ObjectMapComponentMapper;
import com.arvernistudio.wolfengine.memory.ComponentPools;
import com.arvernistudio.wolfengine.memory.GameObjectPool;
import com.badlogic.gdx.Gdx;

public class ServiceLocator {
    private static final String TAG = ServiceLocator.class.getSimpleName();
    private static ComponentMapper _componentMapper;
    private static FamilyBuilder _familyBuilder;
    private static GameObjectPool _gameObjectPool;
    private static ComponentPools _componentPools;

    static {
        Gdx.app.log(ServiceLocator.TAG, "Instantiate " + ObjectMapComponentMapper.class.getSimpleName() +
        " to handle services of " + ComponentMapper.class.getSimpleName());
        ServiceLocator._componentMapper = new ObjectMapComponentMapper();

        Gdx.app.log(ServiceLocator.TAG, "Instantiate " + FamilyBuilder.class.getSimpleName() +
                " to handle services of " + FamilyBuilder.class.getSimpleName());
        ServiceLocator._familyBuilder = new FamilyBuilder();

        Gdx.app.log(ServiceLocator.TAG, "Instantiate " + GameObjectPool.class.getSimpleName() +
                " to handle services of " + GameObjectPool.class.getSimpleName());
        ServiceLocator._gameObjectPool = new GameObjectPool();

        Gdx.app.log(ServiceLocator.TAG, "Instantiate " + ComponentPools.class.getSimpleName() +
                " to handle services of " + ComponentPools.class.getSimpleName());
        ServiceLocator._componentPools = new ComponentPools();
    }

    public static ComponentMapper getComponentTypeMapper(){
        return ServiceLocator._componentMapper;
    }

    public static FamilyBuilder getFamilyBuilder(){
        return ServiceLocator._familyBuilder;
    }

    public static GameObjectPool getGameObjectPool(){
        return ServiceLocator._gameObjectPool; }

    public static ComponentPools getComponentPools(){
        return ServiceLocator._componentPools;
    }

    public static void injectComponentMapper(ComponentMapper mapper){
        if(mapper == null){
            throw new IllegalArgumentException("The parameter mapper cannot be null.");
        }
        ServiceLocator._componentMapper = mapper;
        Gdx.app.log(ServiceLocator.TAG, "A new component mapper has been injected");
    }

    public static void injectFamilyBuilder(FamilyBuilder familyBuilder){
        if(familyBuilder == null){
            throw new IllegalArgumentException("The parameter familyBuilder cannot be null.");
        }
        ServiceLocator._familyBuilder = familyBuilder;
        Gdx.app.log(ServiceLocator.TAG, "A new family builder has been injected");
    }

    public static void injectGameObjectPool(GameObjectPool gameObjectPool){
        if(_gameObjectPool == null){
            throw new IllegalArgumentException("The parameter gameObjectPool cannot be null.");
        }
        ServiceLocator._gameObjectPool = gameObjectPool;
        Gdx.app.log(ServiceLocator.TAG, "A new game object pool has been injected");
    }

    public static void injectComponentPools(ComponentPools componentPools){
        if(componentPools == null){
            throw new IllegalArgumentException("The parameter componentPools cannot be null.");
        }
        ServiceLocator._componentPools = componentPools;
        Gdx.app.log(ServiceLocator.TAG, "New component pools have been injected");
    }
}
