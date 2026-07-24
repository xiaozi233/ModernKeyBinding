package committee.nova.mkb.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import committee.nova.mkb.api.IKeyBinding;
import committee.nova.mkb.keybinding.IKeyConflictContext;
import committee.nova.mkb.keybinding.KeyConflictContext;
import committee.nova.mkb.keybinding.KeyModifier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;

@Mixin(GameOptions.class)
public abstract class MixinGameSettings {
    @Shadow
    public KeyBinding forwardKey;

    @Shadow
    public KeyBinding leftKey;

    @Shadow
    public KeyBinding backKey;

    @Shadow
    public KeyBinding rightKey;

    @Shadow
    public KeyBinding jumpKey;

    @Shadow
    public KeyBinding sneakKey;

    @Shadow
    public KeyBinding sprintKey;

    @Shadow
    public KeyBinding attackKey;

    @Shadow
    public KeyBinding chatKey;

    @Shadow
    public KeyBinding playerListKey;

    @Shadow
    public KeyBinding commandKey;

    @Shadow
    public KeyBinding togglePerspectiveKey;

    @Shadow
    public KeyBinding smoothCameraKey;

    @Inject(method = "<init>()V", at = @At("RETURN"))
    public void inject$init$1(CallbackInfo ci) {
        setKeyBindProperties();
    }

    @Inject(method = "<init>(Lnet/minecraft/client/Minecraft;Ljava/io/File;)V", at = @At("RETURN"))
    public void inject$init$2(Minecraft mc, File dir, CallbackInfo ci) {
        setKeyBindProperties();
    }

    @Redirect(method = "save", at = @At(value = "INVOKE", target = "Ljava/io/PrintWriter;println(Ljava/lang/String;)V", ordinal = 55))
    public void redirect$saveOptions$trap(PrintWriter instance, String s) {
        // Do nothing
    }

    @Inject(method = "save", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundCategory;values()[Lnet/minecraft/client/sound/SoundCategory;"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void inject$saveOptions(CallbackInfo ci, PrintWriter printwriter, KeyBinding[] keyBindings) {
        for (final KeyBinding binding : keyBindings) {
            final String x = "key_" + binding.getName() + ":" + binding.getKeyCode();
            final IKeyBinding mixined = (IKeyBinding) binding;
            printwriter.println(mixined.getKeyModifier() != KeyModifier.NONE ? x + ":" + mixined.getKeyModifier() : x);
        }
    }

    @Redirect(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/options/KeyBinding;setKeyCode(I)V"))
    public void redirect$loadOptions$trap(KeyBinding instance, int p_151462_1_) {
        // Do nothing
    }

    @Inject(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundCategory;values()[Lnet/minecraft/client/sound/SoundCategory;"))
    public void inject$loadOptions(CallbackInfo ci, @Local String[] aString, @Local KeyBinding[] aKeybinding, @Local(ordinal = 0) int i) {
        for (int j = 0; j < i; ++j) {
            final KeyBinding keybind = aKeybinding[j];

            if (aString[0].equals("key_" + keybind.getName())) {
                final String s2 = aString[1];
                final IKeyBinding mixined = (IKeyBinding) keybind;
                try {
                    if (aString.length > 2) {
                        mixined.setKeyModifierAndCode(KeyModifier.valueFromString(aString[2]), Integer.parseInt(s2));
                    } else mixined.setKeyModifierAndCode(KeyModifier.NONE, Integer.parseInt(s2));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Unique
	private void setKeyBindProperties() {
        setKeyConflictContext(forwardKey, leftKey, backKey, rightKey, jumpKey, sneakKey,
                sprintKey, attackKey, chatKey, playerListKey, commandKey, togglePerspectiveKey, smoothCameraKey);
    }

    @Unique
	private static void setKeyConflictContext(KeyBinding... keybindingArray) {
        final IKeyConflictContext inGame = KeyConflictContext.IN_GAME;
        for (final KeyBinding keyBinding : keybindingArray) {
            final IKeyBinding mixined = (IKeyBinding) keyBinding;
            mixined.setKeyConflictContext(inGame);
        }
    }
}
