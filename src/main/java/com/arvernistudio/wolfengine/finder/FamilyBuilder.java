package com.arvernistudio.wolfengine.finder;

import com.arvernistudio.wolfengine.component.Component;
import com.arvernistudio.wolfengine.mapper.ComponentMapper;
import com.arvernistudio.wolfengine.services.ServiceLocator;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Bits;
import com.badlogic.gdx.utils.ObjectMap;

public class FamilyBuilder {
    private static final String TAG = FamilyBuilder.class.getSimpleName();
    private ObjectMap<String, Family> _families;
    private Bits _all;
    private Bits _one;

    public FamilyBuilder(){
        _families = new ObjectMap<>();
        _all = new Bits();
        _one = new Bits();
    }

    @SafeVarargs
    public final FamilyBuilder all(Class<? extends Component>... componentTypes){
        _all = getComponentsMaskFor(componentTypes);

        return this;
    }

    @SafeVarargs
    public final FamilyBuilder one(Class<? extends Component>... componentTypes){
        _one = getComponentsMaskFor(componentTypes);

        return this;
    }

    public Family get(){
        String familyKey = Family.getFamilyKey(_all, _one);
        Family family = _families.get(familyKey);

        if(family == null) {
            family = new Family(_all, _one);
            _families.put(familyKey, family);
            Gdx.app.log(FamilyBuilder.TAG, "Create a new family with the following key:"
            + familyKey);
        }

        resetBuilder();

        return family;
    }

    private void resetBuilder(){
        _all.clear();
        _one.clear();
    }

    @SafeVarargs
    private final Bits getComponentsMaskFor(Class<? extends Component>... componentTypes){
        ComponentMapper mapper = ServiceLocator.getComponentTypeMapper();
        Bits componentsMask = new Bits();

        for (Class<? extends Component> componentType : componentTypes) {
            componentsMask.set(mapper.getComponentIndex(componentType));
        }

        return componentsMask;
    }
}
