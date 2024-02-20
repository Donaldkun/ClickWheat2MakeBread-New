package com.github.iamtakagi.clickwheat2makebread;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ClickWheat2MakeBread extends JavaPlugin {

  @Override
  public void onEnable() {
    this.getServer().getPluginManager().registerEvents(new Listener() {
      @EventHandler
      public void onClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.PHYSICAL) {
          ItemStack item = event.getPlayer().getItemInHand();
          if (item == null) {
            return;
          }
          if (item.getType() == Material.WHEAT && item.getAmount() >= 3) {
            item.setAmount(item.getAmount() - 3);
            Player player = event.getPlayer();
            if (player == null) {
              return;
            }
            player.getInventory().addItem(new ItemStack(Material.BREAD, 1));
          }
        }
      }
    }, this);
  }
}
