package com.arvernistudio.wolfengine.mapper;

import com.arvernistudio.wolfengine.component.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;

public class ObjectMapComponentMapper implements ComponentMapper {
    private static final String TAG = ObjectMapComponentMapper.class.getSimpleName();
    private ObjectMap<Class<? extends Component>, Integer> _componentTypeToIndex;
    private int _uniqueId;

    public ObjectMapComponentMapper() {
        _componentTypeToIndex = new ObjectMap<>();
        _uniqueId = 0;
    }

    public Integer getComponentIndex(Class<? extends Component> clazz) {
        Integer componentIndex = _componentTypeToIndex.get(clazz);

        if(componentIndex == null){
            componentIndex = _uniqueId;
            _componentTypeToIndex.put(clazz, componentIndex);
            _uniqueId += 1;

            Gdx.app.log(ObjectMapComponentMapper.TAG,
                    "Return index " + componentIndex + " for component class " +
                            clazz.getSimpleName());
        }

        return componentIndex;
    }
}
