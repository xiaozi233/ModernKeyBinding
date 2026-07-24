package committee.nova.mkb.mixin;

import net.minecraft.text.Formatting;
import committee.nova.mkb.api.IKeyBinding;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.options.ControlsListWidget;
import net.minecraft.client.gui.screen.options.ControlsOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.KeyBinding;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ControlsListWidget.KeyBindingEntry.class})
public abstract class MixinKeyEntry {
    @Shadow
    @Final
    private ButtonWidget resetButton;

    @Shadow
    @Final
    private KeyBinding keyBinding;

    @Shadow
    @Final
    private ButtonWidget keyBindingButton;

    @Shadow(aliases = {"field_7805", "this$0"})
    private ControlsListWidget outer;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;render(Lnet/minecraft/client/Minecraft;II)V", ordinal = 0))
    public void inject$drawEntry$1(int index, int x, int y, int rowWidth, int rowHeight, int mouseX, int mouseY, boolean hovered, CallbackInfo ci) {
        resetButton.active = !((IKeyBinding) keyBinding).isSetToDefaultValue();
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ButtonWidget;render(Lnet/minecraft/client/Minecraft;II)V", ordinal = 1))
    public void inject$drawEntry$2(int index, int x, int y, int rowWidth, int rowHeight, int mouseX, int mouseY, boolean hovered, CallbackInfo ci) {
        keyBindingButton.message = ((IKeyBinding) keyBinding).getDisplayName();
        boolean conflicted = false;
        boolean keyCodeModifierConflict = true; // less severe form of conflict, like SHIFT conflicting with SHIFT+G

        final ControlsOptionsScreen controls = ((AccessorControlsListWidget) outer).getParent();
        final Minecraft mc = ((AccessorScreen) controls).getMinecraft();

        if (this.keyBinding.getKeyCode() != 0) {
            for (KeyBinding keybinding : mc.options.keyBindings) {
                final IKeyBinding mixined = (IKeyBinding) keybinding;
                if (keybinding != this.keyBinding && mixined.conflicts(this.keyBinding)) {
                    conflicted = true;
                    keyCodeModifierConflict &= mixined.hasKeyCodeModifierConflict(this.keyBinding);
                }
            }
        }

        if (controls.selectedKeyBinding == this.keyBinding) {
            this.keyBindingButton.message = Formatting.WHITE + "> " + Formatting.YELLOW + this.keyBindingButton.message + Formatting.WHITE + " <";
        } else if (conflicted) {
            this.keyBindingButton.message = (keyCodeModifierConflict ? Formatting.GOLD : Formatting.RED) + this.keyBindingButton.message;
        }
    }

    @Inject(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/options/GameOptions;setKeyCode(Lnet/minecraft/client/options/KeyBinding;I)V"))
    public void inject$mousePressed(int index, int mouseX, int mouseY, int button, int x, int y, CallbackInfoReturnable<Boolean> cir) {
        ((IKeyBinding) keyBinding).setToDefault();
    }
}
