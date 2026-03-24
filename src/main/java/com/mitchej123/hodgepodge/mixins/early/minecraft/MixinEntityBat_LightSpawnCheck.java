package com.mitchej123.hodgepodge.mixins.early.minecraft;

import net.minecraft.entity.passive.EntityBat;
import net.minecraft.util.MathHelper;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mitchej123.hodgepodge.config.TweaksConfig;

@Mixin(EntityBat.class)
public abstract class MixinEntityBat_LightSpawnCheck {

    @ModifyReturnValue(method = "getCanSpawnHere", at = @At("RETURN"))
    private boolean hodgepodge$checkLightLevel(boolean original) {
        if (!original || !TweaksConfig.suppressBatSpawnWithLight) {
            return original;
        }
        EntityBat bat = (EntityBat) (Object) this;
        int x = MathHelper.floor_double(bat.posX);
        int y = MathHelper.floor_double(bat.posY);
        int z = MathHelper.floor_double(bat.posZ);
        return bat.worldObj.getBlockLightValue(x, y, z) <= 7;
    }
}
