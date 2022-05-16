package com.kinghis.yyoauth.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class JsTree implements Serializable{

	private static final long serialVersionUID = -2193450801343566518L;
	
	private String id;
	
	private String text;

	/**
	 * 类型,0顶级菜单、1父菜单、2子菜单、3按扭
	 */
	private String type;
	
	private String parent_id;
	
	private String url;

	/**
	 * 图标
	 */
	private String icon;
	
	private State state = new State();
	
	private List<JsTree> children = new ArrayList<JsTree>();
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<JsTree> getChildren() {
		return children;
	}

	public void setChildren(List<JsTree> children) {
		this.children = children;
	}
	
	

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public class State {
		
		private boolean opened = false;
		
		private boolean selected = false;

		public boolean isOpened() {
			return opened;
		}

		public void setOpened(boolean opened) {
			this.opened = opened;
		}

		public boolean isSelected() {
			return selected;
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}
		
		
		
	}
	
}
