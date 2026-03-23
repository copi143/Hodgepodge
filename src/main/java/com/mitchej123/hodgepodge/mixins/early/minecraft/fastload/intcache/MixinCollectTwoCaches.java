package com.mitchej123.hodgepodge.mixins.early.minecraft.fastload.intcache;

import static com.mitchej123.hodgepodge.hax.FastIntCache.releaseCache;

import net.minecraft.world.gen.layer.GenLayerHills;
import net.minecraft.world.gen.layer.GenLayerRiverMix;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;

@Mixin({ GenLayerHills.class, GenLayerRiverMix.class })
public class MixinCollectTwoCaches {

    @Inject(method = "getInts", at = @At(value = "RETURN"))
    private void hodgepodge$collectInts(int areaX, int areaY, int areaWidth, int areaHeight,
            CallbackInfoReturnable<int[]> cir, @Local(ordinal = 0) int[] ints, @Local(ordinal = 1) int[] ints2) {
        // GenLayerHills / GenLayerRiverMix each have TWO parent inputs (ordinal 0, ordinal 1) and
        // allocate their output array at ordinal 2 (the return value). Releasing ordinals 0 and 1
        // here is correct; ordinal 2 is never captured so the return value is safe.
        // Safety guards in case of unexpected bytecode layout.
        if (ints != cir.getReturnValue()) releaseCache(ints);
        if (ints2 != cir.getReturnValue()) releaseCache(ints2);
    }
}
