package games.caravan;

import java.io.File;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import gameEngine.input.action.*;
import gameEngine.input.action.QuitGameAction;
import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import sage.app.BaseGame;
import sage.camera.ICamera;
import sage.display.IDisplaySystem;
import sage.event.IEventManager;
import sage.input.IInputManager;
import sage.input.action.IAction;
import sage.renderer.IRenderer;
import sage.scene.HUDString;
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
	
//	private Avatar p1;
//	private Avatar p2;
	
	private SkyBox sky;
	
	private IEventManager eventMgr;
	
	public CaravanGame() {
		score = 0;
		score2 = 0;
		time = 0;
	}
	
	public void initGame()
	{
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
		IInputManager im = getInputManager();
		
		String kbName = im.getKeyboardName();
		
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
		Point3D terrainOrigin = new Point3D(0, -cornerHeight, 0);
		
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

}
