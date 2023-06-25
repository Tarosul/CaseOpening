package de.Lukas.CaseOpening.System.Base;

import com.google.common.collect.Lists;
import net.minecraft.nbt.*;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemBuilder {
    private org.bukkit.inventory.ItemStack item;

    private ItemMeta meta;

    private List<String> lore = new ArrayList<>();

    public ItemBuilder(Material material, int amount) {
        this.item = new org.bukkit.inventory.ItemStack(material, amount);
        this.meta = this.item.getItemMeta();
    }

    public ItemBuilder(Material material) {
        this(material, 1);
    }

    public ItemBuilder(org.bukkit.inventory.ItemStack item) {
        this.item = item;
        this.meta = item.getItemMeta();
    }

    public ItemBuilder(String skullOwner) {
        this(Material.PLAYER_HEAD, 1);
        setSkullOwner(skullOwner);
    }

    public ItemBuilder(Material material, int amount, Color color) {
        this.item = new org.bukkit.inventory.ItemStack(material, amount);
        LeatherArmorMeta armorMeta = (LeatherArmorMeta)this.item.getItemMeta();
        ((LeatherArmorMeta)Objects.<LeatherArmorMeta>requireNonNull(armorMeta)).setColor(color);
        this.meta = (ItemMeta)armorMeta;
    }

    public ItemBuilder addPotionEffect(PotionEffect effect) {
        ((PotionMeta)this.meta).addCustomEffect(effect, true);
        return this;
    }

    public ItemBuilder addItemFlag(ItemFlag flag) {
        this.meta.addItemFlags(new ItemFlag[] { flag });
        return this;
    }

    public ItemBuilder setAmount(int value) {
        this.item.setAmount(value);
        return this;
    }

    public ItemBuilder setGlow() {
        this.meta.addEnchant(Enchantment.DURABILITY, 1, true);
        this.meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
        return this;
    }

    public ItemBuilder setGlow(boolean arg) {
        if (!arg)
            return this;
        this.meta.addEnchant(Enchantment.DURABILITY, 1, true);
        this.meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
        return this;
    }

    public ItemBuilder setNoName() {
        this.meta.setDisplayName(" ");
        return this;
    }

    public ItemBuilder setSkullOwner(String owner) {
        ((SkullMeta)this.meta).setOwner(owner);
        return this;
    }

    public ItemBuilder setDurability(short durability) {
        this.item.setDurability(durability);
        return this;
    }

    public ItemBuilder addLore(String lore) {
        this.lore.add(lore);
        return this;
    }

    public ItemBuilder addLoreArray(String... lines) {
        for (String line : lines) {
            if (line != null)
                this.lore.add(line);
        }
        return this;
    }

    public ItemBuilder addLoreList(List<String> list) {
        for (String s : list) {
            if (s != null)
                this.lore.add(s);
        }
        return this;
    }

    public ItemBuilder setDisplayName(String displayname) {
        this.meta.setDisplayName(displayname);
        return this;
    }

    public ItemBuilder setUnbreakable(boolean arg) {
        this.meta.setUnbreakable(arg);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment ench, int lvl) {
        this.meta.addEnchant(ench, lvl, true);
        return this;
    }

    public ItemBuilder setSubID(int subID) {
        this.item.setDurability((short)subID);
        return this;
    }

    public ItemBuilder setMetadata(String metadata, Object value) {
        this.item = CraftItemStack.asBukkitCopy(setMetadata(CraftItemStack.asNMSCopy(this.item), metadata, value));
        return this;
    }

    public boolean hasMetadata(String metadata) {
        return hasMetadata(CraftItemStack.asNMSCopy(this.item), metadata);
    }

    public Object getMetadata(String metadata) {
        return getMetadata(CraftItemStack.asNMSCopy(this.item), metadata);
    }

    private boolean hasMetadata(ItemStack item, String metadata) {
        return (item.a(metadata) != null && item.a(metadata).b(metadata));
    }

    private ItemStack setMetadata(ItemStack item, String metadata, Object value) {
        if (item.a() == null)
            item.c(new NBTTagCompound());
        NBTTagCompound tag = setTag(item.a(metadata), metadata, value);
        item.a(tag);
        return item;
    }

    private Object getMetadata(ItemStack item, String metadata) {
        if (!hasMetadata(item, metadata))
            return null;
        return getObject(item.a(metadata));
    }

    private NBTTagCompound setTag(NBTTagCompound tag, String tagString, Object value) {
        NBTBase base = null;
        if (base != null)
            tag.a(tagString, base);
        return tag;
    }

    private Object getObject(NBTBase tag) {
        if (tag instanceof net.minecraft.nbt.NBTTagEnd)
            return null;
        if (tag instanceof NBTTagLong)
            return Long.valueOf(((NBTTagLong)tag).f());
        if (tag instanceof NBTTagByte)
            return Byte.valueOf(((NBTTagByte)tag).b());
        if (tag instanceof NBTTagShort)
            return Short.valueOf(((NBTTagShort)tag).h());
        if (tag instanceof NBTTagInt)
            return Integer.valueOf(((NBTTagInt)tag).g());
        if (tag instanceof NBTTagFloat)
            return Float.valueOf(((NBTTagFloat)tag).k());
        if (tag instanceof NBTTagDouble)
            return Double.valueOf(((NBTTagDouble)tag).j());
        if (tag instanceof NBTTagByteArray)
            return ((NBTTagByteArray)tag).e();
        if (tag instanceof NBTTagString)
            return ((NBTTagString)tag).f_();
        if (tag instanceof net.minecraft.nbt.NBTTagList) {
            List<NBTBase> list = null;
            try {
                Field field = tag.getClass().getDeclaredField("list");
                field.setAccessible(true);
                list = (List<NBTBase>)field.get(tag);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (list == null)
                return null;
            List<Object> toReturn = Lists.newArrayList();
            for (NBTBase base : list)
                toReturn.add(getObject(base));
            return toReturn;
        }
        if (tag instanceof NBTTagCompound)
            return tag;
        if (tag instanceof NBTTagIntArray)
            return ((NBTTagIntArray)tag).g();
        return null;
    }

    public org.bukkit.inventory.ItemStack build() {
        if (!this.lore.isEmpty())
            this.meta.setLore(this.lore);
        this.item.setItemMeta(this.meta);
        return this.item;
    }
}
