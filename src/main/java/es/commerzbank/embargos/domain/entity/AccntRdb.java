package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the ACCNT_RDB database table.
 * 
 */
@Entity
@Table(name="ACCNT_RDB")
@NamedQuery(name="AccntRdb.findAll", query="SELECT a FROM AccntRdb a")
public class AccntRdb implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(nullable=false, length=1)
	private String branch;

	@Column(name="BRANCH_NO", length=3)
	private String branchNo;

	@Temporal(TemporalType.DATE)
	@Column(name="BUS_DATE")
	private Date busDate;

	@Column(name="CH_FLAG", length=1)
	private String chFlag;

	@Column(name="DAY_ID", nullable=false, length=1)
	private String dayId;

	@Temporal(TemporalType.DATE)
	@Column(name="EOD_ENTRY_DATE")
	private Date eodEntryDate;

	@Column(name="MAST_ACCR_BACK_VAL_DATE", length=10)
	private String mastAccrBackValDate;

	@Column(name="MAST_ACCR_BASIS", length=1)
	private String mastAccrBasis;

	@Column(name="MAST_ACCT_CLOSE_DATE", length=10)
	private String mastAcctCloseDate;

	@Column(name="MAST_ACCT_DORM_POST_DATE", length=10)
	private String mastAcctDormPostDate;

	@Column(name="MAST_ACCT_NO", nullable=false, length=15)
	private String mastAcctNo;

	@Column(name="MAST_ACCT_POST_DATE", length=10)
	private String mastAcctPostDate;

	@Column(name="MAST_ACCT_STAT", length=2)
	private String mastAcctStat;

	@Column(name="MAST_ACCT_TERM_DATE", length=10)
	private String mastAcctTermDate;

	@Column(name="MAST_ACCT_TITLE", length=35)
	private String mastAcctTitle;

	@Column(name="MAST_ACCT_TYPE", length=3)
	private String mastAcctType;

	@Column(name="MAST_ADVICE_IND", length=1)
	private String mastAdviceInd;

	@Column(name="MAST_AGE_DATE", length=10)
	private String mastAgeDate;

	@Column(name="MAST_AUTH_KEY", length=1)
	private String mastAuthKey;

	@Column(name="MAST_AVAIL_BAL_LST_INT_STMT", precision=15, scale=2)
	private BigDecimal mastAvailBalLstIntStmt;

	@Column(name="MAST_AVAIL_BAL_LST_STMT", precision=15, scale=2)
	private BigDecimal mastAvailBalLstStmt;

	@Column(name="MAST_AVG_BAL_CHG", precision=15, scale=2)
	private BigDecimal mastAvgBalChg;

	@Column(name="MAST_AVG_BAL_TBL")
	private BigDecimal mastAvgBalTbl;

	@Column(name="MAST_AVG_BAL_TOT_FEE", precision=15, scale=2)
	private BigDecimal mastAvgBalTotFee;

	@Column(name="MAST_AVG_MO_BAL", precision=15, scale=2)
	private BigDecimal mastAvgMoBal;

	@Column(name="MAST_BACK_VAL_DATE", length=10)
	private String mastBackValDate;

	@Column(name="MAST_BAL_KEY", length=3)
	private String mastBalKey;

	@Column(name="MAST_BK_BAL_LST_INT_STMT", precision=15, scale=2)
	private BigDecimal mastBkBalLstIntStmt;

	@Column(name="MAST_BK_BAL_LST_STMT", precision=15, scale=2)
	private BigDecimal mastBkBalLstStmt;

	@Column(name="MAST_BR_CODE", length=2)
	private String mastBrCode;

	@Column(name="MAST_CABLE_DAYS")
	private BigDecimal mastCableDays;

	@Column(name="MAST_CASH_MGMT_CHG", precision=15, scale=2)
	private BigDecimal mastCashMgmtChg;

	@Column(name="MAST_CASH_MGMT_TBL")
	private BigDecimal mastCashMgmtTbl;

	@Column(name="MAST_CASH_MGMT_TOT_FEE", precision=15, scale=2)
	private BigDecimal mastCashMgmtTotFee;

	@Column(name="MAST_CASH_MGNT_IND", length=1)
	private String mastCashMgntInd;

	@Column(name="MAST_CASH_MGT_INC_ACCRUAL", precision=15, scale=2)
	private BigDecimal mastCashMgtIncAccrual;

	@Column(name="MAST_CASHFLOW_IND", length=1)
	private String mastCashflowInd;

	@Column(name="MAST_CHRG_CNT")
	private BigDecimal mastChrgCnt;

	@Column(name="MAST_CMI_STMT_NO")
	private BigDecimal mastCmiStmtNo;

	@Column(name="MAST_COINFO_FIRST_STATEMENT")
	private BigDecimal mastCoinfoFirstStatement;

	@Column(name="MAST_COINFO_INDICATOR", length=1)
	private String mastCoinfoIndicator;

	@Column(name="MAST_COINFO_LAST_MSG_BAL", precision=15, scale=2)
	private BigDecimal mastCoinfoLastMsgBal;

	@Temporal(TemporalType.DATE)
	@Column(name="MAST_COINFO_LAST_MSG_DATE")
	private Date mastCoinfoLastMsgDate;

	@Column(name="MAST_COINFO_LAST_MSG_TIME")
	private BigDecimal mastCoinfoLastMsgTime;

	@Column(name="MAST_COINFO_LAST_STMT_NO")
	private BigDecimal mastCoinfoLastStmtNo;

	@Column(name="MAST_COINFO_SYSTEM_IND", length=1)
	private String mastCoinfoSystemInd;

	@Column(name="MAST_COINFO_TIME_INTERVAL")
	private BigDecimal mastCoinfoTimeInterval;

	@Column(name="MAST_COINFO_TIME_REFERENCE", length=1)
	private String mastCoinfoTimeReference;

	@Column(name="MAST_COLL_KEY", length=5)
	private String mastCollKey;

	@Column(name="MAST_COMM_ADJ", precision=15, scale=2)
	private BigDecimal mastCommAdj;

	@Column(name="MAST_COMM_ON_DEBIT_ACCRUAL", precision=15, scale=2)
	private BigDecimal mastCommOnDebitAccrual;

	@Column(name="MAST_COMM_ON_DEBIT_CHG", precision=15, scale=2)
	private BigDecimal mastCommOnDebitChg;

	@Column(name="MAST_CON_BAL", precision=15, scale=2)
	private BigDecimal mastConBal;

	@Column(name="MAST_CORP_DDA", length=15)
	private String mastCorpDda;

	@Column(name="MAST_CORRESP_CALL_ACCT", length=15)
	private String mastCorrespCallAcct;

	@Column(name="MAST_CR_ACCR_TO_DATE", precision=15, scale=2)
	private BigDecimal mastCrAccrToDate;

	@Column(name="MAST_CR_ACCR_TO_DATE_PD", precision=15, scale=2)
	private BigDecimal mastCrAccrToDatePd;

	@Column(name="MAST_CR_AVG_INT_RATE", precision=15, scale=2)
	private BigDecimal mastCrAvgIntRate;

	@Column(name="MAST_CR_DATE_AMT", precision=15, scale=2)
	private BigDecimal mastCrDateAmt;

	@Column(name="MAST_CR_INT_ADJ", precision=15, scale=2)
	private BigDecimal mastCrIntAdj;

	@Column(name="MAST_CR_INT_AMT1", precision=15, scale=2)
	private BigDecimal mastCrIntAmt1;

	@Column(name="MAST_CR_INT_AMT2", precision=15, scale=2)
	private BigDecimal mastCrIntAmt2;

	@Column(name="MAST_CR_INT_AMT3", precision=15, scale=2)
	private BigDecimal mastCrIntAmt3;

	@Column(name="MAST_CR_INT_AMT4", precision=15, scale=2)
	private BigDecimal mastCrIntAmt4;

	@Column(name="MAST_CR_INT_AMT5", precision=15, scale=2)
	private BigDecimal mastCrIntAmt5;

	@Column(name="MAST_CR_INT_AMT6", precision=15, scale=2)
	private BigDecimal mastCrIntAmt6;

	@Column(name="MAST_CR_INT_AMT7", precision=15, scale=2)
	private BigDecimal mastCrIntAmt7;

	@Column(name="MAST_CR_INT_THIS_YR", precision=15, scale=2)
	private BigDecimal mastCrIntThisYr;

	@Column(name="MAST_CREDIT_TYPE", length=3)
	private String mastCreditType;

	@Column(name="MAST_CRLM_REV_DATE", length=10)
	private String mastCrlmRevDate;

	@Column(name="MAST_CURR", length=3)
	private String mastCurr;

	@Column(name="MAST_CURRENT_MAX_BAL", precision=15, scale=2)
	private BigDecimal mastCurrentMaxBal;

	@Column(name="MAST_CURRENT_MIN_BAL", precision=15, scale=2)
	private BigDecimal mastCurrentMinBal;

	@Column(name="MAST_CUST_NO", length=13)
	private String mastCustNo;

	@Column(name="MAST_DB_DATE_AMT", precision=15, scale=2)
	private BigDecimal mastDbDateAmt;

	@Column(name="MAST_DB_INT_THIS_YR", precision=15, scale=2)
	private BigDecimal mastDbIntThisYr;

	@Column(name="MAST_DD_NS_IND", length=1)
	private String mastDdNsInd;

	@Column(name="MAST_DR_ACCR_TO_DATE", precision=15, scale=2)
	private BigDecimal mastDrAccrToDate;

	@Column(name="MAST_DR_ACCR_TO_DATE_PD", precision=15, scale=2)
	private BigDecimal mastDrAccrToDatePd;

	@Column(name="MAST_DR_INT_ADJ", precision=15, scale=2)
	private BigDecimal mastDrIntAdj;

	@Column(name="MAST_DR_INT_AMT1", precision=15, scale=2)
	private BigDecimal mastDrIntAmt1;

	@Column(name="MAST_DR_INT_AMT2", precision=15, scale=2)
	private BigDecimal mastDrIntAmt2;

	@Column(name="MAST_DR_INT_AMT3", precision=15, scale=2)
	private BigDecimal mastDrIntAmt3;

	@Column(name="MAST_DR_INT_AMT4", precision=15, scale=2)
	private BigDecimal mastDrIntAmt4;

	@Column(name="MAST_DR_INT_AMT5", precision=15, scale=2)
	private BigDecimal mastDrIntAmt5;

	@Column(name="MAST_DR_INT_AMT6", precision=15, scale=2)
	private BigDecimal mastDrIntAmt6;

	@Column(name="MAST_DR_INT_AMT7", precision=15, scale=2)
	private BigDecimal mastDrIntAmt7;

	@Column(name="MAST_EXT_ACCNT_NO", length=23)
	private String mastExtAccntNo;

	@Column(name="MAST_EXTERNAL_ACCT_NO", length=35)
	private String mastExternalAcctNo;

	@Column(name="MAST_FCDAIL_KEY", length=21)
	private String mastFcdailKey;

	@Column(name="MAST_FCDAIL_REV_FLAG", length=1)
	private String mastFcdailRevFlag;

	@Column(name="MAST_FEE_CHG_ACCT", length=15)
	private String mastFeeChgAcct;

	@Column(name="MAST_FGN_POST", precision=15, scale=2)
	private BigDecimal mastFgnPost;

	@Column(name="MAST_GL_NO", length=14)
	private String mastGlNo;

	@Column(name="MAST_GMC_AMOUNT", precision=15, scale=2)
	private BigDecimal mastGmcAmount;

	@Column(name="MAST_IBAN_ELECT", length=27)
	private String mastIbanElect;

	@Column(name="MAST_IBAN_PAPER", length=38)
	private String mastIbanPaper;

	@Column(name="MAST_INSTR_TYPE", length=6)
	private String mastInstrType;

	@Column(name="MAST_INSUFF_FUND_IND", length=1)
	private String mastInsuffFundInd;

	@Column(name="MAST_INT_CHG_ACCT", length=15)
	private String mastIntChgAcct;

	@Column(name="MAST_INT_REV_DATE", length=10)
	private String mastIntRevDate;

	@Column(name="MAST_INT_STMT_BEG_DATE1", length=10)
	private String mastIntStmtBegDate1;

	@Column(name="MAST_INT_STMT_BEG_DATE2", length=10)
	private String mastIntStmtBegDate2;

	@Column(name="MAST_INT_STMT_BEG_DATE3", length=10)
	private String mastIntStmtBegDate3;

	@Column(name="MAST_INT_STMT_BEG_DATE4", length=10)
	private String mastIntStmtBegDate4;

	@Column(name="MAST_INT_STMT_BEG_DATE5", length=10)
	private String mastIntStmtBegDate5;

	@Column(name="MAST_INT_STMT_BEG_DATE6", length=10)
	private String mastIntStmtBegDate6;

	@Column(name="MAST_INT_STMT_BEG_DATE7", length=10)
	private String mastIntStmtBegDate7;

	@Column(name="MAST_INT_STMT_END_DATE1", length=10)
	private String mastIntStmtEndDate1;

	@Column(name="MAST_INT_STMT_END_DATE2", length=10)
	private String mastIntStmtEndDate2;

	@Column(name="MAST_INT_STMT_END_DATE3", length=10)
	private String mastIntStmtEndDate3;

	@Column(name="MAST_INT_STMT_END_DATE4", length=10)
	private String mastIntStmtEndDate4;

	@Column(name="MAST_INT_STMT_END_DATE5", length=10)
	private String mastIntStmtEndDate5;

	@Column(name="MAST_INT_STMT_END_DATE6", length=10)
	private String mastIntStmtEndDate6;

	@Column(name="MAST_INT_STMT_END_DATE7", length=10)
	private String mastIntStmtEndDate7;

	@Column(name="MAST_INT_STMT_FREQ")
	private BigDecimal mastIntStmtFreq;

	@Column(name="MAST_IOL", precision=15, scale=2)
	private BigDecimal mastIol;

	@Column(name="MAST_LAST_GL_DATE", length=10)
	private String mastLastGlDate;

	@Column(name="MAST_LAST_INT_STMT_DATE", length=10)
	private String mastLastIntStmtDate;

	@Column(name="MAST_LAST_INT_STMT_DATE2", length=10)
	private String mastLastIntStmtDate2;

	@Column(name="MAST_LAST_STMT_NO")
	private BigDecimal mastLastStmtNo;

	@Column(name="MAST_LAST_STMT_NO_PD")
	private BigDecimal mastLastStmtNoPd;

	@Column(name="MAST_LIQMGT_FIRST_STATEMENT")
	private BigDecimal mastLiqmgtFirstStatement;

	@Column(name="MAST_LIQMGT_INDICATOR", length=1)
	private String mastLiqmgtIndicator;

	@Temporal(TemporalType.DATE)
	@Column(name="MAST_LIQMGT_LAST_MSG_DATE")
	private Date mastLiqmgtLastMsgDate;

	@Column(name="MAST_LIQMGT_LAST_MSG_TIME")
	private BigDecimal mastLiqmgtLastMsgTime;

	@Column(name="MAST_LIQMGT_LAST_STMT_NO")
	private BigDecimal mastLiqmgtLastStmtNo;

	@Column(name="MAST_LIQMGT_TIME_INTERVAL")
	private BigDecimal mastLiqmgtTimeInterval;

	@Column(name="MAST_LIQMGT_TIME_REFERENCE", length=1)
	private String mastLiqmgtTimeReference;

	@Column(name="MAST_LOCAL_POST", precision=15, scale=2)
	private BigDecimal mastLocalPost;

	@Column(name="MAST_LST_STAT_CHG_DATE", length=10)
	private String mastLstStatChgDate;

	@Column(name="MAST_LST_STMT_DATE", length=10)
	private String mastLstStmtDate;

	@Column(name="MAST_LST_STMT_DATE_PD", length=10)
	private String mastLstStmtDatePd;

	@Column(name="MAST_LZB_FLAG", length=1)
	private String mastLzbFlag;

	@Column(name="MAST_MAIL_IND", length=1)
	private String mastMailInd;

	@Column(name="MAST_MAX_CHG", precision=15, scale=2)
	private BigDecimal mastMaxChg;

	@Column(name="MAST_MIN_CHG", precision=15, scale=2)
	private BigDecimal mastMinChg;

	@Column(name="MAST_MIN_MAX_IND", length=1)
	private String mastMinMaxInd;

	@Column(name="MAST_MINMAX_CHG_TOTAL", precision=15, scale=2)
	private BigDecimal mastMinmaxChgTotal;

	@Column(name="MAST_MMACB_ACCRUAL0", precision=15, scale=2)
	private BigDecimal mastMmacbAccrual0;

	@Column(name="MAST_MMACB_ACCRUAL1", precision=15, scale=2)
	private BigDecimal mastMmacbAccrual1;

	@Column(name="MAST_MMACB_ACCRUAL2", precision=15, scale=2)
	private BigDecimal mastMmacbAccrual2;

	@Column(name="MAST_MMACB_ACCRUAL3", precision=15, scale=2)
	private BigDecimal mastMmacbAccrual3;

	@Column(name="MAST_MMACB_ACCRUAL4", precision=15, scale=2)
	private BigDecimal mastMmacbAccrual4;

	@Column(name="MAST_MMACB_ACCRUAL5", precision=15, scale=2)
	private BigDecimal mastMmacbAccrual5;

	@Column(name="MAST_MMACB_ACCRUAL6", precision=15, scale=2)
	private BigDecimal mastMmacbAccrual6;

	@Column(name="MAST_MMACB_FEE0", precision=15, scale=2)
	private BigDecimal mastMmacbFee0;

	@Column(name="MAST_MMACB_FEE1", precision=15, scale=2)
	private BigDecimal mastMmacbFee1;

	@Column(name="MAST_MMACB_FEE2", precision=15, scale=2)
	private BigDecimal mastMmacbFee2;

	@Column(name="MAST_MMACB_FEE3", precision=15, scale=2)
	private BigDecimal mastMmacbFee3;

	@Column(name="MAST_MMACB_FEE4", precision=15, scale=2)
	private BigDecimal mastMmacbFee4;

	@Column(name="MAST_MMACB_FEE5", precision=15, scale=2)
	private BigDecimal mastMmacbFee5;

	@Column(name="MAST_MMACB_FEE6", precision=15, scale=2)
	private BigDecimal mastMmacbFee6;

	@Column(name="MAST_MMACB0", precision=15, scale=2)
	private BigDecimal mastMmacb0;

	@Column(name="MAST_MMACB5", precision=15, scale=2)
	private BigDecimal mastMmacb5;

	@Column(name="MAST_MMACB6", precision=15, scale=2)
	private BigDecimal mastMmacb6;

	@Column(name="MAST_MOF_AVAIL_BAL", precision=15, scale=2)
	private BigDecimal mastMofAvailBal;

	@Column(name="MAST_MOF_NO", length=10)
	private String mastMofNo;

	@Column(name="MAST_NEED_EOD_MOFHIST", length=1)
	private String mastNeedEodMofhist;

	@Column(name="MAST_NEXT_DAY_CR", precision=15, scale=2)
	private BigDecimal mastNextDayCr;

	@Column(name="MAST_NEXT_DAY_DR", precision=15, scale=2)
	private BigDecimal mastNextDayDr;

	@Column(name="MAST_NO_FINC_ACT_DATE", length=10)
	private String mastNoFincActDate;

	@Column(name="MAST_NOTICE_DAYS")
	private BigDecimal mastNoticeDays;

	@Column(name="MAST_NOW_CR_INT_POSTED1")
	private BigDecimal mastNowCrIntPosted1;

	@Column(name="MAST_NOW_CR_INT_POSTED2")
	private BigDecimal mastNowCrIntPosted2;

	@Column(name="MAST_NOW_CR_INT_POSTED3")
	private BigDecimal mastNowCrIntPosted3;

	@Column(name="MAST_NOW_CR_INT_POSTED4")
	private BigDecimal mastNowCrIntPosted4;

	@Column(name="MAST_NOW_CR_INT_POSTED5")
	private BigDecimal mastNowCrIntPosted5;

	@Column(name="MAST_NOW_CR_INT_POSTED6")
	private BigDecimal mastNowCrIntPosted6;

	@Column(name="MAST_NOW_CR_INT_POSTED7")
	private BigDecimal mastNowCrIntPosted7;

	@Column(name="MAST_NOW_CUR_START_DATE", length=10)
	private String mastNowCurStartDate;

	@Column(name="MAST_NOW_POSTED_CR_INT", precision=15, scale=2)
	private BigDecimal mastNowPostedCrInt;

	@Column(name="MAST_NOW_PREV_START_DATE", length=10)
	private String mastNowPrevStartDate;

	@Column(name="MAST_NOW_TOTAL_CR_INT", precision=15, scale=2)
	private BigDecimal mastNowTotalCrInt;

	@Column(name="MAST_NXT_STMT_DATE", length=10)
	private String mastNxtStmtDate;

	@Column(name="MAST_OD_COUNTER")
	private BigDecimal mastOdCounter;

	@Column(name="MAST_OPEN_BOOK_BAL", precision=15, scale=2)
	private BigDecimal mastOpenBookBal;

	@Column(name="MAST_OPER_ID", length=3)
	private String mastOperId;

	@Column(name="MAST_ORIG_SET_UP_DATE", length=10)
	private String mastOrigSetUpDate;

	@Column(name="MAST_OVDR_LIM", precision=15, scale=2)
	private BigDecimal mastOvdrLim;

	@Column(name="MAST_PARENT_DDA", length=15)
	private String mastParentDda;

	@Column(name="MAST_PART_HOLD_IND", length=1)
	private String mastPartHoldInd;

	@Column(name="MAST_POOL_LEVEL", length=1)
	private String mastPoolLevel;

	@Column(name="MAST_POOL_TYPE", length=1)
	private String mastPoolType;

	@Column(name="MAST_POST_CR_INT_FLAG", length=1)
	private String mastPostCrIntFlag;

	@Column(name="MAST_POST_FCDAIL_KEY", length=21)
	private String mastPostFcdailKey;

	@Column(name="MAST_POST_FCDAIL_REV_FLAG", length=1)
	private String mastPostFcdailRevFlag;

	@Column(name="MAST_POST_INC_ACCRUAL", precision=15, scale=2)
	private BigDecimal mastPostIncAccrual;

	@Column(name="MAST_PREV_AVAIL_BAL", precision=15, scale=2)
	private BigDecimal mastPrevAvailBal;

	@Column(name="MAST_PREV_BK_BAL", precision=15, scale=2)
	private BigDecimal mastPrevBkBal;

	@Column(name="MAST_PREV_END_BK_BAL", precision=15, scale=2)
	private BigDecimal mastPrevEndBkBal;

	@Column(name="MAST_REC_IN_USE_FLAG", length=1)
	private String mastRecInUseFlag;

	@Column(name="MAST_REOPEN_ALLOWED", length=1)
	private String mastReopenAllowed;

	@Column(name="MAST_SECR_CODE", length=6)
	private String mastSecrCode;

	@Column(name="MAST_SET_UP_DATE", length=10)
	private String mastSetUpDate;

	@Column(name="MAST_SPECIAL_IND", length=1)
	private String mastSpecialInd;

	@Column(name="MAST_STAT_CYC", length=3)
	private String mastStatCyc;

	@Column(name="MAST_STATI_ACC_TRAN_DATE", length=10)
	private String mastStatiAccTranDate;

	@Column(name="MAST_STATI_CR_TRANS")
	private BigDecimal mastStatiCrTrans;

	@Column(name="MAST_STATI_DB_TRANS")
	private BigDecimal mastStatiDbTrans;

	@Column(name="MAST_STMT_CR", precision=15, scale=2)
	private BigDecimal mastStmtCr;

	@Column(name="MAST_STMT_CR_PD", precision=15, scale=2)
	private BigDecimal mastStmtCrPd;

	@Column(name="MAST_STMT_DB", precision=15, scale=2)
	private BigDecimal mastStmtDb;

	@Column(name="MAST_STMT_DB_PD", precision=15, scale=2)
	private BigDecimal mastStmtDbPd;

	@Column(name="MAST_STMT_NEEDED", length=1)
	private String mastStmtNeeded;

	@Column(name="MAST_STOP_PAYMENT")
	private BigDecimal mastStopPayment;

	@Column(name="MAST_SWIFT_FEES", precision=15, scale=2)
	private BigDecimal mastSwiftFees;

	@Column(name="MAST_SWIFT_FEES_ACCRUAL", precision=15, scale=2)
	private BigDecimal mastSwiftFeesAccrual;

	@Column(name="MAST_T4M_CUSTOMER", length=1)
	private String mastT4mCustomer;

	@Column(name="MAST_TAX_CHG", precision=15, scale=2)
	private BigDecimal mastTaxChg;

	@Column(name="MAST_TAX_CHG_IND", length=1)
	private String mastTaxChgInd;

	@Column(name="MAST_TODAY_BACKVALUE_AMOUNT", precision=15, scale=2)
	private BigDecimal mastTodayBackvalueAmount;

	@Column(name="MAST_TODAY_CR_AMT", precision=15, scale=2)
	private BigDecimal mastTodayCrAmt;

	@Column(name="MAST_TODAY_DB_AMT", precision=15, scale=2)
	private BigDecimal mastTodayDbAmt;

	@Column(name="MAST_TOT_CR_AMT", precision=15, scale=2)
	private BigDecimal mastTotCrAmt;

	@Column(name="MAST_TOT_DB_AMT", precision=15, scale=2)
	private BigDecimal mastTotDbAmt;

	@Column(name="MAST_TOT_STMT_CNT")
	private BigDecimal mastTotStmtCnt;

	@Column(name="MAST_TOT_TRAN")
	private BigDecimal mastTotTran;

	@Column(name="MAST_TOT_TRAN_PD")
	private BigDecimal mastTotTranPd;

	@Column(name="MAST_TRAN_AMT", precision=15, scale=2)
	private BigDecimal mastTranAmt;

	@Column(name="MAST_TRAN_AMT_PD", precision=15, scale=2)
	private BigDecimal mastTranAmtPd;

	@Column(name="MAST_TRAN_CHG", precision=15, scale=2)
	private BigDecimal mastTranChg;

	@Column(name="MAST_TRAN_CHG_EACH", precision=15, scale=2)
	private BigDecimal mastTranChgEach;

	@Column(name="MAST_TRAN_DATE_CNT")
	private BigDecimal mastTranDateCnt;

	@Column(name="MAST_TRAN_FREE")
	private BigDecimal mastTranFree;

	@Column(name="MAST_TRAN_TAB_IND", length=1)
	private String mastTranTabInd;

	@Column(name="MAST_TRAN_TURN_INC_ACCRUAL", precision=15, scale=2)
	private BigDecimal mastTranTurnIncAccrual;

	@Column(name="MAST_TURN_CHG", precision=15, scale=2)
	private BigDecimal mastTurnChg;

	@Column(name="MAST_TURN_CHG_IND", length=1)
	private String mastTurnChgInd;

	@Column(name="MAST_TURNOVER_TYPE", length=1)
	private String mastTurnoverType;

	@Column(name="MAST_VALID_TRANS")
	private BigDecimal mastValidTrans;

	@Column(nullable=false, length=8)
	private String source;

	public AccntRdb() {
	}

	public String getBranch() {
		return this.branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getBranchNo() {
		return this.branchNo;
	}

	public void setBranchNo(String branchNo) {
		this.branchNo = branchNo;
	}

	public Date getBusDate() {
		return this.busDate;
	}

	public void setBusDate(Date busDate) {
		this.busDate = busDate;
	}

	public String getChFlag() {
		return this.chFlag;
	}

	public void setChFlag(String chFlag) {
		this.chFlag = chFlag;
	}

	public String getDayId() {
		return this.dayId;
	}

	public void setDayId(String dayId) {
		this.dayId = dayId;
	}

	public Date getEodEntryDate() {
		return this.eodEntryDate;
	}

	public void setEodEntryDate(Date eodEntryDate) {
		this.eodEntryDate = eodEntryDate;
	}

	public String getMastAccrBackValDate() {
		return this.mastAccrBackValDate;
	}

	public void setMastAccrBackValDate(String mastAccrBackValDate) {
		this.mastAccrBackValDate = mastAccrBackValDate;
	}

	public String getMastAccrBasis() {
		return this.mastAccrBasis;
	}

	public void setMastAccrBasis(String mastAccrBasis) {
		this.mastAccrBasis = mastAccrBasis;
	}

	public String getMastAcctCloseDate() {
		return this.mastAcctCloseDate;
	}

	public void setMastAcctCloseDate(String mastAcctCloseDate) {
		this.mastAcctCloseDate = mastAcctCloseDate;
	}

	public String getMastAcctDormPostDate() {
		return this.mastAcctDormPostDate;
	}

	public void setMastAcctDormPostDate(String mastAcctDormPostDate) {
		this.mastAcctDormPostDate = mastAcctDormPostDate;
	}

	public String getMastAcctNo() {
		return this.mastAcctNo;
	}

	public void setMastAcctNo(String mastAcctNo) {
		this.mastAcctNo = mastAcctNo;
	}

	public String getMastAcctPostDate() {
		return this.mastAcctPostDate;
	}

	public void setMastAcctPostDate(String mastAcctPostDate) {
		this.mastAcctPostDate = mastAcctPostDate;
	}

	public String getMastAcctStat() {
		return this.mastAcctStat;
	}

	public void setMastAcctStat(String mastAcctStat) {
		this.mastAcctStat = mastAcctStat;
	}

	public String getMastAcctTermDate() {
		return this.mastAcctTermDate;
	}

	public void setMastAcctTermDate(String mastAcctTermDate) {
		this.mastAcctTermDate = mastAcctTermDate;
	}

	public String getMastAcctTitle() {
		return this.mastAcctTitle;
	}

	public void setMastAcctTitle(String mastAcctTitle) {
		this.mastAcctTitle = mastAcctTitle;
	}

	public String getMastAcctType() {
		return this.mastAcctType;
	}

	public void setMastAcctType(String mastAcctType) {
		this.mastAcctType = mastAcctType;
	}

	public String getMastAdviceInd() {
		return this.mastAdviceInd;
	}

	public void setMastAdviceInd(String mastAdviceInd) {
		this.mastAdviceInd = mastAdviceInd;
	}

	public String getMastAgeDate() {
		return this.mastAgeDate;
	}

	public void setMastAgeDate(String mastAgeDate) {
		this.mastAgeDate = mastAgeDate;
	}

	public String getMastAuthKey() {
		return this.mastAuthKey;
	}

	public void setMastAuthKey(String mastAuthKey) {
		this.mastAuthKey = mastAuthKey;
	}

	public BigDecimal getMastAvailBalLstIntStmt() {
		return this.mastAvailBalLstIntStmt;
	}

	public void setMastAvailBalLstIntStmt(BigDecimal mastAvailBalLstIntStmt) {
		this.mastAvailBalLstIntStmt = mastAvailBalLstIntStmt;
	}

	public BigDecimal getMastAvailBalLstStmt() {
		return this.mastAvailBalLstStmt;
	}

	public void setMastAvailBalLstStmt(BigDecimal mastAvailBalLstStmt) {
		this.mastAvailBalLstStmt = mastAvailBalLstStmt;
	}

	public BigDecimal getMastAvgBalChg() {
		return this.mastAvgBalChg;
	}

	public void setMastAvgBalChg(BigDecimal mastAvgBalChg) {
		this.mastAvgBalChg = mastAvgBalChg;
	}

	public BigDecimal getMastAvgBalTbl() {
		return this.mastAvgBalTbl;
	}

	public void setMastAvgBalTbl(BigDecimal mastAvgBalTbl) {
		this.mastAvgBalTbl = mastAvgBalTbl;
	}

	public BigDecimal getMastAvgBalTotFee() {
		return this.mastAvgBalTotFee;
	}

	public void setMastAvgBalTotFee(BigDecimal mastAvgBalTotFee) {
		this.mastAvgBalTotFee = mastAvgBalTotFee;
	}

	public BigDecimal getMastAvgMoBal() {
		return this.mastAvgMoBal;
	}

	public void setMastAvgMoBal(BigDecimal mastAvgMoBal) {
		this.mastAvgMoBal = mastAvgMoBal;
	}

	public String getMastBackValDate() {
		return this.mastBackValDate;
	}

	public void setMastBackValDate(String mastBackValDate) {
		this.mastBackValDate = mastBackValDate;
	}

	public String getMastBalKey() {
		return this.mastBalKey;
	}

	public void setMastBalKey(String mastBalKey) {
		this.mastBalKey = mastBalKey;
	}

	public BigDecimal getMastBkBalLstIntStmt() {
		return this.mastBkBalLstIntStmt;
	}

	public void setMastBkBalLstIntStmt(BigDecimal mastBkBalLstIntStmt) {
		this.mastBkBalLstIntStmt = mastBkBalLstIntStmt;
	}

	public BigDecimal getMastBkBalLstStmt() {
		return this.mastBkBalLstStmt;
	}

	public void setMastBkBalLstStmt(BigDecimal mastBkBalLstStmt) {
		this.mastBkBalLstStmt = mastBkBalLstStmt;
	}

	public String getMastBrCode() {
		return this.mastBrCode;
	}

	public void setMastBrCode(String mastBrCode) {
		this.mastBrCode = mastBrCode;
	}

	public BigDecimal getMastCableDays() {
		return this.mastCableDays;
	}

	public void setMastCableDays(BigDecimal mastCableDays) {
		this.mastCableDays = mastCableDays;
	}

	public BigDecimal getMastCashMgmtChg() {
		return this.mastCashMgmtChg;
	}

	public void setMastCashMgmtChg(BigDecimal mastCashMgmtChg) {
		this.mastCashMgmtChg = mastCashMgmtChg;
	}

	public BigDecimal getMastCashMgmtTbl() {
		return this.mastCashMgmtTbl;
	}

	public void setMastCashMgmtTbl(BigDecimal mastCashMgmtTbl) {
		this.mastCashMgmtTbl = mastCashMgmtTbl;
	}

	public BigDecimal getMastCashMgmtTotFee() {
		return this.mastCashMgmtTotFee;
	}

	public void setMastCashMgmtTotFee(BigDecimal mastCashMgmtTotFee) {
		this.mastCashMgmtTotFee = mastCashMgmtTotFee;
	}

	public String getMastCashMgntInd() {
		return this.mastCashMgntInd;
	}

	public void setMastCashMgntInd(String mastCashMgntInd) {
		this.mastCashMgntInd = mastCashMgntInd;
	}

	public BigDecimal getMastCashMgtIncAccrual() {
		return this.mastCashMgtIncAccrual;
	}

	public void setMastCashMgtIncAccrual(BigDecimal mastCashMgtIncAccrual) {
		this.mastCashMgtIncAccrual = mastCashMgtIncAccrual;
	}

	public String getMastCashflowInd() {
		return this.mastCashflowInd;
	}

	public void setMastCashflowInd(String mastCashflowInd) {
		this.mastCashflowInd = mastCashflowInd;
	}

	public BigDecimal getMastChrgCnt() {
		return this.mastChrgCnt;
	}

	public void setMastChrgCnt(BigDecimal mastChrgCnt) {
		this.mastChrgCnt = mastChrgCnt;
	}

	public BigDecimal getMastCmiStmtNo() {
		return this.mastCmiStmtNo;
	}

	public void setMastCmiStmtNo(BigDecimal mastCmiStmtNo) {
		this.mastCmiStmtNo = mastCmiStmtNo;
	}

	public BigDecimal getMastCoinfoFirstStatement() {
		return this.mastCoinfoFirstStatement;
	}

	public void setMastCoinfoFirstStatement(BigDecimal mastCoinfoFirstStatement) {
		this.mastCoinfoFirstStatement = mastCoinfoFirstStatement;
	}

	public String getMastCoinfoIndicator() {
		return this.mastCoinfoIndicator;
	}

	public void setMastCoinfoIndicator(String mastCoinfoIndicator) {
		this.mastCoinfoIndicator = mastCoinfoIndicator;
	}

	public BigDecimal getMastCoinfoLastMsgBal() {
		return this.mastCoinfoLastMsgBal;
	}

	public void setMastCoinfoLastMsgBal(BigDecimal mastCoinfoLastMsgBal) {
		this.mastCoinfoLastMsgBal = mastCoinfoLastMsgBal;
	}

	public Date getMastCoinfoLastMsgDate() {
		return this.mastCoinfoLastMsgDate;
	}

	public void setMastCoinfoLastMsgDate(Date mastCoinfoLastMsgDate) {
		this.mastCoinfoLastMsgDate = mastCoinfoLastMsgDate;
	}

	public BigDecimal getMastCoinfoLastMsgTime() {
		return this.mastCoinfoLastMsgTime;
	}

	public void setMastCoinfoLastMsgTime(BigDecimal mastCoinfoLastMsgTime) {
		this.mastCoinfoLastMsgTime = mastCoinfoLastMsgTime;
	}

	public BigDecimal getMastCoinfoLastStmtNo() {
		return this.mastCoinfoLastStmtNo;
	}

	public void setMastCoinfoLastStmtNo(BigDecimal mastCoinfoLastStmtNo) {
		this.mastCoinfoLastStmtNo = mastCoinfoLastStmtNo;
	}

	public String getMastCoinfoSystemInd() {
		return this.mastCoinfoSystemInd;
	}

	public void setMastCoinfoSystemInd(String mastCoinfoSystemInd) {
		this.mastCoinfoSystemInd = mastCoinfoSystemInd;
	}

	public BigDecimal getMastCoinfoTimeInterval() {
		return this.mastCoinfoTimeInterval;
	}

	public void setMastCoinfoTimeInterval(BigDecimal mastCoinfoTimeInterval) {
		this.mastCoinfoTimeInterval = mastCoinfoTimeInterval;
	}

	public String getMastCoinfoTimeReference() {
		return this.mastCoinfoTimeReference;
	}

	public void setMastCoinfoTimeReference(String mastCoinfoTimeReference) {
		this.mastCoinfoTimeReference = mastCoinfoTimeReference;
	}

	public String getMastCollKey() {
		return this.mastCollKey;
	}

	public void setMastCollKey(String mastCollKey) {
		this.mastCollKey = mastCollKey;
	}

	public BigDecimal getMastCommAdj() {
		return this.mastCommAdj;
	}

	public void setMastCommAdj(BigDecimal mastCommAdj) {
		this.mastCommAdj = mastCommAdj;
	}

	public BigDecimal getMastCommOnDebitAccrual() {
		return this.mastCommOnDebitAccrual;
	}

	public void setMastCommOnDebitAccrual(BigDecimal mastCommOnDebitAccrual) {
		this.mastCommOnDebitAccrual = mastCommOnDebitAccrual;
	}

	public BigDecimal getMastCommOnDebitChg() {
		return this.mastCommOnDebitChg;
	}

	public void setMastCommOnDebitChg(BigDecimal mastCommOnDebitChg) {
		this.mastCommOnDebitChg = mastCommOnDebitChg;
	}

	public BigDecimal getMastConBal() {
		return this.mastConBal;
	}

	public void setMastConBal(BigDecimal mastConBal) {
		this.mastConBal = mastConBal;
	}

	public String getMastCorpDda() {
		return this.mastCorpDda;
	}

	public void setMastCorpDda(String mastCorpDda) {
		this.mastCorpDda = mastCorpDda;
	}

	public String getMastCorrespCallAcct() {
		return this.mastCorrespCallAcct;
	}

	public void setMastCorrespCallAcct(String mastCorrespCallAcct) {
		this.mastCorrespCallAcct = mastCorrespCallAcct;
	}

	public BigDecimal getMastCrAccrToDate() {
		return this.mastCrAccrToDate;
	}

	public void setMastCrAccrToDate(BigDecimal mastCrAccrToDate) {
		this.mastCrAccrToDate = mastCrAccrToDate;
	}

	public BigDecimal getMastCrAccrToDatePd() {
		return this.mastCrAccrToDatePd;
	}

	public void setMastCrAccrToDatePd(BigDecimal mastCrAccrToDatePd) {
		this.mastCrAccrToDatePd = mastCrAccrToDatePd;
	}

	public BigDecimal getMastCrAvgIntRate() {
		return this.mastCrAvgIntRate;
	}

	public void setMastCrAvgIntRate(BigDecimal mastCrAvgIntRate) {
		this.mastCrAvgIntRate = mastCrAvgIntRate;
	}

	public BigDecimal getMastCrDateAmt() {
		return this.mastCrDateAmt;
	}

	public void setMastCrDateAmt(BigDecimal mastCrDateAmt) {
		this.mastCrDateAmt = mastCrDateAmt;
	}

	public BigDecimal getMastCrIntAdj() {
		return this.mastCrIntAdj;
	}

	public void setMastCrIntAdj(BigDecimal mastCrIntAdj) {
		this.mastCrIntAdj = mastCrIntAdj;
	}

	public BigDecimal getMastCrIntAmt1() {
		return this.mastCrIntAmt1;
	}

	public void setMastCrIntAmt1(BigDecimal mastCrIntAmt1) {
		this.mastCrIntAmt1 = mastCrIntAmt1;
	}

	public BigDecimal getMastCrIntAmt2() {
		return this.mastCrIntAmt2;
	}

	public void setMastCrIntAmt2(BigDecimal mastCrIntAmt2) {
		this.mastCrIntAmt2 = mastCrIntAmt2;
	}

	public BigDecimal getMastCrIntAmt3() {
		return this.mastCrIntAmt3;
	}

	public void setMastCrIntAmt3(BigDecimal mastCrIntAmt3) {
		this.mastCrIntAmt3 = mastCrIntAmt3;
	}

	public BigDecimal getMastCrIntAmt4() {
		return this.mastCrIntAmt4;
	}

	public void setMastCrIntAmt4(BigDecimal mastCrIntAmt4) {
		this.mastCrIntAmt4 = mastCrIntAmt4;
	}

	public BigDecimal getMastCrIntAmt5() {
		return this.mastCrIntAmt5;
	}

	public void setMastCrIntAmt5(BigDecimal mastCrIntAmt5) {
		this.mastCrIntAmt5 = mastCrIntAmt5;
	}

	public BigDecimal getMastCrIntAmt6() {
		return this.mastCrIntAmt6;
	}

	public void setMastCrIntAmt6(BigDecimal mastCrIntAmt6) {
		this.mastCrIntAmt6 = mastCrIntAmt6;
	}

	public BigDecimal getMastCrIntAmt7() {
		return this.mastCrIntAmt7;
	}

	public void setMastCrIntAmt7(BigDecimal mastCrIntAmt7) {
		this.mastCrIntAmt7 = mastCrIntAmt7;
	}

	public BigDecimal getMastCrIntThisYr() {
		return this.mastCrIntThisYr;
	}

	public void setMastCrIntThisYr(BigDecimal mastCrIntThisYr) {
		this.mastCrIntThisYr = mastCrIntThisYr;
	}

	public String getMastCreditType() {
		return this.mastCreditType;
	}

	public void setMastCreditType(String mastCreditType) {
		this.mastCreditType = mastCreditType;
	}

	public String getMastCrlmRevDate() {
		return this.mastCrlmRevDate;
	}

	public void setMastCrlmRevDate(String mastCrlmRevDate) {
		this.mastCrlmRevDate = mastCrlmRevDate;
	}

	public String getMastCurr() {
		return this.mastCurr;
	}

	public void setMastCurr(String mastCurr) {
		this.mastCurr = mastCurr;
	}

	public BigDecimal getMastCurrentMaxBal() {
		return this.mastCurrentMaxBal;
	}

	public void setMastCurrentMaxBal(BigDecimal mastCurrentMaxBal) {
		this.mastCurrentMaxBal = mastCurrentMaxBal;
	}

	public BigDecimal getMastCurrentMinBal() {
		return this.mastCurrentMinBal;
	}

	public void setMastCurrentMinBal(BigDecimal mastCurrentMinBal) {
		this.mastCurrentMinBal = mastCurrentMinBal;
	}

	public String getMastCustNo() {
		return this.mastCustNo;
	}

	public void setMastCustNo(String mastCustNo) {
		this.mastCustNo = mastCustNo;
	}

	public BigDecimal getMastDbDateAmt() {
		return this.mastDbDateAmt;
	}

	public void setMastDbDateAmt(BigDecimal mastDbDateAmt) {
		this.mastDbDateAmt = mastDbDateAmt;
	}

	public BigDecimal getMastDbIntThisYr() {
		return this.mastDbIntThisYr;
	}

	public void setMastDbIntThisYr(BigDecimal mastDbIntThisYr) {
		this.mastDbIntThisYr = mastDbIntThisYr;
	}

	public String getMastDdNsInd() {
		return this.mastDdNsInd;
	}

	public void setMastDdNsInd(String mastDdNsInd) {
		this.mastDdNsInd = mastDdNsInd;
	}

	public BigDecimal getMastDrAccrToDate() {
		return this.mastDrAccrToDate;
	}

	public void setMastDrAccrToDate(BigDecimal mastDrAccrToDate) {
		this.mastDrAccrToDate = mastDrAccrToDate;
	}

	public BigDecimal getMastDrAccrToDatePd() {
		return this.mastDrAccrToDatePd;
	}

	public void setMastDrAccrToDatePd(BigDecimal mastDrAccrToDatePd) {
		this.mastDrAccrToDatePd = mastDrAccrToDatePd;
	}

	public BigDecimal getMastDrIntAdj() {
		return this.mastDrIntAdj;
	}

	public void setMastDrIntAdj(BigDecimal mastDrIntAdj) {
		this.mastDrIntAdj = mastDrIntAdj;
	}

	public BigDecimal getMastDrIntAmt1() {
		return this.mastDrIntAmt1;
	}

	public void setMastDrIntAmt1(BigDecimal mastDrIntAmt1) {
		this.mastDrIntAmt1 = mastDrIntAmt1;
	}

	public BigDecimal getMastDrIntAmt2() {
		return this.mastDrIntAmt2;
	}

	public void setMastDrIntAmt2(BigDecimal mastDrIntAmt2) {
		this.mastDrIntAmt2 = mastDrIntAmt2;
	}

	public BigDecimal getMastDrIntAmt3() {
		return this.mastDrIntAmt3;
	}

	public void setMastDrIntAmt3(BigDecimal mastDrIntAmt3) {
		this.mastDrIntAmt3 = mastDrIntAmt3;
	}

	public BigDecimal getMastDrIntAmt4() {
		return this.mastDrIntAmt4;
	}

	public void setMastDrIntAmt4(BigDecimal mastDrIntAmt4) {
		this.mastDrIntAmt4 = mastDrIntAmt4;
	}

	public BigDecimal getMastDrIntAmt5() {
		return this.mastDrIntAmt5;
	}

	public void setMastDrIntAmt5(BigDecimal mastDrIntAmt5) {
		this.mastDrIntAmt5 = mastDrIntAmt5;
	}

	public BigDecimal getMastDrIntAmt6() {
		return this.mastDrIntAmt6;
	}

	public void setMastDrIntAmt6(BigDecimal mastDrIntAmt6) {
		this.mastDrIntAmt6 = mastDrIntAmt6;
	}

	public BigDecimal getMastDrIntAmt7() {
		return this.mastDrIntAmt7;
	}

	public void setMastDrIntAmt7(BigDecimal mastDrIntAmt7) {
		this.mastDrIntAmt7 = mastDrIntAmt7;
	}

	public String getMastExtAccntNo() {
		return this.mastExtAccntNo;
	}

	public void setMastExtAccntNo(String mastExtAccntNo) {
		this.mastExtAccntNo = mastExtAccntNo;
	}

	public String getMastExternalAcctNo() {
		return this.mastExternalAcctNo;
	}

	public void setMastExternalAcctNo(String mastExternalAcctNo) {
		this.mastExternalAcctNo = mastExternalAcctNo;
	}

	public String getMastFcdailKey() {
		return this.mastFcdailKey;
	}

	public void setMastFcdailKey(String mastFcdailKey) {
		this.mastFcdailKey = mastFcdailKey;
	}

	public String getMastFcdailRevFlag() {
		return this.mastFcdailRevFlag;
	}

	public void setMastFcdailRevFlag(String mastFcdailRevFlag) {
		this.mastFcdailRevFlag = mastFcdailRevFlag;
	}

	public String getMastFeeChgAcct() {
		return this.mastFeeChgAcct;
	}

	public void setMastFeeChgAcct(String mastFeeChgAcct) {
		this.mastFeeChgAcct = mastFeeChgAcct;
	}

	public BigDecimal getMastFgnPost() {
		return this.mastFgnPost;
	}

	public void setMastFgnPost(BigDecimal mastFgnPost) {
		this.mastFgnPost = mastFgnPost;
	}

	public String getMastGlNo() {
		return this.mastGlNo;
	}

	public void setMastGlNo(String mastGlNo) {
		this.mastGlNo = mastGlNo;
	}

	public BigDecimal getMastGmcAmount() {
		return this.mastGmcAmount;
	}

	public void setMastGmcAmount(BigDecimal mastGmcAmount) {
		this.mastGmcAmount = mastGmcAmount;
	}

	public String getMastIbanElect() {
		return this.mastIbanElect;
	}

	public void setMastIbanElect(String mastIbanElect) {
		this.mastIbanElect = mastIbanElect;
	}

	public String getMastIbanPaper() {
		return this.mastIbanPaper;
	}

	public void setMastIbanPaper(String mastIbanPaper) {
		this.mastIbanPaper = mastIbanPaper;
	}

	public String getMastInstrType() {
		return this.mastInstrType;
	}

	public void setMastInstrType(String mastInstrType) {
		this.mastInstrType = mastInstrType;
	}

	public String getMastInsuffFundInd() {
		return this.mastInsuffFundInd;
	}

	public void setMastInsuffFundInd(String mastInsuffFundInd) {
		this.mastInsuffFundInd = mastInsuffFundInd;
	}

	public String getMastIntChgAcct() {
		return this.mastIntChgAcct;
	}

	public void setMastIntChgAcct(String mastIntChgAcct) {
		this.mastIntChgAcct = mastIntChgAcct;
	}

	public String getMastIntRevDate() {
		return this.mastIntRevDate;
	}

	public void setMastIntRevDate(String mastIntRevDate) {
		this.mastIntRevDate = mastIntRevDate;
	}

	public String getMastIntStmtBegDate1() {
		return this.mastIntStmtBegDate1;
	}

	public void setMastIntStmtBegDate1(String mastIntStmtBegDate1) {
		this.mastIntStmtBegDate1 = mastIntStmtBegDate1;
	}

	public String getMastIntStmtBegDate2() {
		return this.mastIntStmtBegDate2;
	}

	public void setMastIntStmtBegDate2(String mastIntStmtBegDate2) {
		this.mastIntStmtBegDate2 = mastIntStmtBegDate2;
	}

	public String getMastIntStmtBegDate3() {
		return this.mastIntStmtBegDate3;
	}

	public void setMastIntStmtBegDate3(String mastIntStmtBegDate3) {
		this.mastIntStmtBegDate3 = mastIntStmtBegDate3;
	}

	public String getMastIntStmtBegDate4() {
		return this.mastIntStmtBegDate4;
	}

	public void setMastIntStmtBegDate4(String mastIntStmtBegDate4) {
		this.mastIntStmtBegDate4 = mastIntStmtBegDate4;
	}

	public String getMastIntStmtBegDate5() {
		return this.mastIntStmtBegDate5;
	}

	public void setMastIntStmtBegDate5(String mastIntStmtBegDate5) {
		this.mastIntStmtBegDate5 = mastIntStmtBegDate5;
	}

	public String getMastIntStmtBegDate6() {
		return this.mastIntStmtBegDate6;
	}

	public void setMastIntStmtBegDate6(String mastIntStmtBegDate6) {
		this.mastIntStmtBegDate6 = mastIntStmtBegDate6;
	}

	public String getMastIntStmtBegDate7() {
		return this.mastIntStmtBegDate7;
	}

	public void setMastIntStmtBegDate7(String mastIntStmtBegDate7) {
		this.mastIntStmtBegDate7 = mastIntStmtBegDate7;
	}

	public String getMastIntStmtEndDate1() {
		return this.mastIntStmtEndDate1;
	}

	public void setMastIntStmtEndDate1(String mastIntStmtEndDate1) {
		this.mastIntStmtEndDate1 = mastIntStmtEndDate1;
	}

	public String getMastIntStmtEndDate2() {
		return this.mastIntStmtEndDate2;
	}

	public void setMastIntStmtEndDate2(String mastIntStmtEndDate2) {
		this.mastIntStmtEndDate2 = mastIntStmtEndDate2;
	}

	public String getMastIntStmtEndDate3() {
		return this.mastIntStmtEndDate3;
	}

	public void setMastIntStmtEndDate3(String mastIntStmtEndDate3) {
		this.mastIntStmtEndDate3 = mastIntStmtEndDate3;
	}

	public String getMastIntStmtEndDate4() {
		return this.mastIntStmtEndDate4;
	}

	public void setMastIntStmtEndDate4(String mastIntStmtEndDate4) {
		this.mastIntStmtEndDate4 = mastIntStmtEndDate4;
	}

	public String getMastIntStmtEndDate5() {
		return this.mastIntStmtEndDate5;
	}

	public void setMastIntStmtEndDate5(String mastIntStmtEndDate5) {
		this.mastIntStmtEndDate5 = mastIntStmtEndDate5;
	}

	public String getMastIntStmtEndDate6() {
		return this.mastIntStmtEndDate6;
	}

	public void setMastIntStmtEndDate6(String mastIntStmtEndDate6) {
		this.mastIntStmtEndDate6 = mastIntStmtEndDate6;
	}

	public String getMastIntStmtEndDate7() {
		return this.mastIntStmtEndDate7;
	}

	public void setMastIntStmtEndDate7(String mastIntStmtEndDate7) {
		this.mastIntStmtEndDate7 = mastIntStmtEndDate7;
	}

	public BigDecimal getMastIntStmtFreq() {
		return this.mastIntStmtFreq;
	}

	public void setMastIntStmtFreq(BigDecimal mastIntStmtFreq) {
		this.mastIntStmtFreq = mastIntStmtFreq;
	}

	public BigDecimal getMastIol() {
		return this.mastIol;
	}

	public void setMastIol(BigDecimal mastIol) {
		this.mastIol = mastIol;
	}

	public String getMastLastGlDate() {
		return this.mastLastGlDate;
	}

	public void setMastLastGlDate(String mastLastGlDate) {
		this.mastLastGlDate = mastLastGlDate;
	}

	public String getMastLastIntStmtDate() {
		return this.mastLastIntStmtDate;
	}

	public void setMastLastIntStmtDate(String mastLastIntStmtDate) {
		this.mastLastIntStmtDate = mastLastIntStmtDate;
	}

	public String getMastLastIntStmtDate2() {
		return this.mastLastIntStmtDate2;
	}

	public void setMastLastIntStmtDate2(String mastLastIntStmtDate2) {
		this.mastLastIntStmtDate2 = mastLastIntStmtDate2;
	}

	public BigDecimal getMastLastStmtNo() {
		return this.mastLastStmtNo;
	}

	public void setMastLastStmtNo(BigDecimal mastLastStmtNo) {
		this.mastLastStmtNo = mastLastStmtNo;
	}

	public BigDecimal getMastLastStmtNoPd() {
		return this.mastLastStmtNoPd;
	}

	public void setMastLastStmtNoPd(BigDecimal mastLastStmtNoPd) {
		this.mastLastStmtNoPd = mastLastStmtNoPd;
	}

	public BigDecimal getMastLiqmgtFirstStatement() {
		return this.mastLiqmgtFirstStatement;
	}

	public void setMastLiqmgtFirstStatement(BigDecimal mastLiqmgtFirstStatement) {
		this.mastLiqmgtFirstStatement = mastLiqmgtFirstStatement;
	}

	public String getMastLiqmgtIndicator() {
		return this.mastLiqmgtIndicator;
	}

	public void setMastLiqmgtIndicator(String mastLiqmgtIndicator) {
		this.mastLiqmgtIndicator = mastLiqmgtIndicator;
	}

	public Date getMastLiqmgtLastMsgDate() {
		return this.mastLiqmgtLastMsgDate;
	}

	public void setMastLiqmgtLastMsgDate(Date mastLiqmgtLastMsgDate) {
		this.mastLiqmgtLastMsgDate = mastLiqmgtLastMsgDate;
	}

	public BigDecimal getMastLiqmgtLastMsgTime() {
		return this.mastLiqmgtLastMsgTime;
	}

	public void setMastLiqmgtLastMsgTime(BigDecimal mastLiqmgtLastMsgTime) {
		this.mastLiqmgtLastMsgTime = mastLiqmgtLastMsgTime;
	}

	public BigDecimal getMastLiqmgtLastStmtNo() {
		return this.mastLiqmgtLastStmtNo;
	}

	public void setMastLiqmgtLastStmtNo(BigDecimal mastLiqmgtLastStmtNo) {
		this.mastLiqmgtLastStmtNo = mastLiqmgtLastStmtNo;
	}

	public BigDecimal getMastLiqmgtTimeInterval() {
		return this.mastLiqmgtTimeInterval;
	}

	public void setMastLiqmgtTimeInterval(BigDecimal mastLiqmgtTimeInterval) {
		this.mastLiqmgtTimeInterval = mastLiqmgtTimeInterval;
	}

	public String getMastLiqmgtTimeReference() {
		return this.mastLiqmgtTimeReference;
	}

	public void setMastLiqmgtTimeReference(String mastLiqmgtTimeReference) {
		this.mastLiqmgtTimeReference = mastLiqmgtTimeReference;
	}

	public BigDecimal getMastLocalPost() {
		return this.mastLocalPost;
	}

	public void setMastLocalPost(BigDecimal mastLocalPost) {
		this.mastLocalPost = mastLocalPost;
	}

	public String getMastLstStatChgDate() {
		return this.mastLstStatChgDate;
	}

	public void setMastLstStatChgDate(String mastLstStatChgDate) {
		this.mastLstStatChgDate = mastLstStatChgDate;
	}

	public String getMastLstStmtDate() {
		return this.mastLstStmtDate;
	}

	public void setMastLstStmtDate(String mastLstStmtDate) {
		this.mastLstStmtDate = mastLstStmtDate;
	}

	public String getMastLstStmtDatePd() {
		return this.mastLstStmtDatePd;
	}

	public void setMastLstStmtDatePd(String mastLstStmtDatePd) {
		this.mastLstStmtDatePd = mastLstStmtDatePd;
	}

	public String getMastLzbFlag() {
		return this.mastLzbFlag;
	}

	public void setMastLzbFlag(String mastLzbFlag) {
		this.mastLzbFlag = mastLzbFlag;
	}

	public String getMastMailInd() {
		return this.mastMailInd;
	}

	public void setMastMailInd(String mastMailInd) {
		this.mastMailInd = mastMailInd;
	}

	public BigDecimal getMastMaxChg() {
		return this.mastMaxChg;
	}

	public void setMastMaxChg(BigDecimal mastMaxChg) {
		this.mastMaxChg = mastMaxChg;
	}

	public BigDecimal getMastMinChg() {
		return this.mastMinChg;
	}

	public void setMastMinChg(BigDecimal mastMinChg) {
		this.mastMinChg = mastMinChg;
	}

	public String getMastMinMaxInd() {
		return this.mastMinMaxInd;
	}

	public void setMastMinMaxInd(String mastMinMaxInd) {
		this.mastMinMaxInd = mastMinMaxInd;
	}

	public BigDecimal getMastMinmaxChgTotal() {
		return this.mastMinmaxChgTotal;
	}

	public void setMastMinmaxChgTotal(BigDecimal mastMinmaxChgTotal) {
		this.mastMinmaxChgTotal = mastMinmaxChgTotal;
	}

	public BigDecimal getMastMmacbAccrual0() {
		return this.mastMmacbAccrual0;
	}

	public void setMastMmacbAccrual0(BigDecimal mastMmacbAccrual0) {
		this.mastMmacbAccrual0 = mastMmacbAccrual0;
	}

	public BigDecimal getMastMmacbAccrual1() {
		return this.mastMmacbAccrual1;
	}

	public void setMastMmacbAccrual1(BigDecimal mastMmacbAccrual1) {
		this.mastMmacbAccrual1 = mastMmacbAccrual1;
	}

	public BigDecimal getMastMmacbAccrual2() {
		return this.mastMmacbAccrual2;
	}

	public void setMastMmacbAccrual2(BigDecimal mastMmacbAccrual2) {
		this.mastMmacbAccrual2 = mastMmacbAccrual2;
	}

	public BigDecimal getMastMmacbAccrual3() {
		return this.mastMmacbAccrual3;
	}

	public void setMastMmacbAccrual3(BigDecimal mastMmacbAccrual3) {
		this.mastMmacbAccrual3 = mastMmacbAccrual3;
	}

	public BigDecimal getMastMmacbAccrual4() {
		return this.mastMmacbAccrual4;
	}

	public void setMastMmacbAccrual4(BigDecimal mastMmacbAccrual4) {
		this.mastMmacbAccrual4 = mastMmacbAccrual4;
	}

	public BigDecimal getMastMmacbAccrual5() {
		return this.mastMmacbAccrual5;
	}

	public void setMastMmacbAccrual5(BigDecimal mastMmacbAccrual5) {
		this.mastMmacbAccrual5 = mastMmacbAccrual5;
	}

	public BigDecimal getMastMmacbAccrual6() {
		return this.mastMmacbAccrual6;
	}

	public void setMastMmacbAccrual6(BigDecimal mastMmacbAccrual6) {
		this.mastMmacbAccrual6 = mastMmacbAccrual6;
	}

	public BigDecimal getMastMmacbFee0() {
		return this.mastMmacbFee0;
	}

	public void setMastMmacbFee0(BigDecimal mastMmacbFee0) {
		this.mastMmacbFee0 = mastMmacbFee0;
	}

	public BigDecimal getMastMmacbFee1() {
		return this.mastMmacbFee1;
	}

	public void setMastMmacbFee1(BigDecimal mastMmacbFee1) {
		this.mastMmacbFee1 = mastMmacbFee1;
	}

	public BigDecimal getMastMmacbFee2() {
		return this.mastMmacbFee2;
	}

	public void setMastMmacbFee2(BigDecimal mastMmacbFee2) {
		this.mastMmacbFee2 = mastMmacbFee2;
	}

	public BigDecimal getMastMmacbFee3() {
		return this.mastMmacbFee3;
	}

	public void setMastMmacbFee3(BigDecimal mastMmacbFee3) {
		this.mastMmacbFee3 = mastMmacbFee3;
	}

	public BigDecimal getMastMmacbFee4() {
		return this.mastMmacbFee4;
	}

	public void setMastMmacbFee4(BigDecimal mastMmacbFee4) {
		this.mastMmacbFee4 = mastMmacbFee4;
	}

	public BigDecimal getMastMmacbFee5() {
		return this.mastMmacbFee5;
	}

	public void setMastMmacbFee5(BigDecimal mastMmacbFee5) {
		this.mastMmacbFee5 = mastMmacbFee5;
	}

	public BigDecimal getMastMmacbFee6() {
		return this.mastMmacbFee6;
	}

	public void setMastMmacbFee6(BigDecimal mastMmacbFee6) {
		this.mastMmacbFee6 = mastMmacbFee6;
	}

	public BigDecimal getMastMmacb0() {
		return this.mastMmacb0;
	}

	public void setMastMmacb0(BigDecimal mastMmacb0) {
		this.mastMmacb0 = mastMmacb0;
	}

	public BigDecimal getMastMmacb5() {
		return this.mastMmacb5;
	}

	public void setMastMmacb5(BigDecimal mastMmacb5) {
		this.mastMmacb5 = mastMmacb5;
	}

	public BigDecimal getMastMmacb6() {
		return this.mastMmacb6;
	}

	public void setMastMmacb6(BigDecimal mastMmacb6) {
		this.mastMmacb6 = mastMmacb6;
	}

	public BigDecimal getMastMofAvailBal() {
		return this.mastMofAvailBal;
	}

	public void setMastMofAvailBal(BigDecimal mastMofAvailBal) {
		this.mastMofAvailBal = mastMofAvailBal;
	}

	public String getMastMofNo() {
		return this.mastMofNo;
	}

	public void setMastMofNo(String mastMofNo) {
		this.mastMofNo = mastMofNo;
	}

	public String getMastNeedEodMofhist() {
		return this.mastNeedEodMofhist;
	}

	public void setMastNeedEodMofhist(String mastNeedEodMofhist) {
		this.mastNeedEodMofhist = mastNeedEodMofhist;
	}

	public BigDecimal getMastNextDayCr() {
		return this.mastNextDayCr;
	}

	public void setMastNextDayCr(BigDecimal mastNextDayCr) {
		this.mastNextDayCr = mastNextDayCr;
	}

	public BigDecimal getMastNextDayDr() {
		return this.mastNextDayDr;
	}

	public void setMastNextDayDr(BigDecimal mastNextDayDr) {
		this.mastNextDayDr = mastNextDayDr;
	}

	public String getMastNoFincActDate() {
		return this.mastNoFincActDate;
	}

	public void setMastNoFincActDate(String mastNoFincActDate) {
		this.mastNoFincActDate = mastNoFincActDate;
	}

	public BigDecimal getMastNoticeDays() {
		return this.mastNoticeDays;
	}

	public void setMastNoticeDays(BigDecimal mastNoticeDays) {
		this.mastNoticeDays = mastNoticeDays;
	}

	public BigDecimal getMastNowCrIntPosted1() {
		return this.mastNowCrIntPosted1;
	}

	public void setMastNowCrIntPosted1(BigDecimal mastNowCrIntPosted1) {
		this.mastNowCrIntPosted1 = mastNowCrIntPosted1;
	}

	public BigDecimal getMastNowCrIntPosted2() {
		return this.mastNowCrIntPosted2;
	}

	public void setMastNowCrIntPosted2(BigDecimal mastNowCrIntPosted2) {
		this.mastNowCrIntPosted2 = mastNowCrIntPosted2;
	}

	public BigDecimal getMastNowCrIntPosted3() {
		return this.mastNowCrIntPosted3;
	}

	public void setMastNowCrIntPosted3(BigDecimal mastNowCrIntPosted3) {
		this.mastNowCrIntPosted3 = mastNowCrIntPosted3;
	}

	public BigDecimal getMastNowCrIntPosted4() {
		return this.mastNowCrIntPosted4;
	}

	public void setMastNowCrIntPosted4(BigDecimal mastNowCrIntPosted4) {
		this.mastNowCrIntPosted4 = mastNowCrIntPosted4;
	}

	public BigDecimal getMastNowCrIntPosted5() {
		return this.mastNowCrIntPosted5;
	}

	public void setMastNowCrIntPosted5(BigDecimal mastNowCrIntPosted5) {
		this.mastNowCrIntPosted5 = mastNowCrIntPosted5;
	}

	public BigDecimal getMastNowCrIntPosted6() {
		return this.mastNowCrIntPosted6;
	}

	public void setMastNowCrIntPosted6(BigDecimal mastNowCrIntPosted6) {
		this.mastNowCrIntPosted6 = mastNowCrIntPosted6;
	}

	public BigDecimal getMastNowCrIntPosted7() {
		return this.mastNowCrIntPosted7;
	}

	public void setMastNowCrIntPosted7(BigDecimal mastNowCrIntPosted7) {
		this.mastNowCrIntPosted7 = mastNowCrIntPosted7;
	}

	public String getMastNowCurStartDate() {
		return this.mastNowCurStartDate;
	}

	public void setMastNowCurStartDate(String mastNowCurStartDate) {
		this.mastNowCurStartDate = mastNowCurStartDate;
	}

	public BigDecimal getMastNowPostedCrInt() {
		return this.mastNowPostedCrInt;
	}

	public void setMastNowPostedCrInt(BigDecimal mastNowPostedCrInt) {
		this.mastNowPostedCrInt = mastNowPostedCrInt;
	}

	public String getMastNowPrevStartDate() {
		return this.mastNowPrevStartDate;
	}

	public void setMastNowPrevStartDate(String mastNowPrevStartDate) {
		this.mastNowPrevStartDate = mastNowPrevStartDate;
	}

	public BigDecimal getMastNowTotalCrInt() {
		return this.mastNowTotalCrInt;
	}

	public void setMastNowTotalCrInt(BigDecimal mastNowTotalCrInt) {
		this.mastNowTotalCrInt = mastNowTotalCrInt;
	}

	public String getMastNxtStmtDate() {
		return this.mastNxtStmtDate;
	}

	public void setMastNxtStmtDate(String mastNxtStmtDate) {
		this.mastNxtStmtDate = mastNxtStmtDate;
	}

	public BigDecimal getMastOdCounter() {
		return this.mastOdCounter;
	}

	public void setMastOdCounter(BigDecimal mastOdCounter) {
		this.mastOdCounter = mastOdCounter;
	}

	public BigDecimal getMastOpenBookBal() {
		return this.mastOpenBookBal;
	}

	public void setMastOpenBookBal(BigDecimal mastOpenBookBal) {
		this.mastOpenBookBal = mastOpenBookBal;
	}

	public String getMastOperId() {
		return this.mastOperId;
	}

	public void setMastOperId(String mastOperId) {
		this.mastOperId = mastOperId;
	}

	public String getMastOrigSetUpDate() {
		return this.mastOrigSetUpDate;
	}

	public void setMastOrigSetUpDate(String mastOrigSetUpDate) {
		this.mastOrigSetUpDate = mastOrigSetUpDate;
	}

	public BigDecimal getMastOvdrLim() {
		return this.mastOvdrLim;
	}

	public void setMastOvdrLim(BigDecimal mastOvdrLim) {
		this.mastOvdrLim = mastOvdrLim;
	}

	public String getMastParentDda() {
		return this.mastParentDda;
	}

	public void setMastParentDda(String mastParentDda) {
		this.mastParentDda = mastParentDda;
	}

	public String getMastPartHoldInd() {
		return this.mastPartHoldInd;
	}

	public void setMastPartHoldInd(String mastPartHoldInd) {
		this.mastPartHoldInd = mastPartHoldInd;
	}

	public String getMastPoolLevel() {
		return this.mastPoolLevel;
	}

	public void setMastPoolLevel(String mastPoolLevel) {
		this.mastPoolLevel = mastPoolLevel;
	}

	public String getMastPoolType() {
		return this.mastPoolType;
	}

	public void setMastPoolType(String mastPoolType) {
		this.mastPoolType = mastPoolType;
	}

	public String getMastPostCrIntFlag() {
		return this.mastPostCrIntFlag;
	}

	public void setMastPostCrIntFlag(String mastPostCrIntFlag) {
		this.mastPostCrIntFlag = mastPostCrIntFlag;
	}

	public String getMastPostFcdailKey() {
		return this.mastPostFcdailKey;
	}

	public void setMastPostFcdailKey(String mastPostFcdailKey) {
		this.mastPostFcdailKey = mastPostFcdailKey;
	}

	public String getMastPostFcdailRevFlag() {
		return this.mastPostFcdailRevFlag;
	}

	public void setMastPostFcdailRevFlag(String mastPostFcdailRevFlag) {
		this.mastPostFcdailRevFlag = mastPostFcdailRevFlag;
	}

	public BigDecimal getMastPostIncAccrual() {
		return this.mastPostIncAccrual;
	}

	public void setMastPostIncAccrual(BigDecimal mastPostIncAccrual) {
		this.mastPostIncAccrual = mastPostIncAccrual;
	}

	public BigDecimal getMastPrevAvailBal() {
		return this.mastPrevAvailBal;
	}

	public void setMastPrevAvailBal(BigDecimal mastPrevAvailBal) {
		this.mastPrevAvailBal = mastPrevAvailBal;
	}

	public BigDecimal getMastPrevBkBal() {
		return this.mastPrevBkBal;
	}

	public void setMastPrevBkBal(BigDecimal mastPrevBkBal) {
		this.mastPrevBkBal = mastPrevBkBal;
	}

	public BigDecimal getMastPrevEndBkBal() {
		return this.mastPrevEndBkBal;
	}

	public void setMastPrevEndBkBal(BigDecimal mastPrevEndBkBal) {
		this.mastPrevEndBkBal = mastPrevEndBkBal;
	}

	public String getMastRecInUseFlag() {
		return this.mastRecInUseFlag;
	}

	public void setMastRecInUseFlag(String mastRecInUseFlag) {
		this.mastRecInUseFlag = mastRecInUseFlag;
	}

	public String getMastReopenAllowed() {
		return this.mastReopenAllowed;
	}

	public void setMastReopenAllowed(String mastReopenAllowed) {
		this.mastReopenAllowed = mastReopenAllowed;
	}

	public String getMastSecrCode() {
		return this.mastSecrCode;
	}

	public void setMastSecrCode(String mastSecrCode) {
		this.mastSecrCode = mastSecrCode;
	}

	public String getMastSetUpDate() {
		return this.mastSetUpDate;
	}

	public void setMastSetUpDate(String mastSetUpDate) {
		this.mastSetUpDate = mastSetUpDate;
	}

	public String getMastSpecialInd() {
		return this.mastSpecialInd;
	}

	public void setMastSpecialInd(String mastSpecialInd) {
		this.mastSpecialInd = mastSpecialInd;
	}

	public String getMastStatCyc() {
		return this.mastStatCyc;
	}

	public void setMastStatCyc(String mastStatCyc) {
		this.mastStatCyc = mastStatCyc;
	}

	public String getMastStatiAccTranDate() {
		return this.mastStatiAccTranDate;
	}

	public void setMastStatiAccTranDate(String mastStatiAccTranDate) {
		this.mastStatiAccTranDate = mastStatiAccTranDate;
	}

	public BigDecimal getMastStatiCrTrans() {
		return this.mastStatiCrTrans;
	}

	public void setMastStatiCrTrans(BigDecimal mastStatiCrTrans) {
		this.mastStatiCrTrans = mastStatiCrTrans;
	}

	public BigDecimal getMastStatiDbTrans() {
		return this.mastStatiDbTrans;
	}

	public void setMastStatiDbTrans(BigDecimal mastStatiDbTrans) {
		this.mastStatiDbTrans = mastStatiDbTrans;
	}

	public BigDecimal getMastStmtCr() {
		return this.mastStmtCr;
	}

	public void setMastStmtCr(BigDecimal mastStmtCr) {
		this.mastStmtCr = mastStmtCr;
	}

	public BigDecimal getMastStmtCrPd() {
		return this.mastStmtCrPd;
	}

	public void setMastStmtCrPd(BigDecimal mastStmtCrPd) {
		this.mastStmtCrPd = mastStmtCrPd;
	}

	public BigDecimal getMastStmtDb() {
		return this.mastStmtDb;
	}

	public void setMastStmtDb(BigDecimal mastStmtDb) {
		this.mastStmtDb = mastStmtDb;
	}

	public BigDecimal getMastStmtDbPd() {
		return this.mastStmtDbPd;
	}

	public void setMastStmtDbPd(BigDecimal mastStmtDbPd) {
		this.mastStmtDbPd = mastStmtDbPd;
	}

	public String getMastStmtNeeded() {
		return this.mastStmtNeeded;
	}

	public void setMastStmtNeeded(String mastStmtNeeded) {
		this.mastStmtNeeded = mastStmtNeeded;
	}

	public BigDecimal getMastStopPayment() {
		return this.mastStopPayment;
	}

	public void setMastStopPayment(BigDecimal mastStopPayment) {
		this.mastStopPayment = mastStopPayment;
	}

	public BigDecimal getMastSwiftFees() {
		return this.mastSwiftFees;
	}

	public void setMastSwiftFees(BigDecimal mastSwiftFees) {
		this.mastSwiftFees = mastSwiftFees;
	}

	public BigDecimal getMastSwiftFeesAccrual() {
		return this.mastSwiftFeesAccrual;
	}

	public void setMastSwiftFeesAccrual(BigDecimal mastSwiftFeesAccrual) {
		this.mastSwiftFeesAccrual = mastSwiftFeesAccrual;
	}

	public String getMastT4mCustomer() {
		return this.mastT4mCustomer;
	}

	public void setMastT4mCustomer(String mastT4mCustomer) {
		this.mastT4mCustomer = mastT4mCustomer;
	}

	public BigDecimal getMastTaxChg() {
		return this.mastTaxChg;
	}

	public void setMastTaxChg(BigDecimal mastTaxChg) {
		this.mastTaxChg = mastTaxChg;
	}

	public String getMastTaxChgInd() {
		return this.mastTaxChgInd;
	}

	public void setMastTaxChgInd(String mastTaxChgInd) {
		this.mastTaxChgInd = mastTaxChgInd;
	}

	public BigDecimal getMastTodayBackvalueAmount() {
		return this.mastTodayBackvalueAmount;
	}

	public void setMastTodayBackvalueAmount(BigDecimal mastTodayBackvalueAmount) {
		this.mastTodayBackvalueAmount = mastTodayBackvalueAmount;
	}

	public BigDecimal getMastTodayCrAmt() {
		return this.mastTodayCrAmt;
	}

	public void setMastTodayCrAmt(BigDecimal mastTodayCrAmt) {
		this.mastTodayCrAmt = mastTodayCrAmt;
	}

	public BigDecimal getMastTodayDbAmt() {
		return this.mastTodayDbAmt;
	}

	public void setMastTodayDbAmt(BigDecimal mastTodayDbAmt) {
		this.mastTodayDbAmt = mastTodayDbAmt;
	}

	public BigDecimal getMastTotCrAmt() {
		return this.mastTotCrAmt;
	}

	public void setMastTotCrAmt(BigDecimal mastTotCrAmt) {
		this.mastTotCrAmt = mastTotCrAmt;
	}

	public BigDecimal getMastTotDbAmt() {
		return this.mastTotDbAmt;
	}

	public void setMastTotDbAmt(BigDecimal mastTotDbAmt) {
		this.mastTotDbAmt = mastTotDbAmt;
	}

	public BigDecimal getMastTotStmtCnt() {
		return this.mastTotStmtCnt;
	}

	public void setMastTotStmtCnt(BigDecimal mastTotStmtCnt) {
		this.mastTotStmtCnt = mastTotStmtCnt;
	}

	public BigDecimal getMastTotTran() {
		return this.mastTotTran;
	}

	public void setMastTotTran(BigDecimal mastTotTran) {
		this.mastTotTran = mastTotTran;
	}

	public BigDecimal getMastTotTranPd() {
		return this.mastTotTranPd;
	}

	public void setMastTotTranPd(BigDecimal mastTotTranPd) {
		this.mastTotTranPd = mastTotTranPd;
	}

	public BigDecimal getMastTranAmt() {
		return this.mastTranAmt;
	}

	public void setMastTranAmt(BigDecimal mastTranAmt) {
		this.mastTranAmt = mastTranAmt;
	}

	public BigDecimal getMastTranAmtPd() {
		return this.mastTranAmtPd;
	}

	public void setMastTranAmtPd(BigDecimal mastTranAmtPd) {
		this.mastTranAmtPd = mastTranAmtPd;
	}

	public BigDecimal getMastTranChg() {
		return this.mastTranChg;
	}

	public void setMastTranChg(BigDecimal mastTranChg) {
		this.mastTranChg = mastTranChg;
	}

	public BigDecimal getMastTranChgEach() {
		return this.mastTranChgEach;
	}

	public void setMastTranChgEach(BigDecimal mastTranChgEach) {
		this.mastTranChgEach = mastTranChgEach;
	}

	public BigDecimal getMastTranDateCnt() {
		return this.mastTranDateCnt;
	}

	public void setMastTranDateCnt(BigDecimal mastTranDateCnt) {
		this.mastTranDateCnt = mastTranDateCnt;
	}

	public BigDecimal getMastTranFree() {
		return this.mastTranFree;
	}

	public void setMastTranFree(BigDecimal mastTranFree) {
		this.mastTranFree = mastTranFree;
	}

	public String getMastTranTabInd() {
		return this.mastTranTabInd;
	}

	public void setMastTranTabInd(String mastTranTabInd) {
		this.mastTranTabInd = mastTranTabInd;
	}

	public BigDecimal getMastTranTurnIncAccrual() {
		return this.mastTranTurnIncAccrual;
	}

	public void setMastTranTurnIncAccrual(BigDecimal mastTranTurnIncAccrual) {
		this.mastTranTurnIncAccrual = mastTranTurnIncAccrual;
	}

	public BigDecimal getMastTurnChg() {
		return this.mastTurnChg;
	}

	public void setMastTurnChg(BigDecimal mastTurnChg) {
		this.mastTurnChg = mastTurnChg;
	}

	public String getMastTurnChgInd() {
		return this.mastTurnChgInd;
	}

	public void setMastTurnChgInd(String mastTurnChgInd) {
		this.mastTurnChgInd = mastTurnChgInd;
	}

	public String getMastTurnoverType() {
		return this.mastTurnoverType;
	}

	public void setMastTurnoverType(String mastTurnoverType) {
		this.mastTurnoverType = mastTurnoverType;
	}

	public BigDecimal getMastValidTrans() {
		return this.mastValidTrans;
	}

	public void setMastValidTrans(BigDecimal mastValidTrans) {
		this.mastValidTrans = mastValidTrans;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}