package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the EUL5_EUL_USERS database table.
 * 
 */
@Entity
@Table(name="EUL5_EUL_USERS")
@NamedQuery(name="Eul5EulUser.findAll", query="SELECT e FROM Eul5EulUser e")
public class Eul5EulUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="EU_ID", unique=true, nullable=false, precision=10)
	private long euId;

	@Column(name="EU_BATCH_CMT_SZ", precision=22)
	private BigDecimal euBatchCmtSz;

	@Column(name="EU_BATCH_EXPIRY", precision=22)
	private BigDecimal euBatchExpiry;

	@Column(name="EU_BATCH_JOBS_LMT", precision=22)
	private BigDecimal euBatchJobsLmt;

	@Column(name="EU_BATCH_QTIME_LMT", precision=22)
	private BigDecimal euBatchQtimeLmt;

	@Column(name="EU_BATCH_REP_USER", length=64)
	private String euBatchRepUser;

	@Temporal(TemporalType.DATE)
	@Column(name="EU_BATCH_WND_END")
	private Date euBatchWndEnd;

	@Temporal(TemporalType.DATE)
	@Column(name="EU_BATCH_WND_START")
	private Date euBatchWndStart;

	@Column(name="EU_CREATED_BY", nullable=false, length=64)
	private String euCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="EU_CREATED_DATE", nullable=false)
	private Date euCreatedDate;

	@Column(name="EU_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal euElementState;

	@Temporal(TemporalType.DATE)
	@Column(name="EU_LAST_BATCH_ACC")
	private Date euLastBatchAcc;

	@Column(name="EU_QUERY_EST_LMT", precision=22)
	private BigDecimal euQueryEstLmt;

	@Column(name="EU_QUERY_TIME_LMT", precision=22)
	private BigDecimal euQueryTimeLmt;

	@Column(name="EU_ROLE_FLAG", nullable=false, precision=1)
	private BigDecimal euRoleFlag;

	@Column(name="EU_ROW_FETCH_LMT", precision=22)
	private BigDecimal euRowFetchLmt;

	@Column(name="EU_SECURITY_MODEL", nullable=false, precision=2)
	private BigDecimal euSecurityModel;

	@Column(name="EU_UPDATED_BY", length=64)
	private String euUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="EU_UPDATED_DATE")
	private Date euUpdatedDate;

	@Column(name="EU_USE_PUB_PRIVS", nullable=false, precision=1)
	private BigDecimal euUsePubPrivs;

	@Column(name="EU_USERNAME", nullable=false, length=64)
	private String euUsername;

	@Column(precision=10)
	private BigDecimal notm;

	//bi-directional many-to-one association to Eul5AccessPriv
	@OneToMany(mappedBy="eul5EulUser")
	private List<Eul5AccessPriv> eul5AccessPrivs;

	//bi-directional many-to-one association to Eul5AsmpCon
	@OneToMany(mappedBy="eul5EulUser")
	private List<Eul5AsmpCon> eul5AsmpCons;

	//bi-directional many-to-one association to Eul5BatchReport
	@OneToMany(mappedBy="eul5EulUser")
	private List<Eul5BatchReport> eul5BatchReports;

	//bi-directional many-to-one association to Eul5Document
	@OneToMany(mappedBy="eul5EulUser")
	private List<Eul5Document> eul5Documents;

	//bi-directional many-to-one association to Eul5SumRfshSet
	@OneToMany(mappedBy="eul5EulUser")
	private List<Eul5SumRfshSet> eul5SumRfshSets;

	public Eul5EulUser() {
	}

	public long getEuId() {
		return this.euId;
	}

	public void setEuId(long euId) {
		this.euId = euId;
	}

	public BigDecimal getEuBatchCmtSz() {
		return this.euBatchCmtSz;
	}

	public void setEuBatchCmtSz(BigDecimal euBatchCmtSz) {
		this.euBatchCmtSz = euBatchCmtSz;
	}

	public BigDecimal getEuBatchExpiry() {
		return this.euBatchExpiry;
	}

	public void setEuBatchExpiry(BigDecimal euBatchExpiry) {
		this.euBatchExpiry = euBatchExpiry;
	}

	public BigDecimal getEuBatchJobsLmt() {
		return this.euBatchJobsLmt;
	}

	public void setEuBatchJobsLmt(BigDecimal euBatchJobsLmt) {
		this.euBatchJobsLmt = euBatchJobsLmt;
	}

	public BigDecimal getEuBatchQtimeLmt() {
		return this.euBatchQtimeLmt;
	}

	public void setEuBatchQtimeLmt(BigDecimal euBatchQtimeLmt) {
		this.euBatchQtimeLmt = euBatchQtimeLmt;
	}

	public String getEuBatchRepUser() {
		return this.euBatchRepUser;
	}

	public void setEuBatchRepUser(String euBatchRepUser) {
		this.euBatchRepUser = euBatchRepUser;
	}

	public Date getEuBatchWndEnd() {
		return this.euBatchWndEnd;
	}

	public void setEuBatchWndEnd(Date euBatchWndEnd) {
		this.euBatchWndEnd = euBatchWndEnd;
	}

	public Date getEuBatchWndStart() {
		return this.euBatchWndStart;
	}

	public void setEuBatchWndStart(Date euBatchWndStart) {
		this.euBatchWndStart = euBatchWndStart;
	}

	public String getEuCreatedBy() {
		return this.euCreatedBy;
	}

	public void setEuCreatedBy(String euCreatedBy) {
		this.euCreatedBy = euCreatedBy;
	}

	public Date getEuCreatedDate() {
		return this.euCreatedDate;
	}

	public void setEuCreatedDate(Date euCreatedDate) {
		this.euCreatedDate = euCreatedDate;
	}

	public BigDecimal getEuElementState() {
		return this.euElementState;
	}

	public void setEuElementState(BigDecimal euElementState) {
		this.euElementState = euElementState;
	}

	public Date getEuLastBatchAcc() {
		return this.euLastBatchAcc;
	}

	public void setEuLastBatchAcc(Date euLastBatchAcc) {
		this.euLastBatchAcc = euLastBatchAcc;
	}

	public BigDecimal getEuQueryEstLmt() {
		return this.euQueryEstLmt;
	}

	public void setEuQueryEstLmt(BigDecimal euQueryEstLmt) {
		this.euQueryEstLmt = euQueryEstLmt;
	}

	public BigDecimal getEuQueryTimeLmt() {
		return this.euQueryTimeLmt;
	}

	public void setEuQueryTimeLmt(BigDecimal euQueryTimeLmt) {
		this.euQueryTimeLmt = euQueryTimeLmt;
	}

	public BigDecimal getEuRoleFlag() {
		return this.euRoleFlag;
	}

	public void setEuRoleFlag(BigDecimal euRoleFlag) {
		this.euRoleFlag = euRoleFlag;
	}

	public BigDecimal getEuRowFetchLmt() {
		return this.euRowFetchLmt;
	}

	public void setEuRowFetchLmt(BigDecimal euRowFetchLmt) {
		this.euRowFetchLmt = euRowFetchLmt;
	}

	public BigDecimal getEuSecurityModel() {
		return this.euSecurityModel;
	}

	public void setEuSecurityModel(BigDecimal euSecurityModel) {
		this.euSecurityModel = euSecurityModel;
	}

	public String getEuUpdatedBy() {
		return this.euUpdatedBy;
	}

	public void setEuUpdatedBy(String euUpdatedBy) {
		this.euUpdatedBy = euUpdatedBy;
	}

	public Date getEuUpdatedDate() {
		return this.euUpdatedDate;
	}

	public void setEuUpdatedDate(Date euUpdatedDate) {
		this.euUpdatedDate = euUpdatedDate;
	}

	public BigDecimal getEuUsePubPrivs() {
		return this.euUsePubPrivs;
	}

	public void setEuUsePubPrivs(BigDecimal euUsePubPrivs) {
		this.euUsePubPrivs = euUsePubPrivs;
	}

	public String getEuUsername() {
		return this.euUsername;
	}

	public void setEuUsername(String euUsername) {
		this.euUsername = euUsername;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

	public List<Eul5AccessPriv> getEul5AccessPrivs() {
		return this.eul5AccessPrivs;
	}

	public void setEul5AccessPrivs(List<Eul5AccessPriv> eul5AccessPrivs) {
		this.eul5AccessPrivs = eul5AccessPrivs;
	}

	public Eul5AccessPriv addEul5AccessPriv(Eul5AccessPriv eul5AccessPriv) {
		getEul5AccessPrivs().add(eul5AccessPriv);
		eul5AccessPriv.setEul5EulUser(this);

		return eul5AccessPriv;
	}

	public Eul5AccessPriv removeEul5AccessPriv(Eul5AccessPriv eul5AccessPriv) {
		getEul5AccessPrivs().remove(eul5AccessPriv);
		eul5AccessPriv.setEul5EulUser(null);

		return eul5AccessPriv;
	}

	public List<Eul5AsmpCon> getEul5AsmpCons() {
		return this.eul5AsmpCons;
	}

	public void setEul5AsmpCons(List<Eul5AsmpCon> eul5AsmpCons) {
		this.eul5AsmpCons = eul5AsmpCons;
	}

	public Eul5AsmpCon addEul5AsmpCon(Eul5AsmpCon eul5AsmpCon) {
		getEul5AsmpCons().add(eul5AsmpCon);
		eul5AsmpCon.setEul5EulUser(this);

		return eul5AsmpCon;
	}

	public Eul5AsmpCon removeEul5AsmpCon(Eul5AsmpCon eul5AsmpCon) {
		getEul5AsmpCons().remove(eul5AsmpCon);
		eul5AsmpCon.setEul5EulUser(null);

		return eul5AsmpCon;
	}

	public List<Eul5BatchReport> getEul5BatchReports() {
		return this.eul5BatchReports;
	}

	public void setEul5BatchReports(List<Eul5BatchReport> eul5BatchReports) {
		this.eul5BatchReports = eul5BatchReports;
	}

	public Eul5BatchReport addEul5BatchReport(Eul5BatchReport eul5BatchReport) {
		getEul5BatchReports().add(eul5BatchReport);
		eul5BatchReport.setEul5EulUser(this);

		return eul5BatchReport;
	}

	public Eul5BatchReport removeEul5BatchReport(Eul5BatchReport eul5BatchReport) {
		getEul5BatchReports().remove(eul5BatchReport);
		eul5BatchReport.setEul5EulUser(null);

		return eul5BatchReport;
	}

	public List<Eul5Document> getEul5Documents() {
		return this.eul5Documents;
	}

	public void setEul5Documents(List<Eul5Document> eul5Documents) {
		this.eul5Documents = eul5Documents;
	}

	public Eul5Document addEul5Document(Eul5Document eul5Document) {
		getEul5Documents().add(eul5Document);
		eul5Document.setEul5EulUser(this);

		return eul5Document;
	}

	public Eul5Document removeEul5Document(Eul5Document eul5Document) {
		getEul5Documents().remove(eul5Document);
		eul5Document.setEul5EulUser(null);

		return eul5Document;
	}

	public List<Eul5SumRfshSet> getEul5SumRfshSets() {
		return this.eul5SumRfshSets;
	}

	public void setEul5SumRfshSets(List<Eul5SumRfshSet> eul5SumRfshSets) {
		this.eul5SumRfshSets = eul5SumRfshSets;
	}

	public Eul5SumRfshSet addEul5SumRfshSet(Eul5SumRfshSet eul5SumRfshSet) {
		getEul5SumRfshSets().add(eul5SumRfshSet);
		eul5SumRfshSet.setEul5EulUser(this);

		return eul5SumRfshSet;
	}

	public Eul5SumRfshSet removeEul5SumRfshSet(Eul5SumRfshSet eul5SumRfshSet) {
		getEul5SumRfshSets().remove(eul5SumRfshSet);
		eul5SumRfshSet.setEul5EulUser(null);

		return eul5SumRfshSet;
	}

}