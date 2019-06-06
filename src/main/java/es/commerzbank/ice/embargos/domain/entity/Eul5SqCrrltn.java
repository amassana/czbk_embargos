package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the EUL5_SQ_CRRLTNS database table.
 * 
 */
@Entity
@Table(name="EUL5_SQ_CRRLTNS")
@NamedQuery(name="Eul5SqCrrltn.findAll", query="SELECT e FROM Eul5SqCrrltn e")
public class Eul5SqCrrltn implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SQC_ID", unique=true, nullable=false, precision=10)
	private long sqcId;

	@Column(precision=10)
	private BigDecimal notm;

	@Column(name="SQC_CREATED_BY", nullable=false, length=64)
	private String sqcCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="SQC_CREATED_DATE", nullable=false)
	private Date sqcCreatedDate;

	@Column(name="SQC_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal sqcElementState;

	@Column(name="SQC_UPDATED_BY", length=64)
	private String sqcUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="SQC_UPDATED_DATE")
	private Date sqcUpdatedDate;

	//bi-directional many-to-one association to Eul5Expression
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SQC_IT_OUTER_ID", nullable=false)
	private Eul5Expression eul5Expression1;

	//bi-directional many-to-one association to Eul5Expression
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SQC_IT_INNER_ID", nullable=false)
	private Eul5Expression eul5Expression2;

	//bi-directional many-to-one association to Eul5SubQuery
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SQC_SQ_ID", nullable=false)
	private Eul5SubQuery eul5SubQuery;

	public Eul5SqCrrltn() {
	}

	public long getSqcId() {
		return this.sqcId;
	}

	public void setSqcId(long sqcId) {
		this.sqcId = sqcId;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

	public String getSqcCreatedBy() {
		return this.sqcCreatedBy;
	}

	public void setSqcCreatedBy(String sqcCreatedBy) {
		this.sqcCreatedBy = sqcCreatedBy;
	}

	public Date getSqcCreatedDate() {
		return this.sqcCreatedDate;
	}

	public void setSqcCreatedDate(Date sqcCreatedDate) {
		this.sqcCreatedDate = sqcCreatedDate;
	}

	public BigDecimal getSqcElementState() {
		return this.sqcElementState;
	}

	public void setSqcElementState(BigDecimal sqcElementState) {
		this.sqcElementState = sqcElementState;
	}

	public String getSqcUpdatedBy() {
		return this.sqcUpdatedBy;
	}

	public void setSqcUpdatedBy(String sqcUpdatedBy) {
		this.sqcUpdatedBy = sqcUpdatedBy;
	}

	public Date getSqcUpdatedDate() {
		return this.sqcUpdatedDate;
	}

	public void setSqcUpdatedDate(Date sqcUpdatedDate) {
		this.sqcUpdatedDate = sqcUpdatedDate;
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

	public Eul5SubQuery getEul5SubQuery() {
		return this.eul5SubQuery;
	}

	public void setEul5SubQuery(Eul5SubQuery eul5SubQuery) {
		this.eul5SubQuery = eul5SubQuery;
	}

}