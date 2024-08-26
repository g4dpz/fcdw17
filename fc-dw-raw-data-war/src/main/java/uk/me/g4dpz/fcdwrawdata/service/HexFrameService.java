package uk.me.g4dpz.fcdwrawdata.service;

import uk.me.g4dpz.fcdwcommon.dto.HexFrameDTO;
import uk.me.g4dpz.fcdwrawdata.service.impl.PacketResponseEntity;

import java.util.List;

public interface HexFrameService {

    String ping();

    PacketResponseEntity processHexFrame(String site_id, String digest, String hex_frame);

    void handleMessage(String message);

    HexFrameDTO getFrame(long l, long l1, long l2);

    List<String> getPayloads(Long satelliteId, Long sequenceNumber, String frames);
}
