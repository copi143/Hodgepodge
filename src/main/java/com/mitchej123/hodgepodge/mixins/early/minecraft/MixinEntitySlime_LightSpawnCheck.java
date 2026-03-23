package com.mitchej123.hodgepodge.mixins.early.minecraft;

import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mitchej123.hodgepodge.config.TweaksConfig;

@Mixin(EntitySlime.class)
public abstract class MixinEntitySlime_LightSpawnCheck {

    @Shadow
    public World worldObj;

    @Shadow
    public double posX;

    @Shadow
    public double posY;

    @Shadow
    public double posZ;

    @ModifyReturnValue(method = "getCanSpawnHere", at = @At("RETURN"))
    private boolean hodgepodge$checkLightLevel(boolean original) {
        if (!original || !TweaksConfig.suppressSlimeSpawnWithLight) {
            return original;
        }
        int x = MathHelper.floor_double(posX);
        int y = MathHelper.floor_double(posY);
        int z = MathHelper.floor_double(posZ);
        return worldObj.getBlockLightValue(x, y, z) <= 7;
    }
}
