package cn.mcarl.miars.storage.entity.practice;

import cn.mcarl.miars.storage.enums.practice.FKitType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Duel{
    private FKitType type;
    private String a;
    private String b;
    private Long time;
    private Integer state;

    public boolean isEqual(Duel duel){
        if (this.type == duel.getType()){
            if (Objects.equals(this.a, duel.getA())){
                return Objects.equals(this.b, duel.getB());
            }
        }
        return false;
    }
}