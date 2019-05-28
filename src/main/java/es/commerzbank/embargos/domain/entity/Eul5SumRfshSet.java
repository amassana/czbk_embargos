package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the EUL5_SUM_RFSH_SETS database table.
 * 
 */
@Entity
@Table(name="EUL5_SUM_RFSH_SETS")
@NamedQuery(name="Eul5SumRfshSet.findAll", query="SELECT e FROM Eul5SumRfshSet e")
public class Eul5SumRfshSet implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SRS_ID", unique=true, nullable=false, precision=10)
	private long srsId;

	@Column(precision=10)
	private BigDecimal notm;

	@Column(name="SRS_AUTO_REFRESH", nullable=false, precision=1)
	private BigDecimal srsAutoRefresh;

	@Column(name="SRS_CREATED_BY", nullable=false, length=64)
	private String srsCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="SRS_CREATED_DATE", nullable=false)
	private Date srsCreatedDate;

	@Column(name="SRS_DESCRIPTION", length=240)
	private String srsDescription;

	@Column(name="SRS_DEVELOPER_KEY", nullable=false, length=100)
	private String srsDeveloperKey;

	@Column(name="SRS_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal srsElementState;

	@Column(name="SRS_JOB_ID", precision=22)
	private BigDecimal srsJobId;

	@Temporal(TemporalType.DATE)
	@Column(name="SRS_LAST_REFRESH")
	private Date srsLastRefresh;

	@Column(name="SRS_NAME", nullable=false, length=100)
	private String srsName;

	@Temporal(TemporalType.DATE)
	@Column(name="SRS_NEXT_REFRESH")
	private Date srsNextRefresh;

	@Column(name="SRS_NUM_FREQ_UNITS", precision=22)
	private BigDecimal srsNumFreqUnits;

	@Column(name="SRS_ONLINE", nullable=false, precision=1)
	private BigDecimal srsOnline;

	@Column(name="SRS_REFRESH_COUNT", precision=22)
	private BigDecimal srsRefreshCount;

	@Column(name="SRS_STATE", nullable=false, precision=2)
	private BigDecimal srsState;

	@Column(name="SRS_UPDATED_BY", length=64)
	private String srsUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="SRS_UPDATED_DATE")
	private Date srsUpdatedDate;

	@Column(name="SRS_USER_PROP1", length=100)
	private String srsUserProp1;

	@Column(name="SRS_USER_PROP2", length=100)
	private String srsUserProp2;

	//bi-directional many-to-one association to Eul5SummaryObj
	@OneToMany(mappedBy="eul5SumRfshSet")
	private List<Eul5SummaryObj> eul5SummaryObjs;

	//bi-directional many-to-one association to Eul5EulUser
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SRS_EU_ID", nullable=false)
	private Eul5EulUser eul5EulUser;

	//bi-directional many-to-one association to Eul5FreqUnit
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SRS_RFU_ID")
	private Eul5FreqUnit eul5FreqUnit;

	public Eul5SumRfshSet() {
	}

	public long getSrsId() {
		return this.srsId;
	}

	public void setSrsId(long srsId) {
		this.srsId = srsId;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

	public BigDecimal getSrsAutoRefresh() {
		return this.srsAutoRefresh;
	}

	public void setSrsAutoRefresh(BigDecimal srsAutoRefresh) {
		this.srsAutoRefresh = srsAutoRefresh;
	}

	public String getSrsCreatedBy() {
		return this.srsCreatedBy;
	}

	public void setSrsCreatedBy(String srsCreatedBy) {
		this.srsCreatedBy = srsCreatedBy;
	}

	public Date getSrsCreatedDate() {
		return this.srsCreatedDate;
	}

	public void setSrsCreatedDate(Date srsCreatedDate) {
		this.srsCreatedDate = srsCreatedDate;
	}

	public String getSrsDescription() {
		return this.srsDescription;
	}

	public void setSrsDescription(String srsDescription) {
		this.srsDescription = srsDescription;
	}

	public String getSrsDeveloperKey() {
		return this.srsDeveloperKey;
	}

	public void setSrsDeveloperKey(String srsDeveloperKey) {
		this.srsDeveloperKey = srsDeveloperKey;
	}

	public BigDecimal getSrsElementState() {
		return this.srsElementState;
	}

	public void setSrsElementState(BigDecimal srsElementState) {
		this.srsElementState = srsElementState;
	}

	public BigDecimal getSrsJobId() {
		return this.srsJobId;
	}

	public void setSrsJobId(BigDecimal srsJobId) {
		this.srsJobId = srsJobId;
	}

	public Date getSrsLastRefresh() {
		return this.srsLastRefresh;
	}

	public void setSrsLastRefresh(Date srsLastRefresh) {
		this.srsLastRefresh = srsLastRefresh;
	}

	public String getSrsName() {
		return this.srsName;
	}

	public void setSrsName(String srsName) {
		this.srsName = srsName;
	}

	public Date getSrsNextRefresh() {
		return this.srsNextRefresh;
	}

	public void setSrsNextRefresh(Date srsNextRefresh) {
		this.srsNextRefresh = srsNextRefresh;
	}

	public BigDecimal getSrsNumFreqUnits() {
		return this.srsNumFreqUnits;
	}

	public void setSrsNumFreqUnits(BigDecimal srsNumFreqUnits) {
		this.srsNumFreqUnits = srsNumFreqUnits;
	}

	public BigDecimal getSrsOnline() {
		return this.srsOnline;
	}

	public void setSrsOnline(BigDecimal srsOnline) {
		this.srsOnline = srsOnline;
	}

	public BigDecimal getSrsRefreshCount() {
		return this.srsRefreshCount;
	}

	public void setSrsRefreshCount(BigDecimal srsRefreshCount) {
		this.srsRefreshCount = srsRefreshCount;
	}

	public BigDecimal getSrsState() {
		return this.srsState;
	}

	public void setSrsState(BigDecimal srsState) {
		this.srsState = srsState;
	}

	public String getSrsUpdatedBy() {
		return this.srsUpdatedBy;
	}

	public void setSrsUpdatedBy(String srsUpdatedBy) {
		this.srsUpdatedBy = srsUpdatedBy;
	}

	public Date getSrsUpdatedDate() {
		return this.srsUpdatedDate;
	}

	public void setSrsUpdatedDate(Date srsUpdatedDate) {
		this.srsUpdatedDate = srsUpdatedDate;
	}

	public String getSrsUserProp1() {
		return this.srsUserProp1;
	}

	public void setSrsUserProp1(String srsUserProp1) {
		this.srsUserProp1 = srsUserProp1;
	}

	public String getSrsUserProp2() {
		return this.srsUserProp2;
	}

	public void setSrsUserProp2(String srsUserProp2) {
		this.srsUserProp2 = srsUserProp2;
	}

	public List<Eul5SummaryObj> getEul5SummaryObjs() {
		return this.eul5SummaryObjs;
	}

	public void setEul5SummaryObjs(List<Eul5SummaryObj> eul5SummaryObjs) {
		this.eul5SummaryObjs = eul5SummaryObjs;
	}

	public Eul5SummaryObj addEul5SummaryObj(Eul5SummaryObj eul5SummaryObj) {
		getEul5SummaryObjs().add(eul5SummaryObj);
		eul5SummaryObj.setEul5SumRfshSet(this);

		return eul5SummaryObj;
	}

	public Eul5SummaryObj removeEul5SummaryObj(Eul5SummaryObj eul5SummaryObj) {
		getEul5SummaryObjs().remove(eul5SummaryObj);
		eul5SummaryObj.setEul5SumRfshSet(null);

		return eul5SummaryObj;
	}

	public Eul5EulUser getEul5EulUser() {
		return this.eul5EulUser;
	}

	public void setEul5EulUser(Eul5EulUser eul5EulUser) {
		this.eul5EulUser = eul5EulUser;
	}

	public Eul5FreqUnit getEul5FreqUnit() {
		return this.eul5FreqUnit;
	}

	public void setEul5FreqUnit(Eul5FreqUnit eul5FreqUnit) {
		this.eul5FreqUnit = eul5FreqUnit;
	}

}