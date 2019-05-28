package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the EUL5_QPP_STATS database table.
 * 
 */
@Entity
@Table(name="EUL5_QPP_STATS")
@NamedQuery(name="Eul5QppStat.findAll", query="SELECT e FROM Eul5QppStat e")
public class Eul5QppStat implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="QS_ID", unique=true, nullable=false, precision=10)
	private long qsId;

	@Column(name="QS_ACT_CPU_TIME")
	private BigDecimal qsActCpuTime;

	@Column(name="QS_ACT_ELAP_TIME", nullable=false)
	private BigDecimal qsActElapTime;

	@Column(name="QS_COST")
	private BigDecimal qsCost;

	@Column(name="QS_CREATED_BY", nullable=false, length=64)
	private String qsCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="QS_CREATED_DATE", nullable=false)
	private Date qsCreatedDate;

	@Column(name="QS_DBMP0")
	private byte[] qsDbmp0;

	@Column(name="QS_DBMP1")
	private byte[] qsDbmp1;

	@Column(name="QS_DBMP2")
	private byte[] qsDbmp2;

	@Column(name="QS_DBMP3")
	private byte[] qsDbmp3;

	@Column(name="QS_DBMP4")
	private byte[] qsDbmp4;

	@Column(name="QS_DBMP5")
	private byte[] qsDbmp5;

	@Column(name="QS_DBMP6")
	private byte[] qsDbmp6;

	@Column(name="QS_DBMP7")
	private byte[] qsDbmp7;

	@Column(name="QS_DOC_DETAILS", length=240)
	private String qsDocDetails;

	@Column(name="QS_DOC_NAME", length=100)
	private String qsDocName;

	@Column(name="QS_DOC_OWNER", length=64)
	private String qsDocOwner;

	@Column(name="QS_EST_ELAP_TIME", nullable=false)
	private BigDecimal qsEstElapTime;

	@Column(name="QS_FBMP0")
	private byte[] qsFbmp0;

	@Column(name="QS_FBMP1")
	private byte[] qsFbmp1;

	@Column(name="QS_FBMP2")
	private byte[] qsFbmp2;

	@Column(name="QS_FBMP3")
	private byte[] qsFbmp3;

	@Column(name="QS_FBMP4")
	private byte[] qsFbmp4;

	@Column(name="QS_FBMP5")
	private byte[] qsFbmp5;

	@Column(name="QS_FBMP6")
	private byte[] qsFbmp6;

	@Column(name="QS_FBMP7")
	private byte[] qsFbmp7;

	@Column(name="QS_JBMP0")
	private byte[] qsJbmp0;

	@Column(name="QS_JBMP1")
	private byte[] qsJbmp1;

	@Column(name="QS_JBMP2")
	private byte[] qsJbmp2;

	@Column(name="QS_JBMP3")
	private byte[] qsJbmp3;

	@Column(name="QS_JBMP4")
	private byte[] qsJbmp4;

	@Column(name="QS_JBMP5")
	private byte[] qsJbmp5;

	@Column(name="QS_JBMP6")
	private byte[] qsJbmp6;

	@Column(name="QS_JBMP7")
	private byte[] qsJbmp7;

	@Column(name="QS_MBMP0")
	private byte[] qsMbmp0;

	@Column(name="QS_MBMP1")
	private byte[] qsMbmp1;

	@Column(name="QS_MBMP2")
	private byte[] qsMbmp2;

	@Column(name="QS_MBMP3")
	private byte[] qsMbmp3;

	@Column(name="QS_MBMP4")
	private byte[] qsMbmp4;

	@Column(name="QS_MBMP5")
	private byte[] qsMbmp5;

	@Column(name="QS_MBMP6")
	private byte[] qsMbmp6;

	@Column(name="QS_MBMP7")
	private byte[] qsMbmp7;

	@Column(name="QS_NUM_ROWS", precision=10)
	private BigDecimal qsNumRows;

	@Column(name="QS_OBJECT_USE_KEY", nullable=false, length=240)
	private String qsObjectUseKey;

	@Column(name="QS_SDO_ID", precision=10)
	private BigDecimal qsSdoId;

	@Column(name="QS_STATE", precision=1)
	private BigDecimal qsState;

	@Column(name="QS_SUMMARY_FIT", precision=2)
	private BigDecimal qsSummaryFit;

	public Eul5QppStat() {
	}

	public long getQsId() {
		return this.qsId;
	}

	public void setQsId(long qsId) {
		this.qsId = qsId;
	}

	public BigDecimal getQsActCpuTime() {
		return this.qsActCpuTime;
	}

	public void setQsActCpuTime(BigDecimal qsActCpuTime) {
		this.qsActCpuTime = qsActCpuTime;
	}

	public BigDecimal getQsActElapTime() {
		return this.qsActElapTime;
	}

	public void setQsActElapTime(BigDecimal qsActElapTime) {
		this.qsActElapTime = qsActElapTime;
	}

	public BigDecimal getQsCost() {
		return this.qsCost;
	}

	public void setQsCost(BigDecimal qsCost) {
		this.qsCost = qsCost;
	}

	public String getQsCreatedBy() {
		return this.qsCreatedBy;
	}

	public void setQsCreatedBy(String qsCreatedBy) {
		this.qsCreatedBy = qsCreatedBy;
	}

	public Date getQsCreatedDate() {
		return this.qsCreatedDate;
	}

	public void setQsCreatedDate(Date qsCreatedDate) {
		this.qsCreatedDate = qsCreatedDate;
	}

	public byte[] getQsDbmp0() {
		return this.qsDbmp0;
	}

	public void setQsDbmp0(byte[] qsDbmp0) {
		this.qsDbmp0 = qsDbmp0;
	}

	public byte[] getQsDbmp1() {
		return this.qsDbmp1;
	}

	public void setQsDbmp1(byte[] qsDbmp1) {
		this.qsDbmp1 = qsDbmp1;
	}

	public byte[] getQsDbmp2() {
		return this.qsDbmp2;
	}

	public void setQsDbmp2(byte[] qsDbmp2) {
		this.qsDbmp2 = qsDbmp2;
	}

	public byte[] getQsDbmp3() {
		return this.qsDbmp3;
	}

	public void setQsDbmp3(byte[] qsDbmp3) {
		this.qsDbmp3 = qsDbmp3;
	}

	public byte[] getQsDbmp4() {
		return this.qsDbmp4;
	}

	public void setQsDbmp4(byte[] qsDbmp4) {
		this.qsDbmp4 = qsDbmp4;
	}

	public byte[] getQsDbmp5() {
		return this.qsDbmp5;
	}

	public void setQsDbmp5(byte[] qsDbmp5) {
		this.qsDbmp5 = qsDbmp5;
	}

	public byte[] getQsDbmp6() {
		return this.qsDbmp6;
	}

	public void setQsDbmp6(byte[] qsDbmp6) {
		this.qsDbmp6 = qsDbmp6;
	}

	public byte[] getQsDbmp7() {
		return this.qsDbmp7;
	}

	public void setQsDbmp7(byte[] qsDbmp7) {
		this.qsDbmp7 = qsDbmp7;
	}

	public String getQsDocDetails() {
		return this.qsDocDetails;
	}

	public void setQsDocDetails(String qsDocDetails) {
		this.qsDocDetails = qsDocDetails;
	}

	public String getQsDocName() {
		return this.qsDocName;
	}

	public void setQsDocName(String qsDocName) {
		this.qsDocName = qsDocName;
	}

	public String getQsDocOwner() {
		return this.qsDocOwner;
	}

	public void setQsDocOwner(String qsDocOwner) {
		this.qsDocOwner = qsDocOwner;
	}

	public BigDecimal getQsEstElapTime() {
		return this.qsEstElapTime;
	}

	public void setQsEstElapTime(BigDecimal qsEstElapTime) {
		this.qsEstElapTime = qsEstElapTime;
	}

	public byte[] getQsFbmp0() {
		return this.qsFbmp0;
	}

	public void setQsFbmp0(byte[] qsFbmp0) {
		this.qsFbmp0 = qsFbmp0;
	}

	public byte[] getQsFbmp1() {
		return this.qsFbmp1;
	}

	public void setQsFbmp1(byte[] qsFbmp1) {
		this.qsFbmp1 = qsFbmp1;
	}

	public byte[] getQsFbmp2() {
		return this.qsFbmp2;
	}

	public void setQsFbmp2(byte[] qsFbmp2) {
		this.qsFbmp2 = qsFbmp2;
	}

	public byte[] getQsFbmp3() {
		return this.qsFbmp3;
	}

	public void setQsFbmp3(byte[] qsFbmp3) {
		this.qsFbmp3 = qsFbmp3;
	}

	public byte[] getQsFbmp4() {
		return this.qsFbmp4;
	}

	public void setQsFbmp4(byte[] qsFbmp4) {
		this.qsFbmp4 = qsFbmp4;
	}

	public byte[] getQsFbmp5() {
		return this.qsFbmp5;
	}

	public void setQsFbmp5(byte[] qsFbmp5) {
		this.qsFbmp5 = qsFbmp5;
	}

	public byte[] getQsFbmp6() {
		return this.qsFbmp6;
	}

	public void setQsFbmp6(byte[] qsFbmp6) {
		this.qsFbmp6 = qsFbmp6;
	}

	public byte[] getQsFbmp7() {
		return this.qsFbmp7;
	}

	public void setQsFbmp7(byte[] qsFbmp7) {
		this.qsFbmp7 = qsFbmp7;
	}

	public byte[] getQsJbmp0() {
		return this.qsJbmp0;
	}

	public void setQsJbmp0(byte[] qsJbmp0) {
		this.qsJbmp0 = qsJbmp0;
	}

	public byte[] getQsJbmp1() {
		return this.qsJbmp1;
	}

	public void setQsJbmp1(byte[] qsJbmp1) {
		this.qsJbmp1 = qsJbmp1;
	}

	public byte[] getQsJbmp2() {
		return this.qsJbmp2;
	}

	public void setQsJbmp2(byte[] qsJbmp2) {
		this.qsJbmp2 = qsJbmp2;
	}

	public byte[] getQsJbmp3() {
		return this.qsJbmp3;
	}

	public void setQsJbmp3(byte[] qsJbmp3) {
		this.qsJbmp3 = qsJbmp3;
	}

	public byte[] getQsJbmp4() {
		return this.qsJbmp4;
	}

	public void setQsJbmp4(byte[] qsJbmp4) {
		this.qsJbmp4 = qsJbmp4;
	}

	public byte[] getQsJbmp5() {
		return this.qsJbmp5;
	}

	public void setQsJbmp5(byte[] qsJbmp5) {
		this.qsJbmp5 = qsJbmp5;
	}

	public byte[] getQsJbmp6() {
		return this.qsJbmp6;
	}

	public void setQsJbmp6(byte[] qsJbmp6) {
		this.qsJbmp6 = qsJbmp6;
	}

	public byte[] getQsJbmp7() {
		return this.qsJbmp7;
	}

	public void setQsJbmp7(byte[] qsJbmp7) {
		this.qsJbmp7 = qsJbmp7;
	}

	public byte[] getQsMbmp0() {
		return this.qsMbmp0;
	}

	public void setQsMbmp0(byte[] qsMbmp0) {
		this.qsMbmp0 = qsMbmp0;
	}

	public byte[] getQsMbmp1() {
		return this.qsMbmp1;
	}

	public void setQsMbmp1(byte[] qsMbmp1) {
		this.qsMbmp1 = qsMbmp1;
	}

	public byte[] getQsMbmp2() {
		return this.qsMbmp2;
	}

	public void setQsMbmp2(byte[] qsMbmp2) {
		this.qsMbmp2 = qsMbmp2;
	}

	public byte[] getQsMbmp3() {
		return this.qsMbmp3;
	}

	public void setQsMbmp3(byte[] qsMbmp3) {
		this.qsMbmp3 = qsMbmp3;
	}

	public byte[] getQsMbmp4() {
		return this.qsMbmp4;
	}

	public void setQsMbmp4(byte[] qsMbmp4) {
		this.qsMbmp4 = qsMbmp4;
	}

	public byte[] getQsMbmp5() {
		return this.qsMbmp5;
	}

	public void setQsMbmp5(byte[] qsMbmp5) {
		this.qsMbmp5 = qsMbmp5;
	}

	public byte[] getQsMbmp6() {
		return this.qsMbmp6;
	}

	public void setQsMbmp6(byte[] qsMbmp6) {
		this.qsMbmp6 = qsMbmp6;
	}

	public byte[] getQsMbmp7() {
		return this.qsMbmp7;
	}

	public void setQsMbmp7(byte[] qsMbmp7) {
		this.qsMbmp7 = qsMbmp7;
	}

	public BigDecimal getQsNumRows() {
		return this.qsNumRows;
	}

	public void setQsNumRows(BigDecimal qsNumRows) {
		this.qsNumRows = qsNumRows;
	}

	public String getQsObjectUseKey() {
		return this.qsObjectUseKey;
	}

	public void setQsObjectUseKey(String qsObjectUseKey) {
		this.qsObjectUseKey = qsObjectUseKey;
	}

	public BigDecimal getQsSdoId() {
		return this.qsSdoId;
	}

	public void setQsSdoId(BigDecimal qsSdoId) {
		this.qsSdoId = qsSdoId;
	}

	public BigDecimal getQsState() {
		return this.qsState;
	}

	public void setQsState(BigDecimal qsState) {
		this.qsState = qsState;
	}

	public BigDecimal getQsSummaryFit() {
		return this.qsSummaryFit;
	}

	public void setQsSummaryFit(BigDecimal qsSummaryFit) {
		this.qsSummaryFit = qsSummaryFit;
	}

}