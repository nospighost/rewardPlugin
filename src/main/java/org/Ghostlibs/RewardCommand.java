package org.Ghostlibs;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import org.bukkit.entity.Player;

@CommandAlias("belohnung")
@CommandPermission("belohnung.claim")
public class RewardCommand extends BaseCommand {

    @Default
    public void onClaim(Player player) {
        ClaimManager.getInstance().canClaimAsync(player, claimAble -> {
            if (!claimAble) {
                String remainingTime = ClaimManager.getInstance().getRemainingTime(player);

                player.sendMessage(Main.prefix + "§c§l Du kannst die nächste Belohnung in " + remainingTime + " abholen");


            } else {
                ClaimManager.getInstance().giveReward(player);
                player.sendMessage(Main.prefix);
                player.sendMessage(Main.prefix + "<green> Du hast deine Belohnung erfolgreich abgeholt!");
                player.sendMessage(Main.prefix);
            }
        });

    }
};




