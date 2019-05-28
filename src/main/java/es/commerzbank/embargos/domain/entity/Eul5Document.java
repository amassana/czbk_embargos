package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the EUL5_DOCUMENTS database table.
 * 
 */
@Entity
@Table(name="EUL5_DOCUMENTS")
@NamedQuery(name="Eul5Document.findAll", query="SELECT e FROM Eul5Document e")
public class Eul5Document implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="DOC_ID", unique=true, nullable=false, precision=10)
	private long docId;

	@Column(name="DOC_BATCH", nullable=false, precision=1)
	private BigDecimal docBatch;

	@Column(name="DOC_CONTENT_TYPE", nullable=false, length=100)
	private String docContentType;

	@Column(name="DOC_CREATED_BY", nullable=false, length=64)
	private String docCreatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="DOC_CREATED_DATE", nullable=false)
	private Date docCreatedDate;

	@Column(name="DOC_DESCRIPTION", length=240)
	private String docDescription;

	@Column(name="DOC_DEVELOPER_KEY", nullable=false, length=100)
	private String docDeveloperKey;

	@Column(name="DOC_DOCUMENT")
	private Object docDocument;

	@Column(name="DOC_ELEMENT_STATE", nullable=false, precision=10)
	private BigDecimal docElementState;

	@Column(name="DOC_LENGTH", nullable=false, precision=22)
	private BigDecimal docLength;

	@Column(name="DOC_NAME", nullable=false, length=100)
	private String docName;

	@Column(name="DOC_UPDATED_BY", length=64)
	private String docUpdatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name="DOC_UPDATED_DATE")
	private Date docUpdatedDate;

	@Column(name="DOC_USER_PROP1", length=100)
	private String docUserProp1;

	@Column(name="DOC_USER_PROP2", length=100)
	private String docUserProp2;

	@Column(precision=10)
	private BigDecimal notm;

	//bi-directional many-to-one association to Eul5AccessPriv
	@OneToMany(mappedBy="eul5Document")
	private List<Eul5AccessPriv> eul5AccessPrivs;

	//bi-directional many-to-one association to Eul5EulUser
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="DOC_EU_ID", nullable=false)
	private Eul5EulUser eul5EulUser;

	//bi-directional many-to-one association to Eul5Expression
	@OneToMany(mappedBy="eul5Document1")
	private List<Eul5Expression> eul5Expressions1;

	//bi-directional many-to-one association to Eul5Expression
	@OneToMany(mappedBy="eul5Document2")
	private List<Eul5Expression> eul5Expressions2;

	public Eul5Document() {
	}

	public long getDocId() {
		return this.docId;
	}

	public void setDocId(long docId) {
		this.docId = docId;
	}

	public BigDecimal getDocBatch() {
		return this.docBatch;
	}

	public void setDocBatch(BigDecimal docBatch) {
		this.docBatch = docBatch;
	}

	public String getDocContentType() {
		return this.docContentType;
	}

	public void setDocContentType(String docContentType) {
		this.docContentType = docContentType;
	}

	public String getDocCreatedBy() {
		return this.docCreatedBy;
	}

	public void setDocCreatedBy(String docCreatedBy) {
		this.docCreatedBy = docCreatedBy;
	}

	public Date getDocCreatedDate() {
		return this.docCreatedDate;
	}

	public void setDocCreatedDate(Date docCreatedDate) {
		this.docCreatedDate = docCreatedDate;
	}

	public String getDocDescription() {
		return this.docDescription;
	}

	public void setDocDescription(String docDescription) {
		this.docDescription = docDescription;
	}

	public String getDocDeveloperKey() {
		return this.docDeveloperKey;
	}

	public void setDocDeveloperKey(String docDeveloperKey) {
		this.docDeveloperKey = docDeveloperKey;
	}

	public Object getDocDocument() {
		return this.docDocument;
	}

	public void setDocDocument(Object docDocument) {
		this.docDocument = docDocument;
	}

	public BigDecimal getDocElementState() {
		return this.docElementState;
	}

	public void setDocElementState(BigDecimal docElementState) {
		this.docElementState = docElementState;
	}

	public BigDecimal getDocLength() {
		return this.docLength;
	}

	public void setDocLength(BigDecimal docLength) {
		this.docLength = docLength;
	}

	public String getDocName() {
		return this.docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getDocUpdatedBy() {
		return this.docUpdatedBy;
	}

	public void setDocUpdatedBy(String docUpdatedBy) {
		this.docUpdatedBy = docUpdatedBy;
	}

	public Date getDocUpdatedDate() {
		return this.docUpdatedDate;
	}

	public void setDocUpdatedDate(Date docUpdatedDate) {
		this.docUpdatedDate = docUpdatedDate;
	}

	public String getDocUserProp1() {
		return this.docUserProp1;
	}

	public void setDocUserProp1(String docUserProp1) {
		this.docUserProp1 = docUserProp1;
	}

	public String getDocUserProp2() {
		return this.docUserProp2;
	}

	public void setDocUserProp2(String docUserProp2) {
		this.docUserProp2 = docUserProp2;
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
		eul5AccessPriv.setEul5Document(this);

		return eul5AccessPriv;
	}

	public Eul5AccessPriv removeEul5AccessPriv(Eul5AccessPriv eul5AccessPriv) {
		getEul5AccessPrivs().remove(eul5AccessPriv);
		eul5AccessPriv.setEul5Document(null);

		return eul5AccessPriv;
	}

	public Eul5EulUser getEul5EulUser() {
		return this.eul5EulUser;
	}

	public void setEul5EulUser(Eul5EulUser eul5EulUser) {
		this.eul5EulUser = eul5EulUser;
	}

	public List<Eul5Expression> getEul5Expressions1() {
		return this.eul5Expressions1;
	}

	public void setEul5Expressions1(List<Eul5Expression> eul5Expressions1) {
		this.eul5Expressions1 = eul5Expressions1;
	}

	public Eul5Expression addEul5Expressions1(Eul5Expression eul5Expressions1) {
		getEul5Expressions1().add(eul5Expressions1);
		eul5Expressions1.setEul5Document1(this);

		return eul5Expressions1;
	}

	public Eul5Expression removeEul5Expressions1(Eul5Expression eul5Expressions1) {
		getEul5Expressions1().remove(eul5Expressions1);
		eul5Expressions1.setEul5Document1(null);

		return eul5Expressions1;
	}

	public List<Eul5Expression> getEul5Expressions2() {
		return this.eul5Expressions2;
	}

	public void setEul5Expressions2(List<Eul5Expression> eul5Expressions2) {
		this.eul5Expressions2 = eul5Expressions2;
	}

	public Eul5Expression addEul5Expressions2(Eul5Expression eul5Expressions2) {
		getEul5Expressions2().add(eul5Expressions2);
		eul5Expressions2.setEul5Document2(this);

		return eul5Expressions2;
	}

	public Eul5Expression removeEul5Expressions2(Eul5Expression eul5Expressions2) {
		getEul5Expressions2().remove(eul5Expressions2);
		eul5Expressions2.setEul5Document2(null);

		return eul5Expressions2;
	}

}