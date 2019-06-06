package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the EUL5_ASMP_CONS database table.
 * 
 */
@Entity
@Table(name="EUL5_ASMP_CONS")
@NamedQuery(name="Eul5AsmpCon.findAll", query="SELECT e FROM Eul5AsmpCon e")
public class Eul5AsmpCon implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="APC_ID", unique=true, nullable=false, precision=10)
	private long apcId;

	@Column(name="APC_CONS_TYPE", nullable=false, precision=2)
	private BigDecimal apcConsType;

	@Column(name="APC_CREATED_BY", nullable=false, length=64)
	private String apcCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="APC_CREATED_DATE", nullable=false)
	private Date apcCreatedDate;

	@Column(name="APC_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal apcElementState;

	@Column(name="APC_TYPE", nullable=false, length=10)
	private String apcType;

	@Column(name="APC_UPDATED_BY", length=64)
	private String apcUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="APC_UPDATED_DATE")
	private Date apcUpdatedDate;

	@Column(precision=10)
	private BigDecimal notm;

	//bi-directional many-to-one association to Eul5AsmPolicy
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="APC_ASMP_ID", nullable=false)
	private Eul5AsmPolicy eul5AsmPolicy;

	//bi-directional many-to-one association to Eul5EulUser
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="AUC_EU_ID")
	private Eul5EulUser eul5EulUser;

	//bi-directional many-to-one association to Eul5Obj
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="AOC_OBJ_ID")
	private Eul5Obj eul5Obj;

	//bi-directional many-to-one association to Eul5SummaryObj
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ASOC_SUMO_ID")
	private Eul5SummaryObj eul5SummaryObj;

	public Eul5AsmpCon() {
	}

	public long getApcId() {
		return this.apcId;
	}

	public void setApcId(long apcId) {
		this.apcId = apcId;
	}

	public BigDecimal getApcConsType() {
		return this.apcConsType;
	}

	public void setApcConsType(BigDecimal apcConsType) {
		this.apcConsType = apcConsType;
	}

	public String getApcCreatedBy() {
		return this.apcCreatedBy;
	}

	public void setApcCreatedBy(String apcCreatedBy) {
		this.apcCreatedBy = apcCreatedBy;
	}

	public Date getApcCreatedDate() {
		return this.apcCreatedDate;
	}

	public void setApcCreatedDate(Date apcCreatedDate) {
		this.apcCreatedDate = apcCreatedDate;
	}

	public BigDecimal getApcElementState() {
		return this.apcElementState;
	}

	public void setApcElementState(BigDecimal apcElementState) {
		this.apcElementState = apcElementState;
	}

	public String getApcType() {
		return this.apcType;
	}

	public void setApcType(String apcType) {
		this.apcType = apcType;
	}

	public String getApcUpdatedBy() {
		return this.apcUpdatedBy;
	}

	public void setApcUpdatedBy(String apcUpdatedBy) {
		this.apcUpdatedBy = apcUpdatedBy;
	}

	public Date getApcUpdatedDate() {
		return this.apcUpdatedDate;
	}

	public void setApcUpdatedDate(Date apcUpdatedDate) {
		this.apcUpdatedDate = apcUpdatedDate;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

	public Eul5AsmPolicy getEul5AsmPolicy() {
		return this.eul5AsmPolicy;
	}

	public void setEul5AsmPolicy(Eul5AsmPolicy eul5AsmPolicy) {
		this.eul5AsmPolicy = eul5AsmPolicy;
	}

	public Eul5EulUser getEul5EulUser() {
		return this.eul5EulUser;
	}

	public void setEul5EulUser(Eul5EulUser eul5EulUser) {
		this.eul5EulUser = eul5EulUser;
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