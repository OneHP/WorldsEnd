package display;

import java.util.List;

public abstract class MenuItem {

	private final String item;
	private MenuItem parent;
	private final List<MenuItem> subMenu;
	private final int selectedIndex;

	public MenuItem(String item, List<MenuItem> subMenu) {
		this.parent = null;
		this.item = item;
		this.subMenu = subMenu;
		this.selectedIndex = subMenu.size() > 0 ? 1 : 0;
		for (MenuItem menuItem : subMenu) {
			menuItem.setParent(this);
		}
	}

	public abstract void action();

	public void setParent(MenuItem parent) {
		this.parent = parent;
	}

	public MenuItem getParent() {
		return this.parent;
	}

	public MenuItem getSelectedItem() {
		return this.subMenu.get(this.selectedIndex);
	}

	public String getItem() {
		return item;
	}

	public List<MenuItem> getSubMenu() {
		return subMenu;
	}

}
