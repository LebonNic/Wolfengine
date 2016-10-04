package com.arvernistudio.wolfengine.memory;

import com.arvernistudio.wolfengine.component.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ReflectionPool;

public class ComponentPools {
    private static final String TAG = ComponentPools.class.getSimpleName();
    private static final int DEFAULT_INITIAL_POOL_SIZE = 16;
    private static final int DEFAULT_MAXIMUM_POOL_SIZE = 96;

    private ObjectMap<Class<?>, ReflectionPool> _pools;
    private int _initialSize;
    private int _maxSize;

    public ComponentPools(){
        this(ComponentPools.DEFAULT_INITIAL_POOL_SIZE,
                ComponentPools.DEFAULT_MAXIMUM_POOL_SIZE);
    }

    public ComponentPools(int initialSize, int maxSize){
        _pools = new ObjectMap<>();
        _initialSize = initialSize;
        _maxSize = maxSize;
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> T obtain(Class<T> componentType){
        ReflectionPool pool = _pools.get(componentType);
        T component;

        if(pool == null){
            pool = new ReflectionPool(componentType, _initialSize, _maxSize);
            _pools.put(componentType, pool);
            Gdx.app.log(ComponentPools.TAG,
                    "Instantiation of a new pool to handle dynamic allocation" +
                    " of \"" + componentType.getSimpleName() + "\" components.");
        }

        component = (T) pool.obtain();

        return component;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public <T extends Component> void free(T component){
        if(component == null){
            throw new IllegalArgumentException();
        }

        Class<?> clazz = component.getClass();
        ReflectionPool pool = _pools.get(clazz);

        if(pool != null){
            pool.free(component);
        } else {
            Gdx.app.log(ComponentPools.TAG, "Warning: an attempt to free a component of type \""
            + clazz.getSimpleName() + "\" has failed because no pool was found to" +
                    " manage this kind of objects.");
        }
    }

    public <T extends Component> boolean isComponentPoolCreatedFor(Class<T> componentType){
        boolean isComponentPoolCreated = false;
        ReflectionPool pool = _pools.get(componentType);

        if(pool != null){
            isComponentPoolCreated = true;
        }

        return isComponentPoolCreated;
    }

    public int getInitialSize(){
        return _initialSize;
    }

    public int getMaxSize(){
        return _maxSize;
    }
}
