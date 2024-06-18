package main;

import java.io.File;
import java.io.IOException;

import static org.lwjgl.opengl.GL41.*;

import comp3170.*;

public class App implements  IWindowListener  {

	private Window window;
	
	final private File DIRECTORY = new File("src/main/");
	
	private int screenWidth = 800;
	private int screenHeight = 800;
	private Scene scene;


	public App() throws OpenGLException {
		
		// create window with title, size, and a listener (this)
		window = new Window("LWJGL App", screenWidth, screenHeight, this);
		// make the window resizable
		window.setResizable(true);
		// start running the window
		window.run();
	}

	@Override
	/**
	 * Initialise the Window
	 */
	public void init() {
		
		ShaderLibrary shaderLibrary = new ShaderLibrary(DIRECTORY); // Creates a shader library which points to the directory shaders will be stored in.
		
		scene = new Scene(); // Creates the scene. This is where we will be drawing things!
	}

		
	@Override
	/**
	 * Called when the window is resized
	 */
	public void draw() {
		
        // clear the colour buffer
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f); // BLACK
		glClear(GL_COLOR_BUFFER_BIT);		
		
		// draw the scene
		scene.draw();	
	}

	@Override
	/**
	 * Called when the window is resized
	 */
	public void resize(int width, int height) {
		// record the new width and height
		this.screenWidth = width;
		this.screenHeight = height;
		glViewport(0,0,width,height);
	}
	
	@Override
	/**
	 * Called when we close the window
	 */
	public void close() {
		// nothing to do
	}

	public static void main(String[] args) throws IOException, OpenGLException {
		new App();
	}

}
