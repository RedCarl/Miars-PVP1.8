package cn.mcarl.miars.storage.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Award {
    /**编号*/
    public String id;
    /**概率（0.1代表10%，最多3位小数，即千分之一级）*/
    public float probability;
    /**数量（该类奖品剩余数量）*/
    public int count;

    public Award(String id, float probability, int count) {
        super();
        this.id = id;
        this.probability = probability;
        this.count = count;
    }

    public static Award lottery(List<Award> awards){
        //总的概率区间
        float totalPro = 0f;
        //存储每个奖品新的概率区间
        List<Float> proSection = new ArrayList<Float>();
        proSection.add(0f);
        //遍历每个奖品，设置概率区间，总的概率区间为每个概率区间的总和
        for (Award award : awards) {
            //每个概率区间为奖品概率乘以1000（把三位小数转换为整）再乘以剩余奖品数量
            totalPro += award.probability * 10 * award.count;
            proSection.add(totalPro);
        }
        //获取总的概率区间中的随机数
        Random random = new Random();
        float randomPro = (float)random.nextInt((int)totalPro);
        //判断取到的随机数在哪个奖品的概率区间中
        for (int i = 0,size = proSection.size(); i < size; i++) {
            if(randomPro >= proSection.get(i)
                    && randomPro < proSection.get(i + 1)){
                return awards.get(i);
            }
        }
        return null;
    }
}