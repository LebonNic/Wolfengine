package com.arvernistudio.wolfengine.finder;

import com.arvernistudio.wolfengine.gameobject.GameObject;
import com.badlogic.gdx.utils.Bits;
import com.badlogic.gdx.utils.StringBuilder;

public class Family {
    private static int _instanceCounter = 0;
    private Bits _all;
    private Bits _one;
    private int _id;

    public Family(Bits all, Bits one){
        assert all != null;
        assert one != null;

        _id = Family._instanceCounter;
        Family._instanceCounter++;

        _all = copyBitSet(all);
        _one = copyBitSet(one);
    }

    private Bits copyBitSet(Bits inputBitSet){
        Bits outBitSet = new Bits();
        int setLength = inputBitSet.length();

        for (int i = 0; i < setLength; i++){
            if(inputBitSet.get(i)){
                outBitSet.set(i);
            }
        }

        return outBitSet;
    }

    public boolean matchWith(GameObject gameObject){
        boolean isGoMatchingWithFamily = true;
        Bits componentsMask = gameObject.getComponentsMask();

        if(!componentsMask.containsAll(_all)){
            isGoMatchingWithFamily = false;
        }

        if(!_one.isEmpty() &&
                !componentsMask.intersects(_one)){
            isGoMatchingWithFamily = false;
        }

        return isGoMatchingWithFamily;
    }

    public String getFamilyKey(){
        return Family.getFamilyKey(_all, _one);
    }

    public static String getFamilyKey(Bits all, Bits one){
        StringBuilder stringBuilder = new StringBuilder();

        if(!all.isEmpty())
            stringBuilder.append("{all:").append(Family.getMaskString(all)).append("}");

        if(!one.isEmpty())
            stringBuilder.append("{one:").append(getMaskString(one)).append("}");

        return stringBuilder.toString();
    }

    private static String getMaskString(Bits componentsMask){
        StringBuilder stringBuilder = new StringBuilder();
        int length = componentsMask.length();

        for (int i = 0; i < length; i++){
            stringBuilder.append(componentsMask.get(i) ? 1 : 0);
        }

        return stringBuilder.toString();
    }

    @Override
    public int hashCode(){
        return _id;
    }
}
