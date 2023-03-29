package cn.mcarl.miars.storage.entity.practice;

import cn.mcarl.miars.storage.entity.ffa.FInventoryByte;
import cn.mcarl.miars.storage.enums.practice.FKitType;
import cn.mcarl.miars.storage.enums.practice.QueueType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArenaState {

    // 状态ID
    private Integer id;

    // 房间ID
    private Integer arenaId;

    // 房间世界
    private String world;

    // 0 空闲 | 1 正在分配 | 2 正在战斗 | 3 战斗结束 | 4 房间故障
    private State state;
    public enum State{
        IDLE,
        READY,
        GAME,
        END,
        ERROR
    }

    // 玩家
    private String playerA;
    private PlayerState playerStateA;
    // 玩家的库存
    private FInventoryByte aFInventory;
    // 玩家
    private String playerB;
    private PlayerState playerStateB;
    // 玩家的库存
    private FInventoryByte bFInventory;

    // 对局开始时间
    private Long startTime;

    // 对局结束时间
    private Long endTime;

    // 获胜方
    private String win;

    // 游戏类型
    private FKitType fKitType;

    // 匹配类型
    private QueueType queueType;

    public ArenaState(Integer arenaId,String world,State state){
        this.arenaId = arenaId;
        this.world = world;
        this.state = state;
        this.startTime = 0L;
        this.endTime = 0L;
    }

    public void init(){
        this.state = State.IDLE;
        this.playerA = null;
        this.playerStateA = null;
        this.aFInventory = null;
        this.playerB = null;
        this.playerStateB = null;
        this.bFInventory = null;
        this.startTime = null;
        this.endTime = null;
        this.win = null;
        this.fKitType = null;
        this.queueType = null;
    }
}
