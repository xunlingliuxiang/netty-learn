package com.cosco.chat.constant;

import com.cosco.chat.session.Session;
import io.netty.util.AttributeKey;

public interface Attributes {
    AttributeKey<Session> SESSION = AttributeKey.newInstance("SESSION");
}
