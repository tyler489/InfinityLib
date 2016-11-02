package com.infinityraider.infinitylib.modules.dualwield;

import com.infinityraider.infinitylib.InfinityLib;
import com.infinityraider.infinitylib.network.MessageBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageMouseButtonPressed extends MessageBase<MessageSwingArm> {
    private boolean left;
    private boolean shift;
    private boolean ctrl;

    @SuppressWarnings("unused")
    public MessageMouseButtonPressed() {}

    public MessageMouseButtonPressed(boolean left, boolean shift, boolean ctrl) {
        this.left = left;
        this.shift = shift;
        this.ctrl = ctrl;
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.SERVER;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        EntityPlayer player = ctx.getServerHandler().playerEntity;
        ItemStack stack = player.getHeldItem(left ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
        if (stack != null && stack.getItem() instanceof IDualWieldedWeapon) {
            IDualWieldedWeapon weapon = (IDualWieldedWeapon) stack.getItem();
            weapon.onItemUsed(stack, player, shift, ctrl, left ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
        }
    }

    @Override
    protected MessageSwingArm getReply(MessageContext ctx) {
        if(ctx.side == Side.SERVER) {
            InfinityLib.instance.getNetworkWrapper().sendToAll(new MessageSwingArm(ctx.getServerHandler().playerEntity, left ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND));
        }
        return null;
    }
}
