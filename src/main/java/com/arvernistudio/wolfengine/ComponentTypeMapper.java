package com.arvernistudio.wolfengine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ComponentTypeMapper {
    private static final String TAG = ComponentTypeMapper.class.getSimpleName();
    private static ComponentTypeMapper _instance;
    private ObjectMap<Class<? extends Component>, Integer> _componentTypeToIndex;
    private AtomicInteger _uniqueId;

    private ComponentTypeMapper() {
        _componentTypeToIndex = new ObjectMap<>();
        _uniqueId = new AtomicInteger();
    }

    public Integer getComponentIndex(Class<? extends Component> clazz) {
        Integer componentIndex = _componentTypeToIndex.get(clazz);

        if(componentIndex == null){
            componentIndex = _uniqueId.incrementAndGet();
            _componentTypeToIndex.put(clazz, componentIndex);
        }

        Gdx.app.log(TAG, "Return index " + componentIndex + " for component class " +
            clazz.getSimpleName());

        return componentIndex;
    }

    public static ComponentTypeMapper getInstance(){
        if(_instance == null){
            synchronized (ComponentTypeMapper.class){
                if(_instance == null){
                    _instance = new ComponentTypeMapper();
                    Gdx.app.log(TAG, "ComponentTypeMapper singleton has been successfully" +
                            "instantiated.");
                }
            }
        }

        return _instance;
    }
}
