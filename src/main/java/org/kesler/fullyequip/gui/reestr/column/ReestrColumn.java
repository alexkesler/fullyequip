package org.kesler.fullyequip.gui.reestr.column;


import org.kesler.fullyequip.logic.Unit;

public abstract class ReestrColumn {
	protected String name;
	protected String alias;
	protected int    width;

	public String getName() {
		return name;
	}

	public String getAlias() {
		return alias;
	}

	public int getWidth() {
		return width;
	}

	public abstract Object getValue(Unit unit);


}


