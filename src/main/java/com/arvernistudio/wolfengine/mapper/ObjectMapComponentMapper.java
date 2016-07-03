package com.arvernistudio.wolfengine.mapper;

import com.arvernistudio.wolfengine.component.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ObjectMapComponentMapper implements ComponentMapper {
    private static final String TAG = ObjectMapComponentMapper.class.getSimpleName();
    private ObjectMap<Class<? extends Component>, Integer> _componentTypeToIndex;
    private AtomicInteger _uniqueId;

    public ObjectMapComponentMapper() {
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
}
