package gg.noob.lib.tab.entry;

import gg.noob.lib.tab.skin.SkinType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class TabEntry {

    private final int column;
    private final int row;
    private final String text;
    private final int ping;

    private String[] skinData = SkinType.DARK_GRAY.getSkinData();

    /**
     * Constructor to make a new tab entry object with provided skin data
     *
     * @param column        the x axis
     * @param row        the y axis
     * @param text     the text to display on the slot
     * @param ping     the displayed latency
     * @param skinData the data to display in the skin slot
     */
    public TabEntry(int column, int row, String text, int ping, String[] skinData) {
        this.column = column;
        this.row = row;
        this.text = text;
        this.ping = ping;
        this.skinData = skinData;
    }
}