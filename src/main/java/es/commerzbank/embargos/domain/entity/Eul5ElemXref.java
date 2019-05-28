package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the EUL5_ELEM_XREFS database table.
 * 
 */
@Entity
@Table(name="EUL5_ELEM_XREFS")
@NamedQuery(name="Eul5ElemXref.findAll", query="SELECT e FROM Eul5ElemXref e")
public class Eul5ElemXref implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="EX_ID", unique=true, nullable=false, precision=10)
	private long exId;

	@Column(name="EX_FROM_ID", nullable=false, length=100)
	private String exFromId;

	@Column(name="EX_FROM_TYPE", nullable=false, length=10)
	private String exFromType;

	@Column(name="EX_STATE", precision=22)
	private BigDecimal exState;

	@Column(name="EX_TO_DEVKEY", length=100)
	private String exToDevkey;

	@Column(name="EX_TO_ID", nullable=false, precision=10)
	private BigDecimal exToId;

	@Column(name="EX_TO_NAME", length=100)
	private String exToName;

	@Column(name="EX_TO_PAR_DEVKEY", length=100)
	private String exToParDevkey;

	@Column(name="EX_TO_PAR_NAME", length=100)
	private String exToParName;

	@Column(name="EX_TO_TYPE", length=100)
	private String exToType;

	@Column(name="EX_TYPE", nullable=false, precision=2)
	private BigDecimal exType;

	public Eul5ElemXref() {
	}

	public long getExId() {
		return this.exId;
	}

	public void setExId(long exId) {
		this.exId = exId;
	}

	public String getExFromId() {
		return this.exFromId;
	}

	public void setExFromId(String exFromId) {
		this.exFromId = exFromId;
	}

	public String getExFromType() {
		return this.exFromType;
	}

	public void setExFromType(String exFromType) {
		this.exFromType = exFromType;
	}

	public BigDecimal getExState() {
		return this.exState;
	}

	public void setExState(BigDecimal exState) {
		this.exState = exState;
	}

	public String getExToDevkey() {
		return this.exToDevkey;
	}

	public void setExToDevkey(String exToDevkey) {
		this.exToDevkey = exToDevkey;
	}

	public BigDecimal getExToId() {
		return this.exToId;
	}

	public void setExToId(BigDecimal exToId) {
		this.exToId = exToId;
	}

	public String getExToName() {
		return this.exToName;
	}

	public void setExToName(String exToName) {
		this.exToName = exToName;
	}

	public String getExToParDevkey() {
		return this.exToParDevkey;
	}

	public void setExToParDevkey(String exToParDevkey) {
		this.exToParDevkey = exToParDevkey;
	}

	public String getExToParName() {
		return this.exToParName;
	}

	public void setExToParName(String exToParName) {
		this.exToParName = exToParName;
	}

	public String getExToType() {
		return this.exToType;
	}

	public void setExToType(String exToType) {
		this.exToType = exToType;
	}

	public BigDecimal getExType() {
		return this.exType;
	}

	public void setExType(BigDecimal exType) {
		this.exType = exType;
	}

}