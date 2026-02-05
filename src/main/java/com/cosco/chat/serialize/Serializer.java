package com.cosco.chat.serialize;

import com.cosco.chat.serialize.impl.JsonSerializer;

public interface Serializer {
    /**
     * 序列化算法
     * @return
     */
    byte getSerializerAlgorithm();

    /**
     * 序列化
     * @param object
     * @return
     */
    byte[] serialize(Object object);

    /**
     * 二进制数据转换成Java对象
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);

    Serializer DEFAULT = new JsonSerializer();
}
