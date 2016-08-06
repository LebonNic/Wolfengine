package com.arvernistudio.wolfengine.gameobject;

import com.arvernistudio.wolfengine.component.Component;
import com.arvernistudio.wolfengine.services.ServiceLocator;
import com.arvernistudio.wolfengine.utils.Bag;
import com.arvernistudio.wolfengine.utils.ImmutableArray;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Bits;

public class GameObject {
    private static int _instanceCounter = 0;

    private int _id;
    private Bag<Component> _components;
    private Array<Component> _componentsArray;
    private ImmutableArray<Component> _exposedComponentsArray;
    private Bits _componentsMask;

    public GameObject(){
        GameObject._instanceCounter += 1;
        _id = GameObject._instanceCounter;
        _components = new Bag<>();
        _componentsArray = new Array<>(false, 16);
        _exposedComponentsArray = new ImmutableArray<>(_componentsArray);
        _componentsMask = new Bits();
    }

    public int getId(){
        return _id;
    }

    public static int getInstanceCounterValue(){
        return GameObject._instanceCounter;
    }

    public <T extends Component> GameObject addComponent(T component){
        assert component != null;
        Class <? extends Component> clazz = component.getClass();
        int  componentIndex = ServiceLocator.getComponentTypeMapper().getComponentIndex(clazz);
        component.setContainer(this);
        _components.set(componentIndex, component);
        _componentsArray.add(component);
        _componentsMask.set(componentIndex);

        return this;
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(int componentTypeIndex){
        T component = null;

        if(_components.isIndexWithinBounds(componentTypeIndex)){
            component = (T) _components.get(componentTypeIndex);
        }

        return component;
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(Class<? extends Component> clazz){
        int componentIndex = ServiceLocator.getComponentTypeMapper().getComponentIndex(clazz);
        return getComponent(componentIndex);
    }

    public ImmutableArray<Component> getComponents(){
        return _exposedComponentsArray;
    }

    public Bits getComponentsMask(){
        return _componentsMask;
    }
}
