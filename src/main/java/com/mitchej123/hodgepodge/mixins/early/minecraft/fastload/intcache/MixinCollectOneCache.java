package com.mitchej123.hodgepodge.mixins.early.minecraft.fastload.intcache;

import static com.mitchej123.hodgepodge.hax.FastIntCache.releaseCache;

import net.minecraft.world.gen.layer.GenLayerAddIsland;
import net.minecraft.world.gen.layer.GenLayerAddMushroomIsland;
import net.minecraft.world.gen.layer.GenLayerAddSnow;
import net.minecraft.world.gen.layer.GenLayerBiome;
import net.minecraft.world.gen.layer.GenLayerDeepOcean;
import net.minecraft.world.gen.layer.GenLayerRareBiome;
import net.minecraft.world.gen.layer.GenLayerRemoveTooMuchOcean;
import net.minecraft.world.gen.layer.GenLayerRiver;
import net.minecraft.world.gen.layer.GenLayerRiverInit;
import net.minecraft.world.gen.layer.GenLayerShore;
import net.minecraft.world.gen.layer.GenLayerSmooth;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;

@Mixin({ GenLayerAddIsland.class, GenLayerAddMushroomIsland.class, GenLayerAddSnow.class, GenLayerBiome.class,
        GenLayerDeepOcean.class, GenLayerRareBiome.class, GenLayerRemoveTooMuchOcean.class, GenLayerRiver.class,
        GenLayerRiverInit.class, GenLayerShore.class, GenLayerSmooth.class })
public class MixinCollectOneCache {

    @Inject(method = { "getInts" }, at = @At(value = "RETURN"))
    private void hodgepodge$collectInts(int areaX, int areaY, int areaWidth, int areaHeight,
            CallbackInfoReturnable<int[]> cir, @Local(ordinal = 0) int[] ints) {
        // Guard against releasing the return value: in obfuscated (production) bytecode without an LVT,
        // ProGuard may reuse the parent-input slot for the output array, causing @Local(ordinal=0) to
        // capture the return value instead of the parent input. Releasing the return value while the
        // caller still holds a reference causes use-after-free / biome-ID corruption (Ross128b crash).
        if (ints != cir.getReturnValue()) releaseCache(ints);
    }
}
