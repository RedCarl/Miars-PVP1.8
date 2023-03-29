//package cn.mcarl.miars.practiceffa.manager;
//
//import cn.mcarl.miars.storage.enums.practice.FKitType;
//import cn.mcarl.miars.storage.storage.data.practice.PracticeQueueDataStorage;
//import lombok.Data;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//public class DuelManager {
//
//    private static final DuelManager instance = new DuelManager();
//    public static DuelManager getInstance() {
//        return instance;
//    }
//
//    List<Duel> data = new ArrayList<>();
//
//    public boolean addDuel(Duel duel){
//        for (Duel d:data) {
//            if (d.isEqual(duel)){
//                return false;
//            }
//        }
//        data.add(duel);
//        PracticeQueueDataStorage.getInstance().addDuel()
//        return true;
//    }
//}
