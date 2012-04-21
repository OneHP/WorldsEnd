package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;

public class Main extends SimpleApplication {

	public static void main(String[] args) {
            Main app = new Main();
            app.start();
        }

	@Override
	public void simpleInitApp() {

		this.getCamera().lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
	}

	@Override
	public void simpleUpdate(float tpf) {
	
	}

	@Override
	public void simpleRender(RenderManager rm) {
		// TODO: add render code
	}
}
