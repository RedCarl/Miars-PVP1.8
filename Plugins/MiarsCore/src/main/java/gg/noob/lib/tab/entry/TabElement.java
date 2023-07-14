package gg.noob.lib.tab.entry;

import gg.noob.lib.tab.skin.SkinType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TabElement {

    private final List<TabEntry> entries = new ArrayList<>();

    private List<String> header = new ArrayList<>();
    private List<String> footer = new ArrayList<>();

    /**
     * Get an entry by location in the tab element
     *
     * @param column 	the column axis
     * @param row 		the row axis
     * @return the entry
     */
    public TabEntry getEntry(int column, int row) {
        return this.entries.stream()
                .filter(entry -> entry.getColumn() == column && entry.getRow() == row)
                .findFirst().orElseGet(() -> new TabEntry(column, row, "", -1));
    }

    /**
     * Add a new entry to the element
     *
     * @param index the index to get the axiss from
     * @param text  the text to display on the slot
     */
    public void add(int index, String text) {
        this.add(index, text, -1);
    }

    /**
     * Add a new entry to the element
     *
     * @param column    the column axis
     * @param row    	the row axis
     * @param text 		the text to display on the slot
     */
    public void add(int column, int row, String text) {
        this.add(column, row, text, -1);
    }

    /**
     * Add a new entry to the element
     *
     * @param index the index to get the axiss from
     * @param text  the text to display on the slot
     * @param ping  the ping to display
     */
    public void add(int index, String text, int ping) {
        this.add(index % 4, index / 4, text, ping);
    }

    /**
     * Add a new entry to the element
     *
     * @param column    the x axis
     * @param row    	the y axis
     * @param text 		the text to display on the slot
     * @param ping		the ping to display
     */
    public void add(int column, int row, String text, int ping) {
        this.entries.add(new TabEntry(column, row, text, ping));
    }

    /**
     * Add a new entry to the element
     *
     * @param index the index to get the axiss from
     * @param text     the text to display on the slot
     * @param ping     the ping to display
     * @param skinData the data to display in the skin slot
     */
    public void add(int index, String text, int ping, String[] skinData) {
        this.add(index % 4, index / 4, text, ping, skinData);
    }

    /**
     * Add a new entry to the element
     *
     * @param column        the column axis
     * @param row        	the row axis
     * @param text     		the text to display on the slot
     * @param ping     		the ping to display
     * @param skinData 		the data to display in the skin slot
     */
    public void add(int column, int row, String text, int ping, String[] skinData) {
        this.entries.add(new TabEntry(column, row, text, ping, skinData));
    }

    /**
     * Add a new entry to the element
     *
     * @param index the index to get the axiss from
     * @param text     the text to display on the slot
     * @param ping     the ping to display
     * @param skinType the data to display in the skin slot
     */
    public void add(int index, String text, int ping, SkinType skinType) {
        this.add(index, text, ping, skinType.getSkinData());
    }

    /**
     * Add a new entry to the element
     *
     * @param column        the column axis
     * @param row        	the row axis
     * @param text     		the text to display on the slot
     * @param ping     		the ping to display
     * @param skinType 		the data to display in the skin slot
     */
    public void add(int column, int row, String text, int ping, SkinType skinType) {
        this.add(column, row, text, ping, skinType.getSkinData());
    }
}