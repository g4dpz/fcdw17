package uk.me.g4dpz.fcdwrawdata.service;

import uk.me.g4dpz.fcdwrawdata.domain.Satellite;

import java.util.List;

public interface SatelliteListService {
    List<Satellite> findAllSatellites();
}
