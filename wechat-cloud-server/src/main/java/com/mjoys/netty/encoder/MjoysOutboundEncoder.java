package com.mjoys.netty.encoder;

import com.mjoys.protocol.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

public class MjoysOutboundEncoder extends MessageToByteEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        if(null == msg){
            throw new Exception("msg is null");
        }
        String body = msg.getBody();
        byte[] bodyBytes = body.getBytes(Charset.forName("UTF-8"));
        out.writeInt(msg.getFlag());
        out.writeInt(msg.getType());
        out.writeInt(bodyBytes.length);
        out.writeBytes(bodyBytes);
    }
}