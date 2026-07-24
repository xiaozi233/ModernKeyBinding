package committee.nova.mkb.api;

import committee.nova.mkb.keybinding.IKeyConflictContext;
import committee.nova.mkb.keybinding.KeyModifier;
import net.minecraft.client.options.KeyBinding;
import net.ornithemc.osl.keybinds.api.KeybindRegistry;

@SuppressWarnings("unused")
class KeyBindingRegistryImpl implements KeyBindingRegistry {
    @Override
    public KeyBinding createKeyBinding(String description, IKeyConflictContext ctx, KeyModifier modifier, int keyCode, String category) {
        final KeyBinding keyBinding = new KeyBinding(description, keyCode, category);
        final IKeyBinding mixined = (IKeyBinding) keyBinding;
        mixined.setKeyConflictContext(ctx);
        mixined.setInitialKeyModifierAndCode(modifier, keyCode);
        return (KeyBinding) mixined;
    }

    @Override
    public KeyBinding registerKeyBinding(String description, IKeyConflictContext ctx, KeyModifier modifier, int keyCode, String category) {
        final KeyBinding keyBinding = createKeyBinding(description, ctx, modifier, keyCode, category);
		KeybindRegistry.register(keyBinding);
        return keyBinding;
    }
}
