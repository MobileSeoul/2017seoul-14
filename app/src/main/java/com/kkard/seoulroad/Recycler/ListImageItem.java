package com.kkard.seoulroad.Recycler;

import java.io.Serializable;

/**
 * Created by SuGeun on 2017-10-03.
 */

public class ListImageItem implements Serializable {

    int itemImage;

    /*
     * SETTER
     */
    public void setItemImage(int itemImage) {
        this.itemImage = itemImage;
    }

    /*
     * GETTER
     */
    public int getItemImage() {
        return itemImage;
    }

}
