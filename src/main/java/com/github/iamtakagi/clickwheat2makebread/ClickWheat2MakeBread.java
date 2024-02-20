package com.github.iamtakagi.clickwheat2makebread;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ClickWheat2MakeBread extends JavaPlugin {

  private static Map<UUID, Boolean> players = new HashMap<>();

  @Override
  public void onEnable() {
    this.saveDefaultConfig();
    players = (Map<UUID, Boolean>) this.getConfig().get("players");
    this.getServer().getPluginManager().registerEvents(new Listener() {
      @EventHandler
      public void onClick(PlayerInteractEvent event) {
        if (!players.getOrDefault(event.getPlayer().getUniqueId(), false)) {
          return;
        }
        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) {
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
    this.getCommand("bt").setExecutor((sender, command, label, args) -> {
      if (sender instanceof Player) {
        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();
        if (players.containsKey(uuid)) {
          players.put(uuid, !players.get(uuid));
        } else {
          players.put(uuid, true);
        }
        player.sendMessage("ClickWheat2MakeBread: " + (players.get(uuid) ? "ON" : "OFF"));
        this.getConfig().set("players", players);
        this.saveConfig();
      }
      return true;
    });
  }

  @Override
  public void onDisable() {
    this.getConfig().set("players", players);
    this.saveConfig();
  }
}
