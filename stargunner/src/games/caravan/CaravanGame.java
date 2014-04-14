package games.caravan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JOptionPane;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import gameEngine.input.action.*;
import games.caravan.character.RegularShip;
import games.caravan.character.Ship;
import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import sage.app.BaseGame;
import sage.camera.ICamera;
import sage.display.IDisplaySystem;
import sage.event.IEventManager;
import sage.input.IInputManager;
import sage.input.action.AbstractInputAction;
import sage.input.action.IAction;
import sage.renderer.IRenderer;
import sage.scene.HUDString;
import sage.scene.SceneNode;
import sage.scene.SkyBox;
import sage.scene.state.RenderState.RenderStateType;
import sage.scene.state.TextureState;
import sage.terrain.AbstractHeightMap;
import sage.terrain.HillHeightMap;
import sage.terrain.TerrainBlock;
import sage.texture.Texture;
import sage.texture.TextureManager;

public class CaravanGame extends BaseGame {

	private int score;
	private int score2;
	private float time;
	

	private HUDString scoreString;
	private HUDString score2String;
	private HUDString timeString;
	
	private static final String texFolder = "." + File.separator + "Textures";
	
	private ICamera camera;
	private IRenderer renderer;
	private IDisplaySystem display;
	private IInputManager im;
	
	private String kbName;
	
	private Ship p1;
	private Ship p2;

	
	private SkyBox sky;
	
	private IEventManager eventMgr;
	
	private ScriptEngine engine; 
	private String scriptName = "UpdateCharacter.js"; 
	private File scriptFile;
	
	public CaravanGame() {
		score = 0;
		score2 = 0;
		time = 0;
	}
	
	public Ship getPlayer() {
		return p1;
	}

