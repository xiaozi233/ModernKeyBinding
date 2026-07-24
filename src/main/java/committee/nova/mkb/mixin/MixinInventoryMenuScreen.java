package committee.nova.mkb.mixin;

import committee.nova.mkb.util.Utilities;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.menu.InventoryMenuScreen;
import net.minecraft.client.options.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InventoryMenuScreen.class)
public abstract class MixinInventoryMenuScreen extends Screen {
    @Redirect(method = "keyPressed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/options/KeyBinding;getKeyCode()I"))
    public int redirect$keyTyped(KeyBinding instance) {
        final int original = instance.getKeyCode();
        return Utilities.isActiveIgnoreKeyCode(instance) ? original : original + Integer.MIN_VALUE;
    }

    @Redirect(method = "moveHoveredSlotToHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/options/KeyBinding;getKeyCode()I"))
    public int redirect$checkHotbarKeys(KeyBinding instance) {
        final int original = instance.getKeyCode();
        return Utilities.isActiveIgnoreKeyCode(instance) ? original : original + Integer.MIN_VALUE;
    }

    @Redirect(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/options/KeyBinding;getKeyCode()I"))
    public int redirect$mouseClicked(KeyBinding instance) {
        final int original = instance.getKeyCode();
        return Utilities.isActiveIgnoreKeyCode(instance) ? original : original + Integer.MIN_VALUE;
    }
}
