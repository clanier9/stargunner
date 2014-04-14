package games.caravan.character;

import gameEngine.character.BaseCharacter;
import graphicslib3D.Point3D;

public abstract class Bullet extends BaseCharacter {

	private static float[] vrts = new float[] {-0.5f,1,-1, 
											   -0.5f,-1,-1, 
											   0.5f,1,-1, 
											   0.5f,-1,-1,
											   -0.5f,1,1, 
											   -0.5f,-1,1, 
											   0.5f,1,1, 
											   0.5f,-1,1
											   };
	private static float[] cl = new float[] {1,0,0,1,0,1,0,1,0,0,1,1,1,1,0,1,1,0,1,1};
	private static int[] triangles = new int[] {0,1,2, //Front end
												2,3,1, 
												0,4,5, //Left Side
												5,1,0,
												1,4,2, //Back end
												4,3,2};
	
	public Bullet() {
		// TODO Auto-generated constructor stub
	}

	public Bullet(Point3D p) {
		super(p);
		
	}

}
