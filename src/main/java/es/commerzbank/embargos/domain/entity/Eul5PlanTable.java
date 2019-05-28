package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the EUL5_PLAN_TABLE database table.
 * 
 */
@Entity
@Table(name="EUL5_PLAN_TABLE")
@NamedQuery(name="Eul5PlanTable.findAll", query="SELECT e FROM Eul5PlanTable e")
public class Eul5PlanTable implements Serializable {
	private static final long serialVersionUID = 1L;

	private BigDecimal bytes;

	private BigDecimal cardinality;

	private BigDecimal cost;

	@Column(length=30)
	private String distribution;

	private BigDecimal id;

	@Column(name="OBJECT_INSTANCE")
	private BigDecimal objectInstance;

	@Column(name="OBJECT_NAME", length=30)
	private String objectName;

	@Column(name="OBJECT_NODE", length=128)
	private String objectNode;

	@Column(name="OBJECT_OWNER", length=30)
	private String objectOwner;

	@Column(name="OBJECT_TYPE", length=30)
	private String objectType;

	@Column(name="\"OPERATION\"", length=30)
	private String operation;

	@Column(length=255)
	private String optimizer;

	@Column(length=30)
	private String options;

	private Object other;

	@Column(name="OTHER_TAG", length=255)
	private String otherTag;

	@Column(name="PARENT_ID")
	private BigDecimal parentId;

	@Column(name="PARTITION_ID")
	private BigDecimal partitionId;

	@Column(name="PARTITION_START", length=255)
	private String partitionStart;

	@Column(name="PARTITION_STOP", length=255)
	private String partitionStop;

	@Column(name="\"POSITION\"")
	private BigDecimal position;

	@Column(length=80)
	private String remarks;

	@Column(name="SEARCH_COLUMNS", precision=38)
	private BigDecimal searchColumns;

	@Column(name="STATEMENT_ID", length=30)
	private String statementId;

	@Temporal(TemporalType.DATE)
	@Column(name="\"TIMESTAMP\"")
	private Date timestamp;

	public Eul5PlanTable() {
	}

	public BigDecimal getBytes() {
		return this.bytes;
	}

	public void setBytes(BigDecimal bytes) {
		this.bytes = bytes;
	}

	public BigDecimal getCardinality() {
		return this.cardinality;
	}

	public void setCardinality(BigDecimal cardinality) {
		this.cardinality = cardinality;
	}

	public BigDecimal getCost() {
		return this.cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public String getDistribution() {
		return this.distribution;
	}

	public void setDistribution(String distribution) {
		this.distribution = distribution;
	}

	public BigDecimal getId() {
		return this.id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public BigDecimal getObjectInstance() {
		return this.objectInstance;
	}

	public void setObjectInstance(BigDecimal objectInstance) {
		this.objectInstance = objectInstance;
	}

	public String getObjectName() {
		return this.objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getObjectNode() {
		return this.objectNode;
	}

	public void setObjectNode(String objectNode) {
		this.objectNode = objectNode;
	}

	public String getObjectOwner() {
		return this.objectOwner;
	}

	public void setObjectOwner(String objectOwner) {
		this.objectOwner = objectOwner;
	}

	public String getObjectType() {
		return this.objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getOperation() {
		return this.operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getOptimizer() {
		return this.optimizer;
	}

	public void setOptimizer(String optimizer) {
		this.optimizer = optimizer;
	}

	public String getOptions() {
		return this.options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public Object getOther() {
		return this.other;
	}

	public void setOther(Object other) {
		this.other = other;
	}

	public String getOtherTag() {
		return this.otherTag;
	}

	public void setOtherTag(String otherTag) {
		this.otherTag = otherTag;
	}

	public BigDecimal getParentId() {
		return this.parentId;
	}

	public void setParentId(BigDecimal parentId) {
		this.parentId = parentId;
	}

	public BigDecimal getPartitionId() {
		return this.partitionId;
	}

	public void setPartitionId(BigDecimal partitionId) {
		this.partitionId = partitionId;
	}

	public String getPartitionStart() {
		return this.partitionStart;
	}

	public void setPartitionStart(String partitionStart) {
		this.partitionStart = partitionStart;
	}

	public String getPartitionStop() {
		return this.partitionStop;
	}

	public void setPartitionStop(String partitionStop) {
		this.partitionStop = partitionStop;
	}

	public BigDecimal getPosition() {
		return this.position;
	}

	public void setPosition(BigDecimal position) {
		this.position = position;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public BigDecimal getSearchColumns() {
		return this.searchColumns;
	}

	public void setSearchColumns(BigDecimal searchColumns) {
		this.searchColumns = searchColumns;
	}

	public String getStatementId() {
		return this.statementId;
	}

	public void setStatementId(String statementId) {
		this.statementId = statementId;
	}

	public Date getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}