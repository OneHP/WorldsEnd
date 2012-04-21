package meshes;

import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;

public class BomberShipMesh extends Mesh {

	private static Vector3f[] VERTICES;

	private static int[] INDEXES = { 0, 2, 1, 0, 3, 2, 0, 5, 4, 0, 1, 5 };

	static {
		VERTICES = new Vector3f[7];
		VERTICES[0] = new Vector3f(0, -1.1f, 0);
		VERTICES[1] = new Vector3f(0, 0, 0);
		VERTICES[2] = new Vector3f(0.7f, -0.7f, 0);
		VERTICES[3] = new Vector3f(0.9f, -1.5f, 0);
		VERTICES[4] = new Vector3f(-0.9f, -1.5f, 0);
		VERTICES[5] = new Vector3f(-0.7f, -0.7f, 0);
	}

	public BomberShipMesh() {
		this.setBuffer(Type.Position, 3,
				BufferUtils.createFloatBuffer(VERTICES));
		this.setBuffer(Type.Index, 3, BufferUtils.createIntBuffer(INDEXES));
		this.updateBound();
	}

}
