package com.netherbyte.spark3d.core.client;

import com.netherbyte.spark3d.SharedConstants;
import com.netherbyte.spark3d.core.display.WindowManager;
import com.netherbyte.spark3d.core.engine.EngineManager;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.netherbyte.spark3d.SharedConstants.VERSION;
import static com.netherbyte.spark3d.core.ModuleConstants.NAME;

public class Launcher {
    private static final Logger logger = LoggerFactory.getLogger(NAME);

    private static boolean windowConfigSet = false;
    private static String windowName;
    private static int windowWidth;
    private static int windowHeight;
    private static boolean windowVsync;

    private static WindowManager window;
    private static EngineManager engine;

    public static void main(String[] args) {
        logger.info("Starting " + NAME + " " + VERSION.getFormattedName());
        logger.info("Using LWJGL " + Version.getVersion());
        logger.info("Using OpenGL " + GLFW.glfwGetVersionString());

        setWindowConfig(SharedConstants.NAME + " " + VERSION.getFormattedName(), 1280, 720, true);

        if (!windowConfigSet) {
            for (int i = 0; i < 10; i++) {
                if (!windowConfigSet) {
                    try {
                        synchronized (Launcher.class) {
                            Launcher.class.wait(1000L);
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    logger.info("Waiting to receive window config");
                } else {
                    logger.info("Window config set");
                    break;
                }
            }
            if (!windowConfigSet) {
                throw new IllegalStateException("Engine did not receive window config");
            }
        }

        window = new WindowManager(windowName, windowWidth, windowHeight, windowVsync);
        engine = new EngineManager();

        try {
            engine.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void setWindowConfig(String name, int width, int height, boolean vsync) {
        windowName = name;
        windowWidth = width;
        windowHeight = height;
        windowVsync = vsync;
        windowConfigSet = true;
    }

    public static WindowManager getWindow() {
        return window;
    }
}
