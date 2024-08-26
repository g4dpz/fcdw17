package uk.me.g4dpz.fcdwrawdata.service;

import uk.me.g4dpz.fcdwrawdata.shared.Ranking;

public interface UserRankingService {
    Ranking getRanking(int draw, int sort, int start, int length, String search);
}
