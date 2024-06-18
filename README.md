# How to convert 2022 JOGL code to 2023 LWJGL

## General 

**Notes:**
* All code in this unit should be written in OpenGL 4.1. Apple does not support newer versions of OpenGL for Macs.
* Running LWJGL code on a Mac requires you to add the command line argument -XstartOnFirstThread to the JVM. 
In Eclipse, this can be done in Run > Run configurations > Arguments > VM arguments
* Or if you're running this in VS Code, you will need to create the [`launch.json`](#launchjson) file in your `.vscode/` folder.
    * this can also be created by clicking the `run | debug` that shows up above the `public static void main(String[] args){`

### `launch.json`

* the launch.json file has the following formatting in a hypothetical repository:

```json
{
    // Use IntelliSense to learn about possible attributes.
    // Hover to view descriptions of existing attributes.
    // For more information, visit: https://go.microsoft.com/fwlink/?linkid=830387
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "App",
            "request": "launch",
            "mainClass": "main.App",
            "projectName": "comp3170-lwjgl",
            "vmArgs": "-XstartOnFirstThread"
        }
    ]
}
```

* the line with: `"mainClass": "main.App",`
    * `main.App` should be replaced with `<packagename>.<MainClass>`
        * `main` or `<packagename>` - is the folder you have your `static void main` inside
        * `App` or `<MainClass>` - is the name of the file that `static void main` is within, not including the `.java`
        * not having it as a subfolder of the `src/` folder has some weird implications, so it's best not to go there
        * it's also important to make sure your [shader directory]() is also updated to where ever you put it
        * for the sake of less headache, it's best to use a folder that "branches" away from the other comp3170 content, to keep it somewhat compartmentalised 
    * if you had your `static void main(String[] args){` in say, `src/thecoolpackage/gamefiles/MyGame.java`, then you'd change the line to `"mainClass": "thecoolpackage.gamefiles.MyGame"`
* changing `projectName` stuff, is not tested, but likely will change the way the compiler references everything and may mean it cant find your content depending on how your vscode environment is set up.
* changing `name` should just be for the menu option in vscode
* changing `request` may cause weirdness with the way vscode references the `launch.json` file
* `vmArgs` can be made to be an array of items if there's other arguements you want to add, by using something like:
```json
"vmArgs": [ "-XstartOnFirstThread" ]
```
* vscode also has the `launch.json` attribute/key/the thingies we are speaking about of `args`, which are passed on to your `String[] args` in your `static void main(String[] args){`

## Shader directory

* make sure that whatever directory/folder/package you use for shader files is reflected in your main java class. In this example we use:

```java
final private File DIRECTORY = new File("src/main/");
```

* if you decide you want to just keep all your java files and shaders seperate, it might be nice to have a third folder inside the `src` folder called `shaders`, then you can just update it to be

```java
final private File DIRECTORY = new File("src/shaders/");
```
* Make sure to move your `.glsl` files there afterwards :D (guilty of that myself)

## Imports 

* Delete any JOGL imports.
* Most GL methods and constants can be found on the org.lwjgl.opengl.GL41 class
* You will also need the GLCapabilites class

**Old:**
```java
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
```

**New:**

```java
import org.lwjgl.opengl.GLCapabilities;
import static org.lwjgl.opengl.GL41.*;
```

The new library classes have slightly diiferent names:

**Old:**

```java
import comp3170.GLBuffers;
import comp3170.GLException;
import comp3170.Shader;
```

**New:**

```java
import comp3170.GLBuffers;
import comp3170.OpenGLException;
import comp3170.IWindowListener;
import comp3170.Shader;
import comp3170.Window;
```

## Main class 

* Windowing is now handled by JWGL rather than Java Swing, so you don't need to use the JFrame anymore
* The JOGL GLEventListener has been replaced with the interface IWindowListener

**Old:**

```java
public class Week2 extends JFrame implements GLEventListener {
```

**New**

```java
public class Week2 implements IWindowListener {
```

## Constructor

The new constructor is much simpler, just create a window and run it.

**Old:**

```java
public Week2() {
    super("Week 2 example");

    // set up a GL canvas
    GLProfile profile = GLProfile.get(GLProfile.GL4);		 
    GLCapabilities capabilities = new GLCapabilities(profile);

    canvas = new GLCanvas(capabilities);
    canvas.addGLEventListener(this);
    add(canvas);

    // set up the JFrame

    setSize(width,height);
    setVisible(true);
    addWindowListener(new WindowAdapter() {
    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }
    });
}
```

