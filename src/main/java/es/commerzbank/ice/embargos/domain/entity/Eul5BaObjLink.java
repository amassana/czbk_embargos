package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the EUL5_BA_OBJ_LINKS database table.
 * 
 */
@Entity
@Table(name="EUL5_BA_OBJ_LINKS")
@NamedQuery(name="Eul5BaObjLink.findAll", query="SELECT e FROM Eul5BaObjLink e")
public class Eul5BaObjLink implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="BOL_ID", unique=true, nullable=false, precision=10)
	private long bolId;

	@Column(name="BOL_CREATED_BY", nullable=false, length=64)
	private String bolCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="BOL_CREATED_DATE", nullable=false)
	private Date bolCreatedDate;

	@Column(name="BOL_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal bolElementState;

	@Column(name="BOL_SEQUENCE", precision=22)
	private BigDecimal bolSequence;

	@Column(name="BOL_UPDATED_BY", length=64)
	private String bolUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="BOL_UPDATED_DATE")
	private Date bolUpdatedDate;

	@Column(precision=10)
	private BigDecimal notm;

	//bi-directional many-to-one association to Eul5Ba
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="BOL_BA_ID", nullable=false)
	private Eul5Ba eul5Ba;

	//bi-directional many-to-one association to Eul5Obj
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="BOL_OBJ_ID", nullable=false)
	private Eul5Obj eul5Obj;

	public Eul5BaObjLink() {
	}

	public long getBolId() {
		return this.bolId;
	}

	public void setBolId(long bolId) {
		this.bolId = bolId;
	}

	public String getBolCreatedBy() {
		return this.bolCreatedBy;
	}

	public void setBolCreatedBy(String bolCreatedBy) {
		this.bolCreatedBy = bolCreatedBy;
	}

	public Date getBolCreatedDate() {
		return this.bolCreatedDate;
	}

	public void setBolCreatedDate(Date bolCreatedDate) {
		this.bolCreatedDate = bolCreatedDate;
	}

	public BigDecimal getBolElementState() {
		return this.bolElementState;
	}

	public void setBolElementState(BigDecimal bolElementState) {
		this.bolElementState = bolElementState;
	}

	public BigDecimal getBolSequence() {
		return this.bolSequence;
	}

	public void setBolSequence(BigDecimal bolSequence) {
		this.bolSequence = bolSequence;
	}

	public String getBolUpdatedBy() {
		return this.bolUpdatedBy;
	}

	public void setBolUpdatedBy(String bolUpdatedBy) {
		this.bolUpdatedBy = bolUpdatedBy;
	}

	public Date getBolUpdatedDate() {
		return this.bolUpdatedDate;
	}

	public void setBolUpdatedDate(Date bolUpdatedDate) {
		this.bolUpdatedDate = bolUpdatedDate;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

	public Eul5Ba getEul5Ba() {
		return this.eul5Ba;
	}

	public void setEul5Ba(Eul5Ba eul5Ba) {
		this.eul5Ba = eul5Ba;
	}

	public Eul5Obj getEul5Obj() {
		return this.eul5Obj;
	}

	public void setEul5Obj(Eul5Obj eul5Obj) {
		this.eul5Obj = eul5Obj;
	}

}