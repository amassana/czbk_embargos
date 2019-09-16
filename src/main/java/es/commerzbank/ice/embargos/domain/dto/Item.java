package es.commerzbank.ice.embargos.domain.dto;

public class Item {
	private Object code;
	private String name;
	
	public Item() {
		
	}
	
	public Item(Object code, String name) {
		this.code = code;
		this.name = name;
	}

	
	public Object getCode() {
		return code;
	}

	public void setCode(Object code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
