package uk.me.g4dpz.fcdwrawdata.service;

import uk.me.g4dpz.fcdwcommon.dto.ValMinMaxDTO;

import java.util.List;

public interface FitterMessageService {
    List<ValMinMaxDTO> getMessages(String url);
}
