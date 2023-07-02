package com.netherbyte.spark3d.core.display;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.netherbyte.spark3d.core.ModuleConstants.NAME;

public class WindowManager {
    private static final Logger logger = LoggerFactory.getLogger(NAME);

    public static final float FOV = (float) Math.toRadians(60);
    public static final float Z_NEAR = 0.01f;
    public static final float Z_FAR = 1000f;

    private String title;

    private int width, height;
    private long window;

    private boolean resize, vsync;

    private final Matrix4f projMatrix;

    public WindowManager(String title, int width, int height, boolean vsync) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.vsync = vsync;
        projMatrix = new Matrix4f();
    }

    public void init() {
        logger.info("Initializing window");

        logger.info("Creating error callback");
        GLFWErrorCallback.createPrint(System.err).set();

        if (!GLFW.glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");

        logger.info("Setting window hints");
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, 0);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, 1);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, 1);

        boolean maximized = false;
        if (width == 0 || height == 0) {
            width = 100;
            height = 100;
            GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, 1);
            maximized = true;
        }

        logger.info("Creating window");
        window = GLFW.glfwCreateWindow(width, height, title, 0L, 0L);
        if (window == 0L) throw new RuntimeException("Failed to create GLFW window");

        GLFW.glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            this.width = width;
            this.height = height;
            this.setResize(true);
        });

        GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS) GLFW.glfwSetWindowShouldClose(window, true);
        });

        if (maximized) GLFW.glfwMaximizeWindow(window);
        else {
            GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
            GLFW.glfwSetWindowPos(window, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);
        }

        GLFW.glfwMakeContextCurrent(window);

        if (isVsync()) GLFW.glfwSwapInterval(1);

        logger.info("Showing window");
        GLFW.glfwShowWindow(window);

        GL.createCapabilities();

        GL11.glClearColor(0f,0f,0f,1f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    public void update() {
        GLFW.glfwSwapBuffers(window);
        GLFW.glfwPollEvents();
    }

    public void cleanup() {
        logger.info("Destroying window");
        GLFW.glfwDestroyWindow(window);
    }

    public void setClearColor(float r, float g, float b, float a) {
        GL11.glClearColor(r,g,b,a);
    }

    public boolean isKeyPressed(int keycode) {
        return GLFW.glfwGetKey(window, keycode) == GLFW.GLFW_PRESS;
    }

    public boolean isKeyReleased(int keycode) {
        return GLFW.glfwGetKey(window, keycode) == GLFW.GLFW_RELEASE;
    }

    public boolean windowShouldClose() {
        return GLFW.glfwWindowShouldClose(window);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        GLFW.glfwSetWindowTitle(window, title);
    }

    public boolean isResize() {
        return resize;
    }

    public void setResize(boolean resize) {
        this.resize = resize;
    }

    public boolean isVsync() {
        return vsync;
    }

    public void setVsync(boolean vsync) {
        this.vsync = vsync;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public long getWindow() {
        return window;
    }

    public Matrix4f getProjMatrix() {
        return projMatrix;
    }

    public Matrix4f updateProjMatrix() {
        float aspectRatio = (float) width / height;
        return projMatrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }

    public Matrix4f updateProjMatrix(Matrix4f matrix, int width, int height) {
        float aspectRatio = (float) width / height;
        return matrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }
}
