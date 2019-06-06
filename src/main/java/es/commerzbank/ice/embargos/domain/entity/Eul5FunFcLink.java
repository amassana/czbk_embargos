package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the EUL5_FUN_FC_LINKS database table.
 * 
 */
@Entity
@Table(name="EUL5_FUN_FC_LINKS")
@NamedQuery(name="Eul5FunFcLink.findAll", query="SELECT e FROM Eul5FunFcLink e")
public class Eul5FunFcLink implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="FFL_ID", unique=true, nullable=false, precision=10)
	private long fflId;

	@Column(name="FFL_CREATED_BY", nullable=false, length=64)
	private String fflCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="FFL_CREATED_DATE", nullable=false)
	private Date fflCreatedDate;

	@Column(name="FFL_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal fflElementState;

	@Column(name="FFL_UPDATED_BY", length=64)
	private String fflUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="FFL_UPDATED_DATE")
	private Date fflUpdatedDate;

	@Column(precision=10)
	private BigDecimal notm;

	//bi-directional many-to-one association to Eul5FunCtg
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FFL_FC_ID", nullable=false)
	private Eul5FunCtg eul5FunCtg;

	//bi-directional many-to-one association to Eul5Function
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FFL_FUN_ID", nullable=false)
	private Eul5Function eul5Function;

	public Eul5FunFcLink() {
	}

	public long getFflId() {
		return this.fflId;
	}

	public void setFflId(long fflId) {
		this.fflId = fflId;
	}

	public String getFflCreatedBy() {
		return this.fflCreatedBy;
	}

	public void setFflCreatedBy(String fflCreatedBy) {
		this.fflCreatedBy = fflCreatedBy;
	}

	public Date getFflCreatedDate() {
		return this.fflCreatedDate;
	}

	public void setFflCreatedDate(Date fflCreatedDate) {
		this.fflCreatedDate = fflCreatedDate;
	}

	public BigDecimal getFflElementState() {
		return this.fflElementState;
	}

	public void setFflElementState(BigDecimal fflElementState) {
		this.fflElementState = fflElementState;
	}

	public String getFflUpdatedBy() {
		return this.fflUpdatedBy;
	}

	public void setFflUpdatedBy(String fflUpdatedBy) {
		this.fflUpdatedBy = fflUpdatedBy;
	}

	public Date getFflUpdatedDate() {
		return this.fflUpdatedDate;
	}

	public void setFflUpdatedDate(Date fflUpdatedDate) {
		this.fflUpdatedDate = fflUpdatedDate;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

	public Eul5FunCtg getEul5FunCtg() {
		return this.eul5FunCtg;
	}

	public void setEul5FunCtg(Eul5FunCtg eul5FunCtg) {
		this.eul5FunCtg = eul5FunCtg;
	}

	public Eul5Function getEul5Function() {
		return this.eul5Function;
	}

	public void setEul5Function(Eul5Function eul5Function) {
		this.eul5Function = eul5Function;
	}

}