package com.github.iamtakagi.clickwheat2makebread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

import net.md_5.bungee.api.ChatColor;

public class ClickWheat2MakeBread extends JavaPlugin {

  private static List<String> enabledPlayers = new ArrayList<String>();

  @Override
  public void onEnable() {
    this.saveDefaultConfig();
    enabledPlayers = this.getConfig().getStringList("enabled_players");
    this.getServer().getPluginManager().registerEvents(new Listener() {
      @EventHandler
      public void onClick(PlayerInteractEvent event) {
        if (!enabledPlayers.contains(event.getPlayer().getUniqueId().toString())) {
          return;
        }
        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.PHYSICAL) {
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
        String uuid = player.getUniqueId().toString();
        if (enabledPlayers.contains(uuid)) {
          enabledPlayers.remove(uuid);
        } else {
          enabledPlayers.add(uuid);
        }
        player.sendMessage("ClickWheat2MakeBread: " + (enabledPlayers.contains(uuid) ? ChatColor.GREEN + "ON" : ChatColor.RED + "OFF"));
        this.getConfig().set("enabled_players", enabledPlayers);
        this.saveConfig();
      }
      return true;
    });
  }

  @Override
  public void onDisable() {
    this.getConfig().set("enabled_players", enabledPlayers);
    this.saveConfig();
  }
}
