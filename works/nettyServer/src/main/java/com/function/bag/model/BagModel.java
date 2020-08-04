package com.function.bag.model;

import com.database.entity.Bag;
import com.function.item.model.Item;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Catherine
 * @create 2020-08-04 10:44
 */
@Data

public class BagModel extends Bag {

    private Map<Integer, Item> itemMap = new HashMap<>();

}
