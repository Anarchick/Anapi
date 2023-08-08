package fr.anarchick.anapi.bukkit.inventory;

import fr.anarchick.anapi.bukkit.PaperComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @version 0.1.4.1 (not fully tested)
 * @authors WarzouMc, Anarchick
 */
public class ItemBuilder {

    /**
     * Vars
     */
    private ItemStack itemStack;
    private ItemMeta itemMeta;

    /**
     * init ItemBuilder without argument
     * get the final item with .build()
     */
    public ItemBuilder(){
        this(Material.AIR);
    }

    /**
     * init ItemBuilder
     * get the final item with .build()
     * @param material
     */
    public ItemBuilder(Material material){
        this(material, 1);
    }

    /**
     * init ItemBuilder
     * get the final item with .build()
     * @param material
     * @param amount
     */
    public ItemBuilder(Material material, int amount) {
    	this.itemStack = new ItemStack(material, amount);
        this.itemMeta = itemStack.getItemMeta();
    }

    /**
     * init ItemBuilder
     * get the final item with .build()
     * @param itemStack
     */
    public ItemBuilder(ItemStack itemStack){
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    /**
     * Set item
     * @param material
     * @return
     */
    public ItemBuilder setItem(Material material){
        this.itemStack.setType(material);
        return this;
    }
    
    /**
     * Set Item
     * @param itemStack
     * @return
     */
    public ItemBuilder setItem(ItemStack itemStack){
        this.itemStack = itemStack;
        return this;
    }

    /**
     * Set amount
     * @param amount
     * @return
     */
    public ItemBuilder setAmount(int amount){
        this.itemStack.setAmount(amount);
        return this;
    }
    
    /**
     * Set the custom model data
     * @param data
     * @return
     */
    public ItemBuilder setCustomModelData(int data) {
    	this.itemMeta.setCustomModelData(data);
    	return this;
    }
    
    /**
     * Dye a Leather Armor
     * @param r g b
     * @return
     */
    public ItemBuilder dyeArmor(int r, int g, int b){
        if (this.itemMeta instanceof LeatherArmorMeta) {
        	LeatherArmorMeta lam = (LeatherArmorMeta) this.itemMeta;
        	lam.setColor(Color.fromRGB(r, g, b));
        	this.itemMeta = lam;
        }
        return this;
    }

    /**
     * @param unbreakable
     * Set item in unbreakable/breakable
     * @return
     */
    public ItemBuilder setUnbreakable(boolean unbreakable){
        if (this.itemMeta == null) {
            if (this.itemStack == null)
                return null;
            this.itemMeta = this.itemStack.getItemMeta();
        }
        this.itemMeta.setUnbreakable(unbreakable);
        return this;
    }

    /**
     * set the display name of the item
     * @param miniMessage
     * @return
     */
    @SuppressWarnings("deprecation")
	public ItemBuilder setName(String miniMessage){
        itemMeta.displayName(toMiniMessage(miniMessage));
        return this;
    }

    /**
     * Add lore from String list
     * @param lores
     * @return
     */
    
	@SuppressWarnings("deprecation")
	public ItemBuilder addLore(List<String> lores){
        List<Component> loresComponent = lores.stream().map(lore -> PaperComponentUtils.getMiniMessageTextComponent(lore)).toList();
        itemMeta.lore(loresComponent);
        return this;
    }

    /**
     * Add lore from String...
     * @param lores
     * @return
     */
    public ItemBuilder addLore(String... lores){
        addLore(Arrays.asList(lores));
        return this;
    }

    /**
     * add enchant to the item
     * @param enchantment
     * @param value
     * @param ignoreLevelRestriction
     * @return
     */
    public ItemBuilder addEnchantment(Enchantment enchantment, int value, boolean ignoreLevelRestriction){
        itemMeta.addEnchant(enchantment, value, ignoreLevelRestriction);
        return this;
    }

    /**
     * add enchants from map (use for json object)
     * @param enchantment
     * @return
     */
    public ItemBuilder setEnchants(Map<Enchantment, Integer> enchantment){
        for (Map.Entry<Enchantment, Integer> entry : enchantment.entrySet()) {
            Enchantment enchant = entry.getKey();
            addEnchantment(enchant, entry.getValue(), entry.getValue() > enchant.getMaxLevel());
        }
        return this;
    }

    /**
     * Remove an enchantment on the item
     * @param enchantment
     * @return
     */
    public ItemBuilder removeEnchant(Enchantment enchantment) {
        if (!this.getEnchantments().containsKey(enchantment))
            return this;
        itemMeta.removeEnchant(enchantment);
        return this;
    }

    /**
     * remove all enchant on item from a list
     * @param enchantments
     * @return
     */
    public ItemBuilder removeEnchants(List<Enchantment> enchantments){
        for (Enchantment enchantment : enchantments) {
            if (!this.getEnchantments().containsKey(enchantment))
                continue;
            this.removeEnchant(enchantment);
        }
        return this;
    }

    /**
     * remove all enchantment on the item
     * @return
     */
    public ItemBuilder clearEnchants() {
        if (this.getEnchantments() == null)
            return this;
        for (Enchantment enchantment : this.getEnchantments().keySet())
            this.removeEnchant(enchantment);
        return this;
    }

    /**
     * add ItemFlag on your item
     * @param itemFlag
     * @return
     */
    public ItemBuilder addItemFlag(ItemFlag itemFlag){
        itemMeta.addItemFlags(itemFlag);
        return this;
    }

    /**
     * add ItemFlags on your item
     * @param itemFlag
     * @return
     */
    public ItemBuilder addItemFlag(ItemFlag... itemFlag){
        itemMeta.addItemFlags(itemFlag);
        return this;
    }

    /**
     * add ItemFlags on your item from ItemFlag list
     * @param itemFlag
     * @return
     */
    public ItemBuilder addItemFlag(List<ItemFlag> itemFlag){
        itemFlag.forEach(this::addItemFlag);
        return this;
    }

    /**
     * remove ItemFlag on your item
     * @param itemFlag
     * @return
     */
    public ItemBuilder removeItemFlag(ItemFlag itemFlag){
        itemMeta.removeItemFlags(itemFlag);
        return this;
    }

    /**
     * hide enchant
     * @return
     */
    public ItemBuilder hideEnchant(){
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    /**
     * show enchant
     * @return
     */
    public ItemBuilder showEnchant(){
        itemMeta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    /**
     * Set durrability of item
     * /!\ 100 >= percent >= 0
     * @param percent
     * @return
     */
    public ItemBuilder setDurability(float percent){
        if (percent > 100.0){
            return this;
        }else if (percent < 0.0){
            return this;
        }
        if (itemMeta instanceof Damageable) {
        	int damage =  (int) (((Damageable)itemMeta).getDamage() * (percent / 100));
        	((Damageable)itemMeta).setDamage(damage);
        }
        return this;
    }

    /**
     * Set durrability of item
     * @param durability
     * @return
     */
    public ItemBuilder setNewDurability(int durability){
    	((Damageable)itemMeta).setDamage(durability);
        return this;
    }

    /**
     * If your item is a player skull you can apply a special player skull texture
     * @param playerName
     * @return
     */
    @SuppressWarnings("deprecation")
	public ItemBuilder setSkullOwner(String playerName){
        return setSkullOwner(Bukkit.getOfflinePlayer(playerName));
    }
    
    /**
     * If your item is a player skull you can apply a special player skull texture
     * @param uuid of offline player
     * @return
     */
	public ItemBuilder setSkullOwner(UUID uuid){
        return setSkullOwner(Bukkit.getOfflinePlayer(uuid));
    }
    
    
    /**
     * If your item is a player skull you can apply a special player skull texture
     * @param player
     * @return
     */
    public ItemBuilder setSkullOwner(OfflinePlayer player){
    	if (itemMeta instanceof SkullMeta) {
    		SkullMeta skullMeta = (SkullMeta) itemMeta;
            skullMeta.setOwningPlayer(player);
            itemMeta = skullMeta;
    	}
        return this;
    }

    /**
     * If your item is a player skull you can apply a texture
     * value is the base64 value of the skull texture
     * You can find the value on https://minecraft-heads.com
     * @param texture Base64 texture value
     * @return
     */
    public ItemBuilder setSkullTexture(String texture){
        if (!(itemMeta instanceof SkullMeta)) return this;
        SkullMeta skullMeta = (SkullMeta) itemMeta;
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
        gameProfile.getProperties().put("textures", new Property("textures", texture));
        try{
            Field gameProfileField = skullMeta.getClass().getDeclaredField("profile");
            gameProfileField.setAccessible(true);
            gameProfileField.set(skullMeta, gameProfile);
            itemMeta = skullMeta;
        } catch (IllegalAccessException | NoSuchFieldException error) {
            error.printStackTrace();
        }
        return this;
    }

    /**
     * apply color on the potion bottle
     * @param color
     * @return
     */
    public ItemBuilder setPotionColor(Color color) {
        if (itemMeta instanceof PotionMeta) {
            PotionMeta potionMeta = (PotionMeta) itemMeta;
            potionMeta.setColor(color);
            itemMeta = potionMeta;
        }
        return this;
    }

    /**
     * apply potion effect type on the potion bottle
     * @param potionEffectType
     * @return
     */
    public ItemBuilder addPotionEffect(PotionEffectType potionEffectType) {
        addPotionEffect(potionEffectType, 10);
        return this;
    }

    /**
     * apply potion effect type with duration on the potion bottle
     * @param potionEffectType
     * @param duration
     * @return
     */
    public ItemBuilder addPotionEffect(PotionEffectType potionEffectType, int duration) {
        addPotionEffect(potionEffectType, duration, 1);
        return this;
    }

    /**
     * apply potion effect type with duration and level on the potion bottle
     * @param potionEffectType
     * @param duration
     * @param amplifier
     * @return
     */
    public ItemBuilder addPotionEffect(PotionEffectType potionEffectType, int duration, int amplifier) {
        addPotionEffect(potionEffectType, duration, amplifier, true);
        return this;
    }

    /**
     * apply potion effect type with duration, level and ambiance option on the potion bottle
     * @param potionEffectType
     * @param duration
     * @param amplifier
     * @param ambient
     * @return
     */
    public ItemBuilder addPotionEffect(PotionEffectType potionEffectType, int duration, int amplifier, boolean ambient) {
        addPotionEffect(potionEffectType, duration, amplifier, ambient, false);
        return this;
    }

    /**
     * apply potion effect type with duration, level and ambiance option on the potion bottle, can make this effect to overwrite
     * @param potionEffectType
     * @param duration
     * @param amplifier
     * @param ambient
     * @param overwrite
     * @return
     */
    public ItemBuilder addPotionEffect(PotionEffectType potionEffectType, int duration, int amplifier, boolean ambient, boolean overwrite) {
        if (!(itemMeta instanceof PotionMeta)) return this;
        PotionMeta potionMeta = (PotionMeta) itemMeta;
        potionMeta.addCustomEffect(new PotionEffect(potionEffectType, duration, amplifier, ambient), overwrite);
        itemMeta = potionMeta;
        return this;
    }

    /**
     * remove specific potion effect type
     * @param potionEffectType
     * @return
     */
    public ItemBuilder removePotionEffect(PotionEffectType potionEffectType) {
    	if (!(itemMeta instanceof PotionMeta)) return this;
        PotionMeta potionMeta = (PotionMeta) itemMeta;
        if (potionMeta == null || !potionMeta.hasCustomEffect(potionEffectType))
            return this;
        potionMeta.removeCustomEffect(potionEffectType);
        itemMeta = potionMeta;
        return this;
    }

    /**
     * remove all potion effect from a list
     * @param potionEffectTypes
     * @return
     */
    public ItemBuilder removePotionEffect(List<PotionEffectType> potionEffectTypes) {
        for (PotionEffectType potionEffectType : potionEffectTypes) {
            removePotionEffect(potionEffectType);
        }
        return this;
    }

    /**
     * clear potion effect on item
     * @return
     */
    public ItemBuilder clearPotionEffect() {
        if (this.getPotionEffects() == null)
            return this;
        for (PotionEffect potionEffect : this.getPotionEffects()) {
            removePotionEffect(potionEffect.getType());
        }
        return this;
    }

    /**
     * build item
     * @return
     */
    public ItemStack build() {
    	this.itemStack.setItemMeta(this.itemMeta);
        return itemStack;
    }

    /**
     * @param itemBuilder
     * returns if two item builder are exactly same
     * This method compare all parameter of items
     * @return
     */
    public boolean isExactlySame(ItemBuilder itemBuilder){
        return hasSameMaterial(itemBuilder) && hasSameDisplayName(itemBuilder)
                && hasSameAmount(itemBuilder) && hasSameDurability(itemBuilder) && hasSameEnchantment(itemBuilder)
                && hasSameItemFlag(itemBuilder) && hasSameLore(itemBuilder) && hasSameBreakableStat(itemBuilder);
    }

    /**
     * @param itemBuilder
     * returns if two item builder has same type
     * @return
     */
    public boolean hasSameMaterial(ItemBuilder itemBuilder){
        return getMaterial() == itemBuilder.getMaterial();
    }

    /**
     * @param itemBuilder
     * returns if two item builder has same display name
     * @return
     */
    public boolean hasSameDisplayName(ItemBuilder itemBuilder){
        return getDisplayName().equalsIgnoreCase(itemBuilder.getDisplayName());
    }

    /**
     * @param itemBuilder
     * returns if two item builder has same enchantments
     * @return
     */
    public boolean hasSameEnchantment(ItemBuilder itemBuilder){
        return getEnchantments().equals(itemBuilder.getEnchantments());
    }

    /**
     * @param itemBuilder
     * returns if two item builder has same item flags
     * @return
     */
    public boolean hasSameItemFlag(ItemBuilder itemBuilder){
        return getItemFlag().equals(itemBuilder.getItemFlag());
    }

    /**
     * @param itemBuilder
     * returns if two item builder has same lore
     * @return
     */
    public boolean hasSameLore(ItemBuilder itemBuilder){
        if (getLore() == null)
            return false;
        return getLore().equals(itemBuilder.getLore());
    }

    /**
     * @param itemBuilder
     * returns if two item builder has same amount
     * @return
     */
    public boolean hasSameAmount(ItemBuilder itemBuilder){
        return getAmount() == itemBuilder.getAmount();
    }

    /**
     * @param itemBuilder
     * returns if two item builder has same durability
     * @return
     */
    public boolean hasSameDurability(ItemBuilder itemBuilder){
        return getDurability() == itemBuilder.getDurability();
    }

    /**
     * @param itemBuilder
     * returns if two item builder has same breakable stat
     * @return
     */
    public boolean hasSameBreakableStat(ItemBuilder itemBuilder){
        return isUnbreakable() == itemBuilder.isUnbreakable();
    }

    /**
     * get type
     * @return
     */
    public Material getMaterial(){
        return itemStack.getType();
    }

    /**
     * get amount
     * @return
     */
    public int getAmount(){
        return itemStack.getAmount();
    }

    /**
     * get durability
     * @return
     */
    public int getDurability(){
        return (itemMeta instanceof Damageable) ? ((Damageable)itemMeta).getDamage() : 0;
    }

    /**
     * get item meta
     * @return
     */
    public ItemMeta getItemMeta(){
        return itemMeta;
    }
    
    /**
     * get item meta
     * @return
     */
    public ItemStack getItemStack(){
        return itemStack;
    }

    /**
     * get display name
     * @return
     */
    @SuppressWarnings("deprecation")
	public String getDisplayName(){
        return itemStack.hasItemMeta() && itemMeta.hasDisplayName() ? itemMeta.getDisplayName() : "";
    }

    /**
     * get enchant
     * @return
     */
    public Map<Enchantment, Integer> getEnchantments(){
        return this.itemStack.hasItemMeta() && this.itemMeta.hasEnchants() ? this.itemMeta.getEnchants() : null;
    }

    /**
     * get lore
     * @return
     */
    @SuppressWarnings("deprecation")
	public List<String> getLore(){
        return itemStack.hasItemMeta() && itemMeta.hasLore() ? itemMeta.getLore() : null;
    }

    /**
     * get item flag
     * @return
     */
    public Set<ItemFlag> getItemFlag(){
        return itemStack.hasItemMeta() && itemMeta.getItemFlags().size() > 0 ? itemMeta.getItemFlags() : null;
    }

    /**
     * get potion effects
     * @return
     */
    public List<PotionEffect> getPotionEffects() {
    	if (!(itemMeta instanceof PotionMeta potionMeta)) return null;
        return potionMeta.getCustomEffects().size() > 0 ? potionMeta.getCustomEffects() : null;
    }

    /**
     * get if item is or isn't unbreakable
     * @return
     */
    public boolean isUnbreakable(){
        return itemStack.hasItemMeta() && itemMeta.isUnbreakable();
    }

    protected Component toMiniMessage(String miniMessage) {
        return PaperComponentUtils.getMiniMessageTextComponent(miniMessage);
    }

}
