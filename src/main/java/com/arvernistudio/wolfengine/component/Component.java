package com.arvernistudio.wolfengine.component;

import com.arvernistudio.wolfengine.gameobject.GameObject;

public abstract class Component {
    private GameObject _container;

    public Component(){
        _container = null;
    }

    public void setContainer(GameObject gameObject){
        assert gameObject != null;
        _container = gameObject;
    }

    public GameObject getContainer(){
        return _container;
    }
}
