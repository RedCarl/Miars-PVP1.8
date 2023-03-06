package cn.mcarl.miars.megawalls.classes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Listener;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class Classes implements Listener {
    private String name;
    private String displayName;
    protected ChatColor nameColor;
    private Material iconType;
    private byte iconData;
    private ClassesType classesType;
    private ClassesInfo.Orientation[] orientations;
    private ClassesInfo.Difficulty difficulty;
    private EquipmentPackage equipmentPackage;
    protected Skill mainSkill;
    protected Skill secondSkill;
    protected Skill thirdSkill;
    protected CollectSkill collectSkill;
    private ClassesSkin defaultSkin;

    public Classes(String name, String displayName, ChatColor nameColor, Material iconType, byte iconData, ClassesType classesType, ClassesInfo.Orientation[] orientations, ClassesInfo.Difficulty difficulty) {
        this.name = name;
        this.displayName = displayName;
        this.nameColor = nameColor;
        this.iconType = iconType;
        this.iconData = iconData;
        this.classesType = classesType;
        this.orientations = orientations;
        this.difficulty = difficulty;
    }

    public void setDefaultSkin(String value, String signature) {
        this.defaultSkin = new ClassesSkin("Default", this.getDisplayName(), List.of("&7" + this.getDisplayName() + "的默认皮肤"), value, signature);
    }

    public int energyMelee() {
        return 0;
    }

    public int energyBow() {
        return 0;
    }

    public abstract List<String> getInfo();

    public abstract int unlockCost();

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public ChatColor getNameColor() {
        return this.nameColor;
    }

    public Material getIconType() {
        return this.iconType;
    }

    public byte getIconData() {
        return this.iconData;
    }

    public ClassesType getClassesType() {
        return this.classesType;
    }

    public ClassesInfo.Orientation[] getOrientations() {
        return this.orientations;
    }

    public ClassesInfo.Difficulty getDifficulty() {
        return this.difficulty;
    }

    public EquipmentPackage getEquipmentPackage() {
        return this.equipmentPackage;
    }

    public void setEquipmentPackage(EquipmentPackage equipmentPackage) {
        this.equipmentPackage = equipmentPackage;
    }

    public Skill getMainSkill() {
        return this.mainSkill;
    }

    public void setMainSkill(Skill mainSkill) {
        this.mainSkill = mainSkill;
    }

    public Skill getSecondSkill() {
        return this.secondSkill;
    }

    public void setSecondSkill(Skill secondSkill) {
        this.secondSkill = secondSkill;
    }

    public Skill getThirdSkill() {
        return this.thirdSkill;
    }

    public void setThirdSkill(Skill thirdSkill) {
        this.thirdSkill = thirdSkill;
    }

    public CollectSkill getCollectSkill() {
        return this.collectSkill;
    }

    public void setCollectSkill(CollectSkill collectSkill) {
        this.collectSkill = collectSkill;
    }

    public ClassesSkin getDefaultSkin() {
        return this.defaultSkin;
    }
}
