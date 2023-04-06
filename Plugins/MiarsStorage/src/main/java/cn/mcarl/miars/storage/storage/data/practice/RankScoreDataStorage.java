package cn.mcarl.miars.storage.storage.data.practice;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.storage.MiarsStorage;
import cn.mcarl.miars.storage.entity.practice.RankScore;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RankScoreDataStorage {
    private static final RankScoreDataStorage instance = new RankScoreDataStorage();
    public static RankScoreDataStorage getInstance() {
        return instance;
    }

    private final Map<RankScore.User, RankScore> dataMap = new HashMap<>();

    public void putRankScore(RankScore rankScore){
        try {

            MiarsStorage.getMySQLStorage().replaceRankScore(rankScore);

            RankScore data = MiarsStorage.getMySQLStorage().queryRankScore(rankScore.getUuid(),rankScore.getSeason());
            dataMap.put(
                    new RankScore.User(
                            rankScore.getUuid(),
                            rankScore.getSeason()
                    ),
                    data
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public RankScore getRankScore(UUID uuid, Integer season){
        if (dataMap.containsKey(
                new RankScore.User(
                        uuid,
                        season
                )
        )){
            return dataMap.get(
                    new RankScore.User(
                            uuid,
                            season
                    )
            );
        }
        RankScore data = MiarsStorage.getMySQLStorage().queryRankScore(uuid,season);

        //如果没有数据，就初始化玩家数据
        if (data==null){
            try {
                putRankScore(
                        new RankScore(
                                null,
                                uuid,
                                season,
                                100L
                        )
                );
                data = MiarsStorage.getMySQLStorage().queryRankScore(uuid,season);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        dataMap.put(
                new RankScore.User(
                        uuid,
                        season
                )
                ,data);
        return dataMap.get(
                new RankScore.User(
                        uuid,
                        season
                )
        );
    }

    public boolean checkRankScore(UUID uuid, Integer season){
        return MiarsStorage.getMySQLStorage().queryRankScore(uuid,season)!=null;
    }

    public void clearUserCacheData(UUID uuid, Integer season){
        dataMap.remove(
                new RankScore.User(
                        uuid,
                        season
                )
        );
    }

    public void scoreOperation(Player win,Player fail){
        RankScore winScore = getRankScore(win.getUniqueId(),1);
        RankScore failScore = getRankScore(fail.getUniqueId(),1);

        long difference = Math.abs(winScore.getScore() - failScore.getScore());

        long score = 20;

        if (difference>=500){
            if (winScore.getScore()>failScore.getScore()){
                score = (long) (score - (score*0.9));
            }else {
                score = (long) (score + (score*0.9));
            }
        }else if (difference>=300){
            if (winScore.getScore()>failScore.getScore()){
                score = (long) (score - (score*0.8));
            }else {
                score = (long) (score + (score*0.8));
            }
        }else if (difference>=150){
            if (winScore.getScore()>failScore.getScore()){
                score = (long) (score - (score*0.5));
            }else {
                score = (long) (score + (score*0.5));
            }
        }else if (difference>=100){
            if (winScore.getScore()>failScore.getScore()){
                score = (long) (score - (score*0.3));
            }else {
                score = (long) (score + (score*0.3));
            }
        }

        winScore.setScore(winScore.getScore()+score);
        win.sendMessage(ColorParser.parse("&a+"+score+" &7RANK SCORE."));
        failScore.setScore(failScore.getScore()-score);
        fail.sendMessage(ColorParser.parse("&c-"+score+" &7RANK SCORE."));

        putRankScore(winScore);
        putRankScore(failScore);
    }

    public void reload(){
        dataMap.clear();
    }
}
