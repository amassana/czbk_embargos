package es.commerzbank.ice.embargos.domain.dto;

import java.math.BigDecimal;

public class ManualLiftingDTO {
    private CommunicatingEntity communicatingEntity;
    private FileControlDTO fileControlDTOF4;
    private SeizureDTO seizureCase;
    private SeizedBankAccountDTO seizedBankAccount;
    private BigDecimal liftedAmount;

    public CommunicatingEntity getCommunicatingEntity() {
        return communicatingEntity;
    }

    public void setCommunicatingEntity(CommunicatingEntity communicatingEntity) {
        this.communicatingEntity = communicatingEntity;
    }

    public FileControlDTO getFileControlDTOF4() {
        return fileControlDTOF4;
    }

    public void setFileControlDTOF4(FileControlDTO fileControlDTOF4) {
        this.fileControlDTOF4 = fileControlDTOF4;
    }

    public SeizureDTO getSeizureCase() {
        return seizureCase;
    }

    public void setSeizureCase(SeizureDTO seizureCase) {
        this.seizureCase = seizureCase;
    }

    public SeizedBankAccountDTO getSeizedBankAccount() {
        return seizedBankAccount;
    }

    public void setSeizedBankAccount(SeizedBankAccountDTO seizedBankAccount) {
        this.seizedBankAccount = seizedBankAccount;
    }

    public BigDecimal getLiftedAmount() {
        return liftedAmount;
    }

    public void setLiftedAmount(BigDecimal liftedAmount) {
        this.liftedAmount = liftedAmount;
    }
}
