package es.commerzbank.ice.embargos.domain.dto;

public class Item {
	private long code;
	private String name;
	
	public Item() {
		
	}
	
	public Item(long code, String name) {
		this.code = code;
		this.name = name;
	}

	
	public long getCode() {
		return code;
	}

	public void setCode(long code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
