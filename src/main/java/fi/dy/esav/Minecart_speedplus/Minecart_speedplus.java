package fi.dy.esav.Minecart_speedplus;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public class Minecart_speedplus extends JavaPlugin {
    static final double SPEED_MULTIPLIER_MAX = 30.0D;
    static final double SPEED_LIMIT = 350;

    static double speedMultiplier = 1.25D;
    private final Minecart_speedplusVehicleListener VehicleListener = new Minecart_speedplusVehicleListener(this);
    private final Minecart_speedplusSignListener SignListener = new Minecart_speedplusSignListener(this);
    final Logger log = Logger.getLogger("Minecraft");

    public static double getSpeedMultiplier() {
        return speedMultiplier;
    }

    public boolean setSpeedMultiplier(final double multiplier) {
        if ((((0.0D < multiplier) ? 1 : 0) & ((multiplier <= SPEED_MULTIPLIER_MAX) ? 1 : 0)) != 0) {
            speedMultiplier = multiplier;
            return true;
        }
        return false;
    }

    public void onEnable() {
        this.log.info("MinecartSpeedPlus started.");
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(this.VehicleListener, this);
        pm.registerEvents(this.SignListener, this);
    }

    public void onDisable() {
        this.log.info("MinecartSpeedPlus stopped.");
    }

    public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String commandLabel, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("msp")) {
            return false;
        }

        if ((sender instanceof final Player player) && !player.hasPermission("msp.cmd")) {
            sender.sendMessage("You don't have permission to do that");
            return true;
        }

        var multiplier = 0D;
        try {
            multiplier = Double.parseDouble(args[0]);
        } catch (final Exception e) {
            sender.sendMessage(Component.text("speed multiplier must be between 0.0 and " + SPEED_MULTIPLIER_MAX).color(NamedTextColor.RED));
            return false;
        }

        var success = setSpeedMultiplier(multiplier);
        if (success) {
            sender.sendMessage(Component.text("multiplier for new mine carts set to: " + multiplier).color(NamedTextColor.YELLOW));
            return true;
        }
        sender.sendMessage(Component.text("speed multiplier must be between 0.0 and " + SPEED_MULTIPLIER_MAX).color(NamedTextColor.YELLOW));
        return true;
    }
}