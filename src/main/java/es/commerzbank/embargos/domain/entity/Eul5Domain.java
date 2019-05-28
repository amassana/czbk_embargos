package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the EUL5_DOMAINS database table.
 * 
 */
@Entity
@Table(name="EUL5_DOMAINS")
@NamedQuery(name="Eul5Domain.findAll", query="SELECT e FROM Eul5Domain e")
public class Eul5Domain implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DOM_ID", unique=true, nullable=false, precision=10)
	private long domId;

	@Column(name="DOM_ARRAY_FETCH_SZ", precision=22)
	private BigDecimal domArrayFetchSz;

	@Column(name="DOM_CACHED", nullable=false, precision=1)
	private BigDecimal domCached;

	@Column(name="DOM_CARDINALITY", precision=22)
	private BigDecimal domCardinality;

	@Column(name="DOM_CREATED_BY", nullable=false, length=64)
	private String domCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="DOM_CREATED_DATE", nullable=false)
	private Date domCreatedDate;

	@Column(name="DOM_DATA_TYPE", nullable=false, precision=2)
	private BigDecimal domDataType;

	@Column(name="DOM_DESCRIPTION", length=240)
	private String domDescription;

	@Column(name="DOM_DEVELOPER_KEY", nullable=false, length=100)
	private String domDeveloperKey;

	@Column(name="DOM_DIST_ORD_BY", precision=1)
	private BigDecimal domDistOrdBy;

	@Column(name="DOM_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal domElementState;

	@Column(name="DOM_FTCH_IMMEDIATE", precision=1)
	private BigDecimal domFtchImmediate;

	@Column(name="DOM_LAST_EXEC_TIME", precision=22)
	private BigDecimal domLastExecTime;

	@Column(name="DOM_LOGICAL_ITEM", nullable=false, precision=1)
	private BigDecimal domLogicalItem;

	@Column(name="DOM_NAME", nullable=false, length=100)
	private String domName;

	@Column(name="DOM_SHOW_NAVIGATOR", precision=1)
	private BigDecimal domShowNavigator;

	@Column(name="DOM_SYS_GENERATED", nullable=false, precision=1)
	private BigDecimal domSysGenerated;

	@Column(name="DOM_UPDATED_BY", length=64)
	private String domUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="DOM_UPDATED_DATE")
	private Date domUpdatedDate;

	@Column(name="DOM_USER_PROP1", length=100)
	private String domUserProp1;

	@Column(name="DOM_USER_PROP2", length=100)
	private String domUserProp2;

	@Column(precision=10)
	private BigDecimal notm;

	//bi-directional many-to-one association to Eul5Expression
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DOM_IT_ID_LOV")
	private Eul5Expression eul5Expression1;

	//bi-directional many-to-one association to Eul5Expression
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DOM_IT_ID_RANK")
	private Eul5Expression eul5Expression2;

	//bi-directional many-to-one association to Eul5Expression
	@OneToMany(mappedBy="eul5Domain")
	private List<Eul5Expression> eul5Expressions;

	public Eul5Domain() {
	}

	public long getDomId() {
		return this.domId;
	}

	public void setDomId(long domId) {
		this.domId = domId;
	}

	public BigDecimal getDomArrayFetchSz() {
		return this.domArrayFetchSz;
	}

	public void setDomArrayFetchSz(BigDecimal domArrayFetchSz) {
		this.domArrayFetchSz = domArrayFetchSz;
	}

	public BigDecimal getDomCached() {
		return this.domCached;
	}

	public void setDomCached(BigDecimal domCached) {
		this.domCached = domCached;
	}

	public BigDecimal getDomCardinality() {
		return this.domCardinality;
	}

	public void setDomCardinality(BigDecimal domCardinality) {
		this.domCardinality = domCardinality;
	}

	public String getDomCreatedBy() {
		return this.domCreatedBy;
	}

	public void setDomCreatedBy(String domCreatedBy) {
		this.domCreatedBy = domCreatedBy;
	}

	public Date getDomCreatedDate() {
		return this.domCreatedDate;
	}

	public void setDomCreatedDate(Date domCreatedDate) {
		this.domCreatedDate = domCreatedDate;
	}

	public BigDecimal getDomDataType() {
		return this.domDataType;
	}

	public void setDomDataType(BigDecimal domDataType) {
		this.domDataType = domDataType;
	}

	public String getDomDescription() {
		return this.domDescription;
	}

	public void setDomDescription(String domDescription) {
		this.domDescription = domDescription;
	}

	public String getDomDeveloperKey() {
		return this.domDeveloperKey;
	}

	public void setDomDeveloperKey(String domDeveloperKey) {
		this.domDeveloperKey = domDeveloperKey;
	}

	public BigDecimal getDomDistOrdBy() {
		return this.domDistOrdBy;
	}

	public void setDomDistOrdBy(BigDecimal domDistOrdBy) {
		this.domDistOrdBy = domDistOrdBy;
	}

	public BigDecimal getDomElementState() {
		return this.domElementState;
	}

	public void setDomElementState(BigDecimal domElementState) {
		this.domElementState = domElementState;
	}

	public BigDecimal getDomFtchImmediate() {
		return this.domFtchImmediate;
	}

	public void setDomFtchImmediate(BigDecimal domFtchImmediate) {
		this.domFtchImmediate = domFtchImmediate;
	}

	public BigDecimal getDomLastExecTime() {
		return this.domLastExecTime;
	}

	public void setDomLastExecTime(BigDecimal domLastExecTime) {
		this.domLastExecTime = domLastExecTime;
	}

	public BigDecimal getDomLogicalItem() {
		return this.domLogicalItem;
	}

	public void setDomLogicalItem(BigDecimal domLogicalItem) {
		this.domLogicalItem = domLogicalItem;
	}

	public String getDomName() {
		return this.domName;
	}

	public void setDomName(String domName) {
		this.domName = domName;
	}

	public BigDecimal getDomShowNavigator() {
		return this.domShowNavigator;
	}

	public void setDomShowNavigator(BigDecimal domShowNavigator) {
		this.domShowNavigator = domShowNavigator;
	}

	public BigDecimal getDomSysGenerated() {
		return this.domSysGenerated;
	}

	public void setDomSysGenerated(BigDecimal domSysGenerated) {
		this.domSysGenerated = domSysGenerated;
	}

	public String getDomUpdatedBy() {
		return this.domUpdatedBy;
	}

	public void setDomUpdatedBy(String domUpdatedBy) {
		this.domUpdatedBy = domUpdatedBy;
	}

	public Date getDomUpdatedDate() {
		return this.domUpdatedDate;
	}

	public void setDomUpdatedDate(Date domUpdatedDate) {
		this.domUpdatedDate = domUpdatedDate;
	}

	public String getDomUserProp1() {
		return this.domUserProp1;
	}

	public void setDomUserProp1(String domUserProp1) {
		this.domUserProp1 = domUserProp1;
	}

	public String getDomUserProp2() {
		return this.domUserProp2;
	}

	public void setDomUserProp2(String domUserProp2) {
		this.domUserProp2 = domUserProp2;
	}

	public BigDecimal getNotm() {
		return this.notm;
	}

	public void setNotm(BigDecimal notm) {
		this.notm = notm;
	}

	public Eul5Expression getEul5Expression1() {
		return this.eul5Expression1;
	}

	public void setEul5Expression1(Eul5Expression eul5Expression1) {
		this.eul5Expression1 = eul5Expression1;
	}

	public Eul5Expression getEul5Expression2() {
		return this.eul5Expression2;
	}

	public void setEul5Expression2(Eul5Expression eul5Expression2) {
		this.eul5Expression2 = eul5Expression2;
	}

	public List<Eul5Expression> getEul5Expressions() {
		return this.eul5Expressions;
	}

	public void setEul5Expressions(List<Eul5Expression> eul5Expressions) {
		this.eul5Expressions = eul5Expressions;
	}

	public Eul5Expression addEul5Expression(Eul5Expression eul5Expression) {
		getEul5Expressions().add(eul5Expression);
		eul5Expression.setEul5Domain(this);

		return eul5Expression;
	}

	public Eul5Expression removeEul5Expression(Eul5Expression eul5Expression) {
		getEul5Expressions().remove(eul5Expression);
		eul5Expression.setEul5Domain(null);

		return eul5Expression;
	}

}