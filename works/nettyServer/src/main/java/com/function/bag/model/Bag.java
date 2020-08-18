package com.function.bag.model;

import com.function.item.model.Item;
import com.jpa.entity.TBag;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Catherine
 * @create 2020-08-04 10:44
 */
@Data

public class Bag {
    private TBag tBag;
    private Map<Integer, Item> itemMap = new HashMap<>();

}
