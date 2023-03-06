package cn.mcarl.miars.megawalls.classes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassesSkin {
   private String name;
   private String displayName;
   private List<String> info;
   private String value;
   private String signature;
}
