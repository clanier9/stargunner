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

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import gameEngine.character.BaseCharacter;
import gameEngine.input.action.*;
import games.caravan.character.FighterJet;
import games.caravan.character.RegularShip;
import games.caravan.character.Ship;
import games.caravan.character.TRex;
import games.caravan.controller.ScrollController;
import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import sage.app.BaseGame;
import sage.audio.AudioManagerFactory;
import sage.audio.AudioResource;
import sage.audio.AudioResourceType;
import sage.audio.IAudioManager;
import sage.audio.Sound;
import sage.audio.SoundType;
import sage.camera.ICamera;
import sage.display.IDisplaySystem;
import sage.event.IEventManager;
import sage.input.IInputManager;
import sage.input.action.AbstractInputAction;
import sage.input.action.IAction;
import sage.model.loader.OBJLoader;
import sage.renderer.IRenderer;
import sage.scene.Group;
import sage.scene.HUDString;
import sage.scene.SceneNode;
import sage.scene.SkyBox;
import sage.scene.TriMesh;
import sage.scene.state.RenderState.RenderStateType;
import sage.scene.state.RenderState;
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
	
	private int rank;
	private long fileLastModifiedTime = 0;
	

	private HUDString scoreString;
	private HUDString score2String;
	private HUDString timeString;
	
	private static final String texFolder = "." + File.separator + "Textures";
	
	private ICamera camera;
	private IRenderer renderer;
	private IDisplaySystem display;
	private IInputManager im;
	
	private String kbName;
	
	private BaseCharacter p1;
	private BaseCharacter p2;
	private BaseCharacter boss;

	private SkyBox sky;
	
	private Group background;
	private Group bullets;
	
	private ScrollController scroller;
	
	private IEventManager eventMgr;
	
	private ScriptEngine engine; 
	private String scriptName = "initPlayer.js"; 
	private File scriptFile;
	
	private IAudioManager audioMgr;
	private AudioResource resource1, resource2; 
	private Sound bossSound;
	
	public CaravanGame() {
		score = 0;
		score2 = 0;
		time = 0;
	}
	
	public BaseCharacter getPlayer() {
		return p1;
	}
	
	public BaseCharacter getBoss() {
		return boss;
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
//		initAudio();
		update(0);
	}

	private void initAudio() {
		audioMgr = AudioManagerFactory.createAudioManager("sage.audio.joal.JOALAudioManager"); 
		if(!audioMgr.initialize()) { 
			System.out.println("Audio Manager failed to initialize!"); 
			return; 
		} 
		
		resource1 = audioMgr.createAudioResource("sounds + " + File.separator + "OverHere.wav", AudioResourceType.AUDIO_SAMPLE); 
		bossSound = new Sound(resource1, SoundType.SOUND_EFFECT, 100, true); 
		bossSound.initialize(audioMgr); 
		bossSound.setMaxDistance(50.0f); 
		bossSound.setMinDistance(3.0f); 
		bossSound.setRollOff(5.0f); 
		bossSound.setLocation(new Point3D(boss.getWorldTranslation().getCol(3))); 
		setEarParameters(); 
		
		bossSound.play(); 
	}

	private void setEarParameters() {		
		audioMgr.getEar().setLocation(p1.getLocation()); 
		audioMgr.getEar().setOrientation(new Vector3D(0,0,1), new Vector3D(0,1,0)); 
	}

	private void initPlayers() {
		
		p1 = new FighterJet(new Point3D(0,10,-18));
		p1.scale(.30f,.30f,.30f);
		p1.rotate(-90, new Vector3D(1,0,0));
		textureObj(p1, "fighter6.png");
		//executeScript(engine, scriptName);
		addGameWorldObject(p1);
		
		setUpControls(p1);
		
		camera.setLocation(new Point3D(0,25,-23));
		camera.lookAt(new Point3D(0,0,0), new Vector3D(0,1,0));
		
		boss = new TRex(new Point3D(0,0,20));	
		boss.scale(3, 3, 3);
		addGameWorldObject(boss);
	}
	
	private void setUpControls(BaseCharacter p)
	{
		IAction lstrafe = new LeftAction(p);
		IAction rstrafe = new RightAction(p);
		IAction fwd = new FowardAction(p);
		IAction bck = new BackwardAction(p);
		
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
		
		scroller = new ScrollController(0.002);
		
		TerrainBlock t = initTerrain();
		t.scale(2, 1, 2);
		background = new Group();
		background.addChild(t);
		 
		addGameWorldObject(background);		
	}
	
	public void startScrolling() {
		background.addController(scroller);
		scroller.addControlledNode(background);
	}
	
	private TerrainBlock initTerrain()
	{ // create height map and terrain block
		HillHeightMap myHillHeightMap =
		new HillHeightMap(129, 2000, 5.0f, 20.0f,(byte)2, 12345);
		myHillHeightMap.setHeightScale(0.1f);
		TerrainBlock hillTerrain = createTerBlock(myHillHeightMap);
		
		// create texture and texture state to color the terrain
		TextureState groundState;
		Texture groundTexture = TextureManager.loadTexture2D(texFolder + File.separator +"redrock.jpg");
		
		groundTexture.setApplyMode(sage.texture.Texture.ApplyMode.Replace);
		groundState = (TextureState)
		
		display.getRenderer().createRenderState(RenderStateType.Texture);
		groundState.setTexture(groundTexture,0);
		groundState.setEnabled(true);
		
		// apply the texture to the terrain
		hillTerrain.setRenderState(groundState);
		return hillTerrain;
	}
	
	private static TerrainBlock createTerBlock(AbstractHeightMap heightMap)
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
		
//		bossSound.setLocation(new Point3D(boss.getWorldTranslation().getCol(3))); 
//		setEarParameters(); 

		super.update(elapsedTimeMS);
	
	}
	
	public void textureObj(BaseCharacter c, String file) {
		Texture objTexture = TextureManager.loadTexture2D("materials" + File.separator + file); 
		objTexture.setApplyMode(Texture.ApplyMode.Replace); 
		TextureState objTextureState = (TextureState) display.getRenderer().createRenderState(RenderState.RenderStateType.Texture); 
		objTextureState.setTexture(objTexture, 0); 
		objTextureState.setEnabled(true); 
		c.setRenderState(objTextureState); 
		c.updateRenderStates();
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
	
	public void setSoundsOff() {
		// First release sounds
		bossSound.release(audioMgr); 
				 
		// Next release audio resources 
		resource1.unload();
		 
		// Finally shut down the audio manager 
		audioMgr.shutdown();
	}

}
