package es.commerzbank.ice.embargos.domain.dto;

import java.util.List;

public class CGPJReplyDTO {
    private List<CGPJReplyResultDTO> result;

    public List<CGPJReplyResultDTO> getResult() {
        return result;
    }

    public void setResult(List<CGPJReplyResultDTO> result) {
        this.result = result;
    }
}
