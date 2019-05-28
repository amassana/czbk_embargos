package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the EUL5_BQ_DEPS database table.
 * 
 */
@Entity
@Table(name="EUL5_BQ_DEPS")
@NamedQuery(name="Eul5BqDep.findAll", query="SELECT e FROM Eul5BqDep e")
public class Eul5BqDep implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="BQD_ID", unique=true, nullable=false, precision=10)
	private long bqdId;

	@Column(name="BQD_CREATED_BY", nullable=false, length=64)
	private String bqdCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="BQD_CREATED_DATE", nullable=false)
	private Date bqdCreatedDate;

	@Column(name="BQD_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal bqdElementState;

	@Column(name="BQD_TYPE", nullable=false, length=10)
	private String bqdType;

	@Column(name="BQD_UPDATED_BY", length=64)
	private String bqdUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="BQD_UPDATED_DATE")
	private Date bqdUpdatedDate;

	@Column(precision=10)
	private BigDecimal notm;

	//bi-directional many-to-one association to Eul5BatchQuery
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="BQD_BQ_ID", nullable=false)
	private Eul5BatchQuery eul5BatchQuery;

	//bi-directional many-to-one association to Eul5Expression
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="BFILD_FIL_ID")
	private Eul5Expression eul5Expression1;

	//bi-directional many-to-one association to Eul5Expression
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="BID_IT_ID")
	private Eul5Expression eul5Expression2;

	//bi-directional many-to-one association to Eul5Function
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="BFUND_FUN_ID")
	private Eul5Function eul5Function;

	public Eul5BqDep() {
	}

	public long getBqdId() {
		return this.bqdId;
	}

	public void setBqdId(long bqdId) {
		this.bqdId = bqdId;
	}

	public String getBqdCreatedBy() {
		return this.bqdCreatedBy;
	}

	public void setBqdCreatedBy(String bqdCreatedBy) {
		this.bqdCreatedBy = bqdCreatedBy;
	}

	public Date getBqdCreatedDate() {
		return this.bqdCreatedDate;
	}

	public void setBqdCreatedDate(Date bqdCreatedDate) {
		this.bqdCreatedDate = bqdCreatedDate;
	}

	public BigDecimal getBqdElementState() {
		return this.bqdElementState;
	}

	public void setBqdElementState(BigDecimal bqdElementState) {
		this.bqdElementState = bqdElementState;
	}

	public String getBqdType() {
		return this.bqdType;
	}

	public void setBqdType(String bqdType) {
		this.bqdType = bqdType;
	}

	public String getBqdUpdatedBy() {
		return this.bqdUpdatedBy;
	}

	public void setBqdUpdatedBy(String bqdUpdatedBy) {
		this.bqdUpdatedBy = bqdUpdatedBy;
	}

	public Date getBqdUpdatedDate() {
		return this.bqdUpdatedDate;
	}

	public void setBqdUpdatedDate(Date bqdUpdatedDate) {
		this.bqdUpdatedDate = bqdUpdatedDate;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

	public Eul5BatchQuery getEul5BatchQuery() {
		return this.eul5BatchQuery;
	}

	public void setEul5BatchQuery(Eul5BatchQuery eul5BatchQuery) {
		this.eul5BatchQuery = eul5BatchQuery;
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

	public Eul5Function getEul5Function() {
		return this.eul5Function;
	}

	public void setEul5Function(Eul5Function eul5Function) {
		this.eul5Function = eul5Function;
	}

}