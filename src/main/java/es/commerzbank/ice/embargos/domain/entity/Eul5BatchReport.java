package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the EUL5_BATCH_REPORTS database table.
 * 
 */
@Entity
@Table(name="EUL5_BATCH_REPORTS")
@NamedQuery(name="Eul5BatchReport.findAll", query="SELECT e FROM Eul5BatchReport e")
public class Eul5BatchReport implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="BR_ID", unique=true, nullable=false, precision=10)
	private long brId;

	@Column(name="BR_AUTO_REFRESH", nullable=false, precision=1)
	private BigDecimal brAutoRefresh;

	@Temporal(TemporalType.DATE)
	@Column(name="BR_COMPLETION_DATE")
	private Date brCompletionDate;

	@Column(name="BR_CREATED_BY", nullable=false, length=64)
	private String brCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="BR_CREATED_DATE", nullable=false)
	private Date brCreatedDate;

	@Column(name="BR_DESCRIPTION", length=240)
	private String brDescription;

	@Column(name="BR_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal brElementState;

	@Column(name="BR_EXPIRY", precision=22)
	private BigDecimal brExpiry;

	@Column(name="BR_JOB_ID", precision=22)
	private BigDecimal brJobId;

	@Column(name="BR_NAME", nullable=false, length=100)
	private String brName;

	@Temporal(TemporalType.DATE)
	@Column(name="BR_NEXT_RUN_DATE")
	private Date brNextRunDate;

	@Column(name="BR_NUM_FREQ_UNITS", precision=22)
	private BigDecimal brNumFreqUnits;

	@Column(name="BR_OVERWRITE_RSLTS", precision=1)
	private BigDecimal brOverwriteRslts;

	@Column(name="BR_REPORT_SCHEMA", nullable=false, length=64)
	private String brReportSchema;

	@Column(name="BR_UPDATED_BY", length=64)
	private String brUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="BR_UPDATED_DATE")
	private Date brUpdatedDate;

	@Column(name="BR_WORKBOOK_NAME", nullable=false, length=240)
	private String brWorkbookName;

	@Column(precision=10)
	private BigDecimal notm;

	//bi-directional many-to-one association to Eul5EulUser
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="BR_EU_ID", nullable=false)
	private Eul5EulUser eul5EulUser;

	//bi-directional many-to-one association to Eul5FreqUnit
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="BR_RFU_ID", nullable=false)
	private Eul5FreqUnit eul5FreqUnit;

	//bi-directional many-to-one association to Eul5BatchSheet
	@OneToMany(mappedBy="eul5BatchReport")
	private List<Eul5BatchSheet> eul5BatchSheets;

	//bi-directional many-to-one association to Eul5BrRun
	@OneToMany(mappedBy="eul5BatchReport")
	private List<Eul5BrRun> eul5BrRuns;

	public Eul5BatchReport() {
	}

	public long getBrId() {
		return this.brId;
	}

	public void setBrId(long brId) {
		this.brId = brId;
	}

	public BigDecimal getBrAutoRefresh() {
		return this.brAutoRefresh;
	}

	public void setBrAutoRefresh(BigDecimal brAutoRefresh) {
		this.brAutoRefresh = brAutoRefresh;
	}

	public Date getBrCompletionDate() {
		return this.brCompletionDate;
	}

	public void setBrCompletionDate(Date brCompletionDate) {
		this.brCompletionDate = brCompletionDate;
	}

	public String getBrCreatedBy() {
		return this.brCreatedBy;
	}

	public void setBrCreatedBy(String brCreatedBy) {
		this.brCreatedBy = brCreatedBy;
	}

	public Date getBrCreatedDate() {
		return this.brCreatedDate;
	}

	public void setBrCreatedDate(Date brCreatedDate) {
		this.brCreatedDate = brCreatedDate;
	}

	public String getBrDescription() {
		return this.brDescription;
	}

	public void setBrDescription(String brDescription) {
		this.brDescription = brDescription;
	}

	public BigDecimal getBrElementState() {
		return this.brElementState;
	}

	public void setBrElementState(BigDecimal brElementState) {
		this.brElementState = brElementState;
	}

	public BigDecimal getBrExpiry() {
		return this.brExpiry;
	}

	public void setBrExpiry(BigDecimal brExpiry) {
		this.brExpiry = brExpiry;
	}

	public BigDecimal getBrJobId() {
		return this.brJobId;
	}

	public void setBrJobId(BigDecimal brJobId) {
		this.brJobId = brJobId;
	}

	public String getBrName() {
		return this.brName;
	}

	public void setBrName(String brName) {
		this.brName = brName;
	}

	public Date getBrNextRunDate() {
		return this.brNextRunDate;
	}

	public void setBrNextRunDate(Date brNextRunDate) {
		this.brNextRunDate = brNextRunDate;
	}

	public BigDecimal getBrNumFreqUnits() {
		return this.brNumFreqUnits;
	}

	public void setBrNumFreqUnits(BigDecimal brNumFreqUnits) {
		this.brNumFreqUnits = brNumFreqUnits;
	}

	public BigDecimal getBrOverwriteRslts() {
		return this.brOverwriteRslts;
	}

	public void setBrOverwriteRslts(BigDecimal brOverwriteRslts) {
		this.brOverwriteRslts = brOverwriteRslts;
	}

	public String getBrReportSchema() {
		return this.brReportSchema;
	}

	public void setBrReportSchema(String brReportSchema) {
		this.brReportSchema = brReportSchema;
	}

	public String getBrUpdatedBy() {
		return this.brUpdatedBy;
	}

	public void setBrUpdatedBy(String brUpdatedBy) {
		this.brUpdatedBy = brUpdatedBy;
	}

	public Date getBrUpdatedDate() {
		return this.brUpdatedDate;
	}

	public void setBrUpdatedDate(Date brUpdatedDate) {
		this.brUpdatedDate = brUpdatedDate;
	}

	public String getBrWorkbookName() {
		return this.brWorkbookName;
	}

	public void setBrWorkbookName(String brWorkbookName) {
		this.brWorkbookName = brWorkbookName;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
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

	public List<Eul5BatchSheet> getEul5BatchSheets() {
		return this.eul5BatchSheets;
	}

	public void setEul5BatchSheets(List<Eul5BatchSheet> eul5BatchSheets) {
		this.eul5BatchSheets = eul5BatchSheets;
	}

	public Eul5BatchSheet addEul5BatchSheet(Eul5BatchSheet eul5BatchSheet) {
		getEul5BatchSheets().add(eul5BatchSheet);
		eul5BatchSheet.setEul5BatchReport(this);

		return eul5BatchSheet;
	}

	public Eul5BatchSheet removeEul5BatchSheet(Eul5BatchSheet eul5BatchSheet) {
		getEul5BatchSheets().remove(eul5BatchSheet);
		eul5BatchSheet.setEul5BatchReport(null);

		return eul5BatchSheet;
	}

	public List<Eul5BrRun> getEul5BrRuns() {
		return this.eul5BrRuns;
	}

	public void setEul5BrRuns(List<Eul5BrRun> eul5BrRuns) {
		this.eul5BrRuns = eul5BrRuns;
	}

	public Eul5BrRun addEul5BrRun(Eul5BrRun eul5BrRun) {
		getEul5BrRuns().add(eul5BrRun);
		eul5BrRun.setEul5BatchReport(this);

		return eul5BrRun;
	}

	public Eul5BrRun removeEul5BrRun(Eul5BrRun eul5BrRun) {
		getEul5BrRuns().remove(eul5BrRun);
		eul5BrRun.setEul5BatchReport(null);

		return eul5BrRun;
	}

}