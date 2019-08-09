package net.dynamic.tooling.blocks.container;

import io.github.cottonmc.cotton.gui.CottonScreenController;
import io.github.cottonmc.cotton.gui.ValidatedSlot;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;

public class WGhostItemPanel extends WItemSlot {


    public WGhostItemPanel(Inventory inventory, int index) {
        super(inventory, index, 1, 1, false, true);

        this.peer = new Peer(inventory, index, getAbsoluteX(), getAbsoluteY());
    }

    protected final Peer peer;

//    @Override
//    public void createPeers(CottonScreenController c) {
//
//        c.addSlotPeer(peer);
//    }

    public class Peer extends ValidatedSlot {


        public Peer(Inventory inventoryIn, int index, int xPosition, int yPosition) {

            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean canTakeItems(PlayerEntity player) {

            return false;
        }
    }
}
