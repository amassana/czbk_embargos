package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the EUL5_BATCH_PARAMS database table.
 * 
 */
@Entity
@Table(name="EUL5_BATCH_PARAMS")
@NamedQuery(name="Eul5BatchParam.findAll", query="SELECT e FROM Eul5BatchParam e")
public class Eul5BatchParam implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="BP_ID", unique=true, nullable=false, precision=10)
	private long bpId;

	@Column(name="BP_CREATED_BY", nullable=false, length=64)
	private String bpCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="BP_CREATED_DATE", nullable=false)
	private Date bpCreatedDate;

	@Column(name="BP_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal bpElementState;

	@Column(name="BP_NAME", nullable=false, length=100)
	private String bpName;

	@Column(name="BP_UPDATED_BY", length=64)
	private String bpUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="BP_UPDATED_DATE")
	private Date bpUpdatedDate;

	@Column(name="BP_VALUE1", nullable=false, length=250)
	private String bpValue1;

	@Column(name="BP_VALUE2", length=250)
	private String bpValue2;

	@Column(name="BP_VALUE3", length=250)
	private String bpValue3;

	@Column(name="BP_VALUE4", length=250)
	private String bpValue4;

	@Column(name="BP_VALUE5", length=250)
	private String bpValue5;

	@Column(name="BP_VALUE6", length=250)
	private String bpValue6;

	@Column(precision=10)
	private BigDecimal notm;

	//bi-directional many-to-one association to Eul5BatchSheet
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="BP_BS_ID", nullable=false)
	private Eul5BatchSheet eul5BatchSheet;

	public Eul5BatchParam() {
	}

	public long getBpId() {
		return this.bpId;
	}

	public void setBpId(long bpId) {
		this.bpId = bpId;
	}

	public String getBpCreatedBy() {
		return this.bpCreatedBy;
	}

	public void setBpCreatedBy(String bpCreatedBy) {
		this.bpCreatedBy = bpCreatedBy;
	}

	public Date getBpCreatedDate() {
		return this.bpCreatedDate;
	}

	public void setBpCreatedDate(Date bpCreatedDate) {
		this.bpCreatedDate = bpCreatedDate;
	}

	public BigDecimal getBpElementState() {
		return this.bpElementState;
	}

	public void setBpElementState(BigDecimal bpElementState) {
		this.bpElementState = bpElementState;
	}

	public String getBpName() {
		return this.bpName;
	}

	public void setBpName(String bpName) {
		this.bpName = bpName;
	}

	public String getBpUpdatedBy() {
		return this.bpUpdatedBy;
	}

	public void setBpUpdatedBy(String bpUpdatedBy) {
		this.bpUpdatedBy = bpUpdatedBy;
	}

	public Date getBpUpdatedDate() {
		return this.bpUpdatedDate;
	}

	public void setBpUpdatedDate(Date bpUpdatedDate) {
		this.bpUpdatedDate = bpUpdatedDate;
	}

	public String getBpValue1() {
		return this.bpValue1;
	}

	public void setBpValue1(String bpValue1) {
		this.bpValue1 = bpValue1;
	}

	public String getBpValue2() {
		return this.bpValue2;
	}

	public void setBpValue2(String bpValue2) {
		this.bpValue2 = bpValue2;
	}

	public String getBpValue3() {
		return this.bpValue3;
	}

	public void setBpValue3(String bpValue3) {
		this.bpValue3 = bpValue3;
	}

	public String getBpValue4() {
		return this.bpValue4;
	}

	public void setBpValue4(String bpValue4) {
		this.bpValue4 = bpValue4;
	}

	public String getBpValue5() {
		return this.bpValue5;
	}

	public void setBpValue5(String bpValue5) {
		this.bpValue5 = bpValue5;
	}

	public String getBpValue6() {
		return this.bpValue6;
	}

	public void setBpValue6(String bpValue6) {
		this.bpValue6 = bpValue6;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

	public Eul5BatchSheet getEul5BatchSheet() {
		return this.eul5BatchSheet;
	}

	public void setEul5BatchSheet(Eul5BatchSheet eul5BatchSheet) {
		this.eul5BatchSheet = eul5BatchSheet;
	}

}