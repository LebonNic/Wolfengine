package com.arvernistudio.wolfengine.core;

public interface GameLogicProcessor {
    void start();
    long update();
    long fixedUpdate();
}
