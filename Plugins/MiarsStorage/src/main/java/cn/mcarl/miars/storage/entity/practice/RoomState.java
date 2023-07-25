package cn.mcarl.miars.storage.entity.practice;

import cn.mcarl.miars.storage.entity.ffa.FInventoryByte;
import cn.mcarl.miars.storage.entity.practice.enums.practice.FKitType;
import cn.mcarl.miars.storage.entity.practice.enums.practice.QueueType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomState {

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

    private String playerA;

    private String playerB;

    // 对局开始时间
    private Long startTime;

    // 对局结束时间
    private Long endTime;

    // 游戏类型
    private FKitType fKitType;

    // 匹配类型
    private QueueType queueType;


    public void init(){
        this.state = State.IDLE;
        this.playerA = null;
        this.playerB = null;
        this.startTime = null;
        this.endTime = null;
        this.fKitType = null;
        this.queueType = null;
    }
}
