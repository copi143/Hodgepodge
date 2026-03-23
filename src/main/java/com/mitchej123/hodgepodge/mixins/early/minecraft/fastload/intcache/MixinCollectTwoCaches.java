package com.mitchej123.hodgepodge.mixins.early.minecraft.fastload.intcache;

import static com.mitchej123.hodgepodge.hax.FastIntCache.releaseCache;

import net.minecraft.world.gen.layer.GenLayerHills;
import net.minecraft.world.gen.layer.GenLayerRiverMix;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.GenLayerZoom;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;

@Mixin({ GenLayerHills.class, GenLayerRiverMix.class, GenLayerVoronoiZoom.class, GenLayerZoom.class })
public class MixinCollectTwoCaches {

    @Inject(method = "getInts", at = @At(value = "RETURN"))
    private void hodgepodge$collectInts(int areaX, int areaY, int areaWidth, int areaHeight,
            CallbackInfoReturnable<int[]> cir, @Local(ordinal = 0) int[] ints, @Local(ordinal = 1) int[] ints2) {
        // Guard against releasing the return value: in obfuscated (production) bytecode without an LVT,
        // ProGuard may reuse an input/intermediate slot for the output array, causing @Local(ordinal=N)
        // to capture the return value. Releasing it while the caller still holds a reference causes
        // use-after-free / biome-ID corruption (e.g. Ross128b crash).
        if (ints != cir.getReturnValue()) releaseCache(ints);
        if (ints2 != cir.getReturnValue()) releaseCache(ints2);
    }
}
