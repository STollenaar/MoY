package moy.tollenaar.stephen.Util;


import java.util.Random;

import net.minecraft.server.v1_11_R1.EnchantmentManager;
import net.minecraft.server.v1_11_R1.ItemStack;
import net.minecraft.server.v1_11_R1.WeightedRandom.WeightedRandomChoice;

public class PossibleFishingResult extends WeightedRandomChoice {

    private final ItemStack b;
    private float c;
    private boolean d;

    public PossibleFishingResult(ItemStack itemstack, int i) {
        super(i);
        this.b = itemstack;
    }

    public ItemStack a(Random random) {
        ItemStack itemstack = this.b.cloneItemStack();

        if (this.c > 0.0F) {
            int i = (int) (this.c * (float) this.b.k());
            int j = itemstack.k() - random.nextInt(random.nextInt(i) + 1);

            if (j > i) {
                j = i;
            }

            if (j < 1) {
                j = 1;
            }

            itemstack.setData(j);
        }

        if (this.d) {
            EnchantmentManager.a(random, itemstack, 30,false);
        }

        return itemstack;
    }

    public PossibleFishingResult a(float f) {
        this.c = f;
        return this;
    }

    public PossibleFishingResult a() {
        this.d = true;
        return this;
    }
}
