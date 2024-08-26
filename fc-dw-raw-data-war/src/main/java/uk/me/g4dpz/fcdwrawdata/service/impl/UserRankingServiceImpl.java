package uk.me.g4dpz.fcdwrawdata.service.impl;

import uk.me.g4dpz.fcdwrawdata.dao.UserRankingDao;
import uk.me.g4dpz.fcdwrawdata.domain.UserRanking;
import uk.me.g4dpz.fcdwrawdata.service.UserRankingService;
import uk.me.g4dpz.fcdwrawdata.shared.Data;
import uk.me.g4dpz.fcdwrawdata.shared.Ranking;
import uk.me.g4dpz.fcdwrawdata.utils.BeanComparator;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserRankingServiceImpl implements UserRankingService {

    private static final Table<String, Long, Long> RANKINGS = HashBasedTable.create();

    @Autowired
    UserRankingDao userRankingDao;

    public UserRankingServiceImpl() {

    }

    @Override
    public Ranking getRanking(int draw, int sort, int start, int length, String search) {

        List<UserRanking> userRankings;

        if (search != null) {
            userRankings = userRankingDao.findBySiteIdContainingIgnoreCaseOrSiteAliasContainingIgnoreCase(search, search);
        }
        else {
            userRankings = userRankingDao.findAll();
        }

        Set<String> sites = new HashSet<>();
        List<Data> data = new ArrayList<>();

        for(UserRanking userRanking : userRankings) {
            String siteId = userRanking.getSiteId();
            String siteAlias = userRanking.getSiteAlias();
            if (siteAlias != null && !siteId.equals(siteAlias)) {
                siteId = siteAlias;
            }
            sites.add(siteId);
            RANKINGS.put(siteId, userRanking.getSatelliteId(), userRanking.getNumber());
        }

        for (String site : sites) {
            Data datum = getDatum(site);
            data.add(datum);
        }

        List<Data> unfilteredData = sort(data, sort);
        List<Data> filteredData = new ArrayList<>();

        int count = 0;
        for (Data datum : unfilteredData) {
            if (count >= start && count < start + length) {
                filteredData.add(datum);
            }
            count++;
        }

        return new Ranking(draw, filteredData, sort, unfilteredData.size(), unfilteredData.size());

    }

    /* ----------------------------------------
        Satellite no         Satellite ID            Satellite name
        decimal         Decimal   Binary      HEX
        0               0          00000000   00      FUNcube EM
        1               1          00000001   01      Ukube
        2               2          00000010   02      FUNcube-1
        3               3          00000011   03      EXTENDED PROTOCOL START
        4               7          00000111   07      ESEO
        5               11         00001011   0B      NAYIF-1
        6               15         00001111   0F      JY1SAT EM
        7               19         00010011   13      JY1SAT FM
         */

    private Data getDatum(String site) {

        Data data = new Data();
        data.setSiteId(site);

        // FUNcube-1
        Long score = RANKINGS.get(site, 2L);
        data.setSatellite1((score != null) ? score : 0L);
        // UKube-1
        score = RANKINGS.get(site, 1L);
        data.setSatellite2((score != null) ? score : 0L);
        // Nayif-1
        score = RANKINGS.get(site, 11L);
        data.setSatellite3((score != null) ? score : 0L);
        // JY1Sat
        score = RANKINGS.get(site, 19L);
        data.setSatellite4((score != null) ? score : 0L);
        // ESEO
        score = RANKINGS.get(site, 7L);
        data.setSatellite5((score != null) ? score : 0L);

        data.setTotal(
            data.getSatellite1() +
            data.getSatellite2() +
            data.getSatellite3() +
            data.getSatellite4() +
            data.getSatellite5()
        );

        return data;
    }

    private List<Data> sort(List<Data> data, int sort) {

        String[] sortAttributes
                = new String[] {"getTotal","getTotal","getSatellite1","getSatellite2","getSatellite3","getSatellite4","getSatellite5","getSatellite6"};

        BeanComparator bc = new BeanComparator(Data.class, sortAttributes[Math.abs(sort)], (sort < 0));
        Collections.sort(data, bc);
        return data;
    }
}
