package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the EUL5_IHS_FK_LINKS database table.
 * 
 */
@Entity
@Table(name="EUL5_IHS_FK_LINKS")
@NamedQuery(name="Eul5IhsFkLink.findAll", query="SELECT e FROM Eul5IhsFkLink e")
public class Eul5IhsFkLink implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="IFL_ID", unique=true, nullable=false, precision=10)
	private long iflId;

	@Column(name="IFL_CREATED_BY", nullable=false, length=64)
	private String iflCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="IFL_CREATED_DATE", nullable=false)
	private Date iflCreatedDate;

	@Column(name="IFL_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal iflElementState;

	@Column(name="IFL_UPDATED_BY", length=64)
	private String iflUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="IFL_UPDATED_DATE")
	private Date iflUpdatedDate;

	@Column(precision=10)
	private BigDecimal notm;

	//bi-directional many-to-one association to Eul5HiSegment
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="IFL_IHS_ID", nullable=false)
	private Eul5HiSegment eul5HiSegment;

	//bi-directional many-to-one association to Eul5KeyCon
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="IFL_KEY_ID", nullable=false)
	private Eul5KeyCon eul5KeyCon;

	public Eul5IhsFkLink() {
	}

	public long getIflId() {
		return this.iflId;
	}

	public void setIflId(long iflId) {
		this.iflId = iflId;
	}

	public String getIflCreatedBy() {
		return this.iflCreatedBy;
	}

	public void setIflCreatedBy(String iflCreatedBy) {
		this.iflCreatedBy = iflCreatedBy;
	}

	public Date getIflCreatedDate() {
		return this.iflCreatedDate;
	}

	public void setIflCreatedDate(Date iflCreatedDate) {
		this.iflCreatedDate = iflCreatedDate;
	}

	public BigDecimal getIflElementState() {
		return this.iflElementState;
	}

	public void setIflElementState(BigDecimal iflElementState) {
		this.iflElementState = iflElementState;
	}

	public String getIflUpdatedBy() {
		return this.iflUpdatedBy;
	}

	public void setIflUpdatedBy(String iflUpdatedBy) {
		this.iflUpdatedBy = iflUpdatedBy;
	}

	public Date getIflUpdatedDate() {
		return this.iflUpdatedDate;
	}

	public void setIflUpdatedDate(Date iflUpdatedDate) {
		this.iflUpdatedDate = iflUpdatedDate;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

	public Eul5HiSegment getEul5HiSegment() {
		return this.eul5HiSegment;
	}

	public void setEul5HiSegment(Eul5HiSegment eul5HiSegment) {
		this.eul5HiSegment = eul5HiSegment;
	}

	public Eul5KeyCon getEul5KeyCon() {
		return this.eul5KeyCon;
	}

	public void setEul5KeyCon(Eul5KeyCon eul5KeyCon) {
		this.eul5KeyCon = eul5KeyCon;
	}

}