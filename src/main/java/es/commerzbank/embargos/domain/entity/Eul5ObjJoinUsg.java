package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the EUL5_OBJ_JOIN_USGS database table.
 * 
 */
@Entity
@Table(name="EUL5_OBJ_JOIN_USGS")
@NamedQuery(name="Eul5ObjJoinUsg.findAll", query="SELECT e FROM Eul5ObjJoinUsg e")
public class Eul5ObjJoinUsg implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="OJU_ID", unique=true, nullable=false, precision=10)
	private long ojuId;

	@Column(precision=10)
	private BigDecimal notm;

	@Column(name="OJU_CREATED_BY", nullable=false, length=64)
	private String ojuCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="OJU_CREATED_DATE", nullable=false)
	private Date ojuCreatedDate;

	@Column(name="OJU_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal ojuElementState;

	@Column(name="OJU_JOIN_MODIFIED", nullable=false, precision=1)
	private BigDecimal ojuJoinModified;

	@Column(name="OJU_UPDATED_BY", length=64)
	private String ojuUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="OJU_UPDATED_DATE")
	private Date ojuUpdatedDate;

	//bi-directional many-to-one association to Eul5KeyCon
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="OJU_KEY_ID", nullable=false)
	private Eul5KeyCon eul5KeyCon;

	//bi-directional many-to-one association to Eul5Obj
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="OJU_OBJ_ID")
	private Eul5Obj eul5Obj;

	//bi-directional many-to-one association to Eul5SummaryObj
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="OJU_SUMO_ID")
	private Eul5SummaryObj eul5SummaryObj;

	public Eul5ObjJoinUsg() {
	}

	public long getOjuId() {
		return this.ojuId;
	}

	public void setOjuId(long ojuId) {
		this.ojuId = ojuId;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

	public String getOjuCreatedBy() {
		return this.ojuCreatedBy;
	}

	public void setOjuCreatedBy(String ojuCreatedBy) {
		this.ojuCreatedBy = ojuCreatedBy;
	}

	public Date getOjuCreatedDate() {
		return this.ojuCreatedDate;
	}

	public void setOjuCreatedDate(Date ojuCreatedDate) {
		this.ojuCreatedDate = ojuCreatedDate;
	}

	public BigDecimal getOjuElementState() {
		return this.ojuElementState;
	}

	public void setOjuElementState(BigDecimal ojuElementState) {
		this.ojuElementState = ojuElementState;
	}

	public BigDecimal getOjuJoinModified() {
		return this.ojuJoinModified;
	}

	public void setOjuJoinModified(BigDecimal ojuJoinModified) {
		this.ojuJoinModified = ojuJoinModified;
	}

	public String getOjuUpdatedBy() {
		return this.ojuUpdatedBy;
	}

	public void setOjuUpdatedBy(String ojuUpdatedBy) {
		this.ojuUpdatedBy = ojuUpdatedBy;
	}

	public Date getOjuUpdatedDate() {
		return this.ojuUpdatedDate;
	}

	public void setOjuUpdatedDate(Date ojuUpdatedDate) {
		this.ojuUpdatedDate = ojuUpdatedDate;
	}

	public Eul5KeyCon getEul5KeyCon() {
		return this.eul5KeyCon;
	}

	public void setEul5KeyCon(Eul5KeyCon eul5KeyCon) {
		this.eul5KeyCon = eul5KeyCon;
	}

	public Eul5Obj getEul5Obj() {
		return this.eul5Obj;
	}

	public void setEul5Obj(Eul5Obj eul5Obj) {
		this.eul5Obj = eul5Obj;
	}

	public Eul5SummaryObj getEul5SummaryObj() {
		return this.eul5SummaryObj;
	}

	public void setEul5SummaryObj(Eul5SummaryObj eul5SummaryObj) {
		this.eul5SummaryObj = eul5SummaryObj;
	}

}