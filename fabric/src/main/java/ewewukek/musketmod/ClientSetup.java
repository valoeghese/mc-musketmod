package ewewukek.musketmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class ClientSetup implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(MusketMod.BULLET_ENTITY_TYPE, (ctx) -> new BulletRenderer(ctx));

        ClampedItemPropertyFunction loaded = (stack, world, player, seed) -> GunItem.isLoaded(stack) ? 1 : 0;
        ItemProperties.register(Items.MUSKET, new ResourceLocation("loaded"), loaded);
        ItemProperties.register(Items.MUSKET_WITH_BAYONET, new ResourceLocation("loaded"), loaded);
        ItemProperties.register(Items.PISTOL, new ResourceLocation("loaded"), loaded);

        ClientPlayNetworking.registerGlobalReceiver(MusketMod.SMOKE_EFFECT_PACKET_ID, (client, handler, buf, responseSender) -> {
            ClientLevel world = handler.getLevel();
            Vec3 origin = new Vec3(buf.readFloat(), buf.readFloat(), buf.readFloat());
            Vec3 direction = new Vec3(buf.readFloat(), buf.readFloat(), buf.readFloat());
            GunItem.fireParticles(world, origin, direction);
        });
    }
}
