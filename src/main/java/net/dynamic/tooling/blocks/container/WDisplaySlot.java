package net.dynamic.tooling.blocks.container;

import com.google.common.collect.Lists;
import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.ValidatedSlot;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WSprite;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.List;

public class WDisplaySlot extends WWidget {

    public WDisplaySlot(Inventory inventory, int index ) {

        this.inventory = inventory;
        this.slotIndex = index;
    }

    private final List<Slot> peers = Lists.newArrayList();

    private BackgroundPainter backgroundPainter;

    private Inventory inventory;

    private int slotIndex = 0;
    private int slotsWide = 1;
    private int slotsHigh = 1;

    @Override
    public int getWidth() {
        return slotsWide * 18;
    }

    @Override
    public int getHeight() {
        return slotsHigh * 18;
    }

    public boolean isBigSlot() {
        return false;
    }

//    @Environment(EnvType.CLIENT)
//    @Override
//    public void paintBackground(int x, int y) {
//        if (backgroundPainter!=null) {
//            backgroundPainter.paintBackground(x, y, this);
//        } else {
//            for(int ix = 0; ix < getWidth()/18; ++ix) {
//                for(int iy = 0; iy < getHeight()/18; ++iy) {
//                    int lo = 0xB8000000;
//                    int bg = 0x4C000000;
//                    int hi = 0xB8FFFFFF;
//                    if (isBigSlot()) {
//                        ScreenDrawing.drawBeveledPanel((ix * 18) + x - 4, (iy * 18) + y - 4, 24, 24,
//                                lo, bg, hi);
//                    } else {
//                        ScreenDrawing.drawBeveledPanel((ix * 18) + x - 1, (iy * 18) + y - 1, 18, 18,
//                                lo, bg, hi);
//                    }
//                }
//            }
//        }
//
//    }

    @Override
    public void createPeers(GuiDescription c) {
        peers.clear();

        DisplaySlot slot = new DisplaySlot(inventory, slotIndex, this.getAbsoluteX(), this.getAbsoluteY());
        peers.add(slot);
        c.addSlotPeer(slot);
    }

    @Override
    public void paintForeground(int x, int y, int mouseX, int mouseY) {
        super.paintForeground(x, y, mouseX, mouseY);

        ItemStack stack = (peers.size() >= slotIndex && peers.get(this.slotIndex) != null) ? peers.get(this.slotIndex).getStack() : ItemStack.EMPTY;

        ItemRenderer ir = MinecraftClient.getInstance().getItemRenderer();
        ir.renderGuiItem(stack, getAbsoluteX(), getAbsoluteY());
    }

    public class DisplaySlot extends ValidatedSlot {


        public DisplaySlot(Inventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

//        @Override
//        public boolean doDrawHoveringEffect() {
//            return false;
//        }

        @Override
        public boolean canTakeItems(PlayerEntity player) {
            return false;
        }
    }

}
