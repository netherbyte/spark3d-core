package com.netherbyte.spark3d.test;

import com.netherbyte.spark3d.core.display.WindowManager;
import com.netherbyte.spark3d.core.display.renderer.RenderManager;
import com.netherbyte.spark3d.core.util.ILogic;
import com.netherbyte.spark3d.test.client.main.Main;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.netherbyte.spark3d.core.ModuleConstants.NAME;

public class TestGame implements ILogic {
    private static final Logger logger = LoggerFactory.getLogger("Test Game");

    private int direction = 0;
    private float color = 0.0f;

    private final RenderManager renderer;
    private final WindowManager window;

    public TestGame() {
        renderer = new RenderManager();
        window = Main.getWindow();
    }

    @Override
    public void init() throws Exception {
        logger.info("Initializing");
        renderer.init();
    }

    @Override
    public void input() {
        if (window.isKeyPressed(GLFW.GLFW_KEY_UP)) direction = 1;
        else if (window.isKeyPressed(GLFW.GLFW_KEY_DOWN)) direction = -1;
        else direction = 0;
    }

    @Override
    public void update() {
        color += direction * 0.01f;
        if (color > 1) color = 1.0f;
        else if (color <= 0) color = 0.0f;
    }

    @Override
    public void render() {
        if (window.isResize()) {
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResize(true);
        }

        window.setClearColor(color, color, color, 0.0f);
        renderer.clear();
    }

    @Override
    public void cleanup() {
        logger.info("Cleaning up");
        renderer.cleanup();
    }
}