	protected void initGame()
	{
		ScriptEngineManager factory = new ScriptEngineManager(); 
		List<ScriptEngineFactory> list = factory.getEngineFactories(); 
		engine = factory.getEngineByName("js"); 
		scriptFile = new File(scriptName); 
		runScript();
		
		
		//renderer = display.getRenderer();
		display = this.getDisplaySystem();
		renderer = display.getRenderer();
		camera = renderer.getCamera();
		
		//Actions 
		IAction quitGame = new QuitGameAction(this);
		
		//Movement
		IAction lstrafe = new LeftStrafeAction(camera);
		IAction rstrafe = new RightStrafeAction(camera);
		IAction fwd = new MoveFowardAction(camera);
		IAction bck = new MoveBackAction(camera);
				
		//Turning
		IAction down = new TurnDownAction(camera);
		IAction up = new TurnUpAction(camera);
		IAction left = new TurnLeftAction(camera);
		IAction right = new TurnRightAction(camera);
				
		IAction x = new MoveXAxis(camera, 0.05f);
		IAction y = new MoveYAxis(camera, 0.05f);
		IAction rx = new MoveRXAxis(camera, 0.2f);
		IAction ry = new MoveRYAxis(camera, 0.2f);
		
		//Binding Actions to input
		im = getInputManager();
		
		kbName = im.getKeyboardName();
		
		ControllerEnvironment ce = ControllerEnvironment.getDefaultEnvironment();
		Controller[] cs = ce.getControllers();
		int kbcount = 0;
		
		for (int i=0; i < cs.length; i++)
		{
			if(cs[i].getType() == Controller.Type.KEYBOARD)
			{
				kbcount++;
			}
		}
		
		if(kbcount > 1)
		{
			for (int i=0; i < cs.length; i++)
			{
				if(cs[i].getType() == Controller.Type.KEYBOARD && !(cs[i].getName().contains("HID")))
				{
					kbName = cs[i].getName();
				}
					
			}
		}
		
		String gpName = im.getFirstGamepadName();
		
		
		//Keyboard
		im.associateAction (
				kbName, net.java.games.input.Component.Identifier.Key.ESCAPE,
				quitGame, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		
		im.associateAction (
				kbName, net.java.games.input.Component.Identifier.Key.W,
				fwd, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		
		im.associateAction (
				kbName, net.java.games.input.Component.Identifier.Key.S,
				bck, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		
		im.associateAction (
				kbName, net.java.games.input.Component.Identifier.Key.A,
				lstrafe, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		
		im.associateAction (
				kbName, net.java.games.input.Component.Identifier.Key.D,
				rstrafe, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		
		im.associateAction (
				kbName, net.java.games.input.Component.Identifier.Key.UP,
				up, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		
		im.associateAction (
				kbName, net.java.games.input.Component.Identifier.Key.DOWN,
				down, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		
		im.associateAction (
				kbName, net.java.games.input.Component.Identifier.Key.LEFT,
				left, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		
		im.associateAction (
				kbName, net.java.games.input.Component.Identifier.Key.RIGHT,
				right, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		
		//Gamepad
		if(gpName != null)
		{
			im.associateAction(gpName,
					net.java.games.input.Component.Identifier.Axis.Y, y,
					IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			im.associateAction(gpName,
					net.java.games.input.Component.Identifier.Axis.X, x,
					IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			im.associateAction(gpName,
					net.java.games.input.Component.Identifier.Axis.RY, ry,
					IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			im.associateAction(gpName,
					net.java.games.input.Component.Identifier.Axis.RX, rx,
					IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		}
		
		
		
		initGameObjects();
		initPlayers();
		update(0);
	}

	private void initPlayers() {
		
		p1 = new RegularShip(new Point3D(0,3,0));
		executeScript(engine, scriptName);
		addGameWorldObject(p1);
		
		IAction lstrafe = new LeftAction(p1);
		IAction rstrafe = new RightAction(p1);
		IAction fwd = new FowardAction(p1);
		IAction bck = new BackwardAction(p1);
		
		im.associateAction (
				kbName, net.java.games.input.Component.Identifier.Key.UP,
				fwd, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		
		im.associateAction (
				kbName, net.java.games.input.Component.Identifier.Key.DOWN,
				bck, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		
		im.associateAction (
				kbName, net.java.games.input.Component.Identifier.Key.LEFT,
				lstrafe, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		
		im.associateAction (
				kbName, net.java.games.input.Component.Identifier.Key.RIGHT,
				rstrafe, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		
		camera.setLocation(new Point3D(0,25,-23));
		camera.lookAt(new Point3D(0,0,0), new Vector3D(0,1,0));
		
	}

	private void initGameObjects() {
		sky = new SkyBox();
		Texture spaceTexture = TextureManager.loadTexture2D(texFolder + File.separator +"space.jpg");
		sky.setTexture(SkyBox.Face.Up, spaceTexture);
		sky.setTexture(SkyBox.Face.Down, spaceTexture);
		sky.setTexture(SkyBox.Face.North, spaceTexture);
		sky.setTexture(SkyBox.Face.South, spaceTexture);
		sky.setTexture(SkyBox.Face.East, spaceTexture);
		sky.setTexture(SkyBox.Face.West, spaceTexture);
		addGameWorldObject(sky);
		initTerrain();
		
		
	}
	
	private void initTerrain()
	{ // create height map and terrain block
		HillHeightMap myHillHeightMap =
		new HillHeightMap(129, 2000, 5.0f, 20.0f,(byte)2, 12345);
		myHillHeightMap.setHeightScale(0.1f);
		TerrainBlock hillTerrain = createTerBlock(myHillHeightMap);
		
		// create texture and texture state to color the terrain
		TextureState groundState;
		Texture groundTexture = TextureManager.loadTexture2D(texFolder + File.separator +"ground.jpg");
		
		groundTexture.setApplyMode(sage.texture.Texture.ApplyMode.Replace);
		groundState = (TextureState)
		
		display.getRenderer().createRenderState(RenderStateType.Texture);
		groundState.setTexture(groundTexture,0);
		groundState.setEnabled(true);
		
		// apply the texture to the terrain
		hillTerrain.setRenderState(groundState);
		addGameWorldObject(hillTerrain);
	}
	
	private TerrainBlock createTerBlock(AbstractHeightMap heightMap)
	{ 
		float heightScale = 0.05f;
		Vector3D terrainScale = new Vector3D(1, heightScale, 1);
		
		// use the size of the height map as the size of the terrain
		int terrainSize = heightMap.getSize();
		
		// specify terrain origin so heightmap (0,0) is at world origin
		float cornerHeight =
		heightMap.getTrueHeightAtPoint(0, 0) * heightScale;
		Point3D terrainOrigin = new Point3D(-64.5, -cornerHeight, -64.5);
		
		// create a terrain block using the height map
		String name = "Terrain:" + heightMap.getClass().getSimpleName();
		TerrainBlock tb = new TerrainBlock(name, terrainSize, terrainScale, heightMap.getHeightData(), terrainOrigin);
		
		return tb;
	}
	
	protected void update(float elapsedTimeMS)
	{
		Point3D camLoc = camera.getLocation();
		Matrix3D camTranslation = new Matrix3D();
		camTranslation.translate(camLoc.getX(), camLoc.getY(), camLoc.getZ());
		sky.setLocalTranslation(camTranslation);
		super.update(elapsedTimeMS);
	
	}
	
	private void runScript() 
	{ 
		try 
		{ 
			FileReader fileReader = new FileReader(scriptFile); 
			engine.eval(fileReader); 
			fileReader.close(); 
		} 
		catch (FileNotFoundException e1) 
		{ 
			System.out.println(scriptFile + " not found " + e1);
		} 
		catch (IOException e2) 
		{ 
			System.out.println("IO problem with " + scriptFile + e2); 
		} 
		catch (ScriptException e3) 
		{ 
			System.out.println("ScriptException in " + scriptFile + e3); 
		} 
		catch (NullPointerException e4) 
		{ 
			System.out.println ("Null ptr exception reading " + scriptFile + e4); 
		} 
	} 
	
	private void executeScript(ScriptEngine engine, String scriptFileName) {
		//cast the engine so it supports invoking functions 
		Invocable invocableEngine = (Invocable) engine ; 
		
		//SceneNode to be updated
		SceneNode s = p1;
		
		// invoke the script function 
		try 
		{ 
			invocableEngine.invokeFunction("updateCharacter", s); 
		} 
		catch (ScriptException e1) 
		{ 
			System.out.println("ScriptException in " + scriptFile + e1);
		} 
		catch (NoSuchMethodException e2) 
		{ 
			System.out.println("No such method exception in " + scriptFile + e2);
		} 
		catch (NullPointerException e3) 
		{ 
			System.out.println ("Null ptr exception reading " + scriptFile + e3);
		} 
	}

}
