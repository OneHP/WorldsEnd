package meshes;

import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;

public class MissileShipMesh extends Mesh {

	private static Vector3f[] VERTICES;

	private static int[] INDEXES = { 0, 4, 1, 1, 4, 2, 4, 3, 2 };

	static {
		VERTICES = new Vector3f[5];
		VERTICES[0] = new Vector3f(0, 0, 0);
		VERTICES[1] = new Vector3f(0.2f, -0.2f, 0);
		VERTICES[2] = new Vector3f(0.2f, -1.0f, 0);
		VERTICES[3] = new Vector3f(-0.2f, -1.0f, 0);
		VERTICES[4] = new Vector3f(-0.2f, -0.2f, 0);
	}

	public MissileShipMesh() {
		this.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(VERTICES));
		this.setBuffer(Type.Index, 3, BufferUtils.createIntBuffer(INDEXES));
		this.updateBound();
	}
}
