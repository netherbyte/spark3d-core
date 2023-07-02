package com.netherbyte.spark3d.test.client.main;

import com.netherbyte.spark3d.core.display.WindowManager;
import com.netherbyte.spark3d.core.engine.EngineManager;
import com.netherbyte.spark3d.test.TestGame;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.netherbyte.spark3d.SharedConstants.VERSION;
import static com.netherbyte.spark3d.core.ModuleConstants.NAME;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(NAME);

    private static WindowManager window;
    private static TestGame game;

    public static void main(String[] args) {
        logger.info("Starting " + NAME + " " + VERSION.getFormattedName());
        logger.info("Using LWJGL " + Version.getVersion());
        logger.info("Using OpenGL " + GLFW.glfwGetVersionString());

        window = new WindowManager("Spark3D Test", 1280, 720, true);
        game = new TestGame();
        EngineManager engine = new EngineManager();

        try {
            engine.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static WindowManager getWindow() {
        return window;
    }

    public static TestGame getGame() {
        return game;
    }
}
