package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the EUL5_SEQUENCES database table.
 * 
 */
@Entity
@Table(name="EUL5_SEQUENCES")
@NamedQuery(name="Eul5Sequence.findAll", query="SELECT e FROM Eul5Sequence e")
public class Eul5Sequence implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="SEQ_ID", nullable=false, precision=10)
	private BigDecimal seqId;

	@Column(name="SEQ_NAME", nullable=false, length=100)
	private String seqName;

	@Column(name="SEQ_NEXTVAL", nullable=false, precision=22)
	private BigDecimal seqNextval;

	public Eul5Sequence() {
	}

	public BigDecimal getSeqId() {
		return this.seqId;
	}

	public void setSeqId(BigDecimal seqId) {
		this.seqId = seqId;
	}

	public String getSeqName() {
		return this.seqName;
	}

	public void setSeqName(String seqName) {
		this.seqName = seqName;
	}

	public BigDecimal getSeqNextval() {
		return this.seqNextval;
	}

	public void setSeqNextval(BigDecimal seqNextval) {
		this.seqNextval = seqNextval;
	}

}