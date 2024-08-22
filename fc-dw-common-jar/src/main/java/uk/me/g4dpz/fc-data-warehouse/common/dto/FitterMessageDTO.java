package uk.me.g4dpz.datawarehouse.common.dto;

import java.io.Serializable;

/**
 * Created by davidjohnson on 22/01/2017.
 */
public class FitterMessageDTO implements Serializable {

    private String messageText;
    private String slot;
    private String lastReceived;

    public FitterMessageDTO() {
    }

    public FitterMessageDTO(String messageText, String slot, String lastReceived) {
        this.messageText = messageText;
        this.slot = slot;
        this.lastReceived = lastReceived;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getLastReceived() {
        return lastReceived;
    }

    public void setLastReceived(String lastReceived) {
        this.lastReceived = lastReceived;
    }

    @Override
    public String toString() {
        return "FitterMessageDTO{" +
                "messageText='" + messageText + '\'' +
                ", slot='" + slot + '\'' +
                ", lastReceived='" + lastReceived + '\'' +
                '}';
    }
}
