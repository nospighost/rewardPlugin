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
        ClaimManager.getInstance().canClaim(player, claimAble -> {
            if (!claimAble) {
                ClaimManager.getInstance().getRemainingTime(player, nextClaimTime -> {
                    player.sendMessage(Main.prefix + "§cDu kannst die nächste Belohnung in " + nextClaimTime + " abholen");
                    return;

                });


            } else {
                ClaimManager.getInstance().giveReward(player);
                player.sendMessage(Main.prefix);
                player.sendMessage(Main.prefix + "§bDu hast deine Belohnung erfolgreich abgeholt!");
                player.sendMessage(Main.prefix);
            }
        });

    }
}

