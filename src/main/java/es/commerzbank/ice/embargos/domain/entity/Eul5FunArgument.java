package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the EUL5_FUN_ARGUMENTS database table.
 * 
 */
@Entity
@Table(name="EUL5_FUN_ARGUMENTS")
@NamedQuery(name="Eul5FunArgument.findAll", query="SELECT e FROM Eul5FunArgument e")
public class Eul5FunArgument implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="FA_ID", unique=true, nullable=false, precision=10)
	private long faId;

	@Column(name="FA_CREATED_BY", nullable=false, length=64)
	private String faCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="FA_CREATED_DATE", nullable=false)
	private Date faCreatedDate;

	@Column(name="FA_DATA_TYPE", nullable=false, precision=2)
	private BigDecimal faDataType;

	@Column(name="FA_DESCRIPTION_MN", precision=10)
	private BigDecimal faDescriptionMn;

	@Column(name="FA_DESCRIPTION_S", length=240)
	private String faDescriptionS;

	@Column(name="FA_DEVELOPER_KEY", nullable=false, length=100)
	private String faDeveloperKey;

	@Column(name="FA_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal faElementState;

	@Column(name="FA_NAME_MN", precision=10)
	private BigDecimal faNameMn;

	@Column(name="FA_NAME_S", length=100)
	private String faNameS;

	@Column(name="FA_OPTIONAL", nullable=false, precision=1)
	private BigDecimal faOptional;

	@Column(name="FA_POSITION", nullable=false, precision=22)
	private BigDecimal faPosition;

	@Column(name="FA_UPDATED_BY", length=64)
	private String faUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="FA_UPDATED_DATE")
	private Date faUpdatedDate;

	@Column(name="FA_USER_PROP1", length=100)
	private String faUserProp1;

	@Column(name="FA_USER_PROP2", length=100)
	private String faUserProp2;

	@Column(precision=10)
	private BigDecimal notm;

	//bi-directional many-to-one association to Eul5Function
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FA_FUN_ID", nullable=false)
	private Eul5Function eul5Function;

	public Eul5FunArgument() {
	}

	public long getFaId() {
		return this.faId;
	}

	public void setFaId(long faId) {
		this.faId = faId;
	}

	public String getFaCreatedBy() {
		return this.faCreatedBy;
	}

	public void setFaCreatedBy(String faCreatedBy) {
		this.faCreatedBy = faCreatedBy;
	}

	public Date getFaCreatedDate() {
		return this.faCreatedDate;
	}

	public void setFaCreatedDate(Date faCreatedDate) {
		this.faCreatedDate = faCreatedDate;
	}

	public BigDecimal getFaDataType() {
		return this.faDataType;
	}

	public void setFaDataType(BigDecimal faDataType) {
		this.faDataType = faDataType;
	}

	public BigDecimal getFaDescriptionMn() {
		return this.faDescriptionMn;
	}

	public void setFaDescriptionMn(BigDecimal faDescriptionMn) {
		this.faDescriptionMn = faDescriptionMn;
	}

	public String getFaDescriptionS() {
		return this.faDescriptionS;
	}

	public void setFaDescriptionS(String faDescriptionS) {
		this.faDescriptionS = faDescriptionS;
	}

	public String getFaDeveloperKey() {
		return this.faDeveloperKey;
	}

	public void setFaDeveloperKey(String faDeveloperKey) {
		this.faDeveloperKey = faDeveloperKey;
	}

	public BigDecimal getFaElementState() {
		return this.faElementState;
	}

	public void setFaElementState(BigDecimal faElementState) {
		this.faElementState = faElementState;
	}

	public BigDecimal getFaNameMn() {
		return this.faNameMn;
	}

	public void setFaNameMn(BigDecimal faNameMn) {
		this.faNameMn = faNameMn;
	}

	public String getFaNameS() {
		return this.faNameS;
	}

	public void setFaNameS(String faNameS) {
		this.faNameS = faNameS;
	}

	public BigDecimal getFaOptional() {
		return this.faOptional;
	}

	public void setFaOptional(BigDecimal faOptional) {
		this.faOptional = faOptional;
	}

	public BigDecimal getFaPosition() {
		return this.faPosition;
	}

	public void setFaPosition(BigDecimal faPosition) {
		this.faPosition = faPosition;
	}

	public String getFaUpdatedBy() {
		return this.faUpdatedBy;
	}

	public void setFaUpdatedBy(String faUpdatedBy) {
		this.faUpdatedBy = faUpdatedBy;
	}

	public Date getFaUpdatedDate() {
		return this.faUpdatedDate;
	}

	public void setFaUpdatedDate(Date faUpdatedDate) {
		this.faUpdatedDate = faUpdatedDate;
	}

	public String getFaUserProp1() {
		return this.faUserProp1;
	}

	public void setFaUserProp1(String faUserProp1) {
		this.faUserProp1 = faUserProp1;
	}

	public String getFaUserProp2() {
		return this.faUserProp2;
	}

	public void setFaUserProp2(String faUserProp2) {
		this.faUserProp2 = faUserProp2;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

	public Eul5Function getEul5Function() {
		return this.eul5Function;
	}

	public void setEul5Function(Eul5Function eul5Function) {
		this.eul5Function = eul5Function;
	}

}