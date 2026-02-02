package org.nmascrie.swingy.enums;

/**
 * Type of items.
 */
public enum ItemType {
    WEAPON("weapon"),
    HELM("helmet"),
    ARMOR("body armor"),
    BOOTS("boots"),
    AMMOS("ammo pack"),
    BELT("belt"),
    RUBBISH("item");

    public final String desc;

    private ItemType(String desc) {
        this.desc = desc;
    }
}