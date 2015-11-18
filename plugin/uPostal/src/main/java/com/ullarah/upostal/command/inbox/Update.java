package com.ullarah.upostal.command.inbox;

import com.ullarah.ulib.function.CommonString;
import com.ullarah.ulib.function.TitleSubtitle;
import com.ullarah.upostal.task.PostalReminder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ullarah.upostal.PostalInit.*;

public class Update {

    public static void run(UUID sender, UUID receiver, Inventory inventory) {

        Player player = Bukkit.getPlayer(sender);

        if (!getMaintenanceCheck()) {

            boolean newItems = false;
            String fromString = "" + ChatColor.GRAY + ChatColor.ITALIC + "From: " + player.getPlayerListName();

            File inboxFile = new File(getInboxDataPath(), receiver.toString() + ".yml");
            FileConfiguration inboxConfig = YamlConfiguration.loadConfiguration(inboxFile);

            if (!sender.equals(receiver)) {

                try {

                    ArrayList<ItemStack> itemList = new ArrayList<>();

                    if (!inboxConfig.getList("item").isEmpty())
                        itemList.addAll(inboxConfig.getList("item").stream()
                                .map(item -> (ItemStack) item).collect(Collectors.toList()));

                    for (ItemStack item : inventory.getContents()) {

                        if (item != null) {

                            newItems = true;

                            if (item.hasItemMeta()) {

                                if (item.getItemMeta().hasDisplayName()) {

                                    if (item.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Slot Taken")
                                            && item.getType() == Material.STAINED_GLASS_PANE) {

                                        newItems = false;
                                        continue;

                                    }

                                }

                                List<String> itemLore = new ArrayList<>();

                                if (item.getItemMeta().hasLore()) {
                                    itemLore = item.getItemMeta().getLore();
                                    itemLore.add(fromString);
                                } else itemLore.add(fromString);

                                ItemMeta itemMeta = item.getItemMeta();
                                itemMeta.setLore(itemLore);
                                item.setItemMeta(itemMeta);

                            } else {

                                ItemMeta itemMeta = item.getItemMeta();
                                itemMeta.setLore(Collections.singletonList(fromString));
                                item.setItemMeta(itemMeta);

                            }

                            itemList.add(item);

                        }

                    }

                    inboxConfig.set("item", itemList);
                    inboxConfig.save(inboxFile);

                } catch (IOException e) {

                    new CommonString().messageSend(getPlugin(), player, true, new String[]{
                            ChatColor.RED + "Inbox Update Error!"
                    });

                }

                if (inboxOwnerBusy.isEmpty()) if (newItems)
                    new CommonString().messageSend(getPlugin(), player, true, new String[]{ChatColor.GREEN + "Items sent successfully!"});

                if (newItems) {

                    for (Player receiverPlayer : Bukkit.getServer().getOnlinePlayers()) {

                        if (receiverPlayer.getUniqueId().equals(receiver)) {

                            inboxChanged.put(receiver, PostalReminder.task(receiver));
                            String message = ChatColor.YELLOW + "You have new items in your inbox!";
                            new CommonString().messageSend(getPlugin(), receiverPlayer, true, new String[]{message});
                            new TitleSubtitle().subtitle(receiverPlayer, 5, message);
                            break;

                        }

                    }

                }

            } else try {

                List<ItemStack> itemList = new ArrayList<>();
                for (ItemStack item : inventory.getContents()) if (item != null) itemList.add(item);

                inboxConfig.set("item", itemList);
                inboxConfig.save(inboxFile);

            } catch (IOException e) {

                new CommonString().messageSend(getPlugin(), player, true, new String[]{ChatColor.RED + "Inbox Update Error!"});

            }

        } else new CommonString().messageMaintenance(getPlugin(), player);

    }

}
