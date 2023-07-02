package com.netherbyte.spark3d.core.display.renderer;

import com.netherbyte.spark3d.test.client.main.Main;
import com.netherbyte.spark3d.core.display.WindowManager;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.netherbyte.spark3d.core.ModuleConstants.NAME;

public class RenderManager {
    private static final Logger logger = LoggerFactory.getLogger(NAME);

    private final WindowManager window;

    public RenderManager() {
        window = Main.getWindow();
    }

    public void init() throws Exception {
        logger.info("Initializing renderer");
    }

    public void render() {

    }

    public void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup() {
        logger.info("Cleaning up renderer");
    }
}
