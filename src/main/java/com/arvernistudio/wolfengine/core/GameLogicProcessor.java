package com.arvernistudio.wolfengine.core;

import com.arvernistudio.wolfengine.scene.Scene;

public interface GameLogicProcessor {
    void start(Scene scene);
    long update(Scene scene);
    long fixedUpdate(Scene scene);
}
