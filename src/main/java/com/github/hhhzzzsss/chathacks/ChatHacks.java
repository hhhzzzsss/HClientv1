package com.github.hhhzzzsss.chathacks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import com.github.hhhzzzsss.chathacks.mixin.EntityS2CPacketAccessor;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.Session;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkEncryptionUtils;
import net.minecraft.network.NetworkState;
import net.minecraft.network.ServerAddress;
import net.minecraft.network.listener.ClientLoginPacketListener;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;
import net.minecraft.network.packet.c2s.login.LoginKeyC2SPacket;
import net.minecraft.network.packet.c2s.login.LoginQueryResponseC2SPacket;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.KeepAliveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.SpectatorTeleportC2SPacket;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdatePlayerAbilitiesC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.s2c.login.LoginCompressionS2CPacket;
import net.minecraft.network.packet.s2c.login.LoginDisconnectS2CPacket;
import net.minecraft.network.packet.s2c.login.LoginHelloS2CPacket;
import net.minecraft.network.packet.s2c.login.LoginQueryRequestS2CPacket;
import net.minecraft.network.packet.s2c.login.LoginSuccessS2CPacket;
import net.minecraft.network.packet.s2c.play.AdvancementUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.BlockBreakingProgressS2CPacket;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.BlockEventS2CPacket;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.BossBarS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkDeltaUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkLoadDistanceS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkRenderDistanceCenterS2CPacket;
import net.minecraft.network.packet.s2c.play.CloseScreenS2CPacket;
import net.minecraft.network.packet.s2c.play.CombatEventS2CPacket;
import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket;
import net.minecraft.network.packet.s2c.play.CommandTreeS2CPacket;
import net.minecraft.network.packet.s2c.play.ConfirmScreenActionS2CPacket;
import net.minecraft.network.packet.s2c.play.CooldownUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.CraftFailedResponseS2CPacket;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.network.packet.s2c.play.DifficultyS2CPacket;
import net.minecraft.network.packet.s2c.play.DisconnectS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitiesDestroyS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityAnimationS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityAttachS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityAttributesS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySetHeadYawS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExperienceBarUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExperienceOrbSpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.network.packet.s2c.play.HealthUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.HeldItemChangeS2CPacket;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.network.packet.s2c.play.ItemPickupAnimationS2CPacket;
import net.minecraft.network.packet.s2c.play.KeepAliveS2CPacket;
import net.minecraft.network.packet.s2c.play.LightUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.LookAtS2CPacket;
import net.minecraft.network.packet.s2c.play.MapUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.MobSpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.OpenHorseScreenS2CPacket;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.network.packet.s2c.play.OpenWrittenBookS2CPacket;
import net.minecraft.network.packet.s2c.play.PaintingSpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundFromEntityS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundIdS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerAbilitiesS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerActionResponseS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListHeaderS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerSpawnPositionS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerSpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.RemoveEntityStatusEffectS2CPacket;
import net.minecraft.network.packet.s2c.play.ResourcePackSendS2CPacket;
import net.minecraft.network.packet.s2c.play.ScoreboardDisplayS2CPacket;
import net.minecraft.network.packet.s2c.play.ScoreboardObjectiveUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ScoreboardPlayerUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ScreenHandlerPropertyUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.SelectAdvancementTabS2CPacket;
import net.minecraft.network.packet.s2c.play.SetCameraEntityS2CPacket;
import net.minecraft.network.packet.s2c.play.SetTradeOffersS2CPacket;
import net.minecraft.network.packet.s2c.play.SignEditorOpenS2CPacket;
import net.minecraft.network.packet.s2c.play.StatisticsS2CPacket;
import net.minecraft.network.packet.s2c.play.StopSoundS2CPacket;
import net.minecraft.network.packet.s2c.play.SynchronizeRecipesS2CPacket;
import net.minecraft.network.packet.s2c.play.SynchronizeTagsS2CPacket;
import net.minecraft.network.packet.s2c.play.TagQueryResponseS2CPacket;
import net.minecraft.network.packet.s2c.play.TeamS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.network.packet.s2c.play.UnloadChunkS2CPacket;
import net.minecraft.network.packet.s2c.play.UnlockRecipesS2CPacket;
import net.minecraft.network.packet.s2c.play.VehicleMoveS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldBorderS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldEventS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class ChatHacks implements ModInitializer {
	private static MinecraftClient mc;
	public static ChatHacks chatHacks = new ChatHacks();

	private static Random random = new Random();

	public static String username = null;
	// §b§lhhhzzzsss
	// \u0FE1
	// \u0FE1h\u0FE1hhzzzsss
	// §b§a
	public static Session session = new Session(username, "", "", "mojang");

	public static ArrayList<BotPlayManager> botList = new ArrayList<BotPlayManager>();
	public static PlayerAbilities newBotAbilities = new PlayerAbilities();

	public static int clickTime = 4;
	public static int clickAmount = 1;
	public static long serverStartTime = System.currentTimeMillis();

	public static int botNumber = 0;
	public static double orbitAngle = 0.0;

	public static enum HitMode {
		REGULAR, MULTI
	};

	public static HitMode hitMode = HitMode.MULTI;

	public static boolean immortal = false;
	public static boolean dropping = false;
	public static boolean scaffold = false;
	public static boolean push = true;
	public static boolean recordChat = false;
	public static String targetUUID = "";

	public static String[] chatBlackList = {
			" *", // Blank spaces
			".*\n\n\n\n\n\n+.*", // Blank spaces
			};

	public static int commandLogMode = 1;

	public static ItemStack trollSword;
	public static int rainbowIndex = 0;
	
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		mc = MinecraftClient.getInstance();
		
		CompoundTag abilitiesTag = new CompoundTag();
		abilitiesTag.putBoolean("invulnerable", true);
		abilitiesTag.putBoolean("flying", true);
		abilitiesTag.putBoolean("mayfly", true);
		abilitiesTag.putBoolean("instabuild", true);
		abilitiesTag.putBoolean("mayBuild", true);
		abilitiesTag.putFloat("flySpeed", 1.0f);
		abilitiesTag.putFloat("walkSpeed", 1.0f);
		CompoundTag abilitiesTag2 = new CompoundTag();
		abilitiesTag2.put("abilities", abilitiesTag);
		newBotAbilities.deserialize(abilitiesTag2);
		
		CompoundTag swordTag = new CompoundTag();
		swordTag.putString("id", "diamond_sword");
		swordTag.putByte("Count", (byte) 1);
		trollSword = ItemStack.fromTag(swordTag);
		
		Chatter.init();
		
		System.out.println("ChatHacks has been initialized!");
	}
	
	public static boolean isSpam(Text chatComponent) {
		String chat = chatComponent.getString();
		for (String s : chatBlackList) {
			if (chat.matches(s)) {
				return true;
			}
		}
		//if (chat.matches(".*/username.*")) {
		//	Chatter.processUsername(chat);
		//}
		String siblingStyle = chatComponent.getSiblings().size() == 0 ? "" : ChathacksUtil.styleAsString(chatComponent.getSiblings().get(0).getStyle());
		if (chat.matches(".{0,16}: /.*")/* && siblingStyle.contains("§b")*/) {
			Chatter.processCommandSpy(chat);
			if (commandLogMode == 0) {
				return true;
			} else if (commandLogMode == 2) {
				return false;
			} 
			else { //commandLogMode == 1; doesn't work in latest change to commandspy
				return true;
				/*if (chat.matches("§a.*"))
					return false;
				else
					return true;*/
			}
		}

		if (recordChat) {
			Chatter.processCommandSpy("§r" + chat + "§r");
			return false;
		}

		return false;
	}
	
	public static void convertChatMessages(String chatMessage, ClientPlayNetworkHandler connection) {
		String message = chatMessage.replaceAll("\\$uname", username);
		if (message.contains("$plist")) {
			String[] players = Chatter.playerList.getText().split("\\r?\\n");
			for (String s : players) {
				if (!(s.length() == 0)) {
					convertChatMessages(message.replaceAll("\\$plist", s), connection);
				}
			}
			return;
		}
		/*if (message.contains("$tlist") || message.contains("$ulist")) {
			Collection<PlayerListEntry> players = connection.getPlayerList();
			for (PlayerListEntry s : players) {
				String name = s.getProfile().getName().replaceAll("§[A-Za-z0-9]", "");
				String uuid = s.getProfile().getId().toString();
				convertChatMessages(message.replaceAll("\\$tlist", name).replaceAll("\\$ulist", uuid), connection);
			}
			return;
		}*/
		if (message.contains("$uuid")) {
			message = message.replaceAll("\\$uuid", targetUUID);
		}
		if (message.contains("$coords")) {
			BlockPos bp = mc.player.getBlockPos();
			message = message.replaceAll("\\$coords", String.format("%d %d %d", bp.getX(), bp.getY(), bp.getZ()));
		}
		if (message.contains("$mapcoords")) {
			BlockPos bp = mc.player.getBlockPos();
			int rawx = Math.floorDiv(bp.getX()+64, 128);
			int rawz = Math.floorDiv(bp.getZ()+64, 128);
			message = message.replaceAll("\\$mapcoords", String.format("%d %d %d", rawx*128-64, bp.getY(), rawz*128-64-1));
		}
		if (message.equalsIgnoreCase("fc")) { // fastclick
			ChatHacks.clickTime = 0;
			addChatMessage("Fast Click enabled");
		} else if (message.equalsIgnoreCase("rc")) { // resetclick
			ChatHacks.clickTime = 4;
			addChatMessage("Regular click enabled");
		} else if (message.length() > 6 && message.substring(0, 6).equalsIgnoreCase("throw ")) {
			try {
				ChatHacks.clickAmount = Integer.parseInt(message.substring(6));
			} catch (NumberFormatException e) {
				addChatMessage("Invalid number");
				return;
			}
			addChatMessage("Throw amount set to " + ChatHacks.clickAmount);
		} else if (message.equalsIgnoreCase("immortal")) {
			immortal = !immortal;
			if (immortal)
				addChatMessage("Immortality enabled");
			else
				addChatMessage("Immortality disabled");
		} else if (message.equalsIgnoreCase("drop")) {
			dropping = !dropping;
			if (dropping)
				addChatMessage("Dropping enabled");
			else
				addChatMessage("Dropping disabled");
		} else if (message.equalsIgnoreCase("scaffold")) {
			scaffold = !scaffold;
			if (scaffold)
				addChatMessage("Scaffolding enabled");
			else
				addChatMessage("Scaffolding disabled");
		} else if (message.length() > 12 && message.substring(0, 12).equalsIgnoreCase("dropexplode ")) {
			int explodeAmount;
			try {
				explodeAmount = Integer.parseInt(message.substring(12));
			} catch (NumberFormatException e) {
				addChatMessage("Invalid number");
				return;
			}
			addChatMessage("Exploding " + explodeAmount + " items for both mainhand and offhand");
			for (int i = 0; i < explodeAmount; i++) {
				mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookOnly(random.nextFloat() * 180.0f - 90.0f,
						random.nextFloat() * 360.0f, mc.player.isOnGround()));
				mc.player.networkHandler
						.sendPacket(new CreativeInventoryActionC2SPacket(-1, mc.player.getStackInHand(Hand.MAIN_HAND)));
				mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookOnly(random.nextFloat() * 180.0f - 90.0f,
						random.nextFloat() * 360.0f, mc.player.isOnGround()));
				mc.player.networkHandler
						.sendPacket(new CreativeInventoryActionC2SPacket(-1, mc.player.getStackInHand(Hand.OFF_HAND)));
			}
		} else if (message.equalsIgnoreCase("push")) {
			push = !push;
			if (push) {
				mc.player.pushSpeedReduction = 0.0f;
				addChatMessage("Pushing enabled");
			} else {
				mc.player.pushSpeedReduction = 1.0f;
				addChatMessage("Pushing disabled");
			}
		} else if (message.equalsIgnoreCase("flymode")) {
			mc.player.abilities.allowFlying = !mc.player.abilities.allowFlying;
			if (mc.player.abilities.allowFlying) {
				addChatMessage("Flying enabled");
			} else {
				addChatMessage("Flying disabled");
			}
		} else if (message.equalsIgnoreCase("randTP") || message.equalsIgnoreCase("rtp")) {
			double x = mc.player.getX();
			double y = mc.player.getY();
			double z = mc.player.getZ();
			double x_change = (Math.random()-0.5)*2000.0;
			double z_change = (Math.random()-0.5)*2000.0;
			double y_change = 256-y;
			while (y_change > 50.0) {
				y += 50.0;
				y_change -= 50.0;
				setPosition(x, y, z);
			}
			if (y_change > 0.0) {
				y += y_change;
				setPosition(x, y, z);
			}
			x_change /= 15.0;
			z_change /=15.0;
			for (int i=0; i<15; i++) {
				x += x_change;
				z += z_change;
				setPosition(x, y, z);
			}
		} else if (message.equalsIgnoreCase("recordchat")) {
			recordChat = !recordChat;
			if (recordChat)
				addChatMessage("Recording chat in command log");
			else
				addChatMessage("No longer recording chat in command log");
		} else if (message.equalsIgnoreCase("clearlogs")) {
			Chatter.clearLogs();
			addChatMessage("Logs cleared");
		} else if (message.equalsIgnoreCase("listuuid") || message.equalsIgnoreCase("uuidlist")) {
			Collection<PlayerListEntry> players = connection.getPlayerList();
			int uuidindex = 0;
			for (PlayerListEntry s : players) {
				addChatMessage(String.format("%02d", uuidindex) + ": " + s.getProfile().getName());
				uuidindex++;
			}

		} else if (message.equalsIgnoreCase("setuuid")) {
			Collection<PlayerListEntry> players = connection.getPlayerList();
			int uuidindex = 0;
			for (PlayerListEntry s : players) {
				addChatMessage(String.format("%02d", uuidindex) + ": " + s.getProfile().getName());
				uuidindex++;
			}

		} else if (message.length() > 8 && message.substring(0, 8).equalsIgnoreCase("setuuid ")) {
			String[] commandParts = message.split(" ", 2);
			Collection<PlayerListEntry> players = connection.getPlayerList();
			int uuidindex = 0;
			int ID;
			try {
				ID = Integer.parseInt(commandParts[1]);
			} catch (NumberFormatException e) {
				addChatMessage("invalid ID");
				return;
			}
			for (PlayerListEntry s : players) {
				if (uuidindex == ID) {
					targetUUID = s.getProfile().getId().toString();
					break;
				}
				uuidindex++;
			}
		} else if (message.equalsIgnoreCase("saveuuid")) {
			Collection<PlayerListEntry> players = connection.getPlayerList();
			for (PlayerListEntry s : players) {
				UUID uuid = s.getProfile().getId();
				String name = s.getProfile().getName();
				try {
					UUID offlineUUID = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes("UTF-8"));
					if (uuid.equals(offlineUUID)) {
						Chatter.uuidMap.put(uuid.toString(), name);
						Chatter.saveUUIDs();
						Chatter.updateUsernameList();
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		} else if (message.equalsIgnoreCase("updateuuid")) {
			new Thread() {
				public void run() {
					new PlayerListUpdaterBot();
				}
			}.start();
		} else if (message.equalsIgnoreCase("give")) {
			PlayerInventory playerInventory = mc.player.inventory;
			playerInventory.addPickBlock(trollSword);
			mc.interactionManager.clickCreativeStack(mc.player.getStackInHand(Hand.MAIN_HAND), 36 + playerInventory.selectedSlot);
		} else if (message.length() > 5 && message.substring(0, 5).equalsIgnoreCase("give ")) {
			String[] commandParts = message.split(" ", 2);
			String NBT_Str;
			ItemStack stack;
			try {
				File nbtData = new File("chathacks nbt files/" + commandParts[1] + ".txt"); // Requires UTF-8 Encoding
				InputStreamReader reader = new InputStreamReader(new FileInputStream(nbtData), StandardCharsets.UTF_8);
				char charArray[] = new char[(int) nbtData.length()];
				reader.read(charArray);
				reader.close();
				NBT_Str = new String(charArray);
				NBT_Str = NBT_Str.substring(NBT_Str.indexOf('{'), NBT_Str.lastIndexOf('}') + 1); // Removes Encoding
																									// Header/Footer
			} catch (IOException e) {
				addChatMessage("Failed to read \"chathacks nbt files/" + commandParts[1] + ".txt\": " + e.getMessage());
				return;
			}
			try {
				stack = ItemStack.fromTag(StringNbtReader.parse(NBT_Str));
			} catch (CommandSyntaxException e) {
				addChatMessage("Failed to convert to NBT: " + e.getMessage());
				return;
			}
			PlayerInventory playerInventory = mc.player.inventory;
			playerInventory.addPickBlock(stack);
			mc.interactionManager.clickCreativeStack(mc.player.getStackInHand(Hand.MAIN_HAND),
					36 + playerInventory.selectedSlot);
		} else if (message.equalsIgnoreCase("logNBT") || message.equalsIgnoreCase("getNBT")) {
			File log = new File("NBT_Log.txt");
			try {
				log.createNewFile();
			} catch (IOException e) {
				addChatMessage("Failed to create log file: " + e.getMessage());
				return;
			}
			try {
				OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(log), StandardCharsets.UTF_8);
				writer.write(mc.player.getStackInHand(Hand.MAIN_HAND).toTag(new CompoundTag()).toString());
				writer.flush();
				writer.close();
			} catch (IOException e) {
				addChatMessage("Failed to write to log file: " + e.getMessage());
				return;
			}
		} else if (message.equalsIgnoreCase("fromNBT")) {
			String NBT_Str;
			ItemStack stack;
			try {
				File nbtData = new File("NBT_Give.txt"); // Requires UTF-8 Encoding
				InputStreamReader reader = new InputStreamReader(new FileInputStream(nbtData),
						Charset.forName("UTF-8").newDecoder());
				char charArray[] = new char[(int) nbtData.length()];
				reader.read(charArray);
				reader.close();
				NBT_Str = new String(charArray);
				NBT_Str = NBT_Str.substring(NBT_Str.indexOf('{'), NBT_Str.lastIndexOf('}') + 1); // Removes Encoding
																									// Header/Footer
			} catch (IOException e) {
				addChatMessage("Failed to read NBT_Give.txt: " + e.getMessage());
				return;
			}
			try {
				stack = ItemStack.fromTag(StringNbtReader.parse(NBT_Str));
			} catch (CommandSyntaxException e) {
				addChatMessage("Failed to convert to NBT: " + e.getMessage());
				return;
			}
			PlayerInventory playerInventory = mc.player.inventory;
			playerInventory.addPickBlock(stack);
			mc.interactionManager.clickCreativeStack(mc.player.getStackInHand(Hand.MAIN_HAND),
					36 + playerInventory.selectedSlot);
		} else if (message.length() > 8 && message.substring(0, 8).equalsIgnoreCase("saveNBT ")) {
			String[] commandParts = message.split(" ", 2);
			File nbtData = new File("chathacks nbt files/" + commandParts[1] + ".txt");
			try {
				if (!nbtData.createNewFile()) {
					addChatMessage("File already exists");
					return;
				}
			} catch (IOException e) {
				addChatMessage("Failed to create file \"chathacks nbt files/" + commandParts[1] + ".txt\": " + e.getMessage());
				return;
			}
			try {
				OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(nbtData), StandardCharsets.UTF_8);
				writer.write(mc.player.getStackInHand(Hand.MAIN_HAND).toTag(new CompoundTag()).toString());
				writer.flush();
				writer.close();
			} catch (IOException e) {
				addChatMessage("Failed to write to \"chathacks nbt files/" + commandParts[1] + ".txt\": " + e.getMessage());
				return;
			}
		} else if (message.length() > 4 && message.substring(0, 4).equalsIgnoreCase("stp ")) {
			String[] commandParts = message.split(" ", 2);
			if (commandParts.length == 2) {
				try {
					UUID uuid = UUID.fromString(commandParts[1]);
					connection.sendPacket(new SpectatorTeleportC2SPacket(uuid));
					return;
				}
				catch (IllegalArgumentException e) {
					addChatMessage("Error: " + e.getMessage());
					return;
				}
			}
		} else if (message.length() > 3 && message.substring(0, 3).equalsIgnoreCase("cb ")) {
			String[] commandParts = message.split(" ", 2);
			if (commandParts.length == 2) {
				PlayerInventory playerInventory = mc.player.inventory;
				CompoundTag commandBlockTag = new CompoundTag();
				commandBlockTag.putString("id", "minecraft:command_block");
				commandBlockTag.putByte("Count", (byte) 1);
				
				CompoundTag tag = new CompoundTag();
				commandBlockTag.put("tag", tag);
				
				CompoundTag blockEntityTag = new CompoundTag();
				tag.put("BlockEntityTag", blockEntityTag);
				blockEntityTag.putByte("auto", (byte) 1);
				blockEntityTag.putString("Command", commandParts[1]);
				
				playerInventory.setStack(playerInventory.selectedSlot, ItemStack.fromTag(commandBlockTag));
				mc.interactionManager.clickCreativeStack(mc.player.getStackInHand(Hand.MAIN_HAND), 36 + playerInventory.selectedSlot);

				BlockPos bp = (new BlockPos(mc.player.getPos())).down(3);
				if (!mc.world.isAir(bp)) {
					mc.interactionManager.attackBlock(bp, Direction.UP);
				}
				mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(new Vec3d(0,0,0),Direction.DOWN,bp,false));
		    	
			} else {
				addChatMessage("invalid cb command");
				return;
			}
		} else if (message.length() > 6 && message.substring(0, 6).equalsIgnoreCase("crypt ")) {
			String[] commandParts = message.split(" ", 2);
			if (commandParts.length == 2) {
		    	try {
		    		MessageDigest md = MessageDigest.getInstance("SHA-256");
		    		String time = "" + (System.currentTimeMillis() / 10000);
		    		String salt = "key goes here";
		    		String raw = "#" + commandParts[1].replaceAll("&[0-9a-fklmnor]", "") + ";" + mc.player.getEntityName().replaceAll("§[0-9a-fklmnor]", "") + ";" + time + ";" + salt;
		    		byte[] hash = md.digest(raw.getBytes(StandardCharsets.UTF_8));
		    		BigInteger big_int = new BigInteger(1, Arrays.copyOfRange(hash, 0, 4));
		    		String strHash = big_int.toString(Character.MAX_RADIX);
					connection.sendPacket(new ChatMessageC2SPacket("#" + commandParts[1] + " " + strHash));
					return;
		    	}
		    	catch (NoSuchAlgorithmException e) {
					addChatMessage("No such algorithm exception");
		    		e.printStackTrace();
		    		return;
		    	}
			} else {
				addChatMessage("invalid crypt command");
				return;
			}
		} else if (message.length() > 5 && message.substring(0, 5).equalsIgnoreCase("cval ")) {
			String[] commandParts = message.split(" ", 2);
			if (commandParts.length == 2) {
		    	try {
		    		String secret = "NABQ4YPC4PVT7JXD6U2TRKZ4ILHHAP3Y";
		    		String token = TimeBasedOneTimePasswordUtil.generateCurrentNumberString(secret);
					connection.sendPacket(new ChatMessageC2SPacket("`" + commandParts[1] + " " + token));
		    	} catch (GeneralSecurityException e) {
		    		addChatMessage("General security exception");
					e.printStackTrace();
					return;
				}
			} else {
				addChatMessage("invalid crypt command");
				return;
			}
		} else if (message.length() > 12 && message.substring(0, 12).equalsIgnoreCase("rainbowchat ")) {
			String[] commandParts = message.split(" ", 2);
			String colorCodes = "c6eab9d";
			if (commandParts.length == 2) {
				StringBuilder sb = new StringBuilder();
				int strIdx = 0;
				for (int i=0; i<commandParts[1].length(); i++) {
					int colorIndex = (strIdx+rainbowIndex) % colorCodes.length();
					sb.append("&" + colorCodes.charAt(colorIndex) + commandParts[1].charAt(i));
					if (commandParts[1].charAt(i) != ' ') {
						strIdx++;
					}
				}
				connection.sendPacket(new ChatMessageC2SPacket(sb.toString()));
			}
			if (rainbowIndex == 0) {
				rainbowIndex = colorCodes.length()-1;
			}
			else {
				rainbowIndex--;
			}
		} else if (message.length() > 4 && message.substring(0, 4).equalsIgnoreCase("bot ")) {
			String chat = message.substring(4);
			StringBuilder name = new StringBuilder(8);
			for (int i = 0; i < 8; i++) {
				name.append((char) (random.nextInt(128) + 128));
			}
			new Thread() {
				public void run() {
					//synchronized(botList) {
						createBot(name.toString(), chat);
					//}
				}
			}.start();
		} else if (message.length() > 8 && message.substring(0, 8).equalsIgnoreCase("botname ")) {
			String[] commandParts = message.split(" ", 3);
			if (commandParts.length == 3) {
				String name = commandParts[1];
				String chat = commandParts[2];
				new Thread() {
					public void run() {
						//synchronized (botList) {
							createBot(name, chat);
						//}
					}
				}.start();
			} else {
				addChatMessage("invalid botname command");
				return;
			}
		} else if (message.length() > 7 && message.substring(0, 7).equalsIgnoreCase("botsay ")) {
			String[] commandParts = message.split(" ", 2);
			if (commandParts.length == 2) {
				for (BotPlayManager b : botList) {
					b.sendPacket(new ChatMessageC2SPacket(commandParts[1]));
				}
			} else {
				addChatMessage("invalid botsay command");
				return;
			}
		} else if (message.length() > 9 && message.substring(0, 9).equalsIgnoreCase("botsayid ")) {
			String[] commandParts = message.split(" ", 3);
			if (commandParts.length == 3) {
				int ID;
				try {
					ID = Integer.parseInt(commandParts[1]);
				} catch (NumberFormatException e) {
					addChatMessage("invalid ID");
					return;
				}
				for (BotPlayManager b : botList) {
					if (b.ID == ID) {
						b.sendPacket(new ChatMessageC2SPacket(commandParts[2]));
						return;
					}
				}
			} else {
				addChatMessage("invalid botsay command");
				return;
			}
		} else if (message.length() > 8 && message.substring(0, 8).equalsIgnoreCase("botmove ")) {
			String[] commandParts = message.split(" ", 3);
			if (commandParts.length == 3) {
				BotPlayManager.Movement movementType = BotPlayManager.Movement.IDLE;
				if (commandParts[2].equalsIgnoreCase("idle") || commandParts[2].equalsIgnoreCase("i")) {
					;
				} else if (commandParts[2].equalsIgnoreCase("walk") || commandParts[2].equalsIgnoreCase("w")) {
					movementType = BotPlayManager.Movement.WALK;
				} else if (commandParts[2].equalsIgnoreCase("orbit") || commandParts[2].equalsIgnoreCase("o")) {
					movementType = BotPlayManager.Movement.ORBIT;
				} else if (commandParts[2].equalsIgnoreCase("flank") || commandParts[2].equalsIgnoreCase("f")) {
					movementType = BotPlayManager.Movement.FLANK;
				} else if (commandParts[2].equalsIgnoreCase("random") || commandParts[2].equalsIgnoreCase("r")) {
					movementType = BotPlayManager.Movement.RANDOM;
				} else {
					addChatMessage("Invalid movement type");
					return;
				}

				if (commandParts[1].equalsIgnoreCase("all")) {
					for (BotPlayManager b : botList) {
						b.movement = movementType;
					}
					return;
				}

				int ID;
				try {
					ID = Integer.parseInt(commandParts[1]);
				} catch (NumberFormatException e) {
					addChatMessage("invalid ID");
					return;
				}
				for (BotPlayManager b : botList) {
					if (b.ID == ID) {
						b.movement = movementType;
						return;
					}
				}
			} else {
				addChatMessage("invalid botmove command");
				return;
			}
		} else if (message.length() > 8 && message.substring(0, 8).equalsIgnoreCase("botcopy ")) {
			String[] commandParts = message.split(" ", 3);
			if (commandParts.length >= 2) {
				boolean copy = true;

				if (commandParts.length == 3) {
					if (commandParts[2].equalsIgnoreCase("off"))
						copy = false;
				}

				if (commandParts[1].equalsIgnoreCase("all")) {
					for (BotPlayManager b : botList) {
						b.copy = copy;
					}
					return;
				}

				int ID;
				try {
					ID = Integer.parseInt(commandParts[1]);
				} catch (NumberFormatException e) {
					addChatMessage("invalid ID");
					return;
				}
				for (BotPlayManager b : botList) {
					if (b.ID == ID) {
						b.copy = copy;
						return;
					}
				}
			} else {
				addChatMessage("invalid botcopy command");
				return;
			}
		} else if (message.length() > 8 && message.substring(0, 8).equalsIgnoreCase("botdrop ")) {
			String[] commandParts = message.split(" ", 3);
			if (commandParts.length >= 2) {
				boolean drop = true;

				if (commandParts.length == 3) {
					if (commandParts[2].equalsIgnoreCase("off"))
						drop = false;
				}

				if (commandParts[1].equalsIgnoreCase("all")) {
					for (BotPlayManager b : botList) {
						b.drop = drop;
					}
					return;
				}

				int ID;
				try {
					ID = Integer.parseInt(commandParts[1]);
				} catch (NumberFormatException e) {
					addChatMessage("invalid ID");
					return;
				}
				for (BotPlayManager b : botList) {
					if (b.ID == ID) {
						b.drop = drop;
						return;
					}
				}
			} else {
				addChatMessage("invalid botdrop command");
				return;
			}
		} else if (message.length() > 10 && message.substring(0, 10).equalsIgnoreCase("botattack ")) {
			String[] commandParts = message.split(" ", 3);
			if (commandParts.length >= 2) {
				boolean attack = true;

				if (commandParts.length == 3) {
					if (commandParts[2].equalsIgnoreCase("off"))
						attack = false;
				}

				if (commandParts[1].equalsIgnoreCase("all")) {
					for (BotPlayManager b : botList) {
						b.attack = attack;
					}
					return;
				}

				int ID;
				try {
					ID = Integer.parseInt(commandParts[1]);
				} catch (NumberFormatException e) {
					addChatMessage("invalid ID");
					return;
				}
				for (BotPlayManager b : botList) {
					if (b.ID == ID) {
						b.attack = attack;
						return;
					}
				}
			} else {
				addChatMessage("invalid botattack command");
				return;
			}
		} else if (message.length() > 8 && message.substring(0, 8).equalsIgnoreCase("hitmode ")) {
			String[] commandParts = message.split(" ", 2);
			if (commandParts.length == 2) {
				if (commandParts[1].equalsIgnoreCase("regular") || commandParts[1].equalsIgnoreCase("r")) {
					hitMode = HitMode.REGULAR;
				} else if (commandParts[1].equalsIgnoreCase("multi") || commandParts[1].equalsIgnoreCase("m")) {
					hitMode = HitMode.MULTI;
				} else {
					addChatMessage("Invalid hit type");
					return;
				}
			} else {
				addChatMessage("invalid hitmode command");
				return;
			}
		} else if (message.length() > 8 && message.substring(0, 8).equalsIgnoreCase("bothand ")) {
			String[] commandParts = message.split(" ");
			if (commandParts.length == 2) {
				if (commandParts[1].equalsIgnoreCase("all")) {
					for (BotPlayManager b : botList) {
						b.handItem = mc.player.getStackInHand(Hand.MAIN_HAND);
						b.sendPacket(new CreativeInventoryActionC2SPacket(36, b.handItem));
						b.sendPacket(new UpdateSelectedSlotC2SPacket(0));
					}
					return;
				}

				int ID;
				try {
					ID = Integer.parseInt(commandParts[1]);
				} catch (NumberFormatException e) {
					addChatMessage("invalid ID");
					return;
				}
				for (BotPlayManager b : botList) {
					if (b.ID == ID) {
						b.handItem = mc.player.getStackInHand(Hand.MAIN_HAND);
						b.sendPacket(new CreativeInventoryActionC2SPacket(36, b.handItem));
						b.sendPacket(new UpdateSelectedSlotC2SPacket(0));
						return;
					}
				}
			} else {
				addChatMessage("invalid bothand command");
				return;
			}
		} else if (message.length() > 8 && message.substring(0, 8).equalsIgnoreCase("bottoss ")) {
			String[] commandParts = message.split(" ", 3);
			if (commandParts.length == 3) {
				String NBT_Str;
				ItemStack stack;
				try {
					File nbtData = new File("chathacks nbt files/" + commandParts[2] + ".txt"); // Requires UTF-8 Encoding
					InputStreamReader reader = new InputStreamReader(new FileInputStream(nbtData),
							Charset.forName("UTF-8").newDecoder());
					char charArray[] = new char[(int) nbtData.length()];
					reader.read(charArray);
					reader.close();
					NBT_Str = new String(charArray);
					NBT_Str = NBT_Str.substring(NBT_Str.indexOf('{'), NBT_Str.lastIndexOf('}') + 1); // Removes Encoding
																										// Header/Footer
				} catch (IOException e) {
					addChatMessage("Failed to read \"chathacks nbt files/" + commandParts[1] + ".txt\": " + e.getMessage());
					return;
				}
				try {
					stack = ItemStack.fromTag(StringNbtReader.parse(NBT_Str));
				} catch (CommandSyntaxException e) {
					addChatMessage("Failed to convert to NBT: " + e.getMessage());
					return;
				}
				if (commandParts[1].equalsIgnoreCase("all")) {
					for (BotPlayManager b : botList) {
						b.sendPacket(new CreativeInventoryActionC2SPacket(-1, stack));
					}
					return;
				}

				int ID;
				try {
					ID = Integer.parseInt(commandParts[1]);
				} catch (NumberFormatException e) {
					addChatMessage("invalid ID");
					return;
				}
				for (BotPlayManager b : botList) {
					if (b.ID == ID) {
						b.sendPacket(new CreativeInventoryActionC2SPacket(-1, stack));
						return;
					}
				}
			}
			else {
				addChatMessage("invalid bottoss command");
			}
		/*} else if (message.length() > 8 && message.substring(0, 8).equalsIgnoreCase("botswap ")) { // glitchy and doesn't work properly
			String[] commandParts = message.split(" ", 2);
			int ID;
			try {
				ID = Integer.parseInt(commandParts[1]);
			} catch (NumberFormatException e) {
				addChatMessage("invalid ID");
				return;
			}
			for (BotPlayManager b : botList) {
				if (b.ID == ID) {
					ClientConnection botnetmanager = b.clientConnection;
					b.clientConnection = mc.player.networkHandler.getConnection();
					mc.player.networkHandler.setClientConnection(botnetmanager);
					return;
				}
			}*/
		} else if (message.length() > 9 && message.substring(0, 9).equalsIgnoreCase("botplace ")) {
			String[] commandParts = message.split(" ", 3);
			if (commandParts.length == 3) {
				String NBT_Str;
				ItemStack stack;
				try {
					File nbtData = new File("chathacks nbt files/" + commandParts[2] + ".txt"); // Requires UTF-8 Encoding
					InputStreamReader reader = new InputStreamReader(new FileInputStream(nbtData),
							Charset.forName("UTF-8").newDecoder());
					char charArray[] = new char[(int) nbtData.length()];
					reader.read(charArray);
					reader.close();
					NBT_Str = new String(charArray);
					NBT_Str = NBT_Str.substring(NBT_Str.indexOf('{'), NBT_Str.lastIndexOf('}') + 1); // Removes Encoding
																										// Header/Footer
				} catch (IOException e) {
					addChatMessage("Failed to read \"chathacks nbt files/" + commandParts[1] + ".txt\": " + e.getMessage());
					return;
				}
				try {
					stack = ItemStack.fromTag(StringNbtReader.parse(NBT_Str));
				} catch (CommandSyntaxException e) {
					addChatMessage("Failed to convert to NBT: " + e.getMessage());
					return;
				}
				if (commandParts[1].equalsIgnoreCase("all")) {
					
					for (BotPlayManager b : botList) {
						b.handItem = stack;
						b.sendPacket(new UpdateSelectedSlotC2SPacket(0));
						b.sendPacket(new CreativeInventoryActionC2SPacket(36, b.handItem));
						BlockPos bp = new BlockPos(b.x, b.y-1, b.z);
						b.sendPacket(new PlayerActionC2SPacket(Action.START_DESTROY_BLOCK, bp, Direction.UP));
						b.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, new BlockHitResult(new Vec3d(0,0,0),Direction.DOWN,bp,false)));
					}
					return;
				}

				int ID;
				try {
					ID = Integer.parseInt(commandParts[1]);
				} catch (NumberFormatException e) {
					addChatMessage("invalid ID");
					return;
				}
				for (BotPlayManager b : botList) {
					if (b.ID == ID) {
						b.handItem = stack;
						b.sendPacket(new UpdateSelectedSlotC2SPacket(0));
						b.sendPacket(new CreativeInventoryActionC2SPacket(36, b.handItem));
						BlockPos bp = new BlockPos(b.x, b.y-1, b.z);
						b.sendPacket(new PlayerActionC2SPacket(Action.START_DESTROY_BLOCK, bp, Direction.UP));
						b.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, new BlockHitResult(new Vec3d(0,0,0),Direction.DOWN,bp,false)));
					}
					
				}
			}
			else {
				addChatMessage("invalid botplace command");
			}
		} else if (message.length() > 6 && message.substring(0, 6).equalsIgnoreCase("cmode ")) { // change command log mode
			String[] commandParts = message.split(" ", 2);
			if (commandParts[1].equals("0")) {
				commandLogMode = 0;
				addChatMessage("Command Log Mode set to 0: comamnds do not appear in chat");
			}
			else if (commandParts[1].equals("1")) {
				commandLogMode = 1;
				addChatMessage("Command Log Mode set to 1: commands of people with commandspy appear in chat");
			}
			else if (commandParts[1].equals("2")) {
				commandLogMode = 2;
				addChatMessage("Command Log Mode set to 2: all commands appear in chat");
			}
			else {
				addChatMessage("invalid mode: " + commandParts[1]);
			}
		} else if (message.equalsIgnoreCase("dball")) { // disconnect all bots
			for (BotPlayManager b : botList) {
				b.disconnect();
				botNumber = 0;
			}
			botList.clear();
		} else if (message.length() > 3 && message.substring(0, 3).equalsIgnoreCase("db ")) {
			String[] commandParts = message.split(" ", 3);
			if (commandParts.length == 2) {
				int ID;
				try {
					ID = Integer.parseInt(commandParts[1]);
				} catch (NumberFormatException e) {
					addChatMessage("invalid ID");
					return;
				}
				for (BotPlayManager b : botList) {
					if (b.ID == ID) {
						b.disconnect();
						//botList.remove(b);
						return;
					}
				}
			} else {
				addChatMessage("invalid db command");
				return;
			}
		} else if (message.equalsIgnoreCase("pb")) { // pop bot
			botList.get(botList.size() - 1).disconnect();
			botList.remove(botList.size() - 1);
			botNumber--;
		} else if (message.equalsIgnoreCase("rb") || message.equalsIgnoreCase("recoverbot")) {
			new Thread() {
				public void run() {
					synchronized(botList) {
						for (BotPlayManager b : botList) {
							if (!b.isConnected() && b.disconnect()) {
								addChatMessage("Recovered bot " + b.name);
								recoverBot(b);
								break;
							}
						}
					}
				}
			}.start();
		} else {
			// message = message.replaceAll("&", "§");
			connection.sendPacket(new ChatMessageC2SPacket(message));
		}
	}
	
	public static void tick() {
		try {
			runBotTick();
		}
		catch (Exception e) {
			e.printStackTrace();
			addChatMessage(e.getMessage());
		}
		if (dropping) {
			mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookOnly(random.nextFloat() * 360.0f,
					random.nextFloat() * 180.0f - 90.0f, mc.player.isOnGround()));
			mc.player.networkHandler
					.sendPacket(new CreativeInventoryActionC2SPacket(-1, mc.player.getStackInHand(Hand.MAIN_HAND)));
		}
		if (scaffold) {
			BlockPos bp = (new BlockPos(mc.player.getPos())).down();
			if (mc.player.inventory.getStack(mc.player.inventory.selectedSlot).getItem() instanceof BlockItem && mc.world.isAir(bp)) {
				mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(new Vec3d(0,0,0),Direction.DOWN,bp,false));
			}
		}
	}
	
	public static void createBot(String name, String chat) {
		BotPlayManager playManager;
		name = name.replaceAll("&", "§").replaceAll("\\\\-", " ").replaceAll("\\\\0", "\0");
		synchronized (botList) {
			if (chat.matches(".*-i( .*|$)")) // contains appendID tag //adds bot id at end of name
				name = name + String.format("%04d", botNumber);
			playManager = new BotPlayManager(name, chat);
			botList.add(playManager);
			botNumber++;
		}
		
		ServerAddress serveraddress = ServerAddress.parse(mc.getCurrentServerEntry().address);
		InetAddress address;
		try {
			address = InetAddress.getByName(serveraddress.getAddress());
		} catch (UnknownHostException e) {
			addChatMessage("error with creating bot");
			return;
		}
		int port = serveraddress.getPort();

		ClientConnection clientConnection;
		try {
			clientConnection = ClientConnection.connect(address, port, false);
		} catch (NullPointerException e) {
			addChatMessage("error with creating bot");
			return;
		}
		playManager.clientConnection = clientConnection;

		System.out.println("created network manager");
		clientConnection.setPacketListener(new ClientLoginPacketListener() {
			public void onHello(LoginHelloS2CPacket packet) {
				/*SecretKey secretKey = NetworkEncryptionUtils.generateKey();
				PublicKey publicKey = packet.getPublicKey();
				String string2 = (new BigInteger(NetworkEncryptionUtils.generateServerId(packet.getServerId(), publicKey, secretKey))).toString(16);
				Cipher cipher3 = NetworkEncryptionUtils.cipherFromKey(2, secretKey);
				Cipher cipher4 = NetworkEncryptionUtils.cipherFromKey(1, secretKey);
				LoginKeyC2SPacket loginKeyC2SPacket2 = new LoginKeyC2SPacket(secretKey, publicKey, packet.getNonce());

				clientConnection.send(
						new LoginKeyC2SPacket(secretKey, publicKey, packet.getNonce()),
						(future) -> {
				            clientConnection.setupEncryption(cipher3, cipher4);
						});*/
			}

			public void onLoginSuccess(LoginSuccessS2CPacket packet) {
				System.out.println("bot successfully logged in");
				clientConnection.setState(NetworkState.PLAY);
				clientConnection.setPacketListener(playManager);
			}

			public void onDisconnected(Text reason) {
				System.out.println(reason.getString());
			}
			
			public ClientConnection getConnection() {
				return clientConnection;
			}

			public void onDisconnect(LoginDisconnectS2CPacket packet) {
				System.out.println(packet.getReason().getString());
				clientConnection.disconnect(packet.getReason());
			}

			public void onCompression(LoginCompressionS2CPacket packet) {
				if (!clientConnection.isLocal()) {
					clientConnection.setCompressionThreshold(packet.getCompressionThreshold());
				}
			}

			public void onQueryRequest(LoginQueryRequestS2CPacket packet) {
				clientConnection.send(new LoginQueryResponseC2SPacket(packet.getQueryId(), (PacketByteBuf) null));
			}
		});
		try {
			clientConnection.send(new HandshakeC2SPacket(serveraddress.getAddress(), port, NetworkState.LOGIN));
			clientConnection.send(new LoginHelloC2SPacket(new GameProfile((UUID) null, name)));
		} catch (NullPointerException e) {
			addChatMessage("null pointer exception");
		}
	}

	private static class BotPlayManager implements ClientPlayPacketListener {
		boolean loop;
		String name;
		//String username;
		String chat;
		String chatMessage;
		int ID;
		int entityID = 0;
		HashMap<Integer, SimpleEntity> entityMap;

		ItemStack handItem = new ItemStack(Blocks.AIR);

		double x = 0.0;
		double y = 0.0;
		double z = 0.0;
		float pitch = 0.0f;
		float yaw = 0.0f;

		boolean loaded = false;
		boolean kill = false;

		static enum Movement {
			IDLE, WALK, ORBIT, FLANK, RANDOM
		}

		Movement movement = Movement.IDLE;

		boolean copy = false;
		boolean drop = false;
		boolean attack = false;

		int attackCooldown = 0;

		ClientConnection clientConnection;

		public BotPlayManager(String name, String chat) {
			this.clientConnection = null;
			this.name = name;
			this.chat = chat;
			this.ID = botNumber;
			this.entityMap = new HashMap<Integer, SimpleEntity>();
			String[] splitChat = chat.split(" ");
			for (String s : splitChat) {
				if (s.equals("-l")) {
					loop = true;
				} else if (s.equals("-w")) {
					movement = Movement.WALK;
				} else if (s.equals("-o")) {
					movement = Movement.ORBIT;
				} else if (s.equals("-f")) {
					movement = Movement.FLANK;
				} else if (s.equals("-r")) {
					movement = Movement.RANDOM;
				} else if (s.equals("-c")) {
					copy = true;
				} else if (s.equals("-d")) {
					drop = true;
				} else if (s.equals("-a")) {
					attack = true;
				}
			}
			chatMessage = chat.replaceAll("-. ", "");
		}
		
		public void sendPacket(Packet packet) {
			if (clientConnection != null) {
				clientConnection.send(packet);
			}
		}
		
		public boolean isConnected() {
			if (clientConnection != null) {
				return (clientConnection.isOpen() || clientConnection.hasChannel());
			}
			else {
				return false;
			}
		}
		
		public ClientConnection getConnection() {
			return clientConnection;
		}
		
		public boolean disconnect() {
			if (clientConnection == null) {
				return false;
			}
			else {
				clientConnection.disconnect(new LiteralText("bye"));
				return true;
			}
		}

		public void onGameJoin(GameJoinS2CPacket packet) {
			if (this.kill) {
				clientConnection.disconnect(new LiteralText("bye"));
				return;
			}
			this.entityID = packet.getEntityId();
			clientConnection.send(new ChatMessageC2SPacket(chatMessage));
			clientConnection.send(new UpdatePlayerAbilitiesC2SPacket(newBotAbilities));
			System.out.println("Successfully Created Bot");
		}
		
		public void onDisconnect(DisconnectS2CPacket packet) {
			System.out.println(packet.getReason().getString());
			clientConnection.disconnect(packet.getReason());
		}

		public void onDisconnected(Text reason) {
			System.out.println(reason.getString());
		}

		public void onKeepAlive(KeepAliveS2CPacket packet) {
			clientConnection.send(new KeepAliveC2SPacket(packet.getId()));
			if (loop)
				clientConnection.send(new ChatMessageC2SPacket(chatMessage));
		}

		public void onEntitySpawn(EntitySpawnS2CPacket packet) {
		}

		public void onExperienceOrbSpawn(ExperienceOrbSpawnS2CPacket packet) {
		}

		public void onMobSpawn(MobSpawnS2CPacket packet) {
			synchronized(entityMap) {
				entityMap.put( packet.getId(), new SimpleEntity(packet.getEntityTypeId(), packet.getId(), packet.getUuid(), packet.getX(), packet.getY(), packet.getZ()) );
			}
		}

		public void onScoreboardObjectiveUpdate(ScoreboardObjectiveUpdateS2CPacket packet) {
		}

		public void onPaintingSpawn(PaintingSpawnS2CPacket packet) {
		}

		public void onPlayerSpawn(PlayerSpawnS2CPacket packet) {
			synchronized(entityMap) {
				entityMap.put( packet.getId(), new SimpleEntity(92 , packet.getId(), packet.getPlayerUuid(), packet.getX(), packet.getY(), packet.getZ()) );
			}
		}

		public void onEntityAnimation(EntityAnimationS2CPacket packet) {
		}

		public void onStatistics(StatisticsS2CPacket packet) {
		}

		public void onUnlockRecipes(UnlockRecipesS2CPacket packet) {
		}

		public void onBlockDestroyProgress(BlockBreakingProgressS2CPacket packet) {
		}

		public void onSignEditorOpen(SignEditorOpenS2CPacket packet) {
		}

		public void onBlockEntityUpdate(BlockEntityUpdateS2CPacket packet) {
		}

		public void onBlockEvent(BlockEventS2CPacket packet) {
		}

		public void onBlockUpdate(BlockUpdateS2CPacket packet) {
		}

		public void onGameMessage(GameMessageS2CPacket packet) {
		}

		public void onChunkDeltaUpdate(ChunkDeltaUpdateS2CPacket packet) {
		}

		public void onMapUpdate(MapUpdateS2CPacket packet) {
		}

		public void onConfirmScreenAction(ConfirmScreenActionS2CPacket packet) {
		}

		public void onCloseScreen(CloseScreenS2CPacket packet) {
		}

		public void onInventory(InventoryS2CPacket packet) {
			if (packet.getSyncId() == 0) {
				List<ItemStack> items = packet.getContents();
				if (items.size() > 36) {
					//handItem = items.get(36);
					clientConnection.send(new CreativeInventoryActionC2SPacket(36, handItem));
				}
			}
		}

		public void onOpenHorseScreen(OpenHorseScreenS2CPacket packet) {
		}

		public void onScreenHandlerPropertyUpdate(ScreenHandlerPropertyUpdateS2CPacket packet) {
		}

		public void onScreenHandlerSlotUpdate(ScreenHandlerSlotUpdateS2CPacket packet) {
			if (packet.getSlot() == 36) {
				//handItem = packet.getStack();
				clientConnection.send(new CreativeInventoryActionC2SPacket(36, handItem));
			}
		}

		public void onCustomPayload(CustomPayloadS2CPacket packet) {
		}

		public void onEntityStatus(EntityStatusS2CPacket packet) {
		}

		public void onEntityAttach(EntityAttachS2CPacket packet) {
		}

		public void onEntityPassengersSet(EntityPassengersSetS2CPacket packet) {
		}

		public void onExplosion(ExplosionS2CPacket packet) {
		}

		public void onGameStateChange(GameStateChangeS2CPacket packet) {
		}

		public void onChunkData(ChunkDataS2CPacket packet) {
		}

		public void onUnloadChunk(UnloadChunkS2CPacket packet) {
		}

		public void onWorldEvent(WorldEventS2CPacket packet) {
		}

		public void onEntityUpdate(EntityS2CPacket packet) {
			int entityID = ((EntityS2CPacketAccessor) packet).getId();
			synchronized(entityMap) {
				SimpleEntity entity;
				if ((entity = entityMap.get(entityID)) != null) {
					Vec3d dPos = packet.calculateDeltaPosition(new Vec3d(entity.x, entity.y, entity.z));
					entity.x = dPos.x;
					entity.y = dPos.y;
					entity.z = dPos.z;
				}
			}
		}

		public void onPlayerPositionLook(PlayerPositionLookS2CPacket packet) {
			double d0 = packet.getX();
			double d1 = packet.getY();
			double d2 = packet.getZ();
			float f = packet.getYaw();
			float f1 = packet.getPitch();

			if (packet.getFlags().contains(PlayerPositionLookS2CPacket.Flag.X)) {
				d0 += x;
			}
			if (packet.getFlags().contains(PlayerPositionLookS2CPacket.Flag.Y)) {
				d1 += y;
			}

			if (packet.getFlags().contains(PlayerPositionLookS2CPacket.Flag.Z)) {
				d2 += z;
			}
			if (packet.getFlags().contains(PlayerPositionLookS2CPacket.Flag.X_ROT)) {
				f1 += pitch;
			}

			if (packet.getFlags().contains(PlayerPositionLookS2CPacket.Flag.Y_ROT)) {
				f += yaw;
			}
			x = d0;
			y = d1;
			z = d2;
			pitch = f1;
			yaw = f;
			loaded = true;
			clientConnection.send(new TeleportConfirmC2SPacket(packet.getTeleportId()));
			clientConnection.send(new PlayerMoveC2SPacket.Both(x, y, z, yaw, pitch, false));
		}

		public void onParticle(ParticleS2CPacket packet) {
		}

		public void onPlayerAbilities(PlayerAbilitiesS2CPacket packet) {
		}

		public void onPlayerList(PlayerListS2CPacket packet) {
		}

		public void onEntitiesDestroy(EntitiesDestroyS2CPacket packet) {
			synchronized(entityMap) {
				for (int entityID : packet.getEntityIds()) {
					entityMap.remove(entityID);
				}
			}
		}

		public void onRemoveEntityEffect(RemoveEntityStatusEffectS2CPacket packet) {
		}

		public void onPlayerRespawn(PlayerRespawnS2CPacket packet) {
		}

		public void onEntitySetHeadYaw(EntitySetHeadYawS2CPacket packet) {
		}

		public void onHeldItemChange(HeldItemChangeS2CPacket packet) {
		}

		public void onScoreboardDisplay(ScoreboardDisplayS2CPacket packet) {
		}

		public void onEntityTrackerUpdate(EntityTrackerUpdateS2CPacket packet) {
		}

		public void onVelocityUpdate(EntityVelocityUpdateS2CPacket packet) {
		}

		public void onEquipmentUpdate(EntityEquipmentUpdateS2CPacket packet) {
		}

		public void onExperienceBarUpdate(ExperienceBarUpdateS2CPacket packet) {
		}

		public void onHealthUpdate(HealthUpdateS2CPacket packet) {
			if (packet.getHealth() == 0) {
				clientConnection.send(new ClientStatusC2SPacket(ClientStatusC2SPacket.Mode.PERFORM_RESPAWN));
			}
		}

		public void onTeam(TeamS2CPacket packet) {
		}

		public void onScoreboardPlayerUpdate(ScoreboardPlayerUpdateS2CPacket packet) {
		}

		public void onPlayerSpawnPosition(PlayerSpawnPositionS2CPacket packet) {
		}

		public void onWorldTimeUpdate(WorldTimeUpdateS2CPacket packet) {
		}

		public void onPlaySound(PlaySoundS2CPacket packet) {
		}

		public void onPlaySoundFromEntity(PlaySoundFromEntityS2CPacket packet) {
		}
		
		public void onPlaySoundId(PlaySoundIdS2CPacket packet) {
		}

		public void onItemPickupAnimation(ItemPickupAnimationS2CPacket packet) {
		}

		public void onEntityPosition(EntityPositionS2CPacket packet) {
			SimpleEntity entity;
			int entityID = packet.getId();
			synchronized(entityMap) {
				if ((entity = entityMap.get(entityID)) != null) {
					entity.x = packet.getX();
					entity.y = packet.getY();
					entity.z = packet.getZ();
				}
			}
		}

		public void onEntityAttributes(EntityAttributesS2CPacket packet) {
		}

		public void onEntityPotionEffect(EntityStatusEffectS2CPacket packet) {
		}

		public void onSynchronizeTags(SynchronizeTagsS2CPacket packet) {
		}
		
		public void onCombatEvent(CombatEventS2CPacket packet) {
		}

		public void onDifficulty(DifficultyS2CPacket packet) {
		}

		public void onSetCameraEntity(SetCameraEntityS2CPacket packet) {
		}

		public void onWorldBorder(WorldBorderS2CPacket packet) {
		}

		public void onTitle(TitleS2CPacket packet) {
		}

		public void onPlayerListHeader(PlayerListHeaderS2CPacket packet) {
		}

		public void onResourcePackSend(ResourcePackSendS2CPacket packet) {
		}

		public void onBossBar(BossBarS2CPacket packet) {
		}

		public void onCooldownUpdate(CooldownUpdateS2CPacket packet) {
		}

		public void onVehicleMove(VehicleMoveS2CPacket packet) {
		}

		public void onAdvancements(AdvancementUpdateS2CPacket packet) {
		}

		public void onSelectAdvancementTab(SelectAdvancementTabS2CPacket packet) {
		}
		
		public void onCraftFailedResponse(CraftFailedResponseS2CPacket packet) {
		}

		public void onCommandTree(CommandTreeS2CPacket packet) {
		}

		public void onStopSound(StopSoundS2CPacket packet) {
		}

		public void onCommandSuggestions(CommandSuggestionsS2CPacket packet) {
		}

		public void onSynchronizeRecipes(SynchronizeRecipesS2CPacket packet) {
		}

		public void onLookAt(LookAtS2CPacket packet) {
		}

		public void onTagQuery(TagQueryResponseS2CPacket packet) {
		}

		public void onLightUpdate(LightUpdateS2CPacket packet) {
		}

		public void onOpenWrittenBook(OpenWrittenBookS2CPacket packet) {
		}

		public void onOpenScreen(OpenScreenS2CPacket packet) {
		}

		public void onSetTradeOffers(SetTradeOffersS2CPacket packet) {
		}

		public void onChunkLoadDistance(ChunkLoadDistanceS2CPacket packet) {
		}

		public void onChunkRenderDistanceCenter(ChunkRenderDistanceCenterS2CPacket packet) {
		}

		public void onPlayerActionResponse(PlayerActionResponseS2CPacket packet) {
		}
	}

	public static void runBotTick() {
		int orbitNum = 0;
		int orbitIndex = 0;
		int flankIndex = 0;
		for (BotPlayManager b : botList) {
			if (b.movement == BotPlayManager.Movement.ORBIT)
				orbitNum++;
		}
		double orbitOffset = 0;
		if (orbitNum > 0) {
			orbitOffset = 2.0 * Math.PI / orbitNum;
			orbitAngle += .15;
			orbitAngle = orbitAngle % (2.0 * Math.PI);
		}

		for (BotPlayManager b : botList) {
			if (b.loaded) {
				if (b.copy) {
					b.pitch = mc.player.pitch;
					b.yaw = mc.player.yaw;
					if (b.movement == BotPlayManager.Movement.IDLE)
						b.sendPacket(new PlayerMoveC2SPacket.LookOnly(b.yaw, b.pitch, false));
				}
				if (b.drop) {
					b.pitch = random.nextFloat() * 180.0f - 90.0f;
					b.yaw = random.nextFloat() * 360.0f;
					if (b.movement == BotPlayManager.Movement.IDLE)
						b.sendPacket(new PlayerMoveC2SPacket.LookOnly(b.yaw, b.pitch, false));
				}
				if (b.movement == BotPlayManager.Movement.WALK) {
					double p = ((b.pitch) * Math.PI) / 180;
					double y = ((b.yaw) * Math.PI) / 180;
					b.x += -0.2 * Math.cos(p) * Math.sin(y);
					b.y += -0.2 * Math.sin(p);
					b.z += 0.2 * Math.cos(p) * Math.cos(y);
					b.clientConnection.send(new PlayerMoveC2SPacket.Both(b.x, b.y, b.z, b.yaw, b.pitch, false));

				} else if (b.movement == BotPlayManager.Movement.RANDOM) {
					b.x += 1.0 * (0.5 - Math.random());
					b.y += 1.0 * (0.5 - Math.random());
					b.z += 1.0 * (0.5 - Math.random());
					b.clientConnection.send(new PlayerMoveC2SPacket.Both(b.x, b.y, b.z, b.yaw, b.pitch, false));
				} else if (b.movement == BotPlayManager.Movement.ORBIT) {
					double tx = mc.player.getX() + 2.5 * Math.cos(orbitAngle + orbitOffset * orbitIndex);
					double ty = mc.player.getY();
					double tz = mc.player.getZ() + 2.5 * Math.sin(orbitAngle + orbitOffset * orbitIndex);
					double dx = tx - b.x;
					double dy = ty - b.y;
					double dz = tz - b.z;
					double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
					if (dist < 80.0) {
						b.x = tx;
						b.y = ty;
						b.z = tz;
						b.clientConnection.send(new PlayerMoveC2SPacket.Both(b.x, b.y, b.z, b.yaw, b.pitch, false));
					}
					orbitIndex++;
				} else if (b.movement == BotPlayManager.Movement.FLANK) {
					double rot = (mc.player.yaw + 180.0 * (flankIndex % 2)) / 180.0 * Math.PI;
					double tx = mc.player.getX() + (flankIndex / 2 + 1) * Math.cos(rot);
					double ty = mc.player.getY();
					double tz = mc.player.getZ() + (flankIndex / 2 + 1) * Math.sin(rot);
					double dx = tx - b.x;
					double dy = ty - b.y;
					double dz = tz - b.z;
					double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
					if (dist < 80.0) {
						b.x = tx;
						b.y = ty;
						b.z = tz;
						b.sendPacket(new PlayerMoveC2SPacket.Both(b.x, b.y, b.z, b.yaw, b.pitch, false));
					}
					flankIndex++;
				}

				if (b.drop) {
					b.sendPacket(new CreativeInventoryActionC2SPacket(-1, b.handItem));
				}
				if (b.attack) {
					if (hitMode == HitMode.MULTI || b.attackCooldown <= 0) {
						double minDistSq = Double.MAX_VALUE;
						SimpleEntity nearest = null;
						synchronized (b.entityMap) {
							for (Map.Entry<Integer, SimpleEntity> entry : b.entityMap.entrySet()) {
								SimpleEntity entity = entry.getValue();
								if (entity.getEntityId() == mc.player.getEntityId()) {
									continue;
								}
								boolean isBot = false;
								for (BotPlayManager t : botList) {
									if (entity.getEntityId() == t.entityID) {
										isBot = true;
										break;
									}
								}
								if (isBot) {
									continue;
								}
								
								boolean invulnerable = false;
								Collection<PlayerListEntry> players = mc.player.networkHandler.getPlayerList();
								for (PlayerListEntry info : players) {
									if (info.getProfile().getId().equals(entity.uuid)) {
										if (info.getGameMode().isCreative()) {
											invulnerable = true;
										}
										break;
									}
								}
								if (!invulnerable) {
									double d0 = b.x-entity.x;
									double d1 = b.y-entity.y;
									double d2 = b.z-entity.z;
									double d = d0*d0+d1*d1+d2*d2;
									if (hitMode == HitMode.REGULAR) {
										if (d < minDistSq) {
											minDistSq = d;
											nearest = entity;
										}
									} else {
										if (d <= 36.0) {
											b.sendPacket(new PlayerInteractEntityC2SPacket(entity, false));
										}
									}
								}
							}
							if (hitMode == HitMode.REGULAR && minDistSq <= 36.0) {
								b.sendPacket(new PlayerInteractEntityC2SPacket(nearest, false));
								b.attackCooldown = 12;
							}
						}
					} else if (b.attackCooldown > 0)
						b.attackCooldown--;
				}
			}
		}
	}

	public static void swingArm() {
		for (BotPlayManager b : botList) {
			if (b.copy)
				b.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
		}
	}

	public static void rightClick() {
		for (BotPlayManager b : botList) {
			if (b.copy)
				b.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND));
		}
		ItemStack handItem = mc.player.getStackInHand(Hand.MAIN_HAND);
		if (handItem.getName().getString().equals("§b§lhhhzzzsss§r §6§lArmorstand §a§lSpammer")) {
			double x = mc.player.getX() + (Math.random()-0.5)*50.0;
			double y = mc.player.getY() + (Math.random()-0.5)*50.0;
			double z = mc.player.getZ() + (Math.random()-0.5)*50.0;
			ListTag tagList = new ListTag();
			tagList.add(DoubleTag.of(x));
			tagList.add(DoubleTag.of(y));
			tagList.add(DoubleTag.of(z));
			handItem.getTag().getCompound("EntityTag").put("Pos", tagList);
			mc.interactionManager.clickCreativeStack(mc.player.getStackInHand(Hand.MAIN_HAND), 36 + mc.player.inventory.selectedSlot);
		}
		else if (handItem.getName().getString().equals("§b§lhhhzzzsss§r §6§lFunny Head §a§lMaker")) {
			CompoundTag compoundTag = handItem.getTag().getCompound("SkullOwner");
			compoundTag.putString("Name", UUID.randomUUID().toString());
			if (compoundTag.contains("Id")) {
				compoundTag.remove("Id");
			}
			if (handItem.getTag().contains("SkullOwnerOrig")) {
				handItem.getTag().remove("SkullOwnerOrig");
			}
			mc.interactionManager.clickCreativeStack(mc.player.getStackInHand(Hand.MAIN_HAND), 36 + mc.player.inventory.selectedSlot);
		}
	}

	public static void stopUsingItem() {
		for (BotPlayManager b : botList) {
			if (b.copy)
				b.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.RELEASE_USE_ITEM,
						BlockPos.ORIGIN, Direction.DOWN));
		}
	}

	public static void recoverBot(BotPlayManager b) {
		ServerAddress serveraddress = ServerAddress.parse(mc.getCurrentServerEntry().address);
		InetAddress address;
		try {
			address = InetAddress.getByName(serveraddress.getAddress());
		} catch (UnknownHostException e) {
			addChatMessage("error with creating bot");
			return;
		}
		int port = serveraddress.getPort();

		ClientConnection clientConnection;
		try {
			clientConnection = ClientConnection.connect(address, port, false);
		} catch (NullPointerException e) {
			addChatMessage("error with creating bot");
			return;
		}
		System.out.println("created network manager");
		clientConnection.setPacketListener(new ClientLoginPacketListener() {
			public void onHello(LoginHelloS2CPacket packet) {
				/*final SecretKey secretKey = NetworkEncryptionUtils.generateKey();
				String s = packet.getServerId();
				PublicKey publicKey = packet.getPublicKey();
				String s1 = (new BigInteger(NetworkEncryptionUtils.generateServerId(s, publicKey, secretKey))).toString(16);

				clientConnection.send(
						new LoginKeyC2SPacket(secretKey, publicKey, packet.getNonce()),
						(future) -> {
				            clientConnection.setupEncryption(secretKey);
						});*/
			}

			public void onLoginSuccess(LoginSuccessS2CPacket packet) {
				clientConnection.setState(NetworkState.PLAY);
				b.clientConnection = clientConnection;
				clientConnection.setPacketListener(b);
			}

			public void onDisconnected(Text reason) {
				System.out.println(reason.getString());
			}
			
			public ClientConnection getConnection() {
				return clientConnection;
			}

			public void onDisconnect(LoginDisconnectS2CPacket packet) {
				System.out.println(packet.getReason().getString());
				clientConnection.disconnect(packet.getReason());
			}

			public void onCompression(LoginCompressionS2CPacket packet) {
				if (!clientConnection.isLocal()) {
					clientConnection.setCompressionThreshold(packet.getCompressionThreshold());
				}
			}

			public void onQueryRequest(LoginQueryRequestS2CPacket packet) {
				clientConnection.send(new LoginQueryResponseC2SPacket(packet.getQueryId(), (PacketByteBuf) null));
			}
		});
		try {
			clientConnection.send(new HandshakeC2SPacket(serveraddress.getAddress(), port, NetworkState.LOGIN));
			clientConnection.send(new LoginHelloC2SPacket(new GameProfile((UUID) null, b.name)));
		} catch (NullPointerException e) {
			addChatMessage("null pointer exception");
		}
	}
	
	public static void setPosition(double x, double y, double z) {
		mc.player.setPos(x, y, z);
		mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Both(mc.player.getX(), mc.player.getBoundingBox().minY, mc.player.getZ(), mc.player.yaw, mc.player.pitch, false));
	}

	public static void addChatMessage(String s) {
		mc.player.sendSystemMessage(new LiteralText(s), Util.NIL_UUID);
	}
}
