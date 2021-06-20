package com.github.hhhzzzsss.chathacks;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.PublicKey;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.crypto.SecretKey;

import com.mojang.authlib.GameProfile;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkEncryptionUtils;
import net.minecraft.network.NetworkState;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.ServerAddress;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.ClientLoginPacketListener;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;
import net.minecraft.network.packet.c2s.login.LoginKeyC2SPacket;
import net.minecraft.network.packet.c2s.login.LoginQueryResponseC2SPacket;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import net.minecraft.network.packet.c2s.play.KeepAliveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdatePlayerAbilitiesC2SPacket;
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
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class PlayerListUpdaterBot {
	ClientConnection clientConnection;
	public PlayerListUpdaterBot() {
		ServerAddress serveraddress = ServerAddress.parse(MinecraftClient.getInstance().getCurrentServerEntry().address);
		InetAddress address;
		try {
			address = InetAddress.getByName(serveraddress.getAddress());
		} catch (UnknownHostException e) {
			ChatHacks.addChatMessage("error with creating bot");
			return;
		}
		int port = serveraddress.getPort();

		ClientConnection clientConnection;
		try {
			clientConnection = ClientConnection.connect(address, port, false);
		} catch (NullPointerException e) {
			ChatHacks.addChatMessage("error with creating bot");
			return;
		}

		System.out.println("created network manager");
		BotPlayManager playManager = new BotPlayManager();
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
				clientConnection.setPacketListener(playManager);
			}

			public void onDisconnected(Text reason) {
				System.out.println(reason.getString());
			}
			
			public ClientConnection getConnection() {
				return clientConnection;
			}

			public void onDisconnect(LoginDisconnectS2CPacket packet) {
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
			clientConnection.send(new LoginHelloC2SPacket(new GameProfile((UUID) null, "§0uuid_updater")));
		} catch (NullPointerException e) {
			ChatHacks.addChatMessage("null pointer exception");
		}
	}
	
	private class BotPlayManager implements ClientPlayPacketListener {

		public void onGameJoin(GameJoinS2CPacket packet) {
		}
		
		public void onDisconnect(DisconnectS2CPacket packet) {
			clientConnection.disconnect(packet.getReason());
		}

		public void onDisconnected(Text reason) {
			System.out.println(reason.getString());
		}
		
		public ClientConnection getConnection() {
			return clientConnection;
		}

		public void onKeepAlive(KeepAliveS2CPacket packet) {
		}

		public void onEntitySpawn(EntitySpawnS2CPacket packet) {
		}

		public void onExperienceOrbSpawn(ExperienceOrbSpawnS2CPacket packet) {
		}

		public void onMobSpawn(MobSpawnS2CPacket packet) {
		}

		public void onScoreboardObjectiveUpdate(ScoreboardObjectiveUpdateS2CPacket packet) {
		}

		public void onPaintingSpawn(PaintingSpawnS2CPacket packet) {
		}

		public void onPlayerSpawn(PlayerSpawnS2CPacket packet) {
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
		}

		public void onOpenHorseScreen(OpenHorseScreenS2CPacket packet) {
		}

		public void onScreenHandlerPropertyUpdate(ScreenHandlerPropertyUpdateS2CPacket packet) {
		}

		public void onScreenHandlerSlotUpdate(ScreenHandlerSlotUpdateS2CPacket packet) {
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
		}

		public void onPlayerPositionLook(PlayerPositionLookS2CPacket packet) {
		}

		public void onParticle(ParticleS2CPacket packet) {
		}

		public void onPlayerAbilities(PlayerAbilitiesS2CPacket packet) {
		}

		public void onPlayerList(PlayerListS2CPacket packet) {
			if (packet.getAction() == PlayerListS2CPacket.Action.ADD_PLAYER)
				MinecraftClient.getInstance().player.networkHandler.onPlayerList(packet);
			clientConnection.disconnect(new LiteralText("bye"));
		}

		public void onEntitiesDestroy(EntitiesDestroyS2CPacket packet) {
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
}
