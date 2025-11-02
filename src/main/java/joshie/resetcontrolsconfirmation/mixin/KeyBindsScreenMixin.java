package joshie.resetcontrolsconfirmation.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

#if mc == 201
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
#else
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
#endif

@Mixin(KeyBindsScreen.class)
public abstract class KeyBindsScreenMixin {
    @ModifyArg(
            #if mc == 201
            method = "init",
            #elif mc >= 211
            method = "addFooter",
            #endif
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/Button;builder(Lnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/Button$OnPress;)Lnet/minecraft/client/gui/components/Button$Builder;",
                    ordinal = 0
            ),
            index = 1
    )
    private Button.OnPress rcc$wrapResetPress(Button.OnPress original) {
        return btn -> {
            Minecraft mc = Minecraft.getInstance();
            var parent = (KeyBindsScreen) (Object) this;

            mc.setScreen(new ConfirmScreen(yes -> {
                mc.setScreen(parent);
                if (yes) original.onPress(btn);
            },
                    Component.translatable("controls.reset.confirm.title"),
                    Component.translatable("controls.reset.confirm.body")));
        };
    }
}
