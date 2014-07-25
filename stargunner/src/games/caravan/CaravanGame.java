package games.caravan;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import a3.Starter;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import gameEngine.character.BaseCharacter;
import gameEngine.display.MyDisplaySystem;
import gameEngine.input.action.*;
import games.caravan.ai.BossController;
import games.caravan.character.Bullet;
import games.caravan.character.EnemyBullet;
import games.caravan.character.FighterJet;
import games.caravan.character.RegularBullet;
import games.caravan.character.Ship;
import games.caravan.character.UFO;
import games.caravan.controller.BulletController;
import games.caravan.character.TRex;
import games.caravan.controller.ChaseController;
import games.caravan.controller.ScrollController;
import games.caravan.controller.SnakeController;
import games.caravan.event.ShotEvent;
import games.caravan.event.ShotListener;
import games.caravan.input.action.FireAction;
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
import sage.event.EventManager;
import sage.event.IEventManager;
import sage.input.IInputManager;
import sage.input.InputManager;
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
	private float time;
	
	private String gpName;
	
	private int rank;
	private long fileLastModifiedTime = 0;
	private long lastSpawnTime = 0;
	
	private HUDString scoreString;
	private HUDString timeString;
	
	private static final String texFolder = "." + File.separator + "Textures";
	
	private ICamera camera;
	private IRenderer renderer;
	private IDisplaySystem display;
	private IInputManager im;
	
	private String kbName;
	
	private BaseCharacter p1;
	private BaseCharacter p2;
	private TRex boss;

	private SkyBox sky;
	
	private Group background;
	private Group bullets;
	private Group npcs;
	
	private BulletController bulletControl;
	private SnakeController snakeControl;
	private ScrollController scroller;
	private ChaseController chaser;
	
	private IEventManager eventMgr;
	
	private ScriptEngine engine; 
	private String scriptName = "initPlayer.js"; 
	private File scriptFile;
	
	private IAudioManager audioMgr;
	private AudioResource resource1, resource2, resource3, r3;
	private Sound music;
	
	private BossController bossControl; 
	private Sound shot;
	private ShotListener shotlist = new ShotListener(this);
	
	private static final double worldX = 20;
	private static final double worldZ = 40;
	private static final double yPlane = 10;
	private static final double zSpawn = 20;
	
	OBJLoader obj = new OBJLoader();
	TriMesh ufoModel = obj.loadModel("models" + File.separator + "ufo.obj");
	Texture ufoTexture = TextureManager.loadTexture2D("materials" + File.separator + "ufo.png"); 
	Texture ufoTexture1 = TextureManager.loadTexture2D("materials" + File.separator + "ufo.png"); 
	Texture ufoTexture2 = TextureManager.loadTexture2D("materials" + File.separator + "ufo.png"); 
	Texture ufoTexture3 = TextureManager.loadTexture2D("materials" + File.separator + "ufo.png"); 
	Random r = new Random();
	private TerrainBlock theTerrain;
	
	public CaravanGame() {
		score = 0;
		time = 0;
		ufoTexture.setApplyMode(Texture.ApplyMode.Replace);
		ufoTexture1.setApplyMode(Texture.ApplyMode.Replace);
		ufoTexture2.setApplyMode(Texture.ApplyMode.Replace);
		ufoTexture3.setApplyMode(Texture.ApplyMode.Replace);
	}
	
	protected void initGame()
	{
		initScript();
		initDisplay();
		initInputs();
		initGameObjects();
		initPlayers();
		initHUD();
		initBoss();
		initNPCs();
		initAudio();
		initControllers();
		update(0);
	}

	private void initScript() {
		ScriptEngineManager factory = new ScriptEngineManager(); 
		List<ScriptEngineFactory> list = factory.getEngineFactories(); 
		engine = factory.getEngineByName("js"); 
		scriptFile = new File(scriptName); 
		runScript();
	}
	
	private void initDisplay() {
		display = this.getDisplaySystem();
		renderer = display.getRenderer();
		camera = renderer.getCamera();
		
		camera.setLocation(new Point3D(0,25,-23));
		camera.lookAt(new Point3D(0,0,0), new Vector3D(0,1,0));
	}
	
	private void initInputs() {
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
		
		gpName = im.getFirstGamepadName();
				
				
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
		
		theTerrain = initTerrain();
		theTerrain.scale(2, 1, 2);
		background = new Group();
		background.addChild(theTerrain);
		 
		addGameWorldObject(background);		
		
		bulletControl = new BulletController();
		bullets = new Group();
	
		npcs = new Group();
		
		snakeControl = new SnakeController(2,this);
		
		eventMgr = EventManager.getInstance();
		eventMgr.addListener(shotlist, ShotEvent.class);
		addGameWorldObject(bullets);
		addGameWorldObject(npcs);
	}
	
	
	private void initPlayers() {		
		p1 = new FighterJet(new Point3D(0,10,-18));
		p1.scale(.30f,.30f,.30f);
		textureObj(p1, "fighter6.png");
		//executeScript(engine, scriptName);
		addGameWorldObject(p1);
		
		setUpControls(p1);
		
		chaser = new ChaseController(this);
		chaser.addPlayer((Ship)p1);
		
		snakeControl = new SnakeController(2,this);
		snakeControl.addPlayer((Ship)p1);
		
		camera.setLocation(new Point3D(0,25,-23));
		camera.lookAt(new Point3D(0,0,0), new Vector3D(0,1,0));
	}
	
	private void initHUD()
	{
		//HUDS
		timeString = new HUDString("Time = " + time);
		scoreString = new HUDString ("Score = " + score);
		
		timeString.setName("timer");
		timeString.setLocation(0.2, 0.9);
		timeString.setRenderMode(sage.scene.SceneNode.RENDER_MODE.ORTHO);
		timeString.setColor(Color.red);
		timeString.setCullMode(sage.scene.SceneNode.CULL_MODE.NEVER);
		camera.addToHUD(timeString);
		
		
		scoreString.setName("score");
		scoreString.setLocation(0.8, 0.9);
		scoreString.setRenderMode(sage.scene.SceneNode.RENDER_MODE.ORTHO);
		scoreString.setColor(Color.red);
		scoreString.setCullMode(sage.scene.SceneNode.CULL_MODE.NEVER);
		camera.addToHUD(scoreString);
	}
	
	private void initBoss() {		
		boss = new TRex(new Point3D(0,3.5,10), theTerrain);	
		boss.scale(2, 2, 2);
		textureObj(boss, "skin2.png");
		
		addGameWorldObject(boss);
		
		bossControl = new BossController(this, boss);
	}

	private void initNPCs() {
		// TODO Auto-generated method stub
	}

	private void initAudio() {
		audioMgr = AudioManagerFactory.createAudioManager("sage.audio.joal.JOALAudioManager"); 
		if(!audioMgr.initialize()) { 
			System.out.println("Audio Manager failed to initialize!"); 
			return; 
		} 
		
		resource1 = audioMgr.createAudioResource("sounds" + File.separator + "growl.wav", AudioResourceType.AUDIO_SAMPLE); 
		resource2 = audioMgr.createAudioResource("sounds" + File.separator + "roar.wav", AudioResourceType.AUDIO_SAMPLE); 
		resource3 = audioMgr.createAudioResource("sounds" + File.separator + "background.wav", AudioResourceType.AUDIO_SAMPLE); 
		r3 = audioMgr.createAudioResource("sounds" + File.separator + "LASER1.wav", AudioResourceType.AUDIO_SAMPLE); 
		
		music = new Sound(resource3, SoundType.SOUND_MUSIC, 200, true);
		music.initialize(audioMgr);
		
		boss.setGrowl(new Sound(resource1, SoundType.SOUND_EFFECT, 100, true)); 
		boss.setRoar(new Sound(resource2, SoundType.SOUND_EFFECT, 100, false)); 
		boss.getGrowl().initialize(audioMgr); 
		boss.getRoar().initialize(audioMgr); 
		boss.getGrowl().setMaxDistance(200.0f); 
		boss.getGrowl().setMinDistance(15.0f); 
		boss.getGrowl().setRollOff(10.0f); 
		boss.getGrowl().setLocation(new Point3D(boss.getWorldTranslation().getCol(3))); 
		boss.getRoar().setMaxDistance(200.0f); 
		boss.getRoar().setMinDistance(15.0f); 
		boss.getRoar().setRollOff(10.0f); 
		boss.getRoar().setLocation(new Point3D(boss.getWorldTranslation().getCol(3))); 
		
		shot = new Sound(r3, SoundType.SOUND_EFFECT, 100, false);
		shot.initialize(audioMgr);
		shot.setMaxDistance(30);
		shot.setMinDistance(15);
		shot.setRollOff(12);
		
		setEarParameters(); 
		
		music.play();
		boss.getGrowl().play(); 
	}
	
	private void initControllers() {
		//TODO
	}
	
	protected void update(float elapsedTimeMS)
	{
		super.update(elapsedTimeMS);
		
		hitDetection();
		playerScript();
		bossControl.update(elapsedTimeMS);
		
		if(System.currentTimeMillis() - lastSpawnTime >= 7000)
		{
			Double d = r.nextDouble() * (2*worldX) - (worldX/2);
			Double d1 = r.nextDouble() * (2*worldX) - (worldX/2);
			Double d2 = r.nextDouble() * (2*worldX) - (worldX/2);
			Double d3 = r.nextDouble() * (2*worldX) - (worldX/2);
			addChasingWave(d,d1,d2,d3);
			//addSnakingWave(0);
			lastSpawnTime = System.currentTimeMillis();
		}
		
		//SkyBox
		Point3D camLoc = camera.getLocation();
		Matrix3D camTranslation = new Matrix3D();
		camTranslation.translate(camLoc.getX(), camLoc.getY(), camLoc.getZ());
		sky.setLocalTranslation(camTranslation);
		
		//audio
		boss.getGrowl().setLocation(new Point3D(boss.getWorldTranslation().getCol(3))); 
		boss.getRoar().setLocation(new Point3D(boss.getWorldTranslation().getCol(3))); 
		setEarParameters(); 

		//animations
		boss.updateAnimation(elapsedTimeMS);
		
		boss.update(elapsedTimeMS);
		
		// update the HUD
		scoreString.setText("Score = " + score);
		time += elapsedTimeMS;
		DecimalFormat df = new DecimalFormat("0.0");
		timeString.setText("Time = " + df.format(time/1000));
		
		super.update(elapsedTimeMS);
	}

	public void addBullet(Bullet b)
	{
		bullets.addChild(b);
		b.addController(bulletControl);
		bulletControl.addControlledNode(b);
	}
	
	public void addChasingWave(double x1, double x2, double x3, double x4)
	{
		Point3D p1 = new Point3D(x1,yPlane,zSpawn);
		Point3D p2 = new Point3D(x2,yPlane,zSpawn);
		Point3D p3 = new Point3D(x3,yPlane,zSpawn);
		Point3D p4 = new Point3D(x4,yPlane,zSpawn);
		
		UFO u1 = createEnemy(p1,0);
		UFO u2 = createEnemy(p2,1);
		UFO u3 = createEnemy(p3,2);
		UFO u4 = createEnemy(p4,3);
		
		npcs.addChild(u1);
		npcs.addChild(u2);
		npcs.addChild(u3);
		npcs.addChild(u4);
		
		u1.addController(chaser);
		chaser.addControlledNode(u1);
		
		u2.addController(chaser);
		chaser.addControlledNode(u2);
		
		u3.addController(chaser);
		chaser.addControlledNode(u3);
		
		u4.addController(chaser);
		chaser.addControlledNode(u4);
		
	}
	
	public void addSnakingWave(double x)
	{
		Point3D p1 = new Point3D(x,yPlane,zSpawn);
		Point3D p2 = new Point3D(x,yPlane,zSpawn + 3);
		Point3D p3 = new Point3D(x,yPlane,zSpawn + 6);
		Point3D p4 = new Point3D(x,yPlane,zSpawn + 9);
		
		UFO u1 = createEnemy(p1,0);
		UFO u2 = createEnemy(p2,1);
		UFO u3 = createEnemy(p3,2);
		UFO u4 = createEnemy(p4,3);
		
		npcs.addChild(u1);
		npcs.addChild(u2);
		npcs.addChild(u3);
		npcs.addChild(u4);
		
		u1.addController(snakeControl);
		snakeControl.addControlledNode(u1);
		
		u2.addController(snakeControl);
		snakeControl.addControlledNode(u2);
		
		u3.addController(snakeControl);
		snakeControl.addControlledNode(u3);
		
		u4.addController(snakeControl);
		snakeControl.addControlledNode(u4);
		
	}
	
	public boolean checkPlayerProximity() {
		if (Math.abs(p1.getLocation().getZ() - boss.getLocation().getZ()) <= 100)
			return true;
		else if (Starter.getNumPlayers() == 2)
			if (Math.abs(p2.getLocation().getZ() - boss.getLocation().getZ()) <= 100)
				return true;
		return false;
	}
	
	public UFO createEnemy()
	{
		UFO u = new UFO();
		textureObj(u, "ufo.png");
		return u;
	}
	
	public UFO createEnemy(Point3D p)
	{
		UFO u = new UFO();
		u.addModel(ufoModel);
		u.setLocation(p);
		textureObj(u,ufoTexture,0);
		return u;
	}
	
	public UFO createEnemy(Point3D p, int unit)
	{
		UFO u = new UFO();
		u.addModel(ufoModel);
		u.setLocation(p);
		if(unit == 0)
		textureObj(u,ufoTexture,0);
		else if(unit == 1)
		textureObj(u,ufoTexture1,0);
		else if(unit == 2)
		textureObj(u,ufoTexture2,0);
		else if(unit == 3)
		textureObj(u,ufoTexture3,0);
		return u;
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
	
	@Override
	public void exit() {
		setSoundsOff();
		super.exit();
	}
	
	public BaseCharacter getBoss() {
		return boss;
	}
	
	public BaseCharacter getPlayer() {
		return p1;
	}
	
	public void hitDetection()
	{
		
		ArrayList<SceneNode> deletion = new ArrayList<SceneNode>(); //Have to mark for deletion to avoid concurrency errors
		Iterator<SceneNode> it = bullets.getChildren();
		while(it.hasNext())
		{
			SceneNode b = it.next();
			if(isOutOfBounds(new Point3D(b.getWorldTranslation().getCol(3))))
			{
				deletion.add(b);
			}
			else if(b instanceof EnemyBullet)
			{
				//Check if player collided, do not check other players
				b.updateWorldBound();
				if(p1.getWorldBound().intersects(b.getWorldBound()))
				{
					//Hurt player
					score = score - 10;
					//Remove bullet
					deletion.add(b);
				}
			}
			else if(b instanceof RegularBullet) //Check for player shot hitting enemy
			{
				b.updateWorldBound();
				Iterator<SceneNode> it2 = npcs.getChildren();
				while(it2.hasNext())
				{
					SceneNode a = it2.next();
					if(a instanceof UFO)
					{
						if(a.getWorldBound().intersects(b.getWorldBound()))
						{
							//Remove UFO
							deletion.add(a);
							//Remove Bullet
							deletion.add(b);
							//Increment score
							score++;
							break;
						}
					}
				}
			}
		}
		
		//Delete everything marked for deletion
		it = deletion.iterator();
		while(it.hasNext())
		{
			SceneNode n = it.next();
			if(n instanceof UFO)
			{
				UFO u = (UFO) n;
				removeEnemy(u);
			}
			else
			{
				removeBullet((Bullet) n);
			}
		}
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
		groundState = (TextureState) display.getRenderer().createRenderState(RenderStateType.Texture);
		groundState.setTexture(groundTexture,0);
		groundState.setEnabled(true);
		
		// apply the texture to the terrain
		hillTerrain.setRenderState(groundState);
		return hillTerrain;
	}
	
	public boolean isOutOfBounds(Point3D p)
	{
		if(p.getX() > worldX + 5 || p.getX() < -worldX - 5 || p.getZ() < -worldZ - 5 || p.getZ() > worldZ + 5) return true;
		else return false;
	}
	
	public void removeBullet(Bullet b)
	{
		bullets.removeChild(b);
		b.removeController(bulletControl);
		bulletControl.removeNode(b);
	}
	
	public void removeEnemy(UFO u)
	{
		npcs.removeChild(u);
		u.removeController();
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
	
	private void setEarParameters() {		
		audioMgr.getEar().setLocation(p1.getLocation()); 
		audioMgr.getEar().setOrientation(new Vector3D(0,0,1), new Vector3D(0,1,0)); 
	}
	
	public void setSoundsOff() {
		// First release sounds
		boss.getGrowl().release(audioMgr); 
		boss.getRoar().release(audioMgr);
		music.release(audioMgr);
		
		// Next release audio resources 
		resource1.unload();
		resource2.unload();
		resource3.unload();
		r3.unload();
		 
		// Finally shut down the audio manager 
		audioMgr.shutdown();
	}
	
	private void playerScript()
	{
		long modTime = scriptFile.lastModified();
		if 
		(modTime > fileLastModifiedTime)
		{ 
			fileLastModifiedTime = modTime;
			this.runScript();
			removeGameWorldObject(p1);
			
			p1 = (FighterJet) engine.get("p1");
			this.textureObj(p1, "fighter6.png");
			chaser.addPlayer((Ship)p1);
			snakeControl.addPlayer((Ship)p1);
			setUpControls(p1);
			
			addGameWorldObject(p1);
		}
	}

	private void setUpControls(BaseCharacter p)
	{
		IAction lstrafe = new LeftAction(p);
		IAction rstrafe = new RightAction(p);
		IAction fwd = new FowardAction(p);
		IAction bck = new BackwardAction(p);
		IAction fire = new FireAction((Ship) p, this);
		IAction xa = new MoveCharX(p);
		IAction ya = new MoveCharY(p);
		
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
		im.associateAction (
				kbName, net.java.games.input.Component.Identifier.Key.Z,
				fire, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		
		if(gpName != null)
		{
			im.associateAction(gpName,
					net.java.games.input.Component.Identifier.Axis.Y, ya,
					IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			im.associateAction(gpName,
					net.java.games.input.Component.Identifier.Axis.X, xa,
					IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			im.associateAction(gpName,
					net.java.games.input.Component.Identifier.Button._0, fire,
					IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			
		}
		
	}
	
	public void startScrolling() {
		background.addController(scroller);
		scroller.addControlledNode(background);
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
	
	public void textureObj(BaseCharacter c, Texture objTexture, int unit)
	{
		TextureState objTextureState = (TextureState) display.getRenderer().createRenderState(RenderState.RenderStateType.Texture); 
		objTextureState.setTexture(objTexture, unit); 
		objTextureState.setEnabled(true); 
		c.setRenderState(objTextureState); 
		c.updateRenderStates();
	}	
	
	public void playShot(Point3D p)
	{
		shot.setLocation(p);
		shot.play();
	}
	
	protected void initSystem()
	{
		//call a local method to create a DisplaySystem object
		IDisplaySystem display = createDisplaySystem();
		setDisplaySystem(display);
		//create an Input Manager
		IInputManager inputManager = new InputManager();
		setInputManager(inputManager);
		//create an (empty) gameworld
		ArrayList<SceneNode> gameWorld = new ArrayList<SceneNode>();
		setGameWorld(gameWorld);
	}
	
	private IDisplaySystem createDisplaySystem()
	{
		display = new MyDisplaySystem(1024, 720, 24, 60, false,"sage.renderer.jogl.JOGLRenderer");
		System.out.print("\nWaiting for display creation... ");
		int count = 0;
		// wait until display creation completes or a timeout occurs
		while (!display.isCreated())
		{
			try
			{ 
				Thread.sleep(10); 
			}
			catch (InterruptedException e)
			{ 
				throw new RuntimeException("Display creation interrupted"); 
			}
			count++;
			System.out.print("+");
			if (count % 80 == 0) { System.out.println(); }
			if (count > 2000) // 20 seconds (approx.)
			{ 
				throw new RuntimeException("Unable to create display");
			}
		}
		System.out.println();
		return display ;
	}
	
	protected void shutdown()
	{ 
		display.close() ;
		super.shutdown();
		//...other shutdown methods here as necessary...
	}
	
	public IEventManager getEventMgr()
	{
		return eventMgr;
	}
	
}
