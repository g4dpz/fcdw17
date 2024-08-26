package uk.me.g4dpz.fcdwrawdata.dao;

import uk.me.g4dpz.fcdwrawdata.domain.UserRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRankingDao extends JpaRepository<UserRanking, Long> {

    @Query
    List<UserRanking> findBySatelliteIdAndSiteId(Long satelliteId, String siteId);

    @Query
    List<UserRanking> findBySiteIdContainingIgnoreCaseOrSiteAliasContainingIgnoreCase(String searchId, String searchAlias);
}