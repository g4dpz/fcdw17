package uk.me.g4dpz.datawarehouse.common.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidjohnson on 22/01/2017.
 */
public class FitterMessagesDTO implements Serializable {

    private List<FitterMessageDTO> fitterMessages = new ArrayList<>();

    public FitterMessagesDTO() {
    }

    public FitterMessagesDTO(List<FitterMessageDTO> fitterMessages) {
        this.fitterMessages = fitterMessages;
    }

    public List<FitterMessageDTO> getFitterMessages() {
        return fitterMessages;
    }

    public void setFitterMessages(List<FitterMessageDTO> fitterMessages) {
        this.fitterMessages = fitterMessages;
    }

    @Override
    public String toString() {
        return "FitterMessagesDTO{" +
                "fitterMessages=" + fitterMessages +
                '}';
    }
}
