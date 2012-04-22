package display;

import java.util.List;

public abstract class MenuItem {

	private final String item;
	private MenuItem parent;
	private final List<MenuItem> subMenu;
	private int selectedIndex;

	public MenuItem(String item, List<MenuItem> subMenu) {
		this.parent = null;
		this.item = item;
		this.subMenu = subMenu;
		this.selectedIndex = subMenu.isEmpty() ? -1 : 0;
		for (MenuItem menuItem : subMenu) {
			menuItem.setParent(this);
		}
	}

	/**
	 * @return true if menu needs closing
	 */
	public abstract boolean action();

	public void setParent(MenuItem parent) {
		this.parent = parent;
	}

	public MenuItem getParent() {
		return this.parent;
	}

	public MenuItem getSelectedItem() {
		return -1 == this.selectedIndex ? null : this.subMenu.get(this.selectedIndex);
	}

	public String getItem() {
		return this.item;
	}

	public List<MenuItem> getSubMenu() {
		return this.subMenu;
	}

	public int getSelectedIndex() {
		return this.selectedIndex;
	}

	public void increaseSelection() {
		this.selectedIndex++;
	}

	public void decreaseSelection() {
		this.selectedIndex--;
	}

}
