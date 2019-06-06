package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the EUL5_APP_PARAMS database table.
 * 
 */
@Entity
@Table(name="EUL5_APP_PARAMS")
@NamedQuery(name="Eul5AppParam.findAll", query="SELECT e FROM Eul5AppParam e")
public class Eul5AppParam implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="APP_ID", unique=true, nullable=false, precision=10)
	private long appId;

	@Column(name="APP_CREATED_BY", nullable=false, length=64)
	private String appCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="APP_CREATED_DATE", nullable=false)
	private Date appCreatedDate;

	@Column(name="APP_DESCRIPTION_MN", nullable=false, precision=10)
	private BigDecimal appDescriptionMn;

	@Column(name="APP_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal appElementState;

	@Column(name="APP_NAME_MN", nullable=false, precision=10)
	private BigDecimal appNameMn;

	@Column(name="APP_TYPE", nullable=false, length=10)
	private String appType;

	@Column(name="APP_UPDATED_BY", length=64)
	private String appUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="APP_UPDATED_DATE")
	private Date appUpdatedDate;

	@Column(precision=10)
	private BigDecimal notm;

	@Column(name="SP_DEFAULT_VALUE", length=240)
	private String spDefaultValue;

	@Column(name="SP_VALUE", length=240)
	private String spValue;

	//bi-directional many-to-one association to Eul5AccessPriv
	@OneToMany(mappedBy="eul5AppParam")
	private List<Eul5AccessPriv> eul5AccessPrivs;

	public Eul5AppParam() {
	}

	public long getAppId() {
		return this.appId;
	}

	public void setAppId(long appId) {
		this.appId = appId;
	}

	public String getAppCreatedBy() {
		return this.appCreatedBy;
	}

	public void setAppCreatedBy(String appCreatedBy) {
		this.appCreatedBy = appCreatedBy;
	}

	public Date getAppCreatedDate() {
		return this.appCreatedDate;
	}

	public void setAppCreatedDate(Date appCreatedDate) {
		this.appCreatedDate = appCreatedDate;
	}

	public BigDecimal getAppDescriptionMn() {
		return this.appDescriptionMn;
	}

	public void setAppDescriptionMn(BigDecimal appDescriptionMn) {
		this.appDescriptionMn = appDescriptionMn;
	}

	public BigDecimal getAppElementState() {
		return this.appElementState;
	}

	public void setAppElementState(BigDecimal appElementState) {
		this.appElementState = appElementState;
	}

	public BigDecimal getAppNameMn() {
		return this.appNameMn;
	}

	public void setAppNameMn(BigDecimal appNameMn) {
		this.appNameMn = appNameMn;
	}

	public String getAppType() {
		return this.appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getAppUpdatedBy() {
		return this.appUpdatedBy;
	}

	public void setAppUpdatedBy(String appUpdatedBy) {
		this.appUpdatedBy = appUpdatedBy;
	}

	public Date getAppUpdatedDate() {
		return this.appUpdatedDate;
	}

	public void setAppUpdatedDate(Date appUpdatedDate) {
		this.appUpdatedDate = appUpdatedDate;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

	public String getSpDefaultValue() {
		return this.spDefaultValue;
	}

	public void setSpDefaultValue(String spDefaultValue) {
		this.spDefaultValue = spDefaultValue;
	}

	public String getSpValue() {
		return this.spValue;
	}

	public void setSpValue(String spValue) {
		this.spValue = spValue;
	}

	public List<Eul5AccessPriv> getEul5AccessPrivs() {
		return this.eul5AccessPrivs;
	}

	public void setEul5AccessPrivs(List<Eul5AccessPriv> eul5AccessPrivs) {
		this.eul5AccessPrivs = eul5AccessPrivs;
	}

	public Eul5AccessPriv addEul5AccessPriv(Eul5AccessPriv eul5AccessPriv) {
		getEul5AccessPrivs().add(eul5AccessPriv);
		eul5AccessPriv.setEul5AppParam(this);

		return eul5AccessPriv;
	}

	public Eul5AccessPriv removeEul5AccessPriv(Eul5AccessPriv eul5AccessPriv) {
		getEul5AccessPrivs().remove(eul5AccessPriv);
		eul5AccessPriv.setEul5AppParam(null);

		return eul5AccessPriv;
	}

}