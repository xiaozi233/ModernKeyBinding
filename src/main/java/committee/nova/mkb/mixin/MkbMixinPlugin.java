package committee.nova.mkb.mixin;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import net.fabricmc.loader.api.FabricLoader;

import java.util.List;
import java.util.Set;

public class MkbMixinPlugin implements IMixinConfigPlugin {

	private boolean is1_8_9 = true;

	@Override
	public void onLoad(String mixinPackage) {
		try {
			String version = FabricLoader.getInstance().getModContainer("minecraft")
				.get().getMetadata().getVersion().getFriendlyString();

			if (version.equals("1.8") || version.equals("1.8.1")) {
				is1_8_9 = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if (mixinClassName.endsWith("MixinKeyEntry")) {
			return !is1_8_9;
		}
		if (mixinClassName.contains("MixinControlsListWidget")) {
			return is1_8_9;
		}

		return true;
	}

	// --- 其他必须实现的方法 ---
	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}
}
