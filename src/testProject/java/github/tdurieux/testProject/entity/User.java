package github.tdurieux.testProject.entity;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = 2603156267507828552L;

	public enum Type {
		USER, ADMIN
	}

	private String name;
	private int id;
	private Type type = Type.USER;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

/**
 * Getter
 * @return [description]
 */
	public Type getType() {
		return type;
	}

	@Override
	public String toString() {

		return "[ " + id + "] " + name;
	}

}
