package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the EUL5_GATEWAYS database table.
 * 
 */
@Entity
@Table(name="EUL5_GATEWAYS")
@NamedQuery(name="Eul5Gateway.findAll", query="SELECT e FROM Eul5Gateway e")
public class Eul5Gateway implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="GW_ID", unique=true, nullable=false, precision=10)
	private long gwId;

	@Column(name="EGW_DATABASE_LINK", length=64)
	private String egwDatabaseLink;

	@Column(name="EGW_SCHEMA", length=64)
	private String egwSchema;

	@Column(name="EGW_SQL_PARADIGM", length=10)
	private String egwSqlParadigm;

	@Column(name="EGW_VERSION", length=30)
	private String egwVersion;

	@Column(name="GW_CREATED_BY", nullable=false, length=64)
	private String gwCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="GW_CREATED_DATE", nullable=false)
	private Date gwCreatedDate;

	@Column(name="GW_DESCRIPTION", length=240)
	private String gwDescription;

	@Column(name="GW_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal gwElementState;

	@Column(name="GW_GATEWAY_NAME", nullable=false, length=100)
	private String gwGatewayName;

	@Column(name="GW_PRODUCT_NAME", nullable=false, length=100)
	private String gwProductName;

	@Column(name="GW_TYPE", nullable=false, length=10)
	private String gwType;

	@Column(name="GW_UPDATED_BY", length=64)
	private String gwUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="GW_UPDATED_DATE")
	private Date gwUpdatedDate;

	@Column(precision=10)
	private BigDecimal notm;

	public Eul5Gateway() {
	}

	public long getGwId() {
		return this.gwId;
	}

	public void setGwId(long gwId) {
		this.gwId = gwId;
	}

	public String getEgwDatabaseLink() {
		return this.egwDatabaseLink;
	}

	public void setEgwDatabaseLink(String egwDatabaseLink) {
		this.egwDatabaseLink = egwDatabaseLink;
	}

	public String getEgwSchema() {
		return this.egwSchema;
	}

	public void setEgwSchema(String egwSchema) {
		this.egwSchema = egwSchema;
	}

	public String getEgwSqlParadigm() {
		return this.egwSqlParadigm;
	}

	public void setEgwSqlParadigm(String egwSqlParadigm) {
		this.egwSqlParadigm = egwSqlParadigm;
	}

	public String getEgwVersion() {
		return this.egwVersion;
	}

	public void setEgwVersion(String egwVersion) {
		this.egwVersion = egwVersion;
	}

	public String getGwCreatedBy() {
		return this.gwCreatedBy;
	}

	public void setGwCreatedBy(String gwCreatedBy) {
		this.gwCreatedBy = gwCreatedBy;
	}

	public Date getGwCreatedDate() {
		return this.gwCreatedDate;
	}

	public void setGwCreatedDate(Date gwCreatedDate) {
		this.gwCreatedDate = gwCreatedDate;
	}

	public String getGwDescription() {
		return this.gwDescription;
	}

	public void setGwDescription(String gwDescription) {
		this.gwDescription = gwDescription;
	}

	public BigDecimal getGwElementState() {
		return this.gwElementState;
	}

	public void setGwElementState(BigDecimal gwElementState) {
		this.gwElementState = gwElementState;
	}

	public String getGwGatewayName() {
		return this.gwGatewayName;
	}

	public void setGwGatewayName(String gwGatewayName) {
		this.gwGatewayName = gwGatewayName;
	}

	public String getGwProductName() {
		return this.gwProductName;
	}

	public void setGwProductName(String gwProductName) {
		this.gwProductName = gwProductName;
	}

	public String getGwType() {
		return this.gwType;
	}

	public void setGwType(String gwType) {
		this.gwType = gwType;
	}

	public String getGwUpdatedBy() {
		return this.gwUpdatedBy;
	}

	public void setGwUpdatedBy(String gwUpdatedBy) {
		this.gwUpdatedBy = gwUpdatedBy;
	}

	public Date getGwUpdatedDate() {
		return this.gwUpdatedDate;
	}

	public void setGwUpdatedDate(Date gwUpdatedDate) {
		this.gwUpdatedDate = gwUpdatedDate;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

}