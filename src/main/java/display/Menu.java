package display;

import java.util.List;

import com.google.common.collect.Lists;

public class Menu {

	private MenuItem root;

	public Menu() {

		List<MenuItem> empty = Lists.newArrayList();

		MenuItem item1_1 = new MenuItem("Item 1 - 1", empty) {
			@Override
			public void action() {
				// TODO Auto-generated method stub
			}
		};
		MenuItem item1_2 = new MenuItem("Item 1 - 2", empty) {
			@Override
			public void action() {
				// TODO Auto-generated method stub
			}
		};

		MenuItem item1 = new MenuItem("Send Ship", Lists.newArrayList(item1_1,
				item1_2)) {
			@Override
			public void action() {
				Menu.this.root = getSelectedItem();
			}
		};
		MenuItem item2 = new MenuItem("Item 2", empty) {
			@Override
			public void action() {
				// TODO Auto-generated method stub
			}
		};
		MenuItem item3 = new MenuItem("Item 3", empty) {
			@Override
			public void action() {
				// TODO Auto-generated method stub
			}
		};

		this.root = new MenuItem("Main",
				Lists.newArrayList(item1, item2, item3)) {
			@Override
			public void action() {
				Menu.this.root = getSelectedItem();
			}
		};

	}

	public void upALevel() {
		if (null != this.root.getParent()) {
			this.root = this.root.getParent();
		}
	}

	public MenuItem getRoot() {
		return this.root;
	}

}
