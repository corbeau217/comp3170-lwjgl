package main;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;

import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.*;

public class Scene {
	
	
	// Define the name of the shader files. These will be looked up in the shader library defined in main.
	final private String VERTEX_SHADER = "vertex.glsl"; 
	final private String FRAGMENT_SHADER = "fragment.glsl";
	
	private Vector4f[] vertices;
	private int vertexBuffer;
	private Shader shader;
	
	
	public Scene () {
		
		// compile the shader
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		
		// define the mesh - a collection of vertices that make up a square.
		// @formatter:off
		
		vertices = new Vector4f[] {
			new Vector4f(1.0f, 1.0f, 0.0f, 1.0f),
			new Vector4f(-1.0f, 1.0f, 0.0f, 1.0f),
			new Vector4f(-1.0f, -1.0f, 0.0f, 1.0f),
			
			new Vector4f(-1.0f, -1.0f, 0.0f, 1.0f),
			new Vector4f(1.0f, -1.0f, 0.0f, 1.0f),
			new Vector4f(1.0f, 1.0f, 0.0f, 1.0f),
		};
			
		// @formatter:on
		
		// copy the vertices into a Vertex Buffer Object in graphics memory
		vertexBuffer = GLBuffers.createBuffer(vertices);
	}
	
	public void draw() {
		// activate the shader
		shader.enable();
		
		// connect the vertex buffer (the mesh data) to the a_position attribute
		shader.setAttribute("a_position", vertexBuffer);
		
		// write the colour value into the u_colour uniform. For now, we will write colours as Vec3s (R,G,B).
		Vector3f colour = new Vector3f(1.0f, 0.0f, 0.f); // RED
		shader.setUniform("u_colour", colour);
		
		// mode = GL_TRIANGLES (draws the shape by creating triangles)
		// starting offset = 0 (confirms we want to start at the beginning of the array)
		// number of elements = vertices.length (sets the number of elements to the number of vertices, in this case six)
		glDrawArrays(GL_TRIANGLES, 0, vertices.length);
	}

}
