/*
 * Copyright (c) 2018.
 * created by Christian34
 */

package com.Christian34.varo.Modules;

import com.Christian34.varo.Lobby.Lobby;
import com.Christian34.varo.Varo;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class GravityGrenade implements Listener {

    private static List<UUID> fallingBlocks2 = new ArrayList();
    private static ArrayList<UUID> explosiveIds = new ArrayList();
    private ArrayList<UUID> tntIds = new ArrayList();
    private static final List<Material> blockedBlocks = Arrays.asList(Material.GRASS, Material.YELLOW_FLOWER,
            Material.GRASS_PATH, Material.LEAVES, Material.LEAVES_2, Material.CARPET, Material.GLASS, Material.IRON_ORE,
            Material.STAINED_GLASS_PANE, Material.STAINED_GLASS, Material.THIN_GLASS, Material.CHEST, Material.BOAT,
            Material.COAL_ORE, Material.DIAMOND_ORE, Material.EMERALD_ORE, Material.GOLD_ORE);

    @SuppressWarnings("deprecation")
    public GravityGrenade() {
        ItemStack grenade = new ItemStack(Material.SNOW_BALL);
        ItemMeta grenadeMeta = grenade.getItemMeta();
        grenadeMeta.setDisplayName("§7§l》§3§lGravity Grenade§7§l《");
        grenade.setItemMeta(grenadeMeta);
        ShapedRecipe recipe = new ShapedRecipe(grenade);

        recipe.shape("***", "*%*", "*§*");
        recipe.setIngredient('*', Material.LEATHER);
        recipe.setIngredient('%', Material.CHEST);
        recipe.setIngredient('§', Material.DIAMOND);
        Varo.getPlugin().getServer().addRecipe(recipe);
    }

    private static List<Block> getNearbyBlocks(Location center, int radius) {
        List<Location> locs = circle(center, radius, radius, true, true, 0);
        List<Block> blocks = new ArrayList();
        for (Location loc : locs) {
            blocks.add(loc.getBlock());
        }
        return blocks;
    }

    private static void moveToward(Entity entity, Location to, double speed) {
        Location loc = entity.getLocation();
        double x = loc.getX() - to.getX();
        double y = loc.getY() - to.getY();
        double z = loc.getZ() - to.getZ();
        Vector velocity = new Vector(x, y, z).normalize().multiply(-speed);

        entity.setVelocity(velocity);
    }

    private static List<Location> circle(Location loc, int radius, int height, boolean hollow, boolean sphere, int plusY) {
        List<Location> circleblocks = new ArrayList();
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();
        for (int x = cx - radius; x <= cx + radius; x++) {
            for (int z = cz - radius; z <= cz + radius; z++) {
                for (int y = sphere ? cy - radius : cy; y < (sphere ? cy + radius : cy + height); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if ((dist < radius * radius) && ((!hollow) || (dist >= (radius - 1) * (radius - 1)))) {
                        Location l = new Location(loc.getWorld(), x, y + plusY, z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }

    private static Vector getRandomVelocity() {
        Random random = new Random();
        double power = 3D;
        double rix = random.nextBoolean() ? -power : power;
        double riz = random.nextBoolean() ? -power : power;
        double x = random.nextBoolean() ? rix * (0.25D + random.nextInt(3) / 5) : 0.0D;
        double y = 0.6D + random.nextInt(2) / 4.5D;
        double z = random.nextBoolean() ? riz * (0.25D + random.nextInt(3) / 5) : 0.0D;

        return new Vector(x, y, z);
    }

    private static void performSingularityGrenade(EntityExplodeEvent event, int radius2) {
        event.setYield(0.0F);
        event.blockList().clear();

        final Location center = event.getLocation();
        double speed = 0.6D;
        long moveBlocksDelay = 3L;

        final List<BlockState> oldBlocks = new ArrayList();
        final List<FallingBlock> fallingBlocks = new ArrayList();

        new BukkitRunnable() {
            int radius = 1;

            public void run() {
                if (this.radius <= radius2) {
                    for (Block block : GravityGrenade.getNearbyBlocks(center, this.radius)) {
                        if ((block.getType() != Material.AIR) && (block.getType() != Material.OBSIDIAN) && (block.getType() != Material.BEDROCK)) {
                            Location loc = block.getLocation();
                            Material type = block.getType();
                            byte data = block.getData();

                            oldBlocks.add(block.getState());
                            block.setType(Material.AIR);

                            FallingBlock fallingBlock = block.getWorld().spawnFallingBlock(loc, type, data);
                            if (!blockedBlocks.contains(fallingBlock.getMaterial())) {
                                fallingBlock.setDropItem(true);
                            } else {
                                fallingBlock.setDropItem(false);
                            }
                            fallingBlock.setFireTicks(0);

                            GravityGrenade.fallingBlocks2.add(fallingBlock.getUniqueId());
                            fallingBlocks.add(fallingBlock);
                            GravityGrenade.moveToward(fallingBlock, center, speed);
                        }
                    }
                    this.radius += 1;
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(Varo.getPlugin(), 20L, moveBlocksDelay);

        new BukkitRunnable() {
            int timesRun = 0;

            public void run() {
                if (this.timesRun < radius2 * 3L * 108L) {
                    for (FallingBlock fallingBlock : fallingBlocks) {
                        GravityGrenade.moveToward(fallingBlock, center, 0.6D);
                        this.timesRun += 1;
                    }
                } else {
                    for (FallingBlock fallingBlock : fallingBlocks) {
                        fallingBlock.setVelocity(GravityGrenade.getRandomVelocity());
                    }
                    //   GravityGrenade.performWorldRegen(oldBlocks, center, 12, 120L);
                    cancel();
                }
            }
        }.runTaskTimer(Varo.getPlugin(), 10L, 2L);

    }

   /* private static void regenerateBlocks(Collection<BlockState> blocks, final int blocksPerTime, long delay, Comparator<BlockState> comparator) {
        final List<BlockState> orderedBlocks = new ArrayList();

        orderedBlocks.addAll(blocks);
        if (comparator != null) {
            Collections.sort(orderedBlocks, comparator);
        }
        int size = orderedBlocks.size();
        if (size > 0) {
            new BukkitRunnable() {
                int index = size - 1;

                public void run() {
                    for (int i = 0; i < blocksPerTime; i++) {
                        if (this.index >= 0) {
                            BlockState state = orderedBlocks.get(this.index);

                            GravityGrenade.regenerateBlock(state.getBlock(), state.getType(), state
                                    .getPlayerData().getPlayerData());

                            this.index -= 1;
                        } else {
                            cancel();
                            return;
                        }
                    }
                }
            }.runTaskTimer(MainListener.plugin, 0L, delay);
        }
    } */

    private static void regenerateBlock(Block block, Material type, byte data) {
        Location loc = block.getLocation();

        loc.getWorld().playEffect(loc, Effect.STEP_SOUND, type == Material.AIR ? block.getType().getId() : type.getId());
        block.setType(type);
        block.setData(data);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFallingBlockLand(final EntityChangeBlockEvent event) {
        if ((event.getEntity() instanceof FallingBlock)) {
            FallingBlock fallingBlock = (FallingBlock) event.getEntity();
            UUID id = fallingBlock.getUniqueId();
            if ((event.getBlock().getType() == Material.AIR) &&
                    (fallingBlocks2.contains(id))) {
                new BukkitRunnable() {
                    public void run() {
                        GravityGrenade.regenerateBlock(event.getBlock(), Material.AIR, (byte) 0);
                    }
                }.runTaskLater(Varo.getPlugin(), 1L);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.getEntity() != null) {
            UUID id = event.getEntity().getUniqueId();
            if (this.tntIds.contains(id)) {
                event.blockList().clear();
                Random rnd = new Random();
                int i = rnd.nextInt(5 - 4 + 1) + 4;
                performSingularityGrenade(event, i);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onProjectileHit(ProjectileHitEvent event) {
        if ((event.getEntity() instanceof Snowball)) {
            Snowball fireball = (Snowball) event.getEntity();
            if (event.getEntity().getLocation().getWorld() == Lobby.getWorld()) {
                return;
            }

            //UUID fireballId = fireball.getUniqueId();
            //       if (explosiveIds.contains(fireballId)) {
            TNTPrimed tnt = (TNTPrimed) fireball.getWorld().spawnEntity(fireball
                    .getLocation(), EntityType.PRIMED_TNT);

            UUID tntId = tnt.getUniqueId();

            fireball.remove();
            tnt.setFuseTicks(1);
            this.tntIds.add(tntId);
            //   }
        }
    }

}