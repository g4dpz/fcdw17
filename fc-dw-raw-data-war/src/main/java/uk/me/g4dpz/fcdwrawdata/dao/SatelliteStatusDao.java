package uk.me.g4dpz.fcdwrawdata.dao;

import uk.me.g4dpz.fcdwrawdata.domain.SatelliteStatus;
import uk.me.g4dpz.fcdwrawdata.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SatelliteStatusDao extends JpaRepository<SatelliteStatus, Long> {

    @Query
    User findBySatelliteId(String siteId);
}