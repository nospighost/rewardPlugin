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
        if (!ClaimManager.getInstance().canClaim(player)) {
            String nextClaimTime = ClaimManager.getInstance().getRemainingTime(player);
            player.sendMessage(Main.prefix + "§cDu kannst die nächste Belohnung in " + nextClaimTime + " abholen");
            return;
        }

        ClaimManager.getInstance().giveReward(player);
        player.sendMessage(Main.prefix);
        player.sendMessage(Main.prefix + "§bDu hast deine Belohnung erfolgreich abgeholt!");
        player.sendMessage(Main.prefix);
    }
}
