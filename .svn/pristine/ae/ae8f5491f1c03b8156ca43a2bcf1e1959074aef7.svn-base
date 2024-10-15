package com.sfs.db.jdo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sfs.db.IPersistable;

/**
 * GameMemberList
 */
@Entity
@Table(name = "game_member_list")
public class GameMemberList implements IPersistable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 356067115471379647L;
	private int gmlId;
	private String gmlName;

	@Id
	@Column(name = "gml_id", unique = true, nullable = false)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int getGmlId() {
		return gmlId;
	}
	
	public void setGmlId(int mlId) {
		this.gmlId = mlId;
	}

	@Column(name = "gml_name")

	public String getGmlName() {
		return gmlName;
	}

	public void setGmlName(String gmlName) {
		this.gmlName = gmlName;
	}

	@Override
	public String toString() {
		return "GameMemberList{" +
				"gmlId=" + gmlId +
				", gmlName='" + gmlName + '\'' +
				'}';
	}
}
