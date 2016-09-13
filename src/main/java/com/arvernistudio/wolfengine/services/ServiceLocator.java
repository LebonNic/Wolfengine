package com.arvernistudio.wolfengine.services;

import com.arvernistudio.wolfengine.finder.FamilyBuilder;
import com.arvernistudio.wolfengine.mapper.ComponentMapper;
import com.arvernistudio.wolfengine.mapper.ObjectMapComponentMapper;
import com.badlogic.gdx.Gdx;

public class ServiceLocator {
    private static final String TAG = ServiceLocator.class.getSimpleName();
    private static ComponentMapper _componentMapper;
    private static FamilyBuilder _familyBuilder;

    static {
        Gdx.app.log(ServiceLocator.TAG, "Instantiate " + ObjectMapComponentMapper.class.getSimpleName() +
        " to handle services of " + ComponentMapper.class.getSimpleName());
        _componentMapper = new ObjectMapComponentMapper();

        Gdx.app.log(ServiceLocator.TAG, "Instantiate " + FamilyBuilder.class.getSimpleName() +
                " to handle services of " + FamilyBuilder.class.getSimpleName());
        _familyBuilder = new FamilyBuilder();
    }

    public static ComponentMapper getComponentTypeMapper(){
        return ServiceLocator._componentMapper;
    }

    public static FamilyBuilder getFamilyBuilder(){
        return ServiceLocator._familyBuilder;
    }

    public static void injectComponentMapper(ComponentMapper mapper){
        assert mapper != null;
        ServiceLocator._componentMapper = mapper;
        Gdx.app.log(ServiceLocator.TAG, "A new component mapper has been injected");
    }

    public static void injectFamilyBuilder(FamilyBuilder familyBuilder){
        assert familyBuilder != null;
        ServiceLocator._familyBuilder = familyBuilder;
        Gdx.app.log(ServiceLocator.TAG, "A new family builder has been injected");
    }
}
