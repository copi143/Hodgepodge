package com.mitchej123.hodgepodge.mixins.early.minecraft;

import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.util.MathHelper;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mitchej123.hodgepodge.config.TweaksConfig;

@Mixin(EntitySlime.class)
public abstract class MixinEntitySlime_LightSpawnCheck {

    @ModifyReturnValue(method = "getCanSpawnHere", at = @At("RETURN"))
    private boolean hodgepodge$checkLightLevel(boolean original) {
        if (!original || !TweaksConfig.suppressSlimeSpawnWithLight) {
            return original;
        }
        EntitySlime slime = (EntitySlime) (Object) this;
        int x = MathHelper.floor_double(slime.posX);
        int y = MathHelper.floor_double(slime.posY);
        int z = MathHelper.floor_double(slime.posZ);
        return slime.worldObj.getBlockLightValue(x, y, z) <= 7;
    }
}
