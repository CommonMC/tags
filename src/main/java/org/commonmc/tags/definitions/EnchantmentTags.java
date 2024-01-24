package org.commonmc.tags.definitions;

import org.commonmc.tags.Tags;

public class EnchantmentTags {
    public static void register(Tags.TagSet enchantmentTags) {
        enchantmentTags.tag("increase_block_drops")
                .setComment("""
                        A tag containing enchantments that increase the amount or
                        quality of drops from blocks, such as the fortune enchantment.
                        """)
                .addEntry("block_fortune");
        enchantmentTags.tag("increase_entity_drops")
                .setComment("""
                        A tag containing enchantments that increase the amount or
                        quality of drops from entities, such as the looting enchantment.
                        """)
                .addEntry("mob_looting");
        enchantmentTags.tag("weapon_damage_enhancement")
                .setComment("""
                        For enchantments that increase the damage dealt by an item.
                        """)
                .addEntry("sharpness")
                .addEntry("smite")
                .addEntry("bane_of_arthropods")
                .addEntry("power_arrows")
                .addEntry("impaling");
        enchantmentTags.tag("entity_speed_enhancement")
                .setComment("""
                        For enchantments that increase movement speed for entity wearing armor enchanted with it.
                        """)
                .addEntry("soul_speed")
                .addEntry("swift_sneak")
                .addEntry("depth_strider");
        enchantmentTags.tag("entity_auxiliary_movement_enhancement")
                .setComment("""
                        For enchantments that applies movement-based benefits unrelated to speed for the entity wearing armor enchanted with it.
                        For example, reducing falling speed or allowing walking on water.
                        """)
                .addEntry("fall_protection")
                .addEntry("frost_walker");
        enchantmentTags.tag("entity_defense_enhancement")
                .setComment("""
                        For enchantments that decrease damage taken or otherwise benefit, in regard to damage, the entity wearing armor enchanted with it.
                        """)
                .addEntry("all_damage_protection")
                .addEntry("blast_protection")
                .addEntry("projectile_protection")
                .addEntry("fire_protection")
                .addEntry("respiration")
                .addEntry("fall_protection");
    }
}
