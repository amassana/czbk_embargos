package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the EUL5_EXP_DEPS database table.
 * 
 */
@Entity
@Table(name="EUL5_EXP_DEPS")
@NamedQuery(name="Eul5ExpDep.findAll", query="SELECT e FROM Eul5ExpDep e")
public class Eul5ExpDep implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ED_ID", unique=true, nullable=false, precision=10)
	private long edId;

	@Column(name="ED_CREATED_BY", nullable=false, length=64)
	private String edCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="ED_CREATED_DATE", nullable=false)
	private Date edCreatedDate;

	@Column(name="ED_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal edElementState;

	@Column(name="ED_TYPE", nullable=false, length=10)
	private String edType;

	@Column(name="ED_UPDATED_BY", length=64)
	private String edUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="ED_UPDATED_DATE")
	private Date edUpdatedDate;

	@Column(precision=10)
	private BigDecimal notm;

	//bi-directional many-to-one association to Eul5Expression
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PD_P_ID")
	private Eul5Expression eul5Expression1;

	//bi-directional many-to-one association to Eul5Expression
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CID_EXP_ID")
	private Eul5Expression eul5Expression2;

	//bi-directional many-to-one association to Eul5Expression
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CD_EXP_ID")
	private Eul5Expression eul5Expression3;

	//bi-directional many-to-one association to Eul5Expression
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PED_EXP_ID")
	private Eul5Expression eul5Expression4;

	//bi-directional many-to-one association to Eul5Function
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CFD_FUN_ID")
	private Eul5Function eul5Function1;

	//bi-directional many-to-one association to Eul5Function
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PFD_FUN_ID")
	private Eul5Function eul5Function2;

	//bi-directional many-to-one association to Eul5SubQuery
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PSD_SQ_ID")
	private Eul5SubQuery eul5SubQuery;

	public Eul5ExpDep() {
	}

	public long getEdId() {
		return this.edId;
	}

	public void setEdId(long edId) {
		this.edId = edId;
	}

	public String getEdCreatedBy() {
		return this.edCreatedBy;
	}

	public void setEdCreatedBy(String edCreatedBy) {
		this.edCreatedBy = edCreatedBy;
	}

	public Date getEdCreatedDate() {
		return this.edCreatedDate;
	}

	public void setEdCreatedDate(Date edCreatedDate) {
		this.edCreatedDate = edCreatedDate;
	}

	public BigDecimal getEdElementState() {
		return this.edElementState;
	}

	public void setEdElementState(BigDecimal edElementState) {
		this.edElementState = edElementState;
	}

	public String getEdType() {
		return this.edType;
	}

	public void setEdType(String edType) {
		this.edType = edType;
	}

	public String getEdUpdatedBy() {
		return this.edUpdatedBy;
	}

	public void setEdUpdatedBy(String edUpdatedBy) {
		this.edUpdatedBy = edUpdatedBy;
	}

	public Date getEdUpdatedDate() {
		return this.edUpdatedDate;
	}

	public void setEdUpdatedDate(Date edUpdatedDate) {
		this.edUpdatedDate = edUpdatedDate;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

	public Eul5Expression getEul5Expression1() {
		return this.eul5Expression1;
	}

	public void setEul5Expression1(Eul5Expression eul5Expression1) {
		this.eul5Expression1 = eul5Expression1;
	}

	public Eul5Expression getEul5Expression2() {
		return this.eul5Expression2;
	}

	public void setEul5Expression2(Eul5Expression eul5Expression2) {
		this.eul5Expression2 = eul5Expression2;
	}

	public Eul5Expression getEul5Expression3() {
		return this.eul5Expression3;
	}

	public void setEul5Expression3(Eul5Expression eul5Expression3) {
		this.eul5Expression3 = eul5Expression3;
	}

	public Eul5Expression getEul5Expression4() {
		return this.eul5Expression4;
	}

	public void setEul5Expression4(Eul5Expression eul5Expression4) {
		this.eul5Expression4 = eul5Expression4;
	}

	public Eul5Function getEul5Function1() {
		return this.eul5Function1;
	}

	public void setEul5Function1(Eul5Function eul5Function1) {
		this.eul5Function1 = eul5Function1;
	}

	public Eul5Function getEul5Function2() {
		return this.eul5Function2;
	}

	public void setEul5Function2(Eul5Function eul5Function2) {
		this.eul5Function2 = eul5Function2;
	}

	public Eul5SubQuery getEul5SubQuery() {
		return this.eul5SubQuery;
	}

	public void setEul5SubQuery(Eul5SubQuery eul5SubQuery) {
		this.eul5SubQuery = eul5SubQuery;
	}

}