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
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.GenLayerZoom;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;

@Mixin({ GenLayerAddIsland.class, GenLayerAddMushroomIsland.class, GenLayerAddSnow.class, GenLayerBiome.class,
        GenLayerDeepOcean.class, GenLayerRareBiome.class, GenLayerRemoveTooMuchOcean.class, GenLayerRiver.class,
        GenLayerRiverInit.class, GenLayerShore.class, GenLayerSmooth.class, GenLayerVoronoiZoom.class,
        GenLayerZoom.class })
public class MixinCollectOneCache {

    @Inject(method = { "getInts" }, at = @At(value = "RETURN"))
    private void hodgepodge$collectInts(int areaX, int areaY, int areaWidth, int areaHeight,
            CallbackInfoReturnable<int[]> cir, @Local(ordinal = 0) int[] ints) {
        // All classes mixed here have exactly ONE parent-input array (ordinal 0 = aint).
        // The output array (ordinal 1 = aint2) is the return value and is intentionally
        // NOT captured by this mixin - it must not be freed here because the caller still
        // holds a reference to it. The guard below is a safety net against any unexpected
        // bytecode layout where ordinal 0 might resolve to the return value slot.
        if (ints != cir.getReturnValue()) releaseCache(ints);
    }
}