**New:**

```java
public Week2() {
    Window window = new Window("Week 2 example", width, height, this);
    window.run();
}
```
    
## Event methods

* The event methods are now called init(), draw(), resize() and close()
* The event methods no longer take a GLAutoDrawable parameter
* There is no need to have a GL object to access methods or constants

**Old:**

```java
@Override
public void display(GLAutoDrawable arg0) {
    GL4 gl = (GL4) GLContext.getCurrentGL();
    gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
```

**New:**

```java
@Override
public void draw() {
    glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
    glClear(GL_COLOR_BUFFER_BIT);
```

### Resize 

* The method signature for resize has changed. 
* Resize is always called once when the window is first created

**Note:** on Macs with retina display, the window size does not match the canvas size, so you should always implement a basic resize method, even if the window isn't resizable.

**Old:**

```java
@Override
public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    this.width = width;
    this.height = height;
}
```

**New:**

```java
@Override
public void resize(int width, int height) {
    this.width = width;
    this.height = height;
}
```

### Input

The new InputManager class has the same interface as the old one. The only difference is that you need to create the InputManager in init() rather than in the main constructor, as the window has to be initialised before the InputManager can be attached.

**Old:**

```java
private GLCanvas canvas;
private InputManager input;

public Demo() {
    GLProfile profile = GLProfile.get(GLProfile.GL4);		 
    GLCapabilities capabilities = new GLCapabilities(profile);
    canvas = new GLCanvas(capabilities);
    canvas.addGLEventListener(this);
    add(canvas);

    // set up Input manager
    input = new InputManager(canvas);
}
```

**New:**

```java
private Window window;
private InputManager input;

public Demo() {
    window = new Window("Demo", width, height, true, this);
    window.run();		
}

public void init() {
    // set up Input manager
    input = new InputManager(canvas);        
}
```

The new InputSystem uses [GLFW keycodes](https://www.glfw.org/docs/3.3/group__keys.html) rather than Java KeyEvent keycodes.

**Old:**

```java
if (input.isKeyDown(KeyEvent.VK_LEFT)) {
    cameraAngle = (cameraAngle + CAMERA_ROTATION_SPEED * deltaTime) % TAU;			
}
```

**New:**

```java
if (input.isKeyDown(GLFW_KEY_LEFT)) {
    cameraAngle = (cameraAngle + CAMERA_ROTATION_SPEED * deltaTime) % TAU;			
}
```

### Animation

There is no longer any need to add an Animator or otherwise 'turn-on' animation. When you call Window.run() it will repeatedly call the draw() method on the listener until the window closes. Double buffering is enabled by default (and is tricky to disable without editing the Window class).

Calculating deltaTime is done much the same as before, however I recommend initialising oldTime in init() rather than in the constructor.

**Old:**

```java
private Animator animator;
private long oldTime;

public Week3() {
    // set up a GL canvas
    GLProfile profile = GLProfile.get(GLProfile.GL4);		 
    GLCapabilities capabilities = new GLCapabilities(profile);
    canvas = new GLCanvas(capabilities);
    canvas.addGLEventListener(this);
    add(canvas);

    // set up Animator		
    animator = new Animator(canvas);
    animator.start();
    oldTime = System.currentTimeMillis();				
}

private void update() {
    long time = System.currentTimeMillis();
    float deltaTime = (time - oldTime) / 1000f;
    oldTime = time;
    System.out.println("update: dt = " + deltaTime + "s");
}

public void display(GLAutoDrawable arg0) {
    // update the scene
    update();	
    // ...
}
```

**New:**

```java
private long oldTime;

public Week3() throws OpenGLException {
    window = new Window("Week 3", screenWidth, screenHeight, this);
    window.run();
    // no need for an Animator
}

public void init() {
    oldTime = System.currentTimeMillis();				
}

private void update() {
    // same code as before
    long time = System.currentTimeMillis();
    float deltaTime = (time - oldTime) / 1000f;
    oldTime = time;
    System.out.println("update: dt = " + deltaTime + "s");
}

public void draw() {
    // update the scene
    update();
    // ...
}
```
    
