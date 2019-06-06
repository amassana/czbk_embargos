package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the EUL5_ASM_POLICIES database table.
 * 
 */
@Entity
@Table(name="EUL5_ASM_POLICIES")
@NamedQuery(name="Eul5AsmPolicy.findAll", query="SELECT e FROM Eul5AsmPolicy e")
public class Eul5AsmPolicy implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ASMP_ID", unique=true, nullable=false, precision=10)
	private long asmpId;

	@Column(name="ASMP_CREATED_BY", nullable=false, length=64)
	private String asmpCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="ASMP_CREATED_DATE", nullable=false)
	private Date asmpCreatedDate;

	@Column(name="ASMP_DESCRIPTION", length=240)
	private String asmpDescription;

	@Column(name="ASMP_DEVELOPER_KEY", nullable=false, length=100)
	private String asmpDeveloperKey;

	@Column(name="ASMP_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal asmpElementState;

	@Column(name="ASMP_NAME", nullable=false, length=100)
	private String asmpName;

	@Column(name="ASMP_UPDATED_BY", length=64)
	private String asmpUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="ASMP_UPDATED_DATE")
	private Date asmpUpdatedDate;

	@Column(name="ASMP_USER_PROP1", length=100)
	private String asmpUserProp1;

	@Column(name="ASMP_USER_PROP2", length=100)
	private String asmpUserProp2;

	@Column(precision=10)
	private BigDecimal notm;

	//bi-directional many-to-one association to Eul5AsmpCon
	@OneToMany(mappedBy="eul5AsmPolicy")
	private List<Eul5AsmpCon> eul5AsmpCons;

	//bi-directional many-to-one association to Eul5AsmpLog
	@OneToMany(mappedBy="eul5AsmPolicy")
	private List<Eul5AsmpLog> eul5AsmpLogs;

	//bi-directional many-to-one association to Eul5SummaryObj
	@OneToMany(mappedBy="eul5AsmPolicy")
	private List<Eul5SummaryObj> eul5SummaryObjs;

	public Eul5AsmPolicy() {
	}

	public long getAsmpId() {
		return this.asmpId;
	}

	public void setAsmpId(long asmpId) {
		this.asmpId = asmpId;
	}

	public String getAsmpCreatedBy() {
		return this.asmpCreatedBy;
	}

	public void setAsmpCreatedBy(String asmpCreatedBy) {
		this.asmpCreatedBy = asmpCreatedBy;
	}

	public Date getAsmpCreatedDate() {
		return this.asmpCreatedDate;
	}

	public void setAsmpCreatedDate(Date asmpCreatedDate) {
		this.asmpCreatedDate = asmpCreatedDate;
	}

	public String getAsmpDescription() {
		return this.asmpDescription;
	}

	public void setAsmpDescription(String asmpDescription) {
		this.asmpDescription = asmpDescription;
	}

	public String getAsmpDeveloperKey() {
		return this.asmpDeveloperKey;
	}

	public void setAsmpDeveloperKey(String asmpDeveloperKey) {
		this.asmpDeveloperKey = asmpDeveloperKey;
	}

	public BigDecimal getAsmpElementState() {
		return this.asmpElementState;
	}

	public void setAsmpElementState(BigDecimal asmpElementState) {
		this.asmpElementState = asmpElementState;
	}

	public String getAsmpName() {
		return this.asmpName;
	}

	public void setAsmpName(String asmpName) {
		this.asmpName = asmpName;
	}

	public String getAsmpUpdatedBy() {
		return this.asmpUpdatedBy;
	}

	public void setAsmpUpdatedBy(String asmpUpdatedBy) {
		this.asmpUpdatedBy = asmpUpdatedBy;
	}

	public Date getAsmpUpdatedDate() {
		return this.asmpUpdatedDate;
	}

	public void setAsmpUpdatedDate(Date asmpUpdatedDate) {
		this.asmpUpdatedDate = asmpUpdatedDate;
	}

	public String getAsmpUserProp1() {
		return this.asmpUserProp1;
	}

	public void setAsmpUserProp1(String asmpUserProp1) {
		this.asmpUserProp1 = asmpUserProp1;
	}

	public String getAsmpUserProp2() {
		return this.asmpUserProp2;
	}

	public void setAsmpUserProp2(String asmpUserProp2) {
		this.asmpUserProp2 = asmpUserProp2;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

	public List<Eul5AsmpCon> getEul5AsmpCons() {
		return this.eul5AsmpCons;
	}

	public void setEul5AsmpCons(List<Eul5AsmpCon> eul5AsmpCons) {
		this.eul5AsmpCons = eul5AsmpCons;
	}

	public Eul5AsmpCon addEul5AsmpCon(Eul5AsmpCon eul5AsmpCon) {
		getEul5AsmpCons().add(eul5AsmpCon);
		eul5AsmpCon.setEul5AsmPolicy(this);

		return eul5AsmpCon;
	}

	public Eul5AsmpCon removeEul5AsmpCon(Eul5AsmpCon eul5AsmpCon) {
		getEul5AsmpCons().remove(eul5AsmpCon);
		eul5AsmpCon.setEul5AsmPolicy(null);

		return eul5AsmpCon;
	}

	public List<Eul5AsmpLog> getEul5AsmpLogs() {
		return this.eul5AsmpLogs;
	}

	public void setEul5AsmpLogs(List<Eul5AsmpLog> eul5AsmpLogs) {
		this.eul5AsmpLogs = eul5AsmpLogs;
	}

	public Eul5AsmpLog addEul5AsmpLog(Eul5AsmpLog eul5AsmpLog) {
		getEul5AsmpLogs().add(eul5AsmpLog);
		eul5AsmpLog.setEul5AsmPolicy(this);

		return eul5AsmpLog;
	}

	public Eul5AsmpLog removeEul5AsmpLog(Eul5AsmpLog eul5AsmpLog) {
		getEul5AsmpLogs().remove(eul5AsmpLog);
		eul5AsmpLog.setEul5AsmPolicy(null);

		return eul5AsmpLog;
	}

	public List<Eul5SummaryObj> getEul5SummaryObjs() {
		return this.eul5SummaryObjs;
	}

	public void setEul5SummaryObjs(List<Eul5SummaryObj> eul5SummaryObjs) {
		this.eul5SummaryObjs = eul5SummaryObjs;
	}

	public Eul5SummaryObj addEul5SummaryObj(Eul5SummaryObj eul5SummaryObj) {
		getEul5SummaryObjs().add(eul5SummaryObj);
		eul5SummaryObj.setEul5AsmPolicy(this);

		return eul5SummaryObj;
	}

	public Eul5SummaryObj removeEul5SummaryObj(Eul5SummaryObj eul5SummaryObj) {
		getEul5SummaryObjs().remove(eul5SummaryObj);
		eul5SummaryObj.setEul5AsmPolicy(null);

		return eul5SummaryObj;
	}

}