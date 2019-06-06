package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the EUL5_ACCESS_PRIVS database table.
 * 
 */
@Entity
@Table(name="EUL5_ACCESS_PRIVS")
@NamedQuery(name="Eul5AccessPriv.findAll", query="SELECT e FROM Eul5AccessPriv e")
public class Eul5AccessPriv implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="AP_ID", unique=true, nullable=false, precision=10)
	private long apId;

	@Column(name="AP_CREATED_BY", nullable=false, length=64)
	private String apCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="AP_CREATED_DATE", nullable=false)
	private Date apCreatedDate;

	@Column(name="AP_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal apElementState;

	@Column(name="AP_PRIV_LEVEL", nullable=false, precision=2)
	private BigDecimal apPrivLevel;

	@Column(name="AP_TYPE", nullable=false, length=10)
	private String apType;

	@Column(name="AP_UPDATED_BY", length=64)
	private String apUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="AP_UPDATED_DATE")
	private Date apUpdatedDate;

	@Column(precision=10)
	private BigDecimal notm;

	//bi-directional many-to-one association to Eul5AppParam
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="GP_APP_ID")
	private Eul5AppParam eul5AppParam;

	//bi-directional many-to-one association to Eul5Ba
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="GBA_BA_ID")
	private Eul5Ba eul5Ba;

	//bi-directional many-to-one association to Eul5Document
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="GD_DOC_ID")
	private Eul5Document eul5Document;

	//bi-directional many-to-one association to Eul5EulUser
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="AP_EU_ID", nullable=false)
	private Eul5EulUser eul5EulUser;

	public Eul5AccessPriv() {
	}

	public long getApId() {
		return this.apId;
	}

	public void setApId(long apId) {
		this.apId = apId;
	}

	public String getApCreatedBy() {
		return this.apCreatedBy;
	}

	public void setApCreatedBy(String apCreatedBy) {
		this.apCreatedBy = apCreatedBy;
	}

	public Date getApCreatedDate() {
		return this.apCreatedDate;
	}

	public void setApCreatedDate(Date apCreatedDate) {
		this.apCreatedDate = apCreatedDate;
	}

	public BigDecimal getApElementState() {
		return this.apElementState;
	}

	public void setApElementState(BigDecimal apElementState) {
		this.apElementState = apElementState;
	}

	public BigDecimal getApPrivLevel() {
		return this.apPrivLevel;
	}

	public void setApPrivLevel(BigDecimal apPrivLevel) {
		this.apPrivLevel = apPrivLevel;
	}

	public String getApType() {
		return this.apType;
	}

	public void setApType(String apType) {
		this.apType = apType;
	}

	public String getApUpdatedBy() {
		return this.apUpdatedBy;
	}

	public void setApUpdatedBy(String apUpdatedBy) {
		this.apUpdatedBy = apUpdatedBy;
	}

	public Date getApUpdatedDate() {
		return this.apUpdatedDate;
	}

	public void setApUpdatedDate(Date apUpdatedDate) {
		this.apUpdatedDate = apUpdatedDate;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

	public Eul5AppParam getEul5AppParam() {
		return this.eul5AppParam;
	}

	public void setEul5AppParam(Eul5AppParam eul5AppParam) {
		this.eul5AppParam = eul5AppParam;
	}

	public Eul5Ba getEul5Ba() {
		return this.eul5Ba;
	}

	public void setEul5Ba(Eul5Ba eul5Ba) {
		this.eul5Ba = eul5Ba;
	}

	public Eul5Document getEul5Document() {
		return this.eul5Document;
	}

	public void setEul5Document(Eul5Document eul5Document) {
		this.eul5Document = eul5Document;
	}

	public Eul5EulUser getEul5EulUser() {
		return this.eul5EulUser;
	}

	public void setEul5EulUser(Eul5EulUser eul5EulUser) {
		this.eul5EulUser = eul5EulUser;
	}

}