package uk.me.g4dpz.fcdwrawdata.dao;

import uk.me.g4dpz.fcdwrawdata.domain.Payload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PayloadDao extends JpaRepository<Payload, Long> {

    @Query
    Payload findByHexText(String hexText);
}