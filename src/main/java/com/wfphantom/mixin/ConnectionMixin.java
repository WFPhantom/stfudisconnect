package com.wfphantom.mixin;

import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.PacketFlow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Connection.class)
public abstract class ConnectionMixin {

    @Shadow
    private PacketListener packetListener;

    @Shadow
    public abstract PacketFlow getSending();

    @Redirect(method = "exceptionCaught", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Connection;getSending()Lnet/minecraft/network/protocol/PacketFlow;"))
    private PacketFlow shutthefuckup(Connection instance) {
        PacketListener listener = this.packetListener;
        if (listener != null) {
            ConnectionProtocol protocol = listener.protocol();
            if (protocol == ConnectionProtocol.STATUS || protocol == ConnectionProtocol.HANDSHAKING) return PacketFlow.SERVERBOUND;
        }
        return this.getSending();
    }
}