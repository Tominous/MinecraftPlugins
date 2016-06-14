package com.ullarah.upostal.command;

import com.ullarah.upostal.command.inbox.Prepare;
import com.ullarah.upostal.function.CommonString;
import com.ullarah.upostal.function.PlayerProfile;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.UUID;

import static com.ullarah.upostal.PostalInit.getInboxDataPath;
import static com.ullarah.upostal.PostalInit.getPlugin;
import static com.ullarah.upostal.PostalInit.inboxModification;

public class Modify {

    public void inbox(CommandSender sender, String[] args) {

        CommonString commonString = new CommonString();

        if (!sender.hasPermission("postal.modify")) {
            commonString.messagePermDeny(getPlugin(), sender);
            return;
        }

        if (args.length >= 2) {

            UUID inboxUUID = new PlayerProfile().lookup(args[1]).getId();
            File inboxFile = new File(getInboxDataPath(), inboxUUID.toString() + ".yml");

            if (inboxFile.exists()) {
                inboxModification.add(inboxUUID);
                new Prepare().run((Player) sender, inboxUUID);
            }
            else commonString.messageSend(getPlugin(), (Player) sender, "That player does not have an inbox!");

        } else
            commonString.messageSend(getPlugin(), (Player) sender, ChatColor.YELLOW + "/postal view <player>");

    }

}
