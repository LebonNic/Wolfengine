package com.arvernistudio.wolfengine.gameobject;

import com.arvernistudio.wolfengine.component.Component;
import com.arvernistudio.wolfengine.services.ServiceLocator;
import com.arvernistudio.wolfengine.utils.Bag;

public class GameObject {
    private static int _instanceCounter = 0;

    private int _id;
    private Bag<Component> _components;

    public GameObject(){
        GameObject._instanceCounter += 1;
        _id = GameObject._instanceCounter;
        _components = new Bag<>();
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
}
