package com.flansmod.common.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface INetworkElement {

	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data);

	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data);

}
