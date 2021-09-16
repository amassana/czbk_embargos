package es.commerzbank.ice.embargos.domain.dto;

import java.util.ArrayList;
import java.util.List;

public class CGPJReplyDTO {
    private List<CGPJReplyResultDTO> result;

    public List<CGPJReplyResultDTO> getResult() {
        return result;
    }

    public void addResponse(CGPJReplyResultDTO currentResponse) {
        if (result == null) {
            result = new ArrayList<>();
        }

        result.add(currentResponse);
    }
}
