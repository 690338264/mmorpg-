package com.database.entity;

import java.io.Serializable;

public class Bag implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column bag.volume
     *
     * @mbg.generated Thu Aug 06 17:05:08 CST 2020
     */
    private Integer volume;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column bag.item
     *
     * @mbg.generated Thu Aug 06 17:05:08 CST 2020
     */
    private String item;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column bag.playerid
     *
     * @mbg.generated Thu Aug 06 17:05:08 CST 2020
     */
    private Long playerid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table bag
     *
     * @mbg.generated Thu Aug 06 17:05:08 CST 2020
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column bag.volume
     *
     * @return the value of bag.volume
     *
     * @mbg.generated Thu Aug 06 17:05:08 CST 2020
     */
    public Integer getVolume() {
        return volume;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column bag.volume
     *
     * @param volume the value for bag.volume
     *
     * @mbg.generated Thu Aug 06 17:05:08 CST 2020
     */
    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column bag.item
     *
     * @return the value of bag.item
     *
     * @mbg.generated Thu Aug 06 17:05:08 CST 2020
     */
    public String getItem() {
        return item;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column bag.item
     *
     * @param item the value for bag.item
     *
     * @mbg.generated Thu Aug 06 17:05:08 CST 2020
     */
    public void setItem(String item) {
        this.item = item == null ? null : item.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column bag.playerid
     *
     * @return the value of bag.playerid
     *
     * @mbg.generated Thu Aug 06 17:05:08 CST 2020
     */
    public Long getPlayerid() {
        return playerid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column bag.playerid
     *
     * @param playerid the value for bag.playerid
     *
     * @mbg.generated Thu Aug 06 17:05:08 CST 2020
     */
    public void setPlayerid(Long playerid) {
        this.playerid = playerid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table bag
     *
     * @mbg.generated Thu Aug 06 17:05:08 CST 2020
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", volume=").append(volume);
        sb.append(", item=").append(item);
        sb.append(", playerid=").append(playerid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}