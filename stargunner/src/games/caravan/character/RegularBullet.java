package games.caravan.character;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;

public class RegularBullet extends Bullet {

	private static float[] vrts = new float[] {0,1,0, -1,-1,1 ,1,-1,1, 1,-1,-1, -1,-1,-1};
	private static float[] cl = new float[] {1,0,0,1, 1,0,0,1,  1,0,0,1, 1,0,0,1, 1,0,0,1};
	private static int[] triangles = new int[] {0,1,2, 0,2,3, 0,3,4 ,0,4,1, 1,4,2, 4,3,2};
	
	public RegularBullet(Point3D p) {
		super(p);
		FloatBuffer vertBuf =
				com.jogamp.common.nio.Buffers.newDirectFloatBuffer(vrts);
				FloatBuffer colorBuf =
				com.jogamp.common.nio.Buffers.newDirectFloatBuffer(cl);
				IntBuffer triangleBuf =
				com.jogamp.common.nio.Buffers.newDirectIntBuffer(triangles);
				this.setVertexBuffer(vertBuf);
				this.setColorBuffer(colorBuf);
				this.setIndexBuffer(triangleBuf);
		this.setSpeed(0.01f);
		this.rotate(90, new Vector3D(1,0,0));
		this.scale(0.1f, 1, 0.1f);
		this.updateLocalBound();
	}

}
