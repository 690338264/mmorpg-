package com.event;

import com.function.quest.model.QuestType;

/**
 * @author Catherine
 * @create 2020-09-10 19:33
 */
public abstract class QuestEvent {
    /**
     * 获取任务类
     *
     * @return 任务类
     */
    public abstract QuestType getType();
}
