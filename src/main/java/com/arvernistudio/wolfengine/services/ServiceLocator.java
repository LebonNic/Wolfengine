package com.arvernistudio.wolfengine.services;

import com.arvernistudio.wolfengine.mapper.ComponentMapper;
import com.arvernistudio.wolfengine.mapper.ObjectMapComponentMapper;
import com.badlogic.gdx.Gdx;

public class ServiceLocator {
    private static final String TAG = ServiceLocator.class.getSimpleName();
    private static ComponentMapper _componentMapper;

    static {
        Gdx.app.log(TAG, "Instantiate " + ObjectMapComponentMapper.class.getSimpleName() +
        " to handle services of " + ComponentMapper.class.getSimpleName());
        _componentMapper = new ObjectMapComponentMapper();
    }

    public static ComponentMapper getComponentTypeMapper(){
        return _componentMapper;
    }
}
