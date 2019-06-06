package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the EUL5_OBJ_DEPS database table.
 * 
 */
@Entity
@Table(name="EUL5_OBJ_DEPS")
@NamedQuery(name="Eul5ObjDep.findAll", query="SELECT e FROM Eul5ObjDep e")
public class Eul5ObjDep implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="OD_ID", unique=true, nullable=false, precision=10)
	private long odId;

	@Column(precision=10)
	private BigDecimal notm;

	@Column(name="OD_CREATED_BY", nullable=false, length=64)
	private String odCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="OD_CREATED_DATE", nullable=false)
	private Date odCreatedDate;

	@Column(name="OD_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal odElementState;

	@Column(name="OD_UPDATED_BY", length=64)
	private String odUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="OD_UPDATED_DATE")
	private Date odUpdatedDate;

	//bi-directional many-to-one association to Eul5Obj
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="OD_OBJ_ID_FROM", nullable=false)
	private Eul5Obj eul5Obj1;

	//bi-directional many-to-one association to Eul5Obj
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="OD_OBJ_ID_TO", nullable=false)
	private Eul5Obj eul5Obj2;

	public Eul5ObjDep() {
	}

	public long getOdId() {
		return this.odId;
	}

	public void setOdId(long odId) {
		this.odId = odId;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

	public String getOdCreatedBy() {
		return this.odCreatedBy;
	}

	public void setOdCreatedBy(String odCreatedBy) {
		this.odCreatedBy = odCreatedBy;
	}

	public Date getOdCreatedDate() {
		return this.odCreatedDate;
	}

	public void setOdCreatedDate(Date odCreatedDate) {
		this.odCreatedDate = odCreatedDate;
	}

	public BigDecimal getOdElementState() {
		return this.odElementState;
	}

	public void setOdElementState(BigDecimal odElementState) {
		this.odElementState = odElementState;
	}

	public String getOdUpdatedBy() {
		return this.odUpdatedBy;
	}

	public void setOdUpdatedBy(String odUpdatedBy) {
		this.odUpdatedBy = odUpdatedBy;
	}

	public Date getOdUpdatedDate() {
		return this.odUpdatedDate;
	}

	public void setOdUpdatedDate(Date odUpdatedDate) {
		this.odUpdatedDate = odUpdatedDate;
	}

	public Eul5Obj getEul5Obj1() {
		return this.eul5Obj1;
	}

	public void setEul5Obj1(Eul5Obj eul5Obj1) {
		this.eul5Obj1 = eul5Obj1;
	}

	public Eul5Obj getEul5Obj2() {
		return this.eul5Obj2;
	}

	public void setEul5Obj2(Eul5Obj eul5Obj2) {
		this.eul5Obj2 = eul5Obj2;
	}

}