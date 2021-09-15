package es.commerzbank.ice.embargos.domain.dto;

public class CGPJReplyResultDTO {
    private String petitionCode;
    private String result;

    public String getPetitionCode() {
        return petitionCode;
    }

    public void setPetitionCode(String petitionCode) {
        this.petitionCode = petitionCode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
