package committee.nova.mkb.mixin;

import committee.nova.mkb.util.Utilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.options.ControlsOptionsScreen;
import net.minecraft.client.options.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
	@Shadow
	@Final
	public static boolean IS_MAC;

	@Shadow
	public Screen screen;

	@Shadow
	public static long getTime() {
		throw new RuntimeException("Mixin application failed!");
	}

	@Inject(method = "lockMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Mouse;lock()V"))
	public void inject$setIngameFocus(CallbackInfo ci) {
		if (!IS_MAC) Utilities.updateKeyBindState();
	}

	@Redirect(method = "handleGuiKeyBindings", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/options/KeyBinding;getKeyCode()I"))
	public int redirect$handleKeyInput$1(KeyBinding instance) {
		final int original = instance.getKeyCode();
		return Utilities.isActiveIgnoreKeyCode(instance) ? original : original + Integer.MIN_VALUE;
	}

	@Inject(method = "handleGuiKeyBindings", at = @At("TAIL"))
	public void inject$handleKeyInput(CallbackInfo ci) {
		if (Keyboard.getEventKey() == 0 || Keyboard.isRepeatEvent()) return;
		if (!(screen instanceof ControlsOptionsScreen)) return;
		final ControlsOptionsScreen controls = (ControlsOptionsScreen) screen;
		if (getTime() - controls.time < 20L) return;
		if (!Keyboard.getEventKeyState()) controls.selectedKeyBinding = null;
	}
}
