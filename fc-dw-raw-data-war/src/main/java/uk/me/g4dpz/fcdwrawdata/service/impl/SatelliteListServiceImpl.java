package uk.me.g4dpz.fcdwrawdata.service.impl;

import uk.me.g4dpz.fcdwrawdata.domain.Satellite;
import uk.me.g4dpz.fcdwrawdata.service.SatelliteListService;

import java.util.ArrayList;
import java.util.List;

public class SatelliteListServiceImpl implements SatelliteListService {

    @Override
    public List<Satellite> findAllSatellites() {
        List<Satellite> satellites = new ArrayList<>();
        satellites.add(new Satellite("FUNcube-1 (AO-73)", "fc1-fm", "funcube-1_200.png", true));
        satellites.add(new Satellite("Nayif-1 (EO-88)", "nayif1", "nayif-1_200.png", true));
        satellites.add(new Satellite("JY1Sat (JO-97)", "jy1sat-fm", "jy1sat_200.png", true));
        satellites.add(new Satellite("ESEO", "eseo", "eseo_200.png", true));
        satellites.add(new Satellite("Lunar Gateway", "deepspace", "deepspace_200.png", true));

        return satellites;
    }
}
