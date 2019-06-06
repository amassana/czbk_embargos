package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the EUL5_IG_EXP_LINKS database table.
 * 
 */
@Entity
@Table(name="EUL5_IG_EXP_LINKS")
@NamedQuery(name="Eul5IgExpLink.findAll", query="SELECT e FROM Eul5IgExpLink e")
public class Eul5IgExpLink implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="IEL_ID", unique=true, nullable=false, precision=10)
	private long ielId;

	@Column(name="IEL_CREATED_BY", nullable=false, length=64)
	private String ielCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="IEL_CREATED_DATE", nullable=false)
	private Date ielCreatedDate;

	@Column(name="IEL_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal ielElementState;

	@Column(name="IEL_TYPE", nullable=false, length=10)
	private String ielType;

	@Column(name="IEL_UPDATED_BY", length=64)
	private String ielUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="IEL_UPDATED_DATE")
	private Date ielUpdatedDate;

	@Column(name="KIL_SEQUENCE", precision=22)
	private BigDecimal kilSequence;

	@Column(precision=10)
	private BigDecimal notm;

	//bi-directional many-to-one association to Eul5Expression
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="KIL_EXP_ID")
	private Eul5Expression eul5Expression1;

	//bi-directional many-to-one association to Eul5Expression
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="HIL_EXP_ID")
	private Eul5Expression eul5Expression2;

	//bi-directional many-to-one association to Eul5HiNode
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="HIL_HN_ID")
	private Eul5HiNode eul5HiNode;

	//bi-directional many-to-one association to Eul5KeyCon
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="KIL_KEY_ID")
	private Eul5KeyCon eul5KeyCon;

	public Eul5IgExpLink() {
	}

	public long getIelId() {
		return this.ielId;
	}

	public void setIelId(long ielId) {
		this.ielId = ielId;
	}

	public String getIelCreatedBy() {
		return this.ielCreatedBy;
	}

	public void setIelCreatedBy(String ielCreatedBy) {
		this.ielCreatedBy = ielCreatedBy;
	}

	public Date getIelCreatedDate() {
		return this.ielCreatedDate;
	}

	public void setIelCreatedDate(Date ielCreatedDate) {
		this.ielCreatedDate = ielCreatedDate;
	}

	public BigDecimal getIelElementState() {
		return this.ielElementState;
	}

	public void setIelElementState(BigDecimal ielElementState) {
		this.ielElementState = ielElementState;
	}

	public String getIelType() {
		return this.ielType;
	}

	public void setIelType(String ielType) {
		this.ielType = ielType;
	}

	public String getIelUpdatedBy() {
		return this.ielUpdatedBy;
	}

	public void setIelUpdatedBy(String ielUpdatedBy) {
		this.ielUpdatedBy = ielUpdatedBy;
	}

	public Date getIelUpdatedDate() {
		return this.ielUpdatedDate;
	}

	public void setIelUpdatedDate(Date ielUpdatedDate) {
		this.ielUpdatedDate = ielUpdatedDate;
	}

	public BigDecimal getKilSequence() {
		return this.kilSequence;
	}

	public void setKilSequence(BigDecimal kilSequence) {
		this.kilSequence = kilSequence;
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

	public Eul5HiNode getEul5HiNode() {
		return this.eul5HiNode;
	}

	public void setEul5HiNode(Eul5HiNode eul5HiNode) {
		this.eul5HiNode = eul5HiNode;
	}

	public Eul5KeyCon getEul5KeyCon() {
		return this.eul5KeyCon;
	}

	public void setEul5KeyCon(Eul5KeyCon eul5KeyCon) {
		this.eul5KeyCon = eul5KeyCon;
	}

}