package cn.mcarl.miars.megawalls.classes;

public enum ClassesType {
   STATER("起始", 0),
   NOVICE("新手", 1),
   NORMAL("普通", 2),
   MYTHIC("神话", 3);

   private final String name;
   private final int priority;

   private ClassesType(String name, int priority) {
      this.name = name;
      this.priority = priority;
   }

   public String getName() {
      return this.name;
   }

   public int getPriority() {
      return this.priority;
   }
}
