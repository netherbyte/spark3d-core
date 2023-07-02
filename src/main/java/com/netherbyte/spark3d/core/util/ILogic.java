package com.netherbyte.spark3d.core.util;

public interface ILogic {
    void init() throws Exception;

    void input();

    void update();

    void render();

    void cleanup();
}
