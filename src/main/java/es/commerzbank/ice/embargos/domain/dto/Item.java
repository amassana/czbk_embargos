package es.commerzbank.ice.embargos.domain.dto;

public class Item {
	private Object code;
	private String description;
	
	public Item() {
		
	}
	
	public Item(Object code, String description) {
		this.code = code;
		this.description = description;
	}

	
	public Object getCode() {
		return code;
	}

	public void setCode(Object code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
