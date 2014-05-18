package gameEngine.display;

import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import sage.display.DisplaySettingsDialog;
import sage.display.DisplaySystem;
import sage.display.IDisplaySystem;
import sage.renderer.IRenderer;
import sage.renderer.RendererFactory;

public class MyDisplaySystem implements IDisplaySystem {
	
	private JFrame myFrame;
	private GraphicsDevice device;
	private IRenderer myRenderer;
	private int width, height, bitDepth, refreshRate;
	private Canvas rendererCanvas;
	private boolean isCreated;
	private boolean isFullScreen;

	public MyDisplaySystem(int w, int h, int depth, int rate, boolean isFS, String rName)
	{ 
		//save the input parameters for accessor queries
		
		width = w; height = h; bitDepth = depth; refreshRate = rate;
		this.isFullScreen = isFS;
		
		// get a renderer from the RendererFactory
		
		myRenderer = RendererFactory.createRenderer(rName);
		if (myRenderer == null)
		{ 
			throw new RuntimeException("Unable to find renderer"); 
		}
		rendererCanvas = myRenderer.getCanvas();
		
		myFrame = new JFrame("Default Title");
		myFrame.add(rendererCanvas);
		
		//initialize the screen with the specified parameters
		DisplayMode displayMode = new DisplayMode(width, height, bitDepth, refreshRate);
		//initScreen(displayMode, isFullScreen);
		initScreen();
		
		// save DisplaySystem, show frame and indicate DisplaySystem is created
		
		DisplaySystem.setCurrentDisplaySystem(this);
		myFrame.setVisible(true);
		isCreated = true;
		
	}
	
	private void initScreen(DisplayMode dispMode, boolean FSRequested)
	{
		GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		device = environment.getDefaultScreenDevice();
		if(device.isFullScreenSupported() && FSRequested)
		{
			myFrame.setUndecorated(true); // suppress title bar, borders, etc.
			myFrame.setResizable(false); // full-screen so not resizeable
			myFrame.setIgnoreRepaint(true); // ignore AWT repaints
			// Put device in full-screen mode. This must be done before attempting
			// to change the DisplayMode; the application must first have FSEM
			device.setFullScreenWindow(myFrame);
			//try to set the full-screen device DisplayMode
			if (dispMode != null && device.isDisplayChangeSupported())
			{ 
				try
				{ 
					device.setDisplayMode(dispMode);
					myFrame.setSize(dispMode.getWidth(), dispMode.getHeight());
				} 
				catch (Exception ex)
				{ 
					System.err.println("Exception setting DisplayMode: " + ex ); 
				}
			} 
			else
			{ 
				System.err.println ("Cannot set display mode"); 
			}
		} 
		else
		{ 
			//use windowed mode – set JFrame characteristics
			myFrame.setSize(dispMode.getWidth(),dispMode.getHeight());
			myFrame.setLocationRelativeTo(null); //centers window on screen
		} 
	}
	
	private void initScreen()
	{
		GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		device = environment.getDefaultScreenDevice();
		DisplaySettingsDialog d = new DisplaySettingsDialog(device);
		d.showIt();
		DisplayMode dispMode = d.getSelectedDisplayMode();
		if(device.isFullScreenSupported() && d.isFullScreenModeSelected())
		{
			myFrame.setUndecorated(true); // suppress title bar, borders, etc.
			myFrame.setResizable(false); // full-screen so not resizeable
			myFrame.setIgnoreRepaint(true); // ignore AWT repaints
			// Put device in full-screen mode. This must be done before attempting
			// to change the DisplayMode; the application must first have FSEM
			device.setFullScreenWindow(myFrame);
			//try to set the full-screen device DisplayMode
			if (dispMode != null && device.isDisplayChangeSupported())
			{ 
				try
				{ 
					device.setDisplayMode(dispMode);
					myFrame.setSize(dispMode.getWidth(), dispMode.getHeight());
				} 
				catch (Exception ex)
				{ 
					System.err.println("Exception setting DisplayMode: " + ex ); 
				}
			} 
			else
			{ 
				System.err.println ("Cannot set display mode"); 
			}
		} 
		else
		{ 
			//use windowed mode – set JFrame characteristics
			myFrame.setSize(dispMode.getWidth(),dispMode.getHeight());
			myFrame.setLocationRelativeTo(null); //centers window on screen
		} 
	}

	@Override
	public void addKeyListener(KeyListener l) {
		rendererCanvas.addKeyListener(l);
		
	}

	@Override
	public void addMouseListener(MouseListener l) {
		rendererCanvas.addMouseListener(l);
		
	}

	@Override
	public void addMouseMotionListener(MouseMotionListener l) {
		rendererCanvas.addMouseMotionListener(l);
		
	}

	@Override
	public void close() 
	{
		if (device != null)
		{ 
			Window window = device.getFullScreenWindow();
			if (window != null)
			{ 
				window.dispose();
			}
			device.setFullScreenWindow(null);
		} 
	}

	@Override
	public void convertPointToScreen(Point arg0) {
		// TODO Auto-generated method stub
	
		
	}

	@Override
	public int getBitDepth() {
		return bitDepth;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getRefreshRate() {
		return refreshRate;
	}

	@Override
	public IRenderer getRenderer() {
		return myRenderer;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public boolean isCreated() {
		return isCreated;
	}

	@Override
	public boolean isFullScreen() {
		return isFullScreen;
	}

	@Override
	public boolean isShowing() {
		return myFrame.isVisible();
	}


	public void setBitDepth(int b) {
		bitDepth = b;
	}

	@Override
	public void setCustomCursor(String fileName) {
		Image fImage = new ImageIcon("a1" + File.separator +"Images"+ File.separator + fileName).getImage();
		Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(fImage, new Point(0,0), "CustomCursor");
		rendererCanvas.setCursor(cursor);
		
	}


	public void setHeight(int h) {
		height = h;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setPredefinedCursor(int cursorType) {
		// TODO Auto-generated method stub
		myFrame.setCursor(cursorType);
	}


	public void setRefreshRate(int r) {
		refreshRate = r;
	}

	
	public void setTitle(String title) {
		myFrame.setTitle(title);
	}

	
	public void setWidth(int w) {
		width = w;
		myFrame.setSize(w, height);
		
	}
	
	

}
