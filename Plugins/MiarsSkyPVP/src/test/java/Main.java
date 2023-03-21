import cn.mcarl.miars.core.utils.MiarsUtil;

public class Main {
    public static void main(String[] args) {
        getLevel();
    }


    public static Integer getLevel(){
        int level = 0;

        int exp = 17431;
        int next = 0;

        while (exp>=next){
            System.out.println(next);
            exp=exp - next;
            level++;
            next = level*200;
        }

        System.out.println(exp+"/"+next);

        System.out.println();
        return level;
    }
}
